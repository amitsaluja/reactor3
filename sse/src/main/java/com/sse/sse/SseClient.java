package com.sse.sse;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.sse.sse.service.Event;
/**
 * Spring webclient to handle Server side events
 * 
 * @author amitsaluja
 *
 */
@RestController
@SpringBootApplication
public class SseClient {
	@Bean
	WebClient client(){
		return WebClient.create("http://localhost:8080");
	}
	
	@Bean
	CommandLineRunner demo(WebClient client){
		return args ->{
			
			client.
			get()
			.uri("/events")
		    .accept(MediaType.APPLICATION_STREAM_JSON)
		    .exchange()
		    .flatMapMany(cr->cr.bodyToFlux(Event.class))
		    .subscribe(System.out::println);
		};
		}
	
	
	@Bean
    public RestOperations restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();


        converter.setSupportedMediaTypes(
                Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_STREAM_JSON}));

        restTemplate.setMessageConverters(Arrays.asList(converter, new FormHttpMessageConverter()));
        return restTemplate;
    }

	@Autowired
	private static RestTemplate restTemplate;
	public static void main(String[] args) {
		new SpringApplicationBuilder(SseClient.class).properties(Collections.singletonMap("server.port", "8081"))
		.run(args);
		
		
	}
	
	
	
}
