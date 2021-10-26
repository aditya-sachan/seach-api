package com.search.utility;

public class RegexUtility {

	// helper function to create a regex from a given list of words
	public static String createRegex(String[] words) {
		String regex = "";
		String regexBase = "(?=.*\\b{}\\b)";

		// replacing the {} in the regexBase with the word in the array
		for (String word : words) {
			regex = regex + regexBase.replace("{}", word);
		}
		return "^" + regex;
	}

}
