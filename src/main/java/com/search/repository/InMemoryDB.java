package com.search.repository;

import java.util.ArrayList;
import java.util.List;

import com.search.model.SearchResponse;

public class InMemoryDB {
	private static List<SearchResponse> videoSearchDataStore = new ArrayList<>();

	public static List<SearchResponse> getVideoSearchDataStore() {
		return videoSearchDataStore;
	}

	public static void addVideoSearchDataStore(List<SearchResponse> videoSearchDataStore) {
		InMemoryDB.videoSearchDataStore.addAll(videoSearchDataStore);
	}
	
	public static void addVideoSearchDataStore(SearchResponse videoSearchDataStore) {
		InMemoryDB.videoSearchDataStore.add(videoSearchDataStore);
	}
}
