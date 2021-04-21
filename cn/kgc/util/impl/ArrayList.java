package cn.kgc.util.impl;

import cn.kgc.util.AbstractList;
import cn.kgc.util.Arrays;
import cn.kgc.util.List;

import java.util.Comparator;
import java.util.Iterator;

public class ArrayList<T> extends AbstractList<T> implements List<T> {

    /**
     * 增长因子，用于ArrayList扩容
     * Java 里面是在扩容的时候扩容机制是 oldCapacity + (oldCapacity >> 1)
     * oldCapacity：当前List大小，然后右移一位，相加，依旧是类似是1.5倍的扩容
     * 你们这边的实现扩容大小是1.2倍
     */
    private final float INCREASE_FACTOR = 1.2F;
    /**
     * 要分配的数组的最大大小。 一些虚拟机在数组中保留一些标题字。
     * 尝试分配更大的阵列可能会导致OutOfMemoryError：请求的阵列大小超出VM限制
     */
    private final int MAX_SIZE = Integer.MAX_VALUE - 8;
    /**
     * 用于List来存储元素,初始化ArrayList
     * @see ArrayList#ArrayList()
     */
    private final Object[] EMPTY = {};
    /**
     * 当 array 数组的大小不够存储新加的数据时，作为扩容时的缓存数组
     * @see ArrayList#ensureCapacity(int)
     */
    private Object[] temp = EMPTY;
    private Object[] array;

    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0 || initialCapacity > MAX_SIZE) {
            throw new RuntimeException("initial capacity overflow");
        }
        array = new Object[initialCapacity];
    }

    public ArrayList() {
        array = EMPTY;
    }

    public ArrayList(List<T> list) {
        add(list);
    }

    private int index(T t, boolean forward) {
        if (null != t) {
            int begin = 0, end = size - 1;
            while (begin <= end) {
                if (forward ? t.equals(array[begin]) : t.equals(array[end])) {
                    return forward ? begin : end;
                }
                if (forward) begin++;
                else end--;
            }
        }
        return -1;
    }

    @Override
    public int indexOf(T t) {
        return index(t, true);
    }

    @Override
    public int lastIndexOf(T t) {
        return index(t, false);
    }

    private void release() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        array = null;
    }

    private void ensureCapacity(int howMany) {
        if (size + howMany > array.length) {
            temp = new Object[(int) Math.ceil((size + howMany) * INCREASE_FACTOR)];
            for (int i = 0; i < size; i++) {
                temp[i] = array[i];
            }
            release();
            array = temp;
            temp = EMPTY;
        }
    }

    @Override
    public void add(T t) {
        requireNonNull(t);
        ensureCapacity(1);
        array[size++] = t;
    }

    @Override
    public void add(List<T> list) {
        requireNotEmpty(list);
        final int N = list.size();
        ensureCapacity(N);
        System.arraycopy(list.toArray(), 0, array, size, N);
        size += N;
    }

    @Override
    public void add(int index, T t) {
        validateIndex(index, true);
        requireNonNull(t);
        ensureCapacity(1);
        if (index != size) {
            System.arraycopy(array, index, array, index + 1, size - index);
        }
        array[index] = t;
        size++;
    }

    @Override
    public T set(int index, T t) {
        validateIndex(index, false);
        requireNonNull(t);
        T _t = (T) array[index];
        array[index] = t;
        return _t;
    }

    @Override
    public void add(int index, List<T> list) {
        validateIndex(index, true);
        requireNotEmpty(list);
        final int N = list.size();
        ensureCapacity(N);
        System.arraycopy(array, index, array, index + N, size - index);
        System.arraycopy(list.toArray(), 0, array, index, N);
        size += N;
    }

    @Override
    public T get(int index) {
        validateIndex(index, false);
        return (T) array[index];
    }

    private T _remove(int index) {
        T t = (T) array[index];
        System.arraycopy(array, index + 1, array, index, size - index - 1);
        size--;
        return t;
    }

    @Override
    public T remove(int index) {
        validateIndex(index, false);
        return _remove(index);
    }

    private List<T> _subList(int fromIndex, int toIndex) {
        List<T> list = new ArrayList<>(toIndex - fromIndex + 1);
        for (int i = fromIndex; i <= toIndex; i++) {
            list.add((T) array[i]);
        }
        return list;
    }

    @Override
    public List<T> remove(int fromIndex, int toIndex) {
        validateIndex(fromIndex, toIndex);
        //即将被删除的元素的数量
        final int N = toIndex - fromIndex + 1;
        //被删除元素之后的元素数量
        final int C = size - toIndex - 1;
        List<T> rtn = _subList(fromIndex, toIndex);
        System.arraycopy(array, toIndex + 1, array, fromIndex, C);

        //清楚重复的元素引用
        for (int i = fromIndex + C; i < size; i++) {
            array[i] = null;
        }

        size -= N;
        return rtn;
    }

    private boolean _remove(T t) {
        //定位t下标
        int index = indexOf(t);
        // -1表示array中不存在对象t
        if (-1 == index) {
            return false;
        }
        _remove(index);
        return true;
    }

    @Override
    public boolean remove(T t) {
        requireNonNull(t);
        return _remove(t);
    }

    @Override
    public void remove(List<T> list) {
        requireNotEmpty(list);
        for (T t : list) {
            _remove(t);
        }
    }

    @Override
    public void retain(List<T> list) {
        requireNotEmpty(list);
        temp = new Object[size];
        int count = 0, index;
        for (T t : list) {
            if (-1 != (index = indexOf(t))) {
                temp[count++] = array[index];
            }
        }
        release();
        array = temp;
        temp = EMPTY;
        size = count;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        validateIndex(fromIndex, toIndex);
        return _subList(fromIndex, toIndex);
    }

    @Override
    public void sort(Comparator<T> comparator) {
        if (size <= 1) return;
        if (size <= 7) {
            Arrays.selectSort(array, 0, size - 1, comparator);
        } else {
            Arrays.quickSort(array, 0, size - 1, comparator);
        }
    }

    @Override
    public Object[] toArray() {
        Object[] rtn = new Object[size];
        System.arraycopy(array, 0, rtn, 0, size);
        return rtn;
    }

    @Override
    public void trimToSize() {
        temp = toArray();
        release();
        array = temp;
        temp = EMPTY;
    }

    @Override
    public void clear() {
        release();
        array = EMPTY;
        size = 0;
    }

    @Override
    public int capacity() {
        return array.length;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    //内部类
    private class Itr implements Iterator<T> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return size > 0 && i < size;
        }

        @Override
        public T next() {
            return (T) array[i++];
        }
    }
}
