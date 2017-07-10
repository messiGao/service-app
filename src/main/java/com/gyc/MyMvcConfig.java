package com.gyc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration  
@EnableWebMvc  
@ComponentScan("com.gyc")  
public class MyMvcConfig extends WebMvcConfigurerAdapter{  
   /** 
     * 重写configurePathMatch此方法 
     * 设置其参数PathMatchConfigurer的属性UseSuffixPatternMatch值为false 
     * 可以让路径中带小数点“.”后面的值不被忽略 
     */  
    @Override  
    public void configurePathMatch(PathMatchConfigurer configurer){  
        configurer.setUseSuffixPatternMatch(false);  
    }  
}  
