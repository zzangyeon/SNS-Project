package com.cos.photogramstart.domain.user;

//JPA - Java Persistence API(자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공)
//ORM - object relative mapping - 자바에서 클래스를 만들면 관계형 dB에 테이블이 만들어져 매핑이 된다.

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor//전체생성자
@NoArgsConstructor//빈생성자
@Data//getter setter 등
@Entity//디비에 테이블을 생성하는 어노테이션!
public class User {//User 모델!
	
	@Id//primary key가 필요해서 이용하는 어노
	@GeneratedValue(strategy = GenerationType.IDENTITY)//번호 증가 전략이 데이터베이스를 따라간다.(오라클이면 오라클 마리아면 마리아)
	//테이블의 스키마를 변경하기 위해선 aplication.yml에서 jpa:ddl-auto -> create로 한 뒤(테이블 재 생성?) update로 바꾼 후 insert를 한다.
	private int id;
	@Column(length = 100, unique = true)//@Column - db제약조건  //oauth2로그인을 위해 컬럼 늘리기!
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;
	private String website;
	private String bio;//자기소개
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl;//유저사진
	private String role;//권한
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)//mappedby - 나는 연관관계의 주인이 아니다. 라는뜻!	그러므로 테이블에 컬럼을 만들지마!
	@JsonIgnoreProperties({"user"})//JSON으로 파싱시 Image에 "user"는 파싱을 하지 않는다(getter 호출x)
	private List<Image> images;    //& User를 select할 때 해당 User id로 등록된 image들을 다 가져와!
	//fetchtype.lazy가 디폴트값! - User를 Select할 때 해당 User id로 등록된 image들을 가져오지마! , 대신 getImages()함수의 image들이 호출될 때 가져와!
	//fetchtype.Eager - User를 Select할 때 해당 User id로 등록된 image들을 전부 Join해서 가져와!
	//이것을 양방향 매핑 @@@!!!이라고 한다!
	// onetomany - lazy 전략. manytoone - eager전략!!!!
	
	private LocalDateTime createDate;
	
	@PrePersist//DB에 값이 insert되기 직전에 실행되는 어노테이션
	public void createDate() {
		this.createDate=LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate=" + createDate + "]";
	}
	
	

}
