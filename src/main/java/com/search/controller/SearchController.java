package com.search.controller;

import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.search.model.SearchResponse;
import com.search.service.SearchAPI;
import com.search.utility.Configuration;

@RestController
@EnableScheduling
public class SearchController {
	@Autowired
	SearchAPI service;
	
	private static ResourceBundle propertiesBundle;
	
	static {
		propertiesBundle = ResourceBundle.getBundle(Configuration.PROPERTIES_FILENAME);
	}

	@Scheduled(fixedRate = 3000000)
	public void scheduleSearchYouTube() {
		String search = propertiesBundle.getString("youtube.search.term");
		String limitS = propertiesBundle.getString("youtube.search.limit");
		int limit = Integer.parseInt(limitS);
		service.youTubeSearch(search, limit);
	}

	@RequestMapping(value = { "/fetch-video-response" }, method = RequestMethod.GET)
	public @ResponseBody List<SearchResponse> storedVideoData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {

		List<SearchResponse> result = service.fetchPaginatedResponse(page, size);
		return result;
	}

	@RequestMapping(value = { "/search-video" }, method = RequestMethod.GET)
	public @ResponseBody List<SearchResponse> searchVideo(
			@RequestParam(value = "title", required = false, defaultValue = "") String searchQueryTitle,
			@RequestParam(value = "description", required = false, defaultValue = "") String searchQueryDescription) {

		return service.fetchQueryVideos(searchQueryTitle, searchQueryDescription);
	}

}
