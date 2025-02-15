package it.unict.gallosiciliani.webapp;

import it.unict.gallosiciliani.gs.GSFeatures;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties(WebAppProperties.class)
public class Application {

	@Bean
	GSFeatures gsFeatures() throws IOException {
		return GSFeatures.loadLocal();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
