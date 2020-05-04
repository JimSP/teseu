package br.com.cafebinario.teseu.infrastruct.notification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import br.com.cafebinario.teseu.api.TeseuNotification;
import lombok.SneakyThrows;

@Component("teseuEmailNotification")
public class TeseuEmailNotification implements TeseuNotification {

	@Value("${br.com.cafebinario.teseu.notification.email.to}")
	private String to;
	
	@Value("${br.com.cafebinario.teseu.notification.email.from}")
	private String from;
	
	@Value("${br.com.cafebinario.teseu.notification.email.subject}")
	private String subject;
	
	@Autowired
	private JavaMailSender emailSender;

	@Override
	@SneakyThrows
	public void sendReport(final String name, final Throwable t) {
	
		try(final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
			
			final MimeMessage mimeMessage = emailSender.createMimeMessage();
			
			final PrintStream printStream = new PrintStream(byteArrayOutputStream);
			t.printStackTrace(printStream);
			printStream.flush();
			
			
			final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setText(name + "\r\n" + t.getMessage());
			
			helper.addAttachment("error.txt", () -> new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
			
			emailSender.send(mimeMessage);
			printStream.close();
		}
	}
}
