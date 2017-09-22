package com.ifuture.adonline.grpc;

import com.ifuture.adonline.grpc.AdvertisingServiceGrpc;
import com.ifuture.adonline.grpc.AdvRequest;
import com.ifuture.adonline.grpc.AdvResponse;
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

    ManagedChannel mChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
        .usePlaintext(true).build();

    AdvertisingServiceGrpc.AdvertisingServiceBlockingStub stub = AdvertisingServiceGrpc
        .newBlockingStub(
            ClientInterceptors.intercept(mChannel, new AdvertisingClientInterceptor()));

    try {
      final long startTime = System.nanoTime();

      for (int i = 0; i < 10; i++) {
        AdvRequest.Builder builder = AdvRequest.newBuilder();
        builder.setMaid("" + (i + 1));// 用户ID
        builder.setBussinessId("2");// 商家ID
        builder.setUa("3");// User-Agent的信息
        builder.setIp("4");// 交易时的IP
        builder.setPayMethond("5");// 交易的付账方式
        builder.setPay(6);// 交易金额，单位为分
        builder.setNetworkId("7");// 用户的网络
        AdvRequest request = builder.build();
        AdvResponse response = stub.getAdvertisement(request);
        System.out.println("返回广告内容:" + response.getAdid());
        //Thread.sleep(5000);
      }

      final long endTime = System.nanoTime();

      System.out.println("time consuming : " + (endTime - startTime) / 1000000L + "ms");

    } finally {
      mChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

  }
}
