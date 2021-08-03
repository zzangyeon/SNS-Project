package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cos.photogramstart.domain.user.User;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{ //subscribe모델로 리턴을 받는다. 옆에 integer는 ㅜ머지??

	//:fromUserId(문법임) 아래 int fromUserId를 바인딩해서 넣는 것.
	@Modifying//insert delete update를 native query로 작성하려면 해당 어노테이션이 필요!!!
	@Query(value="INSERT INTO subscribe(fromUserId,toUserId,createDate) VALUES (:fromUserId,:toUserId,now())",nativeQuery=true)
	void mSubscribe(int fromUserId, int toUserId);//변경된 행의 갯수 리턴됨, 실패시 -1리턴됨, 0은 변경된 행이 없음.
	
	@Modifying
	@Query(value="DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserID = :toUserId",nativeQuery=true)
	void mUnSubscribe(int fromUserId, int toUserId);
	
	@Query(value="SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
	int mSubscribeState(int principalId, int pageUserId);
	
	@Query(value="SELECT COUNT(*) FROM subscribe WHERE fromUserId = :pageUserId", nativeQuery = true)
	int mSubscribeCount(int pageUserId);
	
	
	
}
