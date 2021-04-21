package cn.kgc.util;

/**
 * 队列：FIFO  single ended queue
 * 可以追加容量限制，并在达到容量限制时抛异常
 * 可以设置队列不允许为空，若为空抛异常
 * 队列元素不允许为空
 *
 * @param <T>
 */
public interface Queue<T> {
    /**
     * 向队尾插入元素
     */
    boolean offer(T t);

    /**
     * 提取并删除头元素
     */
    T poll();

    /**
     * 提取但不删除头元素
     */
    T peek();
}
