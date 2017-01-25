package com.pixlabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

import java.util.Arrays;

@SpringBootApplication
public class PixlabsApplication {





	public static void main(String[] args) {
		ApplicationContext ctx= SpringApplication.run(PixlabsApplication.class, args);

		System.out.println("Spring boot inistantiated beans");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for(String beanName : beanNames){
			System.out.println(beanName);
		}



	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

//	@Bean
//	public CommandLineRunner demo(UserDetailsServiceDAO repository){
//		return (args) -> {
//			//Saving some users
//			repository.save(new User("pix","e@e.e","password"));
//			repository.save(new User("pix1","eee@e.e","password"));
//			repository.save(new User("pix2","ae@e.e","passw&ord"));
//			repository.save(new User("pix3","eP@e.e","paszeasword"));
//
//
//			//Fetching all users
//			for(User user : repository.findAll()){
//				System.out.println(user.toString());
//			}
//
//			//Find by email
//			for(User entity : repository.findByEmail("e@e.e")){
//				System.out.println(entity.toString());
//			}
//		};
//	}
}
