package com.mycompany.elkouhen.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.mycompany.elkouhen.camel.util.MyRouter;
import org.junit.Ignore;

public class DynamicListTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result1")
    protected MockEndpoint resultEndpoint1;

    @EndpointInject(uri = "mock:result2")
    protected MockEndpoint resultEndpoint2;

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

                from("direct://myqueue").dynamicRouter(
                        bean(MyRouter.class, "slip"));
            }
        };
    }

    @Ignore
    @Test
    public void test1() throws Exception {

        String body = "one";

        resultEndpoint1.expectedBodiesReceived(body);

        template.sendBody("direct://myqueue", body);

        resultEndpoint1.assertIsSatisfied();
    }

    @Ignore
    @Test
    public void test2() throws Exception {

        String body = "two";

        resultEndpoint1.expectedBodiesReceived(body);

        template.sendBody("direct://myqueue", body);

        resultEndpoint1.assertIsSatisfied();
    }

}
