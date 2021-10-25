package com.ravi.springreactive.controller;

import com.github.javafaker.Faker;
import com.ravi.springreactive.domain.NameList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class EmployeeController {

    @Autowired
    private WebClient webClient;

    @GetMapping("/start")
    private void start() {

        Flux<NameList> obj = this.webClient.get()
                .uri("http://localhost:8080/getNames")
                .retrieve()
                .bodyToFlux(NameList.class);

        obj.repeat(10) // can be repeat() to infinite loop
                .doOnEach(nameList -> System.out.println("Sending acknowledgement for name list :" + nameList))
                .subscribe(nameList -> nameList.getNames().parallelStream().forEach(name -> {
                            sleep(3000);
                            System.out.println("Processed : " + name);
                        }
                ));

    }

    @GetMapping("/getNames")
    private NameList getNames() {
        sleep(1000);
        NameList names = new NameList();
        for (int i = 0; i < 10; i++) {
            names.getNames().add(Faker.instance().name().firstName());
        }
        return names;
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}