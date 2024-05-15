package rs.cms.cmsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CmsclientApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CmsclientApplication.class).web(WebApplicationType.NONE).run(args);
    }

}
