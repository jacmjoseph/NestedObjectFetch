package com.kpmg.project3.NestedObjectFetch;

import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

/**
 * This calls would take in a nested JSON string, convert that into a nested
 * JAVA object and then retrieve the value for the key provided. The JSON and
 * the KEY should only be nested at 3 levels. Example nested JSON:
 * {'a':{'b':{'c':'d'}}} Example nested key: a/b/c
 * 
 */

@SpringBootApplication
public class NestedObjectFetchApplication {

	// private final String nestedStr = "{'a':{'b':{'c':'d'}}}";
	// private final String longKey="a/b/c";

	// private final String DEFAULT_NESTEDSTRING = "{'x':{'y':{'z':'a'}}}";
	// private final String DEFAULT_LONGKEY="x/y/z";

	private String nestedStr;

	private String longKey;

	private final String keyDelimiter = "/";

	public static void main(String[] args) {
		SpringApplication.run(NestedObjectFetchApplication.class, args);
	}

	/**
	 * Constructor for the initialization. It is wired to read from
	 * application.properties
	 * 
	 * @param nestedStr the string that is the nested JSON
	 * @param longKey   the nested string key whose value needs to be retrieved from
	 *                  the nested string
	 */
	public NestedObjectFetchApplication(@Value("${nestedstring}") String nestedStr,
			@Value("${longkey}") String longKey) {
		super();
		this.nestedStr = nestedStr;
		this.longKey = longKey;
	}

	/**
	 * This method retrieves the value of the key from the nested string and returns
	 * the value
	 * 
	 * @return the value of the key retrieved from the nested string
	 */
	@Bean
	public String getKeyValue() {
		Map<String, Map<String, Map<String, String>>> tmp = createNextesObjectsFromJson(nestedStr);
		System.out.println("Pretty JSON:" + new GsonBuilder().setPrettyPrinting().create().toJson(tmp));
		return keyLlookup(longKey, tmp);
	}

	/**
	 * This method deserializes the provided JSON string into nested map objects
	 */

	private Map<String, Map<String, Map<String, String>>> createNextesObjectsFromJson(String nestedStr) {
		Gson gson = new Gson();
		try {
			Type mapType = new TypeToken<Map<String, Map<String, Map<String, String>>>>() {
			}.getType();
			return gson.fromJson(nestedStr, mapType);
		} catch (Exception e) {
			System.out.println(
					"Deserialization to Objects failed. Check if the JSON is nested in 3 levels like {'a':{'b':{'c':'d'}}} ==> "
							+ e);
			throw new CustomException("Deserialization failed", e);
		}
	}

	/**
	 * This method looks up the value for the nested key provided.
	 */
	private String keyLlookup(String longKey, Map<String, Map<String, Map<String, String>>> map) {
		try {
			StringTokenizer tokenizer = new StringTokenizer(longKey, keyDelimiter);
			Map<String, ? extends Object> tmpMap = map;
			int tokenCount = tokenizer.countTokens();

			// Now using a while loop because the last fetch Object is not going to be map.
			for (int i = 0; i < tokenCount - 1; i++) {
				/*
				 * Cannot use generic type safety because the last object is of type Map<String,
				 * String>, as opposed to Map <String, Map>
				 */
				tmpMap = (Map) tmpMap.get(tokenizer.nextElement());
			}

			System.out.println("=================================================");
			String lookedupValue = (String) tmpMap.get(tokenizer.nextElement());
			System.out.println("RETRIEVED VALUE IS:: " + lookedupValue);
			System.out.println("=================================================");
			return lookedupValue;
		} catch (Exception e) {
			System.out.println(
					"Object fetch failed. Check if the JSON is nested in 3 levels like {'a':{'b':{'c':'d'}}} and that the key is nested 3 levels and delimited as in a/b/c ==>"
							+ e);
			throw new CustomException("Object fetch failed", e);
		}
	}
}