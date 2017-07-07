package spring.mqtt.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spring.mqtt.broker.server.ApplicationService;

@SpringBootApplication
@EnableAutoConfiguration
public class SpringMqttBrokerApplication implements CommandLineRunner{

	@Autowired
	private ApplicationService app;

	public static void main(String[] args) {
		SpringApplication.run(SpringMqttBrokerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		app.runServie();
	}
}
