package com.re.cinemabookingapp.configuration.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (isAjax) {
            // Customer login qua modal
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\": false, \"message\": \"Sai tài khoản hoặc mật khẩu!\"}");
        } else {
            // Admin/Staff login qua trang riêng — giữ lại username đã nhập
            String referer = request.getHeader("Referer");
            String username = request.getParameter("username");
            String encodedUsername = URLEncoder.encode(username != null ? username : "", StandardCharsets.UTF_8);

            String basePath = "/admin/login";
            if (referer != null && referer.contains("/staff/login")) {
                basePath = "/staff/login";
            }

            response.sendRedirect(basePath + "?error=true&username=" + encodedUsername);
        }
    }
}
