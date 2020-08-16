package com.silexlink.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author: Kevin
 * @date: 2020/6/4
 * @time: 21:46
 * @description:
 **/

public class SomeServer {
  public static void main(String[] args) throws InterruptedException {

    // 用于处理客户端链接请求，将请求发送给childGroup中的eventLoop
    NioEventLoopGroup parentGroup = new NioEventLoopGroup();
    // 用于处理客户端请求
    NioEventLoopGroup childGroup = new NioEventLoopGroup();

    try {
      // 用于启动ServerChannel
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap
          // 指定eventLoopGroup
          .group(parentGroup, childGroup)
          // 指定使用NIO进行通信
          .channel(NioServerSocketChannel.class)
          // 指定childGroup中的eventLoop所绑定的线程所要处理的处理器
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
              // 从Channel中获取pipeline
              ChannelPipeline pipeline = channel.pipeline();
              // 将httpServerCodec处理器放入到pipeline的最后
              // HttpServerCodec处理解码、编码
              pipeline.addLast(new HttpServerCodec());
              pipeline.addLast(new SomeServerHandler());
            }
          });
      // 指定当前服务器所监听的端口号
      // bind()方法的执行是异步的
      // sync()方法会使bind()绑定操作与后续的代码的执行由异步变为了同步
      ChannelFuture future = serverBootstrap.bind(8888).sync();
      System.out.println("服务器启动成功，监听的端口号为8888");
      // 关闭Channel
      // closeFuture()的执行也是异步的
      future.channel().closeFuture().sync();
    } finally {
      //优雅关闭
      parentGroup.shutdownGracefully();
      childGroup.shutdownGracefully();
    }

  }
}
