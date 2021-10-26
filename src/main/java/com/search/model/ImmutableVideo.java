package com.search.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

/**
 * Mongo document for the immutable class for the videos
 */
@Getter
@Document("videos")
public final class ImmutableVideo extends YoutubeVideo {
	private String source;
	@CreatedDate
	private Date createdDate;

	public ImmutableVideo(String videoId, String videoUrl, String videoTitle, String videoDescription,
			String thumbnailDefaultURL, String channelId, String channelTitle, long publishedAt, String source) {
		super(videoId, videoUrl, videoTitle, videoDescription, thumbnailDefaultURL, channelId, channelTitle,
				publishedAt);
		this.source = source;
		this.createdDate = new Date();
	}

}
