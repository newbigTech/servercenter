package com.zm.supplier;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * ClassName: Application <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Jul 27, 2017 4:53:10 PM <br/>
 * 
 * @author hebin
 * @version
 * @since JDK 1.7
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@EnableTransactionManagement
public class SupplierApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(SupplierApplication.class).web(true).run(args);
	}
}
