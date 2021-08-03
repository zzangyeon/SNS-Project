package com.cos.photogramstart.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private static final long serialVersionUID = 1L;
	
	private User user;
	private Map<String, Object> attributes;

	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	public PrincipalDetails(User user,Map<String, Object>attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	//권한:한 개가 아닐 수 있음. SO,그냥 String으로 리턴하면 안되구 collection으로 리턴해야함!
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collector = new ArrayList<>();
		collector.add(()-> { return user.getRole();	});   //목적은 함수를 매개변수로 넘겨주는것. 자바에서는 그렇게 할 시에 인터페에스로 넘겨줘야되는데 보기 지저분
		//so,람다식으로 깔끔하게 표현할 수 있다.
		return collector;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes; //{id:3434343434, name:김덍떙, email:ㄴㄹㄴㄹ@naver.com}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return (String)attributes.get("name");
	}
	
	

}
