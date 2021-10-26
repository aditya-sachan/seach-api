package com.search.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Extends common parameters of a video that can be from any source along with
 * some specific field from the youtube
 */
@Getter
@Setter
public class YoutubeVideo extends VideoCommon {
	String thumbnailDefaultURL;
	String channelId;
	String channelTitle;
	long publishedAt;

	public YoutubeVideo(String videoId, String videoUrl, String videoTitle, String videoDescription,
			String thumbnailDefaultURL, String channelId, String channelTitle, long publishedAt) {
		super(videoId, videoUrl, videoTitle, videoDescription);
		this.thumbnailDefaultURL = thumbnailDefaultURL;
		this.channelId = channelId;
		this.channelTitle = channelTitle;
		this.publishedAt = publishedAt;
	}

}
