package com.ifuture.adonline.grpc;

import com.google.protobuf.ProtocolStringList;
import fpay.bills.AdvertisingServiceGrpc;
import fpay.bills.Ifuture.AdvRequest;
import fpay.bills.Ifuture.AdvResponse;
import io.grpc.ClientInterceptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

public class AdvertisingServiceClient {

  public void test() throws InterruptedException {

    ManagedChannel mChannel = ManagedChannelBuilder.forAddress("lehui-demo-cdh001", 6565)
        .usePlaintext(true).build();

    AdvertisingServiceGrpc.AdvertisingServiceBlockingStub stub = AdvertisingServiceGrpc
        .newBlockingStub(
            ClientInterceptors.intercept(mChannel, new AdvertisingClientInterceptor()));

    AdvRequest.Builder builder = AdvRequest.newBuilder();
    builder.setMaid("1");// 用户ID
    builder.setBussinessId("2");// 商家ID
    builder.setUa("ios");// User-Agent的信息
    builder.setIp("4");// 交易时的IP
    builder.setPayMethond("5");// 交易的付账方式
    builder.setPay(6);// 交易金额，单位为分
    builder.setNetworkId("");// 用户的网络
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
}
