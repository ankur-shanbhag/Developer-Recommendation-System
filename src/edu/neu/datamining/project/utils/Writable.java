package edu.neu.datamining.project.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Writable {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader("data/finalOutputWithAllVersionsIncludingVerified.csv"));

		String[] headers = reader.readLine().toLowerCase().trim().split("\\s*,\\s*");
		String line = null;

		System.out.println(Arrays.toString(headers) + "\n " + headers.length);
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new LinkedHashMap<>();

		System.out.println("Started ");
		int count = 0;
		PrintWriter writer = new PrintWriter("data/bug-data-all-versions.json");

		Map<Integer, Integer> maps = new HashMap<>();
		while ((line = reader.readLine()) != null) {
			try {
				String[] split = line.toLowerCase().trim().split("\\s*,\\s*");

				if (maps.containsKey(split.length)) {
					maps.put(split.length, maps.get(split.length) + 1);
				} else {
					maps.put(split.length, 1);
				}

				if (split.length != 50)
					continue;

				for (int i = 0; i < split.length; i++) {
					map.put(headers[i], split[i]);
				}

				// System.out.println(mapper.writeValueAsString(map));
				writer.println(mapper.writeValueAsString(map));
				writer.flush();
				count++;
			} catch (JsonGenerationException e) {
				System.out.println("Generate");
			} catch (JsonMappingException e) {
				System.out.println("mapping");
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		System.out.println(maps);
		System.out.println("Done : " + count);
		writer.close();
		reader.close();
	}

}
