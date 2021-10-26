package com.search.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.search.utility.ApiKeyUtility;

@Service
public class YoutubeAPIConfig {
	private static final Logger logger = LogManager.getLogger(YoutubeAPIConfig.class);

	public HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public JsonFactory JSON_FACTORY = new JacksonFactory();

	private YouTube youtube;
	private YouTube.Search.List search;

	@Value("${youtube.api.application}")
	private String apiApplication;

	@Value("${youtube.search.type}")
	private String searchType;

	@Value("${youtube.publishedAfter}")
	private String publishedAfter;

	@Value("${youtube.fields}")
	private String youTubeFields;

	@Value("${youtube.max.video}")
	private String maxVideosReturned;
	
	@Autowired
	ApiKeyUtility apiUtility;

	public void intializeYoutubeAPI() {
		// Define the API request for retrieving search results.
		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName(apiApplication).build();

		try {
			search = youtube.search().list("id,snippet");
			
			// Set the api key for the youtube search api
			search.setKey(apiUtility.getAPIKey());
			// Restrict the search results to include videos.
			search.setType(this.searchType);
			// Set fetch query result on after a certain date
			DateTime date = new DateTime(this.publishedAfter);
			search.setPublishedAfter(date);
			// To increase efficiency, only retrieve the fields that the
			// application uses.
			search.setFields(this.youTubeFields);
			// Set query max result to be given number
			search.setMaxResults(Long.valueOf(this.maxVideosReturned));

		} catch (IOException e1) {
			logger.error("There was an IO error: " + e1.getCause() + " : " + e1.getMessage());
		}
	}

	// returning the search type from youtube config
	public YouTube.Search.List fetchYoutubeSearch() {
		return search;
	}

}
