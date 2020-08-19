package com.circuit.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class BookService {

	private static final Logger logger = LogManager.getLogger(BookService.class);

	@Autowired
	RestTemplate rt;

	/*
	 * The service (method annotated with @HystrixCommand) receives number of calls
	 * exceeding a limit. This limit is specified by
	 * circuitBreaker.requestVolumeThreshold (default: 20 calls) And these calls are
	 * received within a a particular time period. This time period is specified by
	 * metrics.rollingStats.timeInMilliseconds (default: 10 seconds) And the number
	 * of failures of calling the service method is greater than a particular
	 * percentage. This percentage is specified by
	 * circuitBreaker.errorThresholdPercentage (default: >50%)
	 */

	/*
	 * @HystrixCommand(fallbackMethod = "reliable", commandProperties= {
	 * 
	 * @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
	 * value = "3000"), // timeout while calling service
	 * 
	 * @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value="1")
	 * , // number of failure of calling service
	 * 
	 * @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value =
	 * "1"), // number of calls received in below time
	 * 
	 * @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value =
	 * "500"), // in 500ms, if above number of calls occur
	 * 
	 * @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value =
	 * "30000") // Circuit will be opened for 30 seconds. Then subsequent call will
	 * go inside the method
	 * 
	 * } ) public String readingList() { logger.info("inside readinglist()"); URI
	 * uri = URI.create("http://localhost:8090/recommended");
	 * 
	 * return rt.getForObject(uri, String.class); }
	 * 
	 * 
	 * public String reliable(Throwable e) {
	 * logger.info("executing reliable method"); logger.error(e.getMessage());
	 * return "from reliable method"; }
	 * 
	 */

	@CircuitBreaker(name = "default", fallbackMethod="commonFallback")
	@Retry(name = "default", fallbackMethod = "commonFallback")
	public String annotation() {
		logger.info("inside annotation");
		throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "This is a remote exception");
	}

	@SuppressWarnings("unused")
	private String commonFallback(Exception e) {
		
		String trigger = "";
		if(e instanceof CallNotPermittedException)
		{
			trigger = "circuit breaker";
		}
		else
			trigger = "retry";
		logger.info("commonFallback:{}",trigger);
		return "commonFallback";
	}

}
