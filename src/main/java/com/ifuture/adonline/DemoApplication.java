package com.ifuture.adonline;

import com.ifuture.adonline.grpc.AdvertisingServiceClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(DemoApplication.class, args);

    AdvertisingServiceClient client = new AdvertisingServiceClient();

    for (int i=0;i<10;i++) {

    }
    client.test();

  }
}
