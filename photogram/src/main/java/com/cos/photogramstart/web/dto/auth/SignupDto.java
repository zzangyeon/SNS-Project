package com.cos.photogramstart.web.dto.auth;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data //Getter,Setter,등     signup-회원가입
public class SignupDto {
	
	@Size(min=2,max=18) //size,notblank등 - 전처리(유효성검사) / 회원가입은 핵심로직 , 전처리 후처리는 공통로직 -> AOP관점지향
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;
	@NotBlank
	private String name;

	public User toEntity() { //빌더패턴을 위해 User클래스에 @Builder 추가
		return User.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.build();
		//유저객체를 만들어서 정보들을 담고 리턴한다.
	}
}
