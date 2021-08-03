package com.cos.photogramstart.domain.subscribe;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor//전체생성자
@NoArgsConstructor//빈생성자
@Data//getter setter 등
@Entity//디비에 테이블을 생성하는 어노테이션!
@Table(
			uniqueConstraints= {
				@UniqueConstraint(
						name = "subscribe_uk",
						columnNames= {"fromUserId","toUserId"}//중복 유니크 제약조건 설정( 1-3 ,1-3 이렇게 중복되면 안되니까!)
						)
			}
		)//유니크 제약조건을 넣기 위한 것.(그냥 복사해서 쓰면된다!)
public class Subscribe {//subscribe 모델!
	
	@Id//primary key가 필요해서 이용하는 어노
	@GeneratedValue(strategy = GenerationType.IDENTITY)//번호 증가 전략이 데이터베이스를 따라간다.(오라클이면 오라클 마리아면 마리아)
	//테이블의 스키마를 변경하기 위해선 aplication.yml에서 jpa:ddl-auto -> create로 한 뒤(테이블 재 생성?) update로 바꾼 후 insert를 한다.
	private int id;
	

	@JoinColumn(name="fromUserId")//컬럼 이름을 원하는 것을 정하고 싶을때.
	@ManyToOne
	private User fromUser;
	
	@JoinColumn(name="toUserId")
	@ManyToOne
	private User toUser;
	
	private LocalDateTime createDate;
	
	@PrePersist//DB에 값이 insert되기 직전에 실행되는 어노테이션
	public void createDate() {
		this.createDate=LocalDateTime.now();
	}

}
