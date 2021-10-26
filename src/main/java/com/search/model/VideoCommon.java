package com.search.model;

import org.springframework.data.mongodb.core.mapping.MongoId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Contains common field of a video that can be from any source.
 */
@Getter
@Setter
@AllArgsConstructor
public class VideoCommon {
	@MongoId
	private String videoId;
	private String videoUrl;
	private String videoTitle;
	private String videoDescription;
}
