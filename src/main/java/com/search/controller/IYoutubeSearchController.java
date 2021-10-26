package com.search.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.search.model.ImmutableVideo;

public interface IYoutubeSearchController {

	public void scheduleSearchYouTube();

	@RequestMapping(value = { "/fetch-video-response" }, method = RequestMethod.GET)
	public @ResponseBody List<ImmutableVideo> paginatedVideoData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size);

	@RequestMapping(value = { "/search-video" }, method = RequestMethod.GET)
	public @ResponseBody List<ImmutableVideo> searchVideo(
			@RequestParam(value = "title", required = false, defaultValue = "") String searchQueryTitle,
			@RequestParam(value = "description", required = false, defaultValue = "") String searchQueryDescription);
}
