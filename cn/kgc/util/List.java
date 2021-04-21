package cn.kgc.util;

import java.util.Comparator;
import java.util.Iterator;

public interface List<T> extends Iterable<T> {
    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    default boolean contains(T t) {
        return -1 != indexOf(t);
    }

    default boolean contains(List<T> list) {
        Iterator<T> it = list.iterator();
        boolean exists = true;
        while (it.hasNext()) {
            T next = it.next();
            if (!contains(next)) {
                exists = false;
                break;
            }
        }
        return exists;
    }

    int indexOf(T t);

    int lastIndexOf(T t);

    void add(T t);

    void add(List<T> list);

    void add(int index, T t);

    T set(int index, T t);

    void add(int index, List<T> list);

    T get(int index);

    T remove(int index);

    List<T> remove(int fromIndex, int toIndex);

    default boolean remove(T t) {
        return false;
    }

    void remove(List<T> list);

    void retain(List<T> list);

    List<T> subList(int fromIndex, int toIndex);

    void sort(Comparator<T> comparator);

    Object[] toArray();

    default void trimToSize() {
        throw new UnsupportedOperationException("trimToSize");
    }

    void clear();

    int capacity();
}
