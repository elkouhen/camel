package com.mycompany.elkouhen.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ResequencerTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result1")
    protected MockEndpoint resultEndpoint1;

    @Override
    protected CamelContext createCamelContext() throws Exception {

        CamelContext camelContext = super.createCamelContext();

        new AnnotationConfigApplicationContext("com.mycompany");

        return camelContext;
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct://myqueue").resequence().header("id")
                        .to("mock:result1");
            }
        };
    }

    @Test
    public void test() throws Exception {

        resultEndpoint1.expectedBodiesReceived("one", "two", "three");

        template.sendBodyAndHeader("direct://myqueue", "three", "id", 3);
        template.sendBodyAndHeader("direct://myqueue", "one", "id", 1);
        template.sendBodyAndHeader("direct://myqueue", "two", "id", 2);

        resultEndpoint1.assertIsSatisfied();
    }
}
