package spring.mqtt.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class SpringMqttConfigApplication{



	public static void main(String[] args) {
		SpringApplication.run(SpringMqttConfigApplication.class, args);
	}
}
