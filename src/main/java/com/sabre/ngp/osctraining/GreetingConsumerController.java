package com.sabre.ngp.osctraining;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.sql.*;

@RestController
public class GreetingConsumerController {

    @RequestMapping("/greeting")
    public ResponseEntity<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name) throws GreetingException {
        RestTemplate restTemplate = new RestTemplate();
        Greeting greeting;
        try {
            greeting = restTemplate.getForObject("http://rest-service.mfp.svc:8080/rest-service-0.1.0/greeting", Greeting.class);
        } catch (Exception e) {
            throw new GreetingException("'rest-service' is not available!", e);
        }
        if (greeting == null) {
            throw new GreetingException("'rest-service' returned null object");
        } else {
            greeting.setContent("Decorated greeting:" + greeting.getContent());
            return ResponseEntity.ok(greeting);
        }
    }


    @RequestMapping("/query")
    public Greeting query() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://mariadb.mfp.svc:3306/test", "test", "test")) {
            try (PreparedStatement statement = connection.prepareStatement("select message from test.data where id = 0")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    String message = resultSet.getString("message");
                    return new Greeting(0, message);
                }
            }
        }
    }

    @RequestMapping("/")
    public ModelAndView redirectWithUsingForwardPrefix() {
        return new ModelAndView("redirect:/greeting");
    }
}
