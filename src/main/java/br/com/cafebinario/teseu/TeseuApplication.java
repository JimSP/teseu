package br.com.cafebinario.teseu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuManager;
import br.com.cafebinario.teseu.api.TeseuParse;

@SpringBootApplication
public class TeseuApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(TeseuApplication.class, args);
	}

	@Component
	class TeseuCommandLine implements CommandLineRunner {

		@Autowired
		private TeseuInvoker teseuInvoker;
		
		@Autowired
		private TeseuParse teseuParse;
		
		@Override
		public void run(String... args) throws Exception {
			
			TeseuManager.execute(teseuParse, teseuInvoker, args);
		}
	}

}
