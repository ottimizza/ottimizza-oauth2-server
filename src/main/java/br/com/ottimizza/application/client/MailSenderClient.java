package br.com.ottimizza.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.ottimizza.application.domain.dtos.MailDTO;
import br.com.ottimizza.application.domain.responses.GenericResponse;

@FeignClient(name = "${email-sender.service.name}", url = "${email-sender.service.url}")
public interface MailSenderClient {

	

	@PostMapping(value = "/api/v1/emails")
	HttpEntity<GenericResponse<?>> sendMail(@RequestBody MailDTO mailDto);	

}
