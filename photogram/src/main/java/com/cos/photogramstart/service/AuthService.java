package com.cos.photogramstart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service//1.ioC로 등록 , 2.트랜잭션 관리
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional//함수가 시작되고 종료될때까지 트랜잭션 관리를 해준다. Write할떄 붙여준다!!(insert.update,delete)
	public User 회원가입(User user) {
		//회원가입 진행@@@@
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);//비밀번호 암호화!
		user.setPassword(encPassword);
		user.setRole("ROLE_USER");
		User userEntity = userRepository.save(user);//매개변수user - 통신을 통해 받은 데이터를 user object 넣은 값이고 userEntity - 데이터베이스에 들어간 뒤 응답 받은 값
		//.save는 jparepository에 존재하는 함수.
		return userEntity;
	}
}
