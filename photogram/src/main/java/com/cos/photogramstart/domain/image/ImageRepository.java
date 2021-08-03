package com.cos.photogramstart.domain.image;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Integer>{

	@Query(value = "SELECT * FROM image WHERE userId IN((SELECT toUserId FROM subscribe WHERE fromUserId = :principalId))", nativeQuery = true)
	List<Image> mStory(int principalId,Pageable pageable);

	@Query(value = "SELECT i.* FROM image i INNER JOIN (SELECT imageId,COUNT(imageId) likeCount FROM likes GROUP BY imageId)l ON i.id = l.imageId ORDER BY likeCount DESC",nativeQuery = true)
	List<Image> mPopular();
}
