package guru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class Spring5RecipeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spring5RecipeAppApplication.class, args);
    }

    /*@Bean
    public MongoTemplate mongoTemplate() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp("localhost");
        MongoClient mongoClient = mongo.getObject();
        return new MongoTemplate(mongoClient, "test_or_whatever_you_want_to_call_this_db");
    }*/
}
