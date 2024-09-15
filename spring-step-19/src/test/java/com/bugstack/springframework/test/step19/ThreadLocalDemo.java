package com.bugstack.springframework.test.step19;

/**
 * ThreadLocal 中使用 弱引用 主要是为了避免内存泄漏，同时确保线程局部变量在不再需要时能够被垃圾回收。这涉及到 ThreadLocal 的实现机制以及弱引用在 ThreadLocal 中的具体作用。我们可以通过分析 ThreadLocal 的工作原理来更好地理解这一点。
 * <p>
 * ThreadLocal 工作原理概述：
 * <p>
 * 1.	ThreadLocal 类的作用：ThreadLocal 提供了一种机制，允许每个线程都拥有自己的独立变量副本。即使多个线程都访问相同的 ThreadLocal 实例，实际操作的对象也是彼此独立的，线程之间互不干扰。
 * 2.	ThreadLocal 存储机制：
 * •	每个线程都有一个 ThreadLocalMap，用于存储线程局部变量。
 * •	ThreadLocalMap 是 Thread 类的一个内部类，它使用 ThreadLocal 实例作为键，变量副本作为值。
 * 3.	引用机制：
 * •	ThreadLocalMap 使用 ThreadLocal 实例作为键存储变量，但这个键实际上是通过弱引用来引用 ThreadLocal 实例的。
 * <p>
 * 弱引用的作用：
 * <p>
 * 在 ThreadLocal 的设计中，键（即 ThreadLocal 实例）被弱引用所引用，而值（存储在线程局部的变量）依然是强引用。具体作用如下：
 * <p>
 * 1.	防止内存泄漏：
 * •	如果 ThreadLocal 的键是强引用，那么即使线程不再使用该 ThreadLocal，线程仍然持有对 ThreadLocal 实例的引用，导致这个 ThreadLocal 对象和它所关联的值无法被垃圾回收。
 * •	使用弱引用后，ThreadLocal 实例一旦不再被外部引用，即使它还在 ThreadLocalMap 中作为键，它仍然会在下一次 GC 时被回收。
 * 2.	清理机制：
 * •	当垃圾回收器回收了 ThreadLocal 实例（因为它是弱引用），对应的键会被回收。
 * •	但是，值（变量副本） 是强引用的，它不会立即被回收。
 * •	这时，ThreadLocalMap 通过定期检查发现键为 null 的条目，将这些条目清理掉，从而避免内存泄漏。
 * <p>
 * 内存泄漏问题及解决：
 * <p>
 * 尽管 ThreadLocal 使用了弱引用，但如果线程不手动清理 ThreadLocal 中的值，线程局部变量的值（即 Map 中的 value）可能会继续占用内存，造成内存泄漏。为此，正确使用 ThreadLocal 的方式是在变量不再使用时调用 ThreadLocal.remove() 方法，手动清理值。
 */
public class ThreadLocalDemo {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        // 在当前线程中设置一个值
        threadLocal.set("Hello, ThreadLocal!");

        // 获取当前线程的值
        System.out.println("Value: " + threadLocal.get());

        // 使用完后移除，避免内存泄漏
        threadLocal.remove();
    }

}
