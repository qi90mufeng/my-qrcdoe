package com.snfq.module.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.snfq.module.interceptor.AuthInteceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new AuthInteceptor()).addPathPatterns("/**");  
    	super.addInterceptors(registry);
    }
    
//    @Override
//    public void configureContentNegotiation(
//    		ContentNegotiationConfigurer configurer) {
//    	configurer.favorParameter(true).parameterName("format")
//    	.ignoreAcceptHeader(true).defaultContentType(MediaType.TEXT_HTML)
//    	.mediaType("xml", MediaType.APPLICATION_XML)
//    	.mediaType("json", MediaType.APPLICATION_JSON_UTF8);
//    }
}
