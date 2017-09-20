package com.ifuture.adonline.grpc;

import com.ifuture.adonline.AdvertisingServiceGrpc;
import com.ifuture.adonline.Ifuture.AdvRequest;
import com.ifuture.adonline.Ifuture.AdvResponse;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdvertisingServiceClient {

  public static void main(String[] args) throws InterruptedException {
    SpringApplication.run(AdvertisingServiceClient.class, args);
    ManagedChannel mChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext(true)
        .build();
    AdvertisingServiceGrpc.AdvertisingServiceBlockingStub stub = AdvertisingServiceGrpc
        .newBlockingStub(
            ClientInterceptors.intercept(mChannel, new AdvertisingClientInterceptor()));
    try {
      for (int i = 0; i < 10; i++) {
        String maid = "" + (i + 1);
        AdvRequest request = AdvRequest.newBuilder().setMaid(maid).build();
        AdvResponse response = stub.getAdvertisement(request);
        System.out.println("返回广告内容:" + response.getAdid());
        Thread.sleep(5000);
      }
    } finally {
      mChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
