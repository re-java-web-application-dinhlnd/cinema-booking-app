package com.re.cinemabookingapp.configuration.security;

import com.re.cinemabookingapp.dto.auth.UserInfoResponseDto;
import com.re.cinemabookingapp.mapper.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if(isAjax){
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            UserInfoResponseDto userDto = userMapper.toDto(userDetails.getUser());

            Map<String, Object> data = new HashMap<>();
            data.put("success", true);
            data.put("message", "Đăng nhập thành công!");
            data.put("user", userDto);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(data));
        }
        else {
            response.sendRedirect("/admin/dashboard");
        }
    }
}
