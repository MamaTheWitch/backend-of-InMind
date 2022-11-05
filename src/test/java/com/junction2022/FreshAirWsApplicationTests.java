package com.junction2022;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

// TestRestTemplate is only auto-configured when @SpringBootTest has been configured
// with a webEnvironment that means it starts the web container and listens for HTTP requests.
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
class FreshAirWsApplicationTests {

	@Test
	void contextLoads() {
	}

}
