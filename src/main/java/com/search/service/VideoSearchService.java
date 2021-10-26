package com.search.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.search.error.ErrorHandler;
import com.search.model.ImmutableVideo;
import com.search.repository.VideoRepository;
import com.search.utility.RegexUtility;

@Service
public class VideoSearchService implements IVideoSearchService {
	private static final Logger logger = LogManager.getLogger(VideoSearchService.class);

	@Autowired
	VideoRepository videoRepository;

	@Autowired
	YoutubeAPIConfig youtube;

	@Autowired
	ErrorHandler errorHandler;

	@Value("${youtube.url}")
	private static String youtubeUrl;

	public void youTubeSearch(String searchQuery) {
		// initialize the youtbe search api
		youtube.intializeYoutubeAPI();
		YouTube.Search.List search = youtube.fetchYoutubeSearch();
		// set the search parameter that user want to search in the youtube search api
		search.setQ(searchQuery);
		
		try {
			// Call the API and print results.
			List<SearchResult> searchResultList = search.execute().getItems();
			if (searchResultList == null || searchResultList.size() == 0) {
				logger.info("No search results got from YouTube API");
				return;
			}
			// mapping the fetched result into the DB
			searchResultList.forEach(video -> {
				ImmutableVideo item = new ImmutableVideo(video.getId().getVideoId(),
						youtubeUrl + video.getId().getVideoId(), video.getSnippet().getTitle(),
						video.getSnippet().getDescription(), video.getSnippet().getThumbnails().getDefault().getUrl(),
						video.getSnippet().getChannelId(), video.getSnippet().getChannelTitle(),
						video.getSnippet().getPublishedAt().getValue(), "youtube");

				videoRepository.save(item);
			});
		}
		catch (GoogleJsonResponseException apiError) {
			errorHandler.gsonErrorHandler(apiError, search);
		} catch (Throwable error) {
			logger.error("There was a other error: " + error.getMessage() + " : " + error.getLocalizedMessage());
		}
	}

	// return the paginated response sorted by publishedAt date
	public List<ImmutableVideo> fetchPaginatedResponse(int page, int size) {
		// create page request as per the paramters with sorting the publishedAt field
		// in descending order
		Pageable paging = PageRequest.of(page, size, Sort.by("publishedAt").descending());

		return videoRepository.findAll(paging).getContent();
	}

	// return the search response with respect to the given title and description
	public List<ImmutableVideo> fetchQueryVideos(String queryTitle, String queryDescription) {
		// create the regex for title query
		String titleRegex = RegexUtility.createRegex(queryTitle.split(" "));
		// create the regex for description query parameter
		String descriptionRegex = RegexUtility.createRegex(queryDescription.split(" "));

		return videoRepository.findByTitle(titleRegex, descriptionRegex);
	}

}
