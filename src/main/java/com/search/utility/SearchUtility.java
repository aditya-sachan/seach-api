package com.search.utility;

import java.util.List;

import org.springframework.stereotype.Component;

import com.search.model.SearchResponse;
import com.search.repository.InMemoryDB;

@Component
public class SearchUtility {
	public boolean dataStoreHasVideo(SearchResponse video) {
		List<SearchResponse> videoData = InMemoryDB.getVideoSearchDataStore();
		for (int i = 0; i < videoData.size(); i++) {
			if(videoData.get(i).getVideoId().equals(video.getVideoId())) {
				return true;
			}
		}
		return false;
	}
}
