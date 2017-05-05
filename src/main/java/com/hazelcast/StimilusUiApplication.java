package com.hazelcast;

import com.google.common.base.MoreObjects;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.Client;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.extern.slf4j.Slf4j;
import org.atmosphere.cpr.SessionSupport;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.annotation.EnableEventBus;
import org.vaadin.spring.events.support.ApplicationContextEventBroker;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@EnableEventBus
@EnableConfigurationProperties(JetProperties.class)
@EnableScheduling
@Slf4j
public class StimilusUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StimilusUiApplication.class, args);
	}

    @ConditionalOnProperty(name = "hazelcast.envz", havingValue = "demo", matchIfMissing = true)
	@Bean
	HazelcastInstance jetClientY () {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getGroupConfig().setName("jet").setPassword("jet-pass");
        return HazelcastClient.newHazelcastClient(clientConfig);
	}

    @ConditionalOnProperty(name = "hazelcast.envz", havingValue = "dev")
    @Bean
    HazelcastInstance jetClientX () {
        return Hazelcast.newHazelcastInstance();
    }

    @ConditionalOnProperty(name = "hazelcast.envz", havingValue = "dev")
    @Bean
    MockDataGenerator mockDataGenerator(HazelcastInstance hi, JetProperties jp) {
	    return new MockDataGenerator(hi, jp);
    }

	@Bean
    SessionSupport sessionSupport() {
	    return new SessionSupport();
    }

    @Bean
    ApplicationContextEventBroker applicationContextEventBroker(EventBus.ApplicationEventBus applicationEventBus) {

        return new ApplicationContextEventBroker(applicationEventBus);
    }

    @Bean
    CommandLineRunner cli(HazelcastInstance hi, JetProperties jp) {
	    return (args) -> {
            final IMap<String, com.hazelcast.model.Client> clientMap = hi.getMap(jp.getQueues().get(JetProperties.QueueType.CLIENTS));
            List<String> lines = Files.readAllLines(Paths.get("clients.txt"));
            Files.readAllLines(Paths.get("clients.txt"))
                        .stream()
                        .map(line -> line.split("\\|"))

                        .map(arr -> new com.hazelcast.model.Client(
                                arr[0],
                                Double.parseDouble(check(arr[1])),
                                Double.parseDouble(check(arr[2]))
                        ))
                        .forEach(cl -> clientMap.put(cl.getName(), cl));

            log.info("loaded client map");
        };
    }

    private String check(String val) {
        return val == null || "null".equals(val) ? "0" : val;
    }
}
