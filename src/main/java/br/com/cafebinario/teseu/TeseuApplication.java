package br.com.cafebinario.teseu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.cafebinario.logger.EnableLog;

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
}
