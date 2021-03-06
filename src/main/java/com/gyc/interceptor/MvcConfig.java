package com.gyc.interceptor;  

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.ComponentScan;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.context.annotation.PropertySource;  
import org.springframework.web.servlet.ViewResolver;  
import org.springframework.web.servlet.config.annotation.*;  
import org.springframework.web.servlet.view.InternalResourceViewResolver;  
  
/** 
 * Created by messigao on 2017/05/20. 
 */  
@Configuration  
@EnableWebMvc  
@ComponentScan(basePackages = "com.gyc")  
@PropertySource(value = "classpath:application.properties",  
        ignoreResourceNotFound = true,encoding = "UTF-8")  
public class MvcConfig extends WebMvcConfigurerAdapter {  
    private static final Logger logger = LoggerFactory.getLogger(MvcConfig.class);  
    @Autowired  
    LoginInterceptor loginInterceptor;  
  
    /** 
 
     * <p> 
     *     视图处理器 
     * </p> 
     * 
     * @return 
     */  
    @Bean  
    public ViewResolver viewResolver() {  
        logger.info("ViewResolver");  
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();  
        viewResolver.setPrefix("/WEB-INF/jsp/");  
        viewResolver.setSuffix(".jsp");  
        return viewResolver;  
    }  
  
    /** 
     * 拦截器配置 
     * @param registry 
     */  
    @Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        // 注册监控拦截器  
        registry.addInterceptor(loginInterceptor)  
                .addPathPatterns("/**")  
         .excludePathPatterns("/configuration/ui");  
  
    }  
  
    @Override  
    public void addCorsMappings(CorsRegistry registry) {  
        registry.addMapping("/**")  
                .allowedOrigins("*")  
                .allowedHeaders("*/*")  
                .allowedMethods("*")  
                .maxAge(120);  
    }  
  
    /** 
     * 资源处理器 
     * @param registry 
     */  
    @Override  
    public void addResourceHandlers(ResourceHandlerRegistry registry) {  
        logger.info("addResourceHandlers");  
        registry.addResourceHandler("/swagger-ui.html")  
                .addResourceLocations("classpath:/META-INF/resources/");  
        registry.addResourceHandler("/webjars/**")  
                .addResourceLocations("classpath:/META-INF/resources/webjars/");  
    }  
  
}  
