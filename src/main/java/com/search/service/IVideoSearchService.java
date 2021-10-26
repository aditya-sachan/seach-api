package com.search.service;

import java.util.List;

import com.search.model.ImmutableVideo;

public interface IVideoSearchService {
	public void youTubeSearch(String searchQuery);
	
	public List<ImmutableVideo> fetchPaginatedResponse(int page, int size);
	
	public List<ImmutableVideo> fetchQueryVideos(String queryTitle, String queryDescription);
}
