package fr.softeam.camel.examples.util;

import org.springframework.stereotype.Service;

@Service
public class MyRouter {
	public String slip(String body) {

		if (body.equals("one")) {
			return "mock:result1";
		} else if (body.equals("two")) {
			return "mock:result2";
		}

		return null;
	}
}
