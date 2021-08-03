package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {

	private boolean PageOwnerState;
	private int imageCount;    //뷰페이지에서 dto.user.images.size()이렇게 연산하는건 좋은방법이 아니다. 최대한 연산을 다 해서 값을 뷰페이지로 넘기는 방향으로 프로그래밍해야한다. 
	private boolean subscribeState;
	private int subscribeCount;
	private User user;
	
}
