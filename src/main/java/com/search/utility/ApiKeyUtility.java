package com.search.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.search.service.VideoSearchService;

@Component
public class ApiKeyUtility {
	private static final Logger logger = LogManager.getLogger(VideoSearchService.class);

	@Value("${youtube.apiKey.baseName}")
	private String apiKeyBaseName;

	@Autowired
	private Environment env;

	// this key keeps the status of which youtube api key is being used currently.
	private static int currentKeyNumber = 1;

	// if api key has exceeed its qouta limits, then change to next key.
	public String fetchNewKey() {
		if (env.getProperty("youtube.apiKey." + ++currentKeyNumber) == null) {
			logger.info("All api keys exhausted, switching back to first key...");
			currentKeyNumber = 1;
			return env.getProperty(apiKeyBaseName + currentKeyNumber);
		}
		return env.getProperty(apiKeyBaseName + currentKeyNumber);
	}

	public String getAPIKey() {
		String keyName = "youtube.apiKey." + currentKeyNumber;
		String keyValue = env.getProperty(keyName);
		return keyValue;
	}
}
