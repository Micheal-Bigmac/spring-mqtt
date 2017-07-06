package spring.mqtt.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SpringMqttConfigApplication  implements CommandLineRunner{


	public static void main(String[] args) {
		SpringApplication.run(SpringMqttConfigApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {

	}
}
