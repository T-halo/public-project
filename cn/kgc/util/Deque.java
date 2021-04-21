package cn.kgc.util;

/**
 * 双向队列：double ended queue  FILO
 * 同 Queue<T>
 *
 * @param <T>
 */
public interface Deque<T> extends Queue<T> {
    /**
     * 头插
     */
    boolean offerFirst(T t);

    /**
     * 尾插 (相当于 Queue 中的 offer)
     */
    default boolean offerLast(T t) {
        return offer(t);
    }

    /**
     * 提头删 (相当于 Queue 中的 poll)
     */
    default T pollFirst() {
        return poll();
    }

    /**
     * 尾提删
     */
    T pollLast();

    /**
     * 提头 (相当于 Queue 中的 peek)
     */
    default T peekFirst() {
        return peek();
    }

    /**
     * 尾提
     */
    T peekLast();
}
