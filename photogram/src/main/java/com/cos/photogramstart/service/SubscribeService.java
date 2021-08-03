package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em;//모든 Repository 는 EntityManager를 구형해서 만들어져 있는 구현체이다. 여기에서는 직접 구현하겠다!.
	
	@Transactional//insert delete등 데이터베이스에 영향을 주니까 트랜잭션화!
	public void 구독하기(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);	
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void 구독취소하기(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
	
	@Transactional(readOnly = true)
	public List<SubscribeDto>구독리스트(int principalId, int pageUserId){
		
		//쿼리준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id,u.username,u.profileImageUrl, "); //끝에 한 칸을 띄워야 오류가 안 난다.
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id),1,0)subscribeState, ");
		sb.append("if((? = u.id),1,0)equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId=?");//마지막에 세미콜론 첨부하면 안 됨!
		
		//1. principalId
		//2. principalId
		//3. pageUserId
		
		//쿼리 완성
		Query query = em.createNativeQuery(sb.toString())//Query - java.persistence.query!!!!! 아니면 망함!
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		
		//쿼리 실행
		JpaResultMapper result = new JpaResultMapper();//qlrm라이브러리 필요! Dto에 DB결과를 매핑하기 위해서!(도메인 모델이 아닌 다른 값들을dto로 받아올때)
		List<SubscribeDto>subscribeDtos = result.list(query, SubscribeDto.class);
		return subscribeDtos;
	}
}
