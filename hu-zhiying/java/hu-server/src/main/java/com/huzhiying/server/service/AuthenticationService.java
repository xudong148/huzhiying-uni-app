package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.server.dto.AuthDtos;
import com.huzhiying.server.exception.AuthUnauthorizedException;
import com.huzhiying.server.exception.AuthValidationException;
import com.huzhiying.server.persistence.PersistenceEntities;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

@Service
@Transactional
public class AuthenticationService {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1\\d{10}$");
    private static final Pattern PURPOSE_PATTERN = Pattern.compile("^(login|register)$");

    private final PlatformRepository platformRepository;
    private final AuthSessionService authSessionService;
    private final PasswordHashService passwordHashService;
    private final java.security.SecureRandom secureRandom = new java.security.SecureRandom();

    public AuthenticationService(PlatformRepository platformRepository,
                                 AuthSessionService authSessionService,
                                 PasswordHashService passwordHashService) {
        this.platformRepository = platformRepository;
        this.authSessionService = authSessionService;
        this.passwordHashService = passwordHashService;
    }

    public AuthDtos.AuthSessionPayload adminLogin(String username, String password) {
        String normalizedUsername = requireUsername(username);
        String normalizedPassword = requirePassword(password);
        PersistenceEntities.UserEntity user = platformRepository.findUserByUsername(normalizedUsername)
                .orElseThrow(() -> new AuthUnauthorizedException("用户名或密码错误"));
        if (!enabled(user.enabled)) {
            throw new AuthUnauthorizedException("当前账号已禁用");
        }
        if (!passwordHashService.matches(normalizedPassword, user.passwordHash)) {
            throw new AuthUnauthorizedException("用户名或密码错误");
        }
        if (!collectRoleCodes(user).contains("admin")) {
            throw new AuthUnauthorizedException("当前账号没有后台登录权限");
        }
        return buildSessionPayload(authSessionService.issueSession(user, RoleCode.ADMIN, "admin-web"));
    }

    public AuthDtos.SmsCodePayload sendSmsCode(String mobile, String purpose) {
        String normalizedMobile = requireMobile(mobile);
        String normalizedPurpose = requirePurpose(purpose);
        platformRepository.deleteExpiredSmsCodes();

        PersistenceEntities.SmsCodeEntity entity = new PersistenceEntities.SmsCodeEntity();
        entity.mobile = normalizedMobile;
        entity.purpose = normalizedPurpose;
        entity.code = buildCode();
        entity.createdAt = LocalDateTime.now();
        entity.expiresAt = entity.createdAt.plusMinutes(5);
        platformRepository.saveSmsCode(entity);
        return new AuthDtos.SmsCodePayload(normalizedMobile, normalizedPurpose, 300);
    }

    public AuthDtos.AuthSessionPayload mobileLogin(String mobile, String code, String role) {
        String normalizedMobile = requireMobile(mobile);
        verifySmsCode(normalizedMobile, "login", code);
        PersistenceEntities.UserEntity user = platformRepository.findUserByMobile(normalizedMobile)
                .orElseThrow(() -> new AuthUnauthorizedException("手机号未注册"));
        if (!enabled(user.enabled)) {
            throw new AuthUnauthorizedException("当前账号已禁用");
        }
        RoleCode roleCode = resolveUserRole(user, role);
        return buildSessionPayload(authSessionService.issueSession(user, roleCode, "mobile-app"));
    }

    public AuthDtos.AuthSessionPayload register(String mobile, String code, String nickname) {
        String normalizedMobile = requireMobile(mobile);
        String normalizedNickname = requireNickname(nickname);
        verifySmsCode(normalizedMobile, "register", code);
        if (platformRepository.findUserByMobile(normalizedMobile).isPresent()) {
            throw new AuthValidationException("该手机号已注册");
        }

        PersistenceEntities.UserEntity user = new PersistenceEntities.UserEntity();
        user.id = platformRepository.nextLongId("UserEntity", "id", 10000L);
        user.nickname = normalizedNickname;
        user.mobile = normalizedMobile;
        user.roleCode = RoleCode.USER;
        user.avatar = "/static/user.png";
        user.levelName = "平台注册会员";
        user.enabled = true;
        platformRepository.saveUser(user);
        return buildSessionPayload(authSessionService.issueSession(user, RoleCode.USER, "mobile-app"));
    }

