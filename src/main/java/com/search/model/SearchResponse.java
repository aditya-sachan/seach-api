package com.search.model;

import com.google.api.client.util.DateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class SearchResponse {
	private String videoId;
    private String url;
    private String title;
    private String thumbnailDefaultURL;
    private String description;
    private String channelId;
    private String channelTitle;
    private DateTime publishedAt;
}
