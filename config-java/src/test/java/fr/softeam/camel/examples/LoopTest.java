package fr.softeam.camel.examples;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LoopTest extends CamelTestSupport {

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

				from("direct://myqueue").loop(2).to("mock:result1");
			}
		};
	}

	@Test
	public void test() throws Exception {

		template.sendBody("direct://myqueue", "body");

	}

}
