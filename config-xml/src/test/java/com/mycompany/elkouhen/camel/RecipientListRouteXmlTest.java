package com.mycompany.elkouhen.camel;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class RecipientListRouteXmlTest extends CamelSpringTestSupport {

    // expected message bodies
    protected Object[] expectedBodies = {
            "<something id='1'>expectedBody1</something>",
            "<something id='2'>expectedBody2</something>"};
    // templates to send to input endpoints
    @Produce(uri = "vm://in")
    protected ProducerTemplate inputEndpoint;
    // mock endpoints used to consume messages from the output endpoints and then perform assertions
    @EndpointInject(uri = "mock:output")
    protected MockEndpoint outputEndpoint;
    @EndpointInject(uri = "mock:output2")
    protected MockEndpoint output2Endpoint;

    @Test
    public void testCamelRoute() throws Exception {
        // lets route from the output endpoints to our mock endpoints so we can assert expectations
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("vm://out1").to(outputEndpoint);
                from("vm://out2").to(output2Endpoint);
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
        return new ClassPathXmlApplicationContext("recipient-list-route.xml");
    }

}