    public AuthDtos.AuthSessionPayload refresh(String refreshToken, String clientType) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AuthValidationException("缺少刷新令牌");
        }
        AuthSessionService.IssuedSession issued = authSessionService.refreshSession(refreshToken.trim(), clientType);
        PersistenceEntities.UserEntity user = platformRepository.findUser(issued.userId()).orElseThrow();
        return buildSessionPayload(user, issued);
    }

    public AuthDtos.AuthSessionPayload currentSession() {
        return currentAccessSession();
    }

    public AuthDtos.AuthSessionPayload currentAccessSession() {
        AuthSessionService.SessionIdentity identity = authSessionService.requireIdentity();
        PersistenceEntities.UserEntity user = platformRepository.findUser(identity.userId()).orElseThrow();
        List<AuthDtos.AuthMenuPayload> menus = identity.roleCode() == RoleCode.ADMIN ? collectMenus(user) : List.of();
        List<String> permissions = identity.roleCode() == RoleCode.ADMIN ? collectPermissionCodes(user) : List.of();
        return new AuthDtos.AuthSessionPayload(
                null,
                null,
                identity.roleCode().name().toLowerCase(Locale.ROOT),
                null,
                null,
                buildProfile(user, identity.roleCode()),
                menus,
                permissions
        );
    }

    private AuthDtos.AuthSessionPayload buildSessionPayload(AuthSessionService.IssuedSession issued) {
        PersistenceEntities.UserEntity user = platformRepository.findUser(issued.userId()).orElseThrow();
        return buildSessionPayload(user, issued);
    }

    private AuthDtos.AuthSessionPayload buildSessionPayload(PersistenceEntities.UserEntity user, AuthSessionService.IssuedSession issued) {
        List<AuthDtos.AuthMenuPayload> menus = issued.roleCode() == RoleCode.ADMIN ? collectMenus(user) : List.of();
        List<String> permissions = issued.roleCode() == RoleCode.ADMIN ? collectPermissionCodes(user) : List.of();
        return new AuthDtos.AuthSessionPayload(
                issued.token(),
                issued.refreshToken(),
                issued.roleCode().name().toLowerCase(Locale.ROOT),
                issued.accessExpiresAt(),
                issued.refreshExpiresAt(),
                buildProfile(user, issued.roleCode()),
                menus,
                permissions
        );
    }

    private AuthDtos.AuthProfilePayload buildProfile(PersistenceEntities.UserEntity user, RoleCode roleCode) {
        String roleName = resolveRoleName(user, roleCode);
        return new AuthDtos.AuthProfilePayload(
                user.id,
                user.username,
                user.nickname,
                user.mobile,
                roleCode.name().toLowerCase(Locale.ROOT),
                roleName
        );
    }

    private List<AuthDtos.AuthMenuPayload> collectMenus(PersistenceEntities.UserEntity user) {
        List<Long> roleIds = collectRoleIds(user);
        List<Long> menuIds = roleIds.stream()
                .flatMap(roleId -> platformRepository.listRoleMenuBindings(roleId).stream())
                .map(binding -> binding.menuId)
                .distinct()
                .toList();
        return platformRepository.listMenusByIds(menuIds).stream()
                .filter(menu -> enabled(menu.enabled))
                .sorted(Comparator.comparing((PersistenceEntities.MenuEntity menu) -> menu.sortOrder == null ? 0 : menu.sortOrder)
                        .thenComparing(menu -> menu.id))
                .map(menu -> new AuthDtos.AuthMenuPayload(menu.id, menu.name, menu.path, menu.icon))
                .toList();
    }

    private List<String> collectPermissionCodes(PersistenceEntities.UserEntity user) {
        List<Long> roleIds = collectRoleIds(user);
        List<Long> permissionIds = roleIds.stream()
                .flatMap(roleId -> platformRepository.listRolePermissionBindings(roleId).stream())
                .map(binding -> binding.permissionId)
                .distinct()
                .toList();
        return platformRepository.listPermissionsByIds(permissionIds).stream()
                .filter(permission -> enabled(permission.enabled))
                .map(permission -> permission.code)
                .sorted()
                .toList();
    }

    private List<Long> collectRoleIds(PersistenceEntities.UserEntity user) {
        Set<Long> roleIds = new LinkedHashSet<>();
        platformRepository.listUserRoles(user.id).forEach(binding -> roleIds.add(binding.roleId));
        if (roleIds.isEmpty() && user.roleCode == RoleCode.ADMIN) {
            platformRepository.findRoleByCode("admin").ifPresent(role -> roleIds.add(role.id));
        }
        return List.copyOf(roleIds);
    }

    private Set<String> collectRoleCodes(PersistenceEntities.UserEntity user) {
        return platformRepository.listRolesByIds(collectRoleIds(user)).stream()
                .filter(role -> enabled(role.enabled))
                .map(role -> role.code)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
    }

    private String resolveRoleName(PersistenceEntities.UserEntity user, RoleCode roleCode) {
        if (roleCode == RoleCode.ADMIN) {
            return platformRepository.listRolesByIds(collectRoleIds(user)).stream()
                    .filter(role -> "admin".equals(role.code))
                    .map(role -> role.name)
                    .findFirst()
                    .orElseGet(() -> user.levelName == null ? "后台账号" : user.levelName);
        }
        return user.levelName == null ? roleCode.name() : user.levelName;
    }

    private RoleCode resolveUserRole(PersistenceEntities.UserEntity user, String role) {
        RoleCode requested = authSessionService.parseRole(role);
        if (requested == RoleCode.ADMIN) {
            throw new AuthUnauthorizedException("后台账号请使用用户名密码登录");
        }
        if (requested == RoleCode.MASTER && user.roleCode != RoleCode.MASTER) {
            throw new AuthUnauthorizedException("当前手机号不是师傅账号");
        }
        return requested == RoleCode.MASTER ? RoleCode.MASTER : user.roleCode == RoleCode.MASTER ? RoleCode.MASTER : RoleCode.USER;
    }

    private void verifySmsCode(String mobile, String purpose, String code) {
        String normalizedCode = code == null ? "" : code.trim();
        if (normalizedCode.isBlank()) {
            throw new AuthValidationException("短信验证码不能为空");
        }
        PersistenceEntities.SmsCodeEntity smsCode = platformRepository.findLatestActiveSmsCode(mobile, purpose)
                .orElseThrow(() -> new AuthUnauthorizedException("请先获取短信验证码"));
        if (smsCode.expiresAt == null || smsCode.expiresAt.isBefore(LocalDateTime.now())) {
            throw new AuthUnauthorizedException("短信验证码已过期");
        }
        if (!normalizedCode.equals(smsCode.code)) {
            throw new AuthUnauthorizedException("短信验证码错误");
        }
        smsCode.consumedAt = LocalDateTime.now();
        platformRepository.saveSmsCode(smsCode);
    }

    private String requireUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthValidationException("用户名不能为空");
        }
        return username.trim().toLowerCase(Locale.ROOT);
    }

    private String requirePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new AuthValidationException("密码不能为空");
        }
        return password;
    }

    private String requireMobile(String mobile) {
        String normalized = mobile == null ? "" : mobile.trim();
        if (!MOBILE_PATTERN.matcher(normalized).matches()) {
            throw new AuthValidationException("手机号格式不正确");
        }
        return normalized;
    }

    private String requirePurpose(String purpose) {
        String normalized = purpose == null ? "" : purpose.trim().toLowerCase(Locale.ROOT);
        if (!PURPOSE_PATTERN.matcher(normalized).matches()) {
            throw new AuthValidationException("验证码用途仅支持 login 或 register");
        }
        return normalized;
    }

    private String requireNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new AuthValidationException("昵称不能为空");
        }
        return nickname.trim();
    }

    private String buildCode() {
        int value = secureRandom.nextInt(1_000_000);
        return String.format("%06d", value);
    }

    private boolean enabled(Boolean value) {
        return value == null || value;
    }
}
