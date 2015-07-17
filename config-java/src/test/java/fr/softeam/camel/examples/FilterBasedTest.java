package fr.softeam.camel.examples;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FilterBasedTest extends CamelTestSupport {

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

				from("direct://myqueue").filter(
						body().isEqualTo("send to endpoint1")).to(
						"mock:result1");
			}
		};
	}

	@Test
	public void testNok() throws Exception {

		String body = "send to endpoint2";

		resultEndpoint1.expectedBodiesReceived(body);

		template.sendBody("direct://myqueue", body);

		resultEndpoint1.assertIsNotSatisfied();
	}

	@Test
	public void testOk() throws Exception {

		String body = "send to endpoint1";

		resultEndpoint1.expectedBodiesReceived(body);

		template.sendBody("direct://myqueue", body);

		resultEndpoint1.assertIsSatisfied();
	}
}
