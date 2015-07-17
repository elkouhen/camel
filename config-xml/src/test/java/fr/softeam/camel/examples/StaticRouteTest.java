package fr.softeam.camel.examples;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StaticRouteTest extends CamelSpringTestSupport {

	// expected message bodies
	protected Object[] expectedBodies = {
			"<something id='1'>expectedBody1</something>",
			"<something id='2'>expectedBody2</something>" };
	// templates to send to input endpoints
	@Produce(uri = "file:c:\\temp\\camel-in")
	protected ProducerTemplate inputEndpoint;
	// mock endpoints used to consume messages from the output endpoints and
	// then perform assertions
	@EndpointInject(uri = "mock:output")
	protected MockEndpoint outputEndpoint;

	@Test
	public void testCamelRoute() throws Exception {
		// lets route from the output endpoints to our mock endpoints so we can
		// assert expectations
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("file:c:\\temp\\camel-out").to(outputEndpoint);
			}
		});

		// define some expectations
		outputEndpoint.expectedBodiesReceivedInAnyOrder(expectedBodies);

		// send some messages to input endpoints
		for (Object expectedBody : expectedBodies) {
			inputEndpoint.sendBody(expectedBody);
		}

		assertMockEndpointsSatisfied();
	}

	@Override
	protected ClassPathXmlApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("static-route.xml");
	}

}
