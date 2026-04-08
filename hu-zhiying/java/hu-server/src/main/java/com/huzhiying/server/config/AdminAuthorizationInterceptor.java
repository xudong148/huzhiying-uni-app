package com.huzhiying.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.server.persistence.PersistenceEntities;
import com.huzhiying.server.repository.PlatformRepository;
import com.huzhiying.server.service.AuthSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private static final Pattern FORCE_ASSIGN_PATH = Pattern.compile("^/api/admin/dispatch/[^/]+/assign$");
    private static final Pattern CANCEL_ORDER_PATH = Pattern.compile("^/api/admin/orders/[^/]+/cancel$");
    private static final Pattern REFUND_ORDER_PATH = Pattern.compile("^/api/admin/orders/[^/]+/refund$");
    private static final Pattern APPROVE_FINANCE_PATH = Pattern.compile("^/api/admin/finance/[^/]+/approve$");

    private final AuthSessionService authSessionService;
    private final PlatformRepository platformRepository;
    private final ObjectMapper objectMapper;

    public AdminAuthorizationInterceptor(AuthSessionService authSessionService,
                                         PlatformRepository platformRepository,
                                         ObjectMapper objectMapper) {
        this.authSessionService = authSessionService;
        this.platformRepository = platformRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthSessionService.SessionIdentity identity = authSessionService.currentIdentityOptional().orElse(null);
        if (identity == null) {
            writeFailure(response, HttpStatus.UNAUTHORIZED, "后台登录已失效，请重新登录");
            return false;
        }

        PersistenceEntities.UserEntity user = platformRepository.findUser(identity.userId()).orElse(null);
        if (user == null || (user.enabled != null && !user.enabled)) {
            writeFailure(response, HttpStatus.UNAUTHORIZED, "当前账号不可用");
            return false;
        }

        if (identity.roleCode() != RoleCode.ADMIN) {
            writeFailure(response, HttpStatus.FORBIDDEN, "当前账号没有后台访问权限");
            return false;
        }

        String requiredPermission = resolveRequiredPermission(request);
        if (requiredPermission == null) {
            return true;
        }

        Set<String> permissions = collectPermissionCodes(user.id);
        if (!permissions.contains(requiredPermission)) {
            writeFailure(response, HttpStatus.FORBIDDEN, "缺少权限: " + requiredPermission);
            return false;
        }
        return true;
    }

    private Set<String> collectPermissionCodes(Long userId) {
        List<Long> roleIds = platformRepository.listUserRoles(userId).stream()
                .map(binding -> binding.roleId)
                .toList();
        return platformRepository.listRolesByIds(roleIds).stream()
                .filter(role -> role.enabled == null || role.enabled)
                .flatMap(role -> platformRepository.listRolePermissionBindings(role.id).stream())
                .map(binding -> binding.permissionId)
                .distinct()
                .map(permissionId -> platformRepository.findById(PersistenceEntities.PermissionEntity.class, permissionId).orElse(null))
                .filter(permission -> permission != null && (permission.enabled == null || permission.enabled))
                .map(permission -> permission.code)
                .collect(java.util.stream.Collectors.toSet());
    }

    private String resolveRequiredPermission(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        if (!"POST".equalsIgnoreCase(method) && !"PUT".equalsIgnoreCase(method) && !"DELETE".equalsIgnoreCase(method)) {
            return null;
        }
        if (FORCE_ASSIGN_PATH.matcher(path).matches()) {
            return "dispatch:force-assign";
        }
        if (CANCEL_ORDER_PATH.matcher(path).matches()) {
            return "order:cancel";
        }
        if (REFUND_ORDER_PATH.matcher(path).matches() || APPROVE_FINANCE_PATH.matcher(path).matches()) {
            return "finance:refund-audit";
        }
        if (path.startsWith("/api/admin/content/")) {
            return "content:publish";
        }
        return null;
    }

    private void writeFailure(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), ApiResponse.fail(message));
    }
}
