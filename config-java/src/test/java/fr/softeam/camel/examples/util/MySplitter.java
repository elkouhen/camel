package fr.softeam.camel.examples.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MySplitter {

	public List<String> splitBody(String body) {
		List<String> answer = new ArrayList<String>();
		String[] parts = body.split(",");
		for (String part : parts) {
			answer.add(part);
		}
		return answer;
	}
}
