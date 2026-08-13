package com.rendaflex.demo;

import com.rendaflex.demo.integration.FinancialAnalysisGateway;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class DemoApplicationTests {

	@MockitoBean
	private FinancialAnalysisGateway financialAnalysisGateway;

	@Test
	void contextLoads() {
	}

}
