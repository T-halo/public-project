package cn.kgc.util;

/**
 * 栈：FILO
 * 同一端进出
 *
 * @param <T>
 */
public interface Stack<T> {
    /**
     * 压栈
     */
    void push(T t);

    /**
     * 出栈
     */
    T pop();
}
