package com.example.finalprojectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FinalProjectBackendApplication {

    // UserService userService;

    // @Autowired
    // public FinalProjectBackendApplication(UserService userService) {
    // this.userService = userService;
    // }

    public static void main(String[] args) {

        SpringApplication.run(FinalProjectBackendApplication.class, args);

    }

    // @Bean
    // CommandLineRunner initDataBase() {
    // return args -> {
    // UserEntity user = new UserEntity();
    // user.setEmail("cgalvan29111999@gmail.com");
    // user.setPassword(Lab2YouUtils.encodePassword("12345"));
    // userService.save(user);
    // };
    // }

}
