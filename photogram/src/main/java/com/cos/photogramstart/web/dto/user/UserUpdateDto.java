package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {

	@NotBlank
	private String name;//필수
	@NotBlank
	private String password;//필수
	private String website;
	private String bio;
	private String phone;
	private String gender;
	
	//필수값과 필수가 아닌 값이 있어서 엔티티로만들기 조금 위험함. 코드수정 필요@@@
	public User toEntity() { //빌더패턴을 위해 User클래스에 @Builder 추가
		return User.builder()
				.name(name)//기재 안하면 문제가됨 =>validation체크 필요
				.password(password)//if 사용자가 패스워드 기재X? ->db에 공백이 들어가버린당.=>validation체크 필요
				.website(website)
				.bio(bio)
				.phone(phone)
				.gender(gender)
				.build();
		//유저객체를 만들어서 정보들을 담고 리턴한다.
	}
	
}
