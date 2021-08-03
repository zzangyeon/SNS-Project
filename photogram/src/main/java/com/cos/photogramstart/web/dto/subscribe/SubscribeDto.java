package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscribeDto {
	
	private int id;//toUserId
	private String username;
	private String profileImageUrl;
	private Integer subscribeState;//마리아DB를 사용할때  true값을 받아 오려면 Integer로 받아야함. int는 못 받음		 
	private Integer equalUserState;	
	
}
