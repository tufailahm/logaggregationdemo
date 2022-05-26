package com.revature.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

	private static final Logger log = LoggerFactory.getLogger(MessageController.class);
	
	@PostMapping
//	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> receiveMessage(@RequestBody String message) {
		
		MDC.put("event", "message-submit");
		
//		log.info("{}, {}, {}, {}", message, "String one", "String two", "String three");
//		log.info("{}", message);
		log.info(message);
		
		log.warn(message, new RuntimeException("Something went wrong!"));
		
		MDC.clear();
		
		return ResponseEntity.ok("Message received and logged");
	}
}
