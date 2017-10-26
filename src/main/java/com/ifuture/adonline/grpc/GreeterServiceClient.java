package com.ifuture.adonline.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

public class GreeterServiceClient {

  public void test() throws InterruptedException {
    ManagedChannel mChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext(true)
        .build();
    GreeterServiceGrpc.GreeterServiceBlockingStub stub = GreeterServiceGrpc
        .newBlockingStub(mChannel);
    try {
      for (int i = 0; i < 10; i++) {
        HelloRequest request = HelloRequest.newBuilder().setName("Jack" + (i + 1)).build();
        HelloReply reply = stub.sayHello(request);
        System.out.println(reply.getMessage());
        Thread.sleep(1000);
      }
    } finally {
      mChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }

}
