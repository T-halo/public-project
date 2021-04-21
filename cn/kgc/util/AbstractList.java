package cn.kgc.util;

public abstract class AbstractList<T> implements List<T> {
    protected int size;

    @Override
    public int size() {
        return size;
    }

    protected void requireNonNull(T t) {
        if (null == t) {
            throw new RuntimeException("parameter t null pointer exception");
        }
    }

    protected void validateIndex(int index, boolean add) {
        if (size == 0 || index < 0 || (add ? index > size : index >= size)) {
            throw new IndexOutOfBoundsException("parameter index out of bounds exception : " + index);
        }
    }

    protected void validateIndex(int beginIndex, int endIndex) {
        if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex > endIndex exception");
        }
        validateIndex(beginIndex, false);
        validateIndex(endIndex, false);
    }

    protected void requireNotEmpty(List<T> list) {
        if (null == list || list.isEmpty()) {
            throw new RuntimeException("parameter list null or empty exception");
        }
    }
}
