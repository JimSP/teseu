package br.com.cafebinario.teseu.infrastruct.batch;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.cafebinario.logger.Log;
import br.com.cafebinario.logger.LogLevel;
import br.com.cafebinario.logger.VerboseMode;
import br.com.cafebinario.teseu.api.TeseuInvoker;
import br.com.cafebinario.teseu.api.TeseuParse;
import br.com.cafebinario.teseu.model.TeseuManager;

@Component
@Profile("batch")
public class TeseuBatchCommandLineRunner implements CommandLineRunner {

	@Autowired
	private TeseuInvoker teseuInvoker;
	
	@Autowired
	@Qualifier("teseuFileParse")
	private TeseuParse<Path> teseuParse;
	
	@Value("${br.com.cafebinario.teseu.context.filename:teseu.context}")
	private String contexFileName;
	
	@Override
	@Log(logLevel = LogLevel.ERROR, verboseMode = VerboseMode.ON)
	public void run(String... args) throws Exception {
		
		try {
			TeseuManager.execute(teseuParse, Paths.get(contexFileName), teseuInvoker, args);
		}catch (Exception e) {
			throw e;
		}
	}
}
