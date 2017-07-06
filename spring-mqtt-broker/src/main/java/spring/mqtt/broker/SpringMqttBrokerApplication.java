package spring.mqtt.broker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringMqttBrokerApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SpringMqttBrokerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
	}
}
