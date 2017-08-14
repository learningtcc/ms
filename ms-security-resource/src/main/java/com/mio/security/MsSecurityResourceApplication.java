package com.mio.security;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableResourceServer
@RestController
public class MsSecurityResourceApplication extends ResourceServerConfigurerAdapter {

	final List<Message> messages = Collections.synchronizedList(new LinkedList<>());

	@PreAuthorize("#oauth2.hasScope('read')")
	@RequestMapping(path = "api/messages", method = RequestMethod.GET)
	List<Message> getMessages(Principal principal) {
		Message message = new Message();
		message.username = principal.getName();
		message.createdAt = LocalDateTime.now();
		messages.add(0, message);
		return messages;
	}

	@RequestMapping(path = "api/messages", method = RequestMethod.POST)
	Message postMessage(Principal principal, @RequestBody Message message) {
		message.username = principal.getName();
		message.createdAt = LocalDateTime.now();
		messages.add(0, message);
		return message;
	}

	public static class Message {
		public String text;
		public String username;
		public LocalDateTime createdAt;
	}

	// @Override
	// public void configure(HttpSecurity http) throws Exception {
	// http
	// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	// .and()
	// .authorizeRequests()
	// .antMatchers(HttpMethod.GET,
	// "/api/**").access("#oauth2.hasScope('read')")
	// .antMatchers(HttpMethod.POST,
	// "/api/**").access("#oauth2.hasScope('write')");
	// }
	//
	public static void main(String[] args) {
		SpringApplication.run(MsSecurityResourceApplication.class, args);
	}
}
