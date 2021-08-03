package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	
	@Transactional
	public Comment 댓글쓰기(int imageId, String content, int userId) {
		
		//Tip 객체를 직접 만들어서 id값만 담아서 insert 할 수 있다.! 이렇게 하지 않으면 imagerepository.findById() 로 db접근해서 찾아와야한다!
		//어차피 db에 들어가는 값은 id값이고, 우리가 comment에서 필요한것은 pk(id)이기 때문에 괜찮다!
		//대신!!! return시 image객체와 user객체는 id값만 가지고 있는 빈 객체를 리턴받는다.
		
		Image image = new Image();
		image.setId(imageId);
		
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
		
		 Comment comment = new Comment();
		 comment.setContent(content);
		 comment.setImage(image);
		 comment.setUser(userEntity);
		 return commentRepository.save(comment);
	}
	
	@Transactional
	public void 댓글삭제(int id) {
		try {//오류가 터지면 이렇게 try catch로 해준다!
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
	}
}
