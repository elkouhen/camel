package com.mycompany.elkouhen.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AggregateTest extends CamelTestSupport {

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

                AggregationStrategy aggregationStrategy = new AggregationStrategy() {

                    @Override
                    public Exchange aggregate(Exchange oldExchange,
                            Exchange newExchange) {

                        if (oldExchange == null) {
                            return newExchange;
                        }

                        String msg1 = oldExchange.getIn().getBody(String.class);

                        String msg2 = newExchange.getIn().getBody(String.class);

                        oldExchange.getIn().setBody(msg1 + "," + msg2);

                        return oldExchange;
                    }
                };
                from("direct://myqueue").aggregate(aggregationStrategy)
                        .header("myheader").completionSize(3)
                        .to("mock:result1");
            }
        };
    }

    @Test
    public void test() throws Exception {

        resultEndpoint1.expectedBodiesReceived("one,two,three");

        template.sendBodyAndHeader("direct://myqueue", "one", "myheader",
                "cool");
        template.sendBodyAndHeader("direct://myqueue", "two", "myheader",
                "cool");
        template.sendBodyAndHeader("direct://myqueue", "three", "myheader",
                "cool");

        resultEndpoint1.assertIsSatisfied();
    }
}
