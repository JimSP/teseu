package br.com.cafebinario.teseu.infrastruct.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
		
		final SimpleMailMessage message = new SimpleMailMessage(); 

		message.setTo(to);
		message.setFrom(from);
		message.setSubject(subject);
		message.setText(name + "\r\n" + t.getMessage() + "\r\n\r\n" + t.toString());

		emailSender.send(message);
	}
}
