package fr.softeam.camel.examples;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.softeam.camel.examples.util.MySplitter;

public class SplitterTest extends CamelTestSupport {

	@EndpointInject(uri = "mock:result1")
	protected MockEndpoint resultEndpoint1;

	@Override
	protected CamelContext createCamelContext() throws Exception {

		CamelContext camelContext = super.createCamelContext();

		new AnnotationConfigApplicationContext("fr.archetype");

		return camelContext;
	}

	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				from("direct://myqueue").split().method(MySplitter.class)
						.to("mock:result1");
			}
		};
	}

	@Test
	public void test() throws Exception {

		String body = "one,two,three";

		resultEndpoint1.expectedBodiesReceived("one", "two", "three");

		template.sendBody("direct://myqueue", body);

		resultEndpoint1.assertIsSatisfied();
	}

}
