package com.jojoldu.book.springboot.config.auth;


import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity /*Spring Security 설정 활성화*/
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable() /*h2-console 화면을 사용하기 위해 해당 옵션들을 disable 합니다*/
                .and()
                    .authorizeRequests()/*URL 별 권한관리 quthorizeRequests 필요*/
                    /*andMatchers 권한관리 대상 지정 옵션*/
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()/*전체 열람권한*/
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())/* USER권한을 가진 사람만 가능*/
                    .anyRequest().authenticated()/* 설정 이외 나머지 인증된 사용자(로그인자)*/
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()/*로그인 성공 이후 사용자 정보 가져올떄의 설정*/
                            .userService(customOAuth2UserService);
                            /* userService
                            * 로그인 성공시 후숙조치를 진행할 UserService 인터페이스의 구현체를 등록
                            * 리소스 서버(소셜서비스)에서 사용자 정보를 가져온 상태에서 추가 진행하려는 기능 명시
                            * */

        super.configure(http);
    }
}
