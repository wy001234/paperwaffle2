package kr.co.paperwaffle.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	
	private final DataSource dataSource;
	
	// 스프링 버전이 업데이트 됨에 따라 WebSecurityConfigurerAdapter이 Deprecated됨 (대체 - @bean, filterchain 등 사용)
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// 인가(접근권한) 설정
		http.authorizeRequests().antMatchers("/").permitAll();
		//http.authorizeRequests().antMatchers("/admin/**").hasAnyRole("2", "7");
		//http.authorizeRequests().antMatchers("/product/cart").hasAnyRole("1","2", "7");
		//http.authorizeRequests().antMatchers("/product/order").hasAnyRole("1","2", "7");
		//http.authorizeRequests().antMatchers("/product/complete").hasAnyRole("1","2", "7");
		
		// 사이트 위조 방지 설정
		http.csrf().disable();
		
		// 자동로그인 설정
		http.rememberMe()
			.userDetailsService(userService)
			.tokenRepository(tokenRepository());

		// 로그인 설정
		http.formLogin()
		.loginPage("/member/login")
		.defaultSuccessUrl("/index")
		.failureUrl("/member/login?success=100")
		.usernameParameter("uid")
		.passwordParameter("pass");
		
		// 로그아웃 설정
		http.logout()
		.invalidateHttpSession(true)
		.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
		.logoutSuccessUrl("/index")
		.deleteCookies("remember-me", "JSESIONID"); // 자동 로그인 쿠키 삭제
		
		return http.build();
	}
	
	@Bean
    public PersistentTokenRepository tokenRepository() {
      // JDBC 기반의 tokenRepository 구현체
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource); // dataSource 주입
        return jdbcTokenRepository;
    }
	
	@Autowired
	private SecurityUserService userService;
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Security 사용자에 대한 권한 설정 (noop은 평문으로 저장해줌)
		//auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("ADMIN");
		//auth.inMemoryAuthentication().withUser("manager").password("{noop}1234").roles("MANAGER");
		//auth.inMemoryAuthentication().withUser("member").password("{noop}1234").roles("MEMBER");
	
		// 로그인 인증 처리 서비스, 암호화 방식 설정(필수 설정)
		auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
}
