package com.cjxy.las.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {
//	/**
//	 * 添加自定义拦截器
//	 */
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new AuthInterceptor()).excludePathPatterns("/**/*.js").excludePathPatterns("/**/*.html")
//				.excludePathPatterns("/**/*.css").excludePathPatterns("/**/*.jpg").excludePathPatterns("/**/*.png")
//				.excludePathPatterns("/**/*.gif").excludePathPatterns("/api/staff/authenticate").excludePathPatterns("/favicon.ico")
//				.excludePathPatterns("/").addPathPatterns("/**");
//	}
}
