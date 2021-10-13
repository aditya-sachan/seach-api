package com.search.utility;

public class Configuration {
	public static final String PROPERTIES_FILENAME = "application";
	public static final String GOOGLE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
	public static final String YOUTUBE_SEARCH_TYPE = "video";
	public static final String YOUTUBE_SEARCH_FIELDS = "items(id/kind,id/videoId,id/channelId,snippet/title,snippet/description,snippet/publishedAt,snippet/channelTitle,snippet/thumbnails/default/url)";
	public static final String YOUTUBE_API_APPLICATION = "google-youtube-api-search";
	public static final String YOUTUBE_APIKEY_ENV = "AIzaSyA3RWwfVVOP1BpM3_U8KdZP37JExXEhZ24";
	public static final String YOUTUBE_PUBLISHED_AFTER_DATE = "2021-06-01T01:00:00.793+05:30";
	public static final long NUMBER_OF_VIDEOS_RETURNED = 25;
	public static int API_KEY_NUMBER = 1;
}
