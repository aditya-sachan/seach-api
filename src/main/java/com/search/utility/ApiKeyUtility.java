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

	@Value("${youtube.apiKey.number}")
	private int keyNumber;
	
	@Autowired
	private Environment env;
	
	private static int currentKeyNumber = 1;
	
	public String fetchNewKey() {
		String keyName = "youtube.apiKey." + ++currentKeyNumber;
		String keyValue = env.getProperty(keyName);
		if(keyValue == null) {
			logger.info("All api keys exhausted, switching to first key...");
			currentKeyNumber = 1;
			keyName = "youtube.apiKey." + currentKeyNumber;
			return env.getProperty(keyName);
		}
		return keyValue;
	}
	
	public String getAPIKey() {
		String keyName = "youtube.apiKey." + currentKeyNumber;
		System.out.println(keyName);
		String keyValue = env.getProperty(keyName);
		return keyValue;
	}
}
