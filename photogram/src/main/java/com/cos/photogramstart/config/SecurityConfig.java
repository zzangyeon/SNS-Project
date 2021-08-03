package com.cos.photogramstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity//해당 파일로 시큐리티를 활성화 시킨다.(이게 없으면 기존의 시큐리티가 활성화됨)
@Configuration//IOC등록     

public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final OAuth2DetailsService oAuth2DetailsService; 
	
	@Bean
	public BCryptPasswordEncoder encoded() {//암호화 할 떄 사용함.
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	//super.configure(http) 삭제 -> 기존 시큐리티가 가지고 있는 기능이 다 비활성화 됨.
	//403코드 - 미승인 사용자.
	http.csrf().disable();//csrf 토큰검사 비활성화
	http.authorizeRequests()
		.antMatchers("/","/user/**","/image/**","/subscribe/**","/comment/**","/api/**").authenticated()//인증이 필요한 주소
		.anyRequest().permitAll()//그 외의 것들은 모두 허용
		.and()
		.formLogin()//인증이 필요한 저 url(위로세번째 줄)로 접근하면 폼로그인페이지로 옮길건데
		.loginPage("/auth/signin")//GET 그 페이지가 이거임
		.loginProcessingUrl("/auth/signin")//POST로 요청오면 ->spring security가 로그인프로세스진행. 회원가입 컨트롤러는 만들었지만 로그인 컨트롤러는 시큐리티가 해줌!
		//spring security가 로그인 프로세스 진행시 userDetailsService를 실행.(principalDetailsService가 상속받아 @Service등록하여 pds가 로그인 진행)
		.defaultSuccessUrl("/")//로그인성공하면 여기로 가게 할거임
		.and()
		.oauth2Login()//form로그인도 하는데, oauth2로그인도 할거야!
		.userInfoEndpoint()//oauth2로그인하면 최종 응답을 회원정보를 바로 받을 수 있다!(중간과정 너가 알아서해주고 바로 회원정보 내놔)
		.userService(oAuth2DetailsService);
		
	}	

}
