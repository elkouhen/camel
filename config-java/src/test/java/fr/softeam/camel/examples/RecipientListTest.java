package fr.softeam.camel.examples;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RecipientListTest extends CamelTestSupport {

	@EndpointInject(uri = "mock:result1")
	protected MockEndpoint resultEndpoint1;

	@EndpointInject(uri = "mock:result2")
	protected MockEndpoint resultEndpoint2;

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

				from("direct://myqueue").multicast().to("mock:result1",
						"mock:result2");
			}
		};
	}

	@Test
	public void test() throws Exception {

		String body = "mymessage";

		resultEndpoint1.expectedBodiesReceived(body);

		resultEndpoint2.expectedBodiesReceived(body);

		template.sendBody("direct://myqueue", body);

		resultEndpoint1.assertIsSatisfied();

		resultEndpoint2.assertIsSatisfied();
	}

}
