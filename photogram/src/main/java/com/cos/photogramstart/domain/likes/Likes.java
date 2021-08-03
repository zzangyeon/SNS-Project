package com.cos.photogramstart.domain.likes;

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

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
			uniqueConstraints= {
				@UniqueConstraint(
						name = "likes_uk",
						columnNames= {"imageId","userId"} //중복 유니크 제약조건! (1번이미지 - 2번아이디, 1-2 이렇게 중복될 수 없게!)
						)
			}
		)

public class Likes {//n
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JoinColumn(name="imageId")
	@ManyToOne
	private Image image;//1
	
	@JsonIgnoreProperties("images")
	@JoinColumn(name="userId")
	@ManyToOne
	private User user;//1
	
	private LocalDateTime createDate;
	
	@PrePersist//DB에 값이 insert되기 직전에 실행되는 어노테이션
	public void createDate() {
		this.createDate=LocalDateTime.now();
	}

}
