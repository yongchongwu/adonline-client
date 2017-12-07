package com.ifuture.adonline;

import com.ifuture.adonline.grpc.AdvertisingServiceClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(DemoApplication.class, args);

    AdvertisingServiceClient client = new AdvertisingServiceClient();

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    for (int i=0;i<20;i++) {
      fixedThreadPool.execute(new Runnable() {
        @Override
        public void run() {
          try {
            client.test();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      });
    }
  }
}
