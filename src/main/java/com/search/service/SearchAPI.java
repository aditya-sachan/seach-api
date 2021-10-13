package com.search.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.search.model.SearchResponse;
import com.search.repository.InMemoryDB;
import com.search.utility.Configuration;
import com.search.utility.SearchUtility;

@Service
public class SearchAPI {

	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static YouTube youtube;
	private static YouTube.Search.List search;
	private static ResourceBundle propertiesBundle;

	@Autowired
	SearchUtility utility;

	static {

		propertiesBundle = ResourceBundle.getBundle(Configuration.PROPERTIES_FILENAME);

		// This object is used to make ` requests. The last
		// argument is required, but since we don't need anything
		// initialized when the HttpRequest is initialized, we override
		// the interface and provide a no-op function.

		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
				// intentionally left empty
			}
		}).setApplicationName(Configuration.YOUTUBE_API_APPLICATION).build();

		// Define the API request for retrieving search results.
		try {
			search = youtube.search().list("id,snippet");
		} catch (IOException e1) {
			System.out.println("There was an IO error: " + e1.getCause() + " : " + e1.getMessage());
		}

	}

	/**
	 * Makes YouTube search into video library with given keywords.
	 *
	 */
	public void youTubeSearch(String searchQuery, int maxSearch) {

		List<SearchResponse> rvalue = new ArrayList<SearchResponse>();
		if (youtube != null || search != null) {

			// Set your developer key from the {{ Google Cloud Console }} for
			// non-authenticated requests. See:
			// {{ https://cloud.google.com/console }}

			String keyName = "youtube.apiKey." + Configuration.API_KEY_NUMBER;
			String apiKey = propertiesBundle.getString(keyName);

			if (apiKey == null) {
				apiKey = System.getenv(Configuration.YOUTUBE_APIKEY_ENV);
			}
			System.out.println("AIzaSyA3RWwfVVOP1BpM3_U8KdZP37JExXEhZ24");
			System.out.println(apiKey);
			System.out.println(apiKey.equals("AIzaSyA3RWwfVVOP1BpM3_U8KdZP37JExXEhZ24"));

			search.setKey(apiKey);
			search.setQ(searchQuery);

			// Restrict the search results to include videos.
			search.setType(Configuration.YOUTUBE_SEARCH_TYPE);

			String publishedAfter = propertiesBundle.getString("youtube.publishedAfter");
			if (publishedAfter == null) {
				publishedAfter = System.getenv(Configuration.YOUTUBE_PUBLISHED_AFTER_DATE);
			}
			DateTime date = new DateTime(publishedAfter);

			search.setPublishedAfter(date);

			// To increase efficiency, only retrieve the fields that the
			// application uses.

			String youTubeFields = propertiesBundle.getString("youtube.fields");

			if (youTubeFields != null && !youTubeFields.isEmpty()) {
				search.setFields(youTubeFields);
			} else {
				search.setFields(Configuration.YOUTUBE_SEARCH_FIELDS);
			}

			if (maxSearch < 1) {
				String maxVideosReturned = propertiesBundle.getString("youtube.maxvid");

				if (maxVideosReturned != null && !maxVideosReturned.isEmpty()) {
					search.setMaxResults(Long.valueOf(maxVideosReturned));
				} else {
					search.setMaxResults(Configuration.NUMBER_OF_VIDEOS_RETURNED);
				}
			} else {
				search.setMaxResults((long) maxSearch);
			}

			try {
				// Call the API and print results.
				SearchListResponse searchResponse = search.execute();
				List<SearchResult> searchResultList = searchResponse.getItems();

				if (searchResultList != null && searchResultList.size() > 0) {

					for (SearchResult r : searchResultList) {
						SearchResponse item = new SearchResponse(r.getId().getVideoId(),
								Configuration.GOOGLE_YOUTUBE_URL + r.getId().getVideoId(), r.getSnippet().getTitle(),
								r.getSnippet().getThumbnails().getDefault().getUrl(), r.getSnippet().getDescription(),
								r.getSnippet().getChannelId(), r.getSnippet().getChannelTitle(),
								r.getSnippet().getPublishedAt());
						if (!utility.dataStoreHasVideo(item))
							rvalue.add(item);
					}

				} else {
					System.out.println("No search results got from YouTube API");
				}
			}

			catch (GoogleJsonResponseException t) {

				if (t.getMessage().contains("exceeded") && t.getMessage().contains("qouta")) {
					System.out.println("Quota exceeded for the given api key, switching to another key");
					Configuration.API_KEY_NUMBER++;
					String nextApiKey = propertiesBundle.getString("youtube.apiKey." + Configuration.API_KEY_NUMBER);
					if (nextApiKey == null) {
						// Go back to first API key
						nextApiKey = propertiesBundle.getString("youtube.apiKey.1");
						if (nextApiKey == null)
							nextApiKey = System.getenv(Configuration.YOUTUBE_APIKEY_ENV);
					}

					search.setKey(nextApiKey);
				} else {
					System.out.println("There was a service error: " + t.getDetails().getCode() + " : "
							+ t.getDetails().getMessage());
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("YouTube API not initialized correctly! or API request undefined");
		}

		InMemoryDB.addVideoSearchDataStore(rvalue);
	}

	// return the paginated response
	public List<SearchResponse> fetchPaginatedResponse(int page, int size) {
		List<SearchResponse> videoData = InMemoryDB.getVideoSearchDataStore();

		// Sort the video data collection with published date
		Collections.sort(videoData,
				(o1, o2) -> (int) (o1.getPublishedAt().getValue() - o2.getPublishedAt().getValue()));

		if ((page - 1) * size < videoData.size() && page * size <= videoData.size())
			return videoData.subList((page - 1) * size, page * size);
		else if ((page - 1) * size < videoData.size() && (page + 1) * size > videoData.size())
			return videoData.subList(page * size, videoData.size());
		else
			return new ArrayList<>();
	}

	// return the search response with respect to the given title and description
	public List<SearchResponse> fetchQueryVideos(String queryTitle, String queryDescription) {

		// if both title and description are blank then return empty list
		if (queryTitle.isBlank() && queryDescription.isBlank())
			return new ArrayList<>();

		List<SearchResponse> videoData = InMemoryDB.getVideoSearchDataStore();
		String[] splitTitle = queryTitle.split(" ");
		String[] splitDescription = queryDescription.split(" ");
		List<SearchResponse> result = new ArrayList<>();
		videoData.forEach(video -> {
			System.out.println(video.getTitle());

			// if tile is not blank and description is blank
			if (!queryTitle.isBlank() && queryDescription.isBlank()) {
				Boolean flag = true;
				System.out.println(splitTitle.length + " - " + splitTitle[0]);
				for (int i = 0; i < splitTitle.length; i++) {
					System.out.println(video.getTitle().toLowerCase().contains(splitTitle[i].toLowerCase()));
					if (!video.getTitle().toLowerCase().contains(splitTitle[i].toLowerCase())) {
						flag = false;
						break;
					}
				}
				if (flag)
					result.add(video);
			}

			// if tile is blank and description is not blank
			else if (queryTitle.isBlank() && !queryDescription.isBlank()) {
				System.out.println("2");
				Boolean flag = true;
				for (int i = 0; i < splitDescription.length; i++) {
					if (!video.getTitle().toLowerCase().contains(splitDescription[i].toLowerCase())) {
						flag = false;
						break;
					}
				}
				if (flag)
					result.add(video);
			}

			// if both title and description are not blank
			else if (!queryTitle.isBlank() && !queryDescription.isBlank()) {
				System.out.println("3");
				Boolean flag = true;
				for (int i = 0; i < splitTitle.length; i++) {
					if (!video.getTitle().toLowerCase().contains(splitTitle[i].toLowerCase())) {
						flag = false;
						break;
					}
				}
				for (int i = 0; i < splitDescription.length; i++) {
					if (!video.getTitle().toLowerCase().contains(splitDescription[i].toLowerCase())) {
						flag = false;
						break;
					}
				}
				if (flag)
					result.add(video);
			}
		});

		return result;
	}
}
