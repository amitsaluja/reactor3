package com.sse.sse.service;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.util.function.Tuple2;

import org.springframework.http.MediaType;

@SpringBootApplication
@RestController
public class ReactiveService {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveService.class, args);
		
	}
	
	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE,value="/events")
	Flux<Event> eventProducer(){
		
		Flux<Event>  eventFlux=Flux.fromStream(Stream.generate(()->new Event(System.currentTimeMillis(),new Date())));
		Flux<Long>  durationFlux=Flux.interval(Duration.ofSeconds(1));
		return Flux.zip(eventFlux,durationFlux).map(Tuple2::getT1);
        
    }
}
