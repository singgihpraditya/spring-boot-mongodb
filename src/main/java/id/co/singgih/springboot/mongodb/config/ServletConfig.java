package id.co.singgih.springboot.mongodb.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

@Configuration
@EnableWebMvc
@ComponentScan("id.co.singgih")
public class ServletConfig implements WebMvcConfigurer{
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new StringHttpMessageConverter());
		converters.add(createMappingJackson2HttpMessageConverter());
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	
	@Bean
	public ObjectMapper objectMapper(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return mapper;
	}

	private MappingJackson2HttpMessageConverter createMappingJackson2HttpMessageConverter(){
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper());
		return converter;
	}
}
