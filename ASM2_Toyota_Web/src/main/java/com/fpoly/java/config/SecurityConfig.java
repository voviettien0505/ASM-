package com.fpoly.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Cho phép truy cập công khai đến tài nguyên tĩnh, WebSocket và endpoint gửi thông báo
                        .requestMatchers("/css/**", "/js/**", "/ws/**", "/order/sendNotification").permitAll()

                        // Không yêu cầu xác thực cho trang đăng ký và đăng nhập
                        .requestMatchers("/register", "/login").not().authenticated()

                        // Chỉ cho phép người dùng với vai trò ADMIN truy cập các trang quản lý
                        .requestMatchers("/category/admin/**", "/product/admin/**", "/user/admin/**", "/order/admin/**").hasRole("ADMIN")

                        // Chỉ cho phép người dùng với vai trò USER truy cập giỏ hàng và trang cập nhật thông tin cá nhân
                        .requestMatchers("/cart/**", "/user/update/**").hasRole("USER")

                        // Chỉ cho phép người dùng có vai trò USER hoặc ADMIN truy cập trang chính
                        .requestMatchers("/home/index/**").hasAnyRole("USER", "ADMIN")

                        // Tất cả các yêu cầu khác yêu cầu xác thực
                        .anyRequest().authenticated()
                )
                // Cấu hình trang đăng nhập
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/redirectAfterLogin", true)
                        .permitAll()
                )
                // Cấu hình đăng xuất
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home/index")
                        .permitAll()
                )
                // Xử lý lỗi truy cập bị từ chối
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler(accessDeniedHandler())
                )
                // Vô hiệu hóa CSRF cho WebSocket và endpoint gửi thông báo
                .csrf(csrf -> csrf.ignoringRequestMatchers("/ws/**", "/order/sendNotification"))
                ;

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/access-denied");
        return accessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

