package com.ifuture.adonline.grpc;

import com.google.protobuf.ProtocolStringList;
import fpay.bills.AdvertisingServiceGrpc;
import fpay.bills.Ifuture.AdvRequest;
import fpay.bills.Ifuture.AdvResponse;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AdvertisingServiceClient {

  public void test() throws InterruptedException {

    ManagedChannel mChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
        .usePlaintext(true).build();

    AdvertisingServiceGrpc.AdvertisingServiceBlockingStub stub = AdvertisingServiceGrpc
        .newBlockingStub(
            ClientInterceptors.intercept(mChannel, new AdvertisingClientInterceptor()));

    AdvRequest.Builder builder = AdvRequest.newBuilder();
    builder.setMaid(getRandomVal(new String[]{"00052","00062","0009g"}));// 用户ID
    builder.setBussinessId("2");// 商家ID
    builder.setUa(getRandomVal(new String[]{"Android","iOS"}));// User-Agent的信息
    builder.setIp("127.0.0.1");// 交易时的IP
    builder.setPayMethond("4JBo");// 交易的付账方式
    builder.setPay(0);// 交易金额，单位为分
    builder.setNetworkId(getRandomVal(new String[]{"4g","3g","wifi"}));// 用户的网络
    AdvRequest request = builder.build();

    try {
      AdvResponse response = stub.getAdvertisement(request);
      ProtocolStringList list = response.getAdidList();
      System.out.println("grpc请求返回结果如下:");
      for (String adid : list) {
        System.out.println(adid);
      }
    } finally {
      mChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }

  public String getRandomVal(String[] arr){
    return arr[new Random().nextInt(arr.length)];
  }

  public static void main(String[] args) {
    AdvertisingServiceClient client = new AdvertisingServiceClient();
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    for (int i=0;i<50;i++) {
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
