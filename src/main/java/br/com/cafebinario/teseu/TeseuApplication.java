package br.com.cafebinario.teseu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuManager;
import br.com.cafebinario.teseu.api.TeseuParse;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class TeseuApplication {

	private static int exitCode = 0;

	public static void main(String[] args) {
		
		final SpringApplicationBuilder teseuApplication = new SpringApplicationBuilder(TeseuApplication.class)
				.web(WebApplicationType.NONE);
		
		teseuApplication.build().addListeners(new ApplicationPidFileWriter("teseu.pid"));
		
		final ApplicationContext applicationContext = teseuApplication.run();
	
		SpringApplication.exit(applicationContext, () -> exitCode);
	}

	@Component
	class TeseuCommandLine implements CommandLineRunner {

		@Autowired
		private TeseuInvoker teseuInvoker;
		
		@Autowired
		private TeseuParse teseuParse;
		
		@Override
		public void run(String... args) throws Exception {
			
			try {
				TeseuManager.execute(teseuParse, teseuInvoker, args);
			}catch (Exception e) {
				exitCode = -1;
				log.error("m=run, args={}", args, e);
			}
		}
	}

}
