package com.search.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.search.utility.ApiKeyUtility;

@Component
public class ErrorHandler {
	private static final Logger logger = LogManager.getLogger(ErrorHandler.class);

	@Autowired
	ApiKeyUtility apiUtility;
	
	public void gsonErrorHandler(GoogleJsonResponseException apiError, YouTube.Search.List search) {
		if (apiError.getDetails().getCode() == 403) {
			logger.error("Your API daily qouta limit has been exceeded, thus switching to next key...");
			search.setKey(apiUtility.fetchNewKey());
		} else {
			logger.error("There was a service error: " + apiError.getDetails().getCode() + " : "
					+ apiError.getDetails().getMessage());
		}
	}

}
