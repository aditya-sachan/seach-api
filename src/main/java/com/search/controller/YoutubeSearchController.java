package com.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.search.model.ImmutableVideo;
import com.search.service.VideoSearchService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@EnableScheduling
public class YoutubeSearchController implements IYoutubeSearchController {
	private static final Logger logger = LogManager.getLogger(YoutubeSearchController.class);

	@Autowired
	VideoSearchService service;

	@Value("${youtube.search.term}")
	private String searchTerm;

	/**
	 * Making a search request to youtube on every fixed interval with a given
	 * search term and then storing that in the DB as an upsert.
	 */
	@Scheduled(fixedRate = 10000)
	@Override
	public void scheduleSearchYouTube() {
		logger.info("Youtube search started with given search parameter... " + this.searchTerm);
		service.youTubeSearch(this.searchTerm);
		return;
	}

	/**
	 * Returning a paginated response to the user sorted on the basis of publishing
	 * date.
	 */
	@Override
	public @ResponseBody List<ImmutableVideo> paginatedVideoData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		logger.info("Fetching of paginated youtube response started...");
		List<ImmutableVideo> result = service.fetchPaginatedResponse(page, size);
		return result;
	}

	/**
	 * A searching api to search a list of video on basis of video titile and
	 * description as provided by user.
	 */
	@Override
	public @ResponseBody List<ImmutableVideo> searchVideo(
			@RequestParam(value = "title", required = false, defaultValue = "") String searchQueryTitle,
			@RequestParam(value = "description", required = false, defaultValue = "") String searchQueryDescription) {
		logger.info("Searching of videos based on title name and description started...");
		return service.fetchQueryVideos(searchQueryTitle, searchQueryDescription);
	}

}
