package com.cos.photogramstart.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{

	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oauth2User = super.loadUser(userRequest);
		Map<String, Object> userInfo = oauth2User.getAttributes();
		
		String username = "facebook_"+(String)userInfo.get("id");
		String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
		String email = (String)userInfo.get("email");
		String name = (String)userInfo.get("name"); //object타입이라 String으로 다운캐스팅!
		
		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {//페이스북으로 최초 로그인
			
			User user = User.builder()
				.username(username)
				.password(password)
				.email(email)
				.name(name)
				.role("ROLE_USER")//안 넣으면 오류남
				.build();
				
			return  new PrincipalDetails(userRepository.save(user),userInfo);//리턴시 그 정보 세선에 저장!
			
		}else {//페이스북으로 이미 회원가입이 되어 있음
			return new PrincipalDetails(userEntity,userInfo);
		}
		
		
		
	}
	

}
