package com.silexlink.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Kevin
 * @date: 2020/8/15
 * @time: 下午9:54
 * @description:
 **/

public class BIOServer {

  public static void main(String[] args) throws IOException {
    //创建线程池
    ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
    //创建ServerSocket
    ServerSocket serverSocket = new ServerSocket(6666);
    System.out.println("服务器启动了");
    while (true) {
      //监听， 等待客户端连接
      final Socket socket = serverSocket.accept();
      System.out.println("连接到一个客户端");

      //创建一个线程
      newCachedThreadPool.execute(new Runnable() {
        @Override
        public void run() {
          //可以和客户端通讯的
          try {
            System.out.println(
                "线程id =" + Thread.currentThread().getId() +
                    "线程名 = " + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while (true) {
              int read = inputStream.read(bytes);
              if (read != -1) {
                System.out.println(new String(bytes, 0, read));
              } else {
                break;
              }
            }
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            try {
              socket.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }

        }
      });
    }
  }
}
