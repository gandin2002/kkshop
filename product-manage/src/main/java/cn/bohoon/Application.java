 package cn.bohoon;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.bohoon.aop.SyncDataJobInterceptor;
import cn.bohoon.main.aop.AuthInterceptor;

/**
 * <p>Description:博宏B2B产品模块启动</p>
 *
 * @author admin
 * @version 1.0　创建时间　2017年9月12日 下午12:00:40
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class Application extends SpringBootServletInitializer {

    @Value("${image.upload.path}")
    String imageUploadPath;
    
    @Bean
    public WebMvcConfigurer corsConfigurer() { // 允许跨域
    	
        return new WebMvcConfigurerAdapter() {

            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                configurer.favorPathExtension(false).favorParameter(false).ignoreAcceptHeader(false).useJaf(false)
                        .defaultContentType(MediaType.TEXT_HTML).mediaType("json", MediaType.APPLICATION_JSON);
            }

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST", "DELETE", "PUT", "GET","OPTIONS");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(authInterceptor()).addPathPatterns("/**");
                registry.addInterceptor(syncDataJobInterceptor()).addPathPatterns("/**");
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/upload/**").addResourceLocations("file://" + imageUploadPath + "/upload/");
                
                if (!registry.hasMappingForPattern("/assets/**")) {
                    registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
                }
                if (!registry.hasMappingForPattern("/kindeditor/**")) {
                    registry.addResourceHandler("/kindeditor/**").addResourceLocations("classpath:/static/kindeditor/");
                }
               
            }

            
        };
    }

    @Bean
    @ConfigurationProperties(prefix = "b2b.datasource")
    public DataSource mallDataSource(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ) {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
    @Bean
    public SyncDataJobInterceptor syncDataJobInterceptor() {
        return new SyncDataJobInterceptor();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ThreadPoolTaskExecutor simpleExecutor() {  
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        executor.setCorePoolSize(40);  
        executor.setMaxPoolSize(800);  
        executor.setQueueCapacity(40);  
        executor.initialize();  
        return executor;  
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
