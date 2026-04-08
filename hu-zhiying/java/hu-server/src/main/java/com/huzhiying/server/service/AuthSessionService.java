package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.domain.model.DomainModels.AuthToken;
import com.huzhiying.server.persistence.PersistenceEntities.AuthSessionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AuthSessionService {

    private static final Pattern ACCESS_TOKEN_PATTERN = Pattern.compile("^token-(\\d+)-([a-z_]+)$");
    private static final Pattern REFRESH_TOKEN_PATTERN = Pattern.compile("^refresh-(\\d+)-([a-z_]+)-([a-z0-9]+)$");
    private static final int ACCESS_TOKEN_HOURS = 2;
    private static final int REFRESH_TOKEN_DAYS = 30;

    private final PlatformRepository platformRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthSessionService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public AuthToken issueToken(UserEntity user, RoleCode roleCode) {
        IssuedSession issued = issueSession(user, roleCode, "legacy");
        return new AuthToken(issued.token(), issued.refreshToken(), issued.roleCode().name().toLowerCase());
    }

    public IssuedSession issueSession(UserEntity user, RoleCode roleCode, String clientType) {
        LocalDateTime now = LocalDateTime.now();
        String accessToken = generateToken("atk");
        String refreshToken = generateToken("rtk");

        AuthSessionEntity session = new AuthSessionEntity();
        session.userId = user.id;
        session.roleCode = roleCode.name();
        session.accessTokenHash = hashToken(accessToken);
        session.accessTokenPreview = preview(accessToken);
        session.refreshTokenHash = hashToken(refreshToken);
        session.clientType = clientType == null || clientType.isBlank() ? "app" : clientType;
        session.accessExpiresAt = now.plusHours(ACCESS_TOKEN_HOURS);
        session.refreshExpiresAt = now.plusDays(REFRESH_TOKEN_DAYS);
        session.createdAt = now;
        session.updatedAt = now;
        session.lastSeenAt = now;
        platformRepository.saveAuthSession(session);

        return new IssuedSession(
                user.id,
                accessToken,
                refreshToken,
                roleCode,
                session.accessExpiresAt,
                session.refreshExpiresAt
        );
    }

    public IssuedSession refreshSession(String refreshToken, String clientType) {
        Optional<AuthSessionEntity> sessionOptional = resolveRefreshSession(refreshToken);
        if (sessionOptional.isPresent()) {
            AuthSessionEntity session = sessionOptional.get();
            UserEntity user = platformRepository.findUser(session.userId).orElseThrow();
            return rotateSession(session, user, parseRole(session.roleCode), clientType);
        }

        SessionIdentity legacyIdentity = refreshIdentity(refreshToken, RoleCode.USER);
        UserEntity user = platformRepository.findUser(legacyIdentity.userId()).orElseThrow();
        return issueSession(user, legacyIdentity.roleCode(), clientType == null ? "legacy" : clientType);
    }

    public Optional<SessionIdentity> currentIdentityOptional() {
        return currentAccessToken().flatMap(this::resolveIdentity);
    }

    public SessionIdentity requireIdentity() {
        return currentIdentityOptional().orElseThrow(() -> new IllegalStateException("登录已失效，请重新登录"));
    }

    public SessionIdentity currentIdentity(RoleCode fallbackRole) {
        return currentIdentityOptional()
                .filter(identity -> platformRepository.findUser(identity.userId()).isPresent())
                .orElseGet(() -> fallbackIdentity(fallbackRole));
    }

    public AuthToken refreshLegacyToken(String refreshToken, RoleCode fallbackRole) {
        SessionIdentity identity = refreshIdentity(refreshToken, fallbackRole);
        UserEntity user = platformRepository.findUser(identity.userId()).orElseThrow();
        return issueToken(user, identity.roleCode());
    }

    public SessionIdentity refreshIdentity(String refreshToken, RoleCode fallbackRole) {
        Optional<AuthSessionEntity> sessionOptional = resolveRefreshSession(refreshToken);
        if (sessionOptional.isPresent()) {
            AuthSessionEntity session = sessionOptional.get();
            if (platformRepository.findUser(session.userId).isPresent()) {
                return new SessionIdentity(session.userId, parseRole(session.roleCode));
            }
        }
        return parseRefreshToken(refreshToken)
                .filter(identity -> platformRepository.findUser(identity.userId()).isPresent())
                .orElseGet(() -> fallbackIdentity(fallbackRole));
    }

    public Long currentUserId(RoleCode fallbackRole) {
        return currentIdentity(fallbackRole).userId();
    }

    public UserEntity currentUser(RoleCode fallbackRole) {
        return platformRepository.findUser(currentUserId(fallbackRole)).orElseThrow();
    }

    public Long currentMasterUserId() {
        return currentIdentity(RoleCode.MASTER).userId();
    }

    public MasterProfileEntity currentMasterProfile() {
        Long masterUserId = currentMasterUserId();
        return platformRepository.findMasterProfileByUserId(masterUserId)
                .orElseGet(() -> platformRepository.findMasterProfileByUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID).orElseThrow());
    }

    private IssuedSession rotateSession(AuthSessionEntity session, UserEntity user, RoleCode roleCode, String clientType) {
        LocalDateTime now = LocalDateTime.now();
        String accessToken = generateToken("atk");
        String refreshToken = generateToken("rtk");

        session.roleCode = roleCode.name();
        session.accessTokenHash = hashToken(accessToken);
        session.accessTokenPreview = preview(accessToken);
        session.refreshTokenHash = hashToken(refreshToken);
        session.clientType = clientType == null || clientType.isBlank() ? session.clientType : clientType;
        session.accessExpiresAt = now.plusHours(ACCESS_TOKEN_HOURS);
        session.refreshExpiresAt = now.plusDays(REFRESH_TOKEN_DAYS);
        session.updatedAt = now;
        session.lastSeenAt = now;
        platformRepository.saveAuthSession(session);

        return new IssuedSession(
                user.id,
                accessToken,
                refreshToken,
                roleCode,
                session.accessExpiresAt,
                session.refreshExpiresAt
        );
    }

    private Optional<String> currentAccessToken() {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (!(attributes instanceof ServletRequestAttributes servletAttributes)) {
            return Optional.empty();
        }
        String header = servletAttributes.getRequest().getHeader("Authorization");
        if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
            return Optional.empty();
        }
        return Optional.of(header.substring(7).trim());
    }

    private Optional<SessionIdentity> resolveIdentity(String accessToken) {
        Optional<AuthSessionEntity> sessionOptional = resolveAccessSession(accessToken);
        if (sessionOptional.isPresent()) {
            AuthSessionEntity session = sessionOptional.get();
            return Optional.of(new SessionIdentity(session.userId, parseRole(session.roleCode)));
        }
        return parseAccessToken(accessToken);
    }

    private Optional<AuthSessionEntity> resolveAccessSession(String accessToken) {
        return platformRepository.findAuthSessionByAccessHash(hashToken(accessToken))
                .filter(session -> session.accessExpiresAt != null && session.accessExpiresAt.isAfter(LocalDateTime.now()))
                .map(session -> {
                    session.lastSeenAt = LocalDateTime.now();
                    return platformRepository.saveAuthSession(session);
                });
    }

    private Optional<AuthSessionEntity> resolveRefreshSession(String refreshToken) {
        return platformRepository.findAuthSessionByRefreshHash(hashToken(refreshToken))
                .filter(session -> session.refreshExpiresAt != null && session.refreshExpiresAt.isAfter(LocalDateTime.now()));
    }

    private Optional<SessionIdentity> parseAccessToken(String token) {
        Matcher matcher = ACCESS_TOKEN_PATTERN.matcher(token == null ? "" : token.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new SessionIdentity(Long.parseLong(matcher.group(1)), parseRole(matcher.group(2))));
    }

    private Optional<SessionIdentity> parseRefreshToken(String token) {
        Matcher matcher = REFRESH_TOKEN_PATTERN.matcher(token == null ? "" : token.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(new SessionIdentity(Long.parseLong(matcher.group(1)), parseRole(matcher.group(2))));
    }

    private SessionIdentity fallbackIdentity(RoleCode fallbackRole) {
        RoleCode roleCode = fallbackRole == null ? RoleCode.USER : fallbackRole;
        UserEntity user = switch (roleCode) {
            case MASTER -> platformRepository.findUser(PlatformDomainSupport.DEFAULT_MASTER_USER_ID)
                    .orElseGet(() -> platformRepository.findFirstUserByRole(RoleCode.MASTER)
                            .orElseGet(() -> platformRepository.findUser(PlatformDomainSupport.DEFAULT_USER_ID).orElseThrow()));
            case ADMIN -> platformRepository.findFirstUserByRole(RoleCode.ADMIN)
                    .orElseGet(() -> platformRepository.findUser(PlatformDomainSupport.DEFAULT_USER_ID).orElseThrow());
            default -> platformRepository.findUser(PlatformDomainSupport.DEFAULT_USER_ID).orElseThrow();
        };
        return new SessionIdentity(user.id, roleCode);
    }

    public RoleCode parseRole(String rawRole) {
        if (rawRole == null || rawRole.isBlank()) {
            return RoleCode.USER;
        }
        return switch (rawRole.toLowerCase()) {
            case "master" -> RoleCode.MASTER;
            case "admin" -> RoleCode.ADMIN;
            case "customer_service" -> RoleCode.CUSTOMER_SERVICE;
            default -> RoleCode.USER;
        };
    }

    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest((token == null ? "" : token).getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("令牌摘要计算失败", exception);
        }
    }

    private String generateToken(String prefix) {
        byte[] random = new byte[24];
        secureRandom.nextBytes(random);
        return prefix + "_" + UUID.randomUUID().toString().replace("-", "")
                + "_" + Base64.getUrlEncoder().withoutPadding().encodeToString(random);
    }

    private String preview(String token) {
        if (token == null || token.isBlank()) {
            return "";
        }
        return token.substring(0, Math.min(12, token.length()));
    }

    public record SessionIdentity(Long userId, RoleCode roleCode) {}

    public record IssuedSession(
            Long userId,
            String token,
            String refreshToken,
            RoleCode roleCode,
            LocalDateTime accessExpiresAt,
            LocalDateTime refreshExpiresAt
    ) {}
}
