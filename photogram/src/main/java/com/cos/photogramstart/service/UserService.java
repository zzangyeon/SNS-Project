package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final SubscribeRepository subscribeRepository;
	
	@Value("${file.path}")//application.yml 의 file path를 불러오는것!! 전역적으로 쓸 수 있기 때문에 좋다! 문법 알자!
	private String uploadFolder;
	
	@Transactional(readOnly = true)//select시에는 이르케 걸어주면 좋다~
	public UserProfileDto 회원프로필(int pageUserId,int principalId) {
		UserProfileDto dto = new UserProfileDto();
		
		//SELET * FROM image WHERE userId = :userId)
		User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});//없는id번호로 /user/4 검색할 수도 있기 때문에 예외처리해줘야함! db를 거쳐야 하기 때문에 후처리!
		
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId);
		dto.setImageCount(userEntity.getImages().size());
		
		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount =subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);
		
		return dto;
	}
	
	@Transactional
	public User 회원수정(int id, User user) {
		//1.영속화 -db에서 findbyid 하면 중간에 영속화컨텍스트에 저장(영속화)가 된다! (영속화된 데이터를 수정하면 자동으로 db에 반영이 된다!)
		User userEntity = userRepository.findById(id).orElseThrow(() -> {return new CustomValidationApiException("찾을 수 없는 id입니다.");});
		//Optional이라는 기능을 자바에서 제공.
		//옵셔널 세가지 중 두가지! 1) 무조건 찾았다. 걱정마 = .get() 2)못찾았어 exception발동시킬게 = .orElseThrow()
		//저 위에는 람다식으로 작성한것!
		
		//2.영속화된 오브젝트를 수정 -> 더티체킹 -> 업데이트 완료
		userEntity.setName(user.getName());
		
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		return userEntity;
		//더티체킹이 일어나서 업데이트가 완료됨.
	}

	@Transactional
	public User 회원프로필사진병경(int principalId, MultipartFile profileImageFile) {
		
		UUID uuid = UUID.randomUUID();
		String imagefileName = uuid+"_"+profileImageFile.getOriginalFilename();
		
		Path imageFilePath = Paths.get(uploadFolder+imagefileName);
		
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
		});
		userEntity.setProfileImageUrl(imagefileName);
		
		return userEntity;
	}//더티체킹으로 업데이트 됨!
	
	
}
