package com.cjxy.las.startup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


import com.cjxy.las.service.UserService;

import jakarta.annotation.Resource;

@Component
public class BootstartService implements ApplicationRunner{

	@Resource
	private UserService userService;
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		userService.addDefaultUser();
	}

}
