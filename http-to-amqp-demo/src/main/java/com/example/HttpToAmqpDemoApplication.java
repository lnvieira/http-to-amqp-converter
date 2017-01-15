package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.http.Http;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
@EnableIntegration
@EnableIntegrationManagement
public class HttpToAmqpDemoApplication {

    private static Logger logger = LoggerFactory.getLogger(HttpToAmqpDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HttpToAmqpDemoApplication.class, args);
	}

	@Bean
    public IntegrationFlow flow(@Value("${http.inbound.gateway.path}") String path, AmqpTemplate amqpTemplate) {
        return IntegrationFlows.from(Http.inboundGateway(path)
                .requestMapping(requestMappingSpec ->
                        requestMappingSpec
                            .methods(HttpMethod.POST)
                            .consumes("application/json")
                            .produces("application/json")
                ))
                .channel(Channels::direct)
                .handle(m -> logger.info("{}",m.getPayload()))
                .get();
    }

}
