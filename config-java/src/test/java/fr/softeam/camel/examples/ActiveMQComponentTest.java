package fr.softeam.camel.examples;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ActiveMQComponentTest extends CamelTestSupport {

	@EndpointInject(uri = "mock:result")
	protected MockEndpoint resultEndpoint;

	private void addActiveMQComponent(CamelContext camelContext) {

		ActiveMQComponent activeMQComponent = activeMQComponent();

		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");

		activeMQComponent.setConnectionFactory(activeMQConnectionFactory);

		camelContext.addComponent("activemq", activeMQComponent);
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {

		CamelContext camelContext = super.createCamelContext();

		new AnnotationConfigApplicationContext("fr.archetype");

		addActiveMQComponent(camelContext);

		return camelContext;
	}

	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				from("activemq://jms.queue").to("mock:result");
			}
		};
	}

	@Override
	protected void debugBefore(Exchange exchange, Processor processor,
			ProcessorDefinition definition, String id, String shortName) {

		log.info("Before " + definition + " with body "
				+ exchange.getIn().getBody());
	}

	@Test
	public void testActiveMQ() throws Exception {

		String body = "mymessage activemq";

		resultEndpoint.expectedBodiesReceived(body);

		template.sendBody("activemq:jms.queue", body);

		resultEndpoint.assertIsSatisfied();
	}

}
