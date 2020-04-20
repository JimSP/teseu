package br.com.cafebinario.teseu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.cafebinario.logger.EnableLog;
import br.com.cafebinario.logger.Log;
import br.com.cafebinario.logger.LogLevel;
import br.com.cafebinario.logger.VerboseMode;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.model.TeseuManager;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableAsync
@EnableScheduling
@EnableLog
public class TeseuApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(TeseuApplication.class, args);
	}

	@Component
	@Profile("batch")
	class TeseuCommandLine implements CommandLineRunner {

		@Autowired
		private TeseuInvoker teseuInvoker;
		
		@Autowired
		private TeseuParse teseuParse;
		
		@Override
		@Log(logLevel = LogLevel.ERROR, verboseMode = VerboseMode.ON)
		public void run(String... args) throws Exception {
			
			try {
				TeseuManager.execute(teseuParse, teseuInvoker, args);
			}catch (Exception e) {
				throw e;
			}
		}
	}
}
