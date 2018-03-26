package com.sabre.ngp.osctraining;

import java.sql.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GreetingConsumerController {

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        RestTemplate restTemplate = new RestTemplate();
        Greeting greeting = restTemplate.getForObject("http://rest-service.mfp.svc:8080/rest-service-0.1.0/greeting", Greeting.class);

        greeting.setContent("Decorated greeting:" + greeting.getContent());

        return greeting;
    }


    @RequestMapping("/query")
    public Greeting guery() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try(
            Connection connection = DriverManager.getConnection("jdbc:mysql://mariadb.mfp.svc:3306/test", "test", "test")) {

            PreparedStatement statement = connection.prepareStatement("select message from test.data where id = 0");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String message = resultSet.getString("message");
            Greeting greeting = new Greeting(0, message);
            return greeting;

        }
    }

}