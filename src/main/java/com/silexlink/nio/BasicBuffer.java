package com.silexlink.nio;

import java.nio.IntBuffer;

/**
 * @author: Kevin
 * @date: 2020/8/15
 * @time: 下午11:32
 * @description:
 **/

public class BasicBuffer {
  public static void main(String[] args) {
    IntBuffer intBuffer = IntBuffer.allocate(5);
    for (int i = 0; i < intBuffer.capacity(); i++) {
      intBuffer.put(i * 2);
    }
    intBuffer.flip();
    while (intBuffer.hasRemaining()) {
      System.out.println(intBuffer.get());
    }
  }
}
