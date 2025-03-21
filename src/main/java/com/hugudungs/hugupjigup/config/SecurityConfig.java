package com.hugudungs.hugupjigup.config;

import com.hugudungs.hugupjigup.auth.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 모든 요청에 대한 접근 제어 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api-docs/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll() // Swagger 관련 경로는 모두 허용
                        .anyRequest().permitAll() // 나머지 요청도 모두 허용 (개발 단계에서)
                )
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트 환경에서만 사용)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // X-Frame-Options SAMEORIGIN 설정
                        .cacheControl(cache -> cache.disable()) // Cache-Control 헤더 비활성화
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
                        )
                );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of("*")); // 모든 Origin 허용 (개발 단계에서만 사용)
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(java.util.List.of("*")); // 모든 헤더 허용
        configuration.setExposedHeaders(java.util.List.of("Authorization", "Link", "X-Total-Count")); // 클라이언트가 접근 가능한 헤더 추가 (필요시)
        configuration.setAllowCredentials(false); // 인증 정보 포함 여부 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new CustomOAuth2UserService(); // 사용자 정의 구현체
    }
}