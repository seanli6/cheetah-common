package com.telus.ssnscheetah.springboot;

import com.telus.ssnscheetah.springboot.kongconsumer.KongRestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CheetahLibApplicationTests {

	@Autowired
	private KongRestClient kongRestClient;

//	@Test
//	void test() throws Exception {
//
//	}

}
