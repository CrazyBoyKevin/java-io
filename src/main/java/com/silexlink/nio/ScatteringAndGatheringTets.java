package com.silexlink.nio;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author: Kevin
 * @date: 2020/8/17
 * @time: 下午11:28
 * @description:
 **/

public class ScatteringAndGatheringTets {
  public static void main(String[] args) throws IOException {
    //使用ServerSocketChannel 和 socketChannel
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
    //绑定端口
    serverSocketChannel.socket().bind(inetSocketAddress);
    //创建buffer数组
    ByteBuffer[] byteBuffers = new ByteBuffer[2];
    byteBuffers[0] = ByteBuffer.allocate(5);
    byteBuffers[1] = ByteBuffer.allocate(3);

    SocketChannel socketChannel = serverSocketChannel.accept();
    int messageLength = 8;
    while (true) {
      int byteRead = 0;
      while (byteRead < messageLength) {
        long l = socketChannel.read(byteBuffers);
        byteRead += l;
        System.out.println("byteRead = " + byteRead);
        Arrays.asList(byteBuffers)
            .stream()
            .map(byteBuffer -> "position = " + byteBuffer.position() +
                ", limit = " + byteBuffer.limit())
            .forEach(System.out::println);
      }

      //将所有的buffer进行flip
      Arrays.asList(byteBuffers)
          .forEach(byteBuffer -> byteBuffer.flip());
      //将数据读出显示到客户端
      long byteWrite = 0;
      while (byteWrite < messageLength) {
        long l = socketChannel.write(byteBuffers);
        byteWrite += l;
      }

      //将所有的buffer 进行clear
      Arrays.asList(byteBuffers)
          .forEach(byteBuffer -> {
            byteBuffer.clear();
          });
      System.out.println("byteRead = " + byteRead +
          ", byteWrite = " + byteWrite +
          ", messageLength = " + messageLength);
    }
  }
}
