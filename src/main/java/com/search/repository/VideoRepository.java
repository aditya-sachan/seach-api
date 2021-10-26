package com.search.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.search.model.ImmutableVideo;

public interface VideoRepository extends MongoRepository<ImmutableVideo, String> {

	// perform a partial search in videos collection on video title and video description
	@Query(value = "{'$and':[ {'videoTitle': {$regex : ?0, $options: 'i'}}, {'videoDescription': {$regex : ?1, $options: 'i'}} ] }")
	public List<ImmutableVideo> findByTitle(String titleRegex, String descriptionRegex);
	
}
