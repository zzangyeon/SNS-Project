package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor//final 필드 DI할때 사용
@Controller//1.loC등록, 2.파일을 리턴하는 컨트롤러
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	private final AuthService authService;

	//로그인
	@GetMapping("/auth/signin")
	public String signInForm() {
		return "auth/signin";
	}
	
	//회원가입 폼(get요청으로 오면 회원가입 폼을 달라)
	@GetMapping("/auth/signup")
	public String signUpForm() {
		
		return "auth/signup";
	}
	
	//회원가입 하기(post요청으로 오면 회원가입 과정 진행을 해라)
	@PostMapping("/auth/signup")//form으로 올때는 key=value 형태(x-www-form-urlencoded)방식으로 온다
	public String signUp(@Valid SignupDto signupDto,BindingResult bindingResult) {
		//@Valid - pom.xml에 spring-boot-starter-validation라이브러리 추가!
		//@valid걸어놓은 곳에 값이 들어올때 유효성검사를 하겠다는것!
		//signupDto에서 에러가 발생하면 bindingResult에 모두 모아준다.
		
			User user = signupDto.toEntity();// User <- SignupDto
			log.info(user.toString());
			User userEntity = authService.회원가입(user);
			System.out.println(userEntity);
			return "auth/signin";
		
		 
	}
	
}
