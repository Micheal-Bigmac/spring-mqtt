package spring.mqtt.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import spring.mqtt.config.netty.NettyConfig;

@SpringBootApplication
public class SpringMqttBrokerApplication implements CommandLineRunner{

	@Autowired
	private NettyConfig nettyConfig;

	public static void main(String[] args) {
		SpringApplication.run(SpringMqttBrokerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		System.out.println(nettyConfig.getBorkerId());
	}
}
