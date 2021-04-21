package cn.kgc.util.impl;

import cn.kgc.util.*;

import java.util.Comparator;
import java.util.Iterator;

public class LinkedList<T> extends AbstractList<T> implements List<T>, Deque<T>, Queue<T>, Stack<T> {
    @Override
    public boolean offerFirst(T t) {
        if (size == 0) add(t);
        else add(0, t);
        return true;
    }

    @Override
    public T pollLast() {
        return _remove(size - 1);
    }

    @Override
    public T peekLast() {
        return _get(size - 1).t;
    }

    @Override
    public boolean offer(T t) {
        add(t);
        return true;
    }

    @Override
    public T poll() {
        return _remove(0);
    }

    @Override
    public T peek() {
        return _get(0).t;
    }

    @Override
    public void push(T t) {
        offerFirst(t);
    }

    @Override
    public T pop() {
        return poll();
    }

    private static class Node<T> {
        Node prev;
        T t;
        Node next;

        public Node() {
        }

        public Node(Node prev, T t, Node next) {
            this.prev = prev;
            this.t = t;
            this.next = next;
        }

        public Node(T t) {
            this(null, t, null);
        }

        public Node(T t, Node next) {
            this(null, t, next);
        }

        public Node(Node prev, T t) {
            this(prev, t, null);
        }
    }

    private Node<T> head;
    private Node<T> foot;

    public LinkedList() {
    }

    public LinkedList(List<T> list) {
        add(list);
    }

    @Override
    public int indexOf(T t) {
        int pos = -1;
        Iterator<Node<T>> it = new _Itr(true);
        while (it.hasNext()) {
            pos++;
            if (it.next().t.equals(t)) {
                return pos;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(T t) {
        int pos = size;
        Iterator<Node<T>> it = new _Itr(false);
        while (it.hasNext()) {
            pos--;
            if (it.next().t.equals(t)) {
                return pos;
            }
        }
        return -1;
    }

    @Override
    public void add(T t) {
        if (size == 0) {
            head = foot = new Node<T>(t);
        } else {
            Node<T> node = new Node<>(foot, t);
            foot.next = node;
            foot = node;
        }
        size++;
    }

    private Node<T>[] toLL(List<T> list) {
        Node<T> _head = null;
        Node<T> _foot = null;
        Iterator<T> it = list.iterator();
        Node<T> curr = null;
        while (it.hasNext()) {
            T t = it.next();
            if (null == curr) {
                _head = _foot = curr = new Node<T>(t);
            } else {
                curr = new Node<>(_foot, t);
                _foot.next = curr;
                _foot = curr;
            }
        }
        return new Node[]{_head, _foot};
    }

    private List<T> toList(Node<T> _head, Node<T> _foot) {
        List<T> list = new ArrayList<>();
        Node<T> curr = _head;
        do {
            list.add(curr.t);
        } while (_foot != (curr = curr.next));
        list.add(curr.t);
        return list;
    }

    @Override
    public void add(List<T> list) {
        requireNotEmpty(list);
        Node<T>[] nodes = toLL(list);
        if (size == 0) {
            head = nodes[0];
        } else {
            foot.next = nodes[0];
            nodes[0].prev = foot;
        }
        foot = nodes[1];
        size += list.size();
    }

    private Node<T> _get(int index) {
        if (size == 0 || index < 0 || index >= size) {
            return null;
        }
        boolean left = index < size / 2.0;
        int pos = left ? -1 : size;
        Iterator<Node<T>> it = new _Itr(left);
        while (it.hasNext()) {
            pos += left ? 1 : -1;
            Node<T> node = it.next();
            if (pos == index) {
                return node;
            }
        }
        throw new RuntimeException("can't find element in index : " + index);
    }

    @Override
    public void add(int index, T t) {
        validateIndex(index, true);
        requireNonNull(t);
        Node<T> curr = new Node<>(t);
        Node<T> next = _get(index);
        size++;
        if (null == next) { //尾插
            foot.next = curr;
            curr.prev = foot;
            foot = curr;
            return;
        }
        Node<T> prev = next.prev;
        if (null == prev) { //头插
            next.prev = curr;
            curr.next = next;
            head = curr;
            return;
        }
        //中插
        prev.next = curr;
        curr.prev = prev;
        curr.next = next;
        next.prev = curr;
    }

    @Override
    public T set(int index, T t) {
        validateIndex(index, false);
        requireNonNull(t);
        Node<T> node = _get(index);
        T rtn = node.t;
        node.t = t;
        return rtn;
    }

    @Override
    public void add(int index, List<T> list) {
        validateIndex(index, true);
        requireNotEmpty(list);
        Node<T>[] nodes = toLL(list);
        Node<T> next = _get(index);
        size += list.size();
        if (null == next) { //尾插
            foot.next = nodes[0];
            nodes[0].prev = foot;
            foot = nodes[1];
            return;
        }
        Node<T> prev = next.prev;
        if (null == prev) { //头插
            head.prev = nodes[1];
            nodes[1].next = head;
            head = nodes[0];
            return;
        }
        //中插
        prev.next = nodes[0];
        nodes[0].prev = prev;
        nodes[1].next = next;
        next.prev = nodes[1];
    }

    @Override
    public T get(int index) {
        validateIndex(index, false);
        return _get(index).t;
    }

    private void _remove(Node<T> delNode) {
        Node prev = delNode.prev;
        Node next = delNode.next;
        size--;
        if (null == prev) {
            if (null == next) { //唯一删
                head = foot = null;
            } else { //头删
                delNode.next = null;
                next.prev = null;
                head = next;
            }
        } else {
            if (null == next) { //尾删
                delNode.prev = null;
                prev.next = null;
                foot = prev;
            } else { //中删
                delNode.prev = null;
                delNode.next = null;
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    private T _remove(int index) {
        Node<T> node = _get(index);
        T t = null;
        if (null != node) {
            t = node.t;
            _remove(node);
        }
        return t;
    }

    @Override
    public T remove(int index) {
        validateIndex(index, false);
        return _remove(index);
    }

    @Override
    public List<T> remove(int fromIndex, int toIndex) {
        validateIndex(fromIndex, toIndex);
        Node<T> _head = _get(fromIndex);
        Node<T> _foot = _get(toIndex);
        List<T> ts = toList(_head, _foot);
        Node _prev = _head.prev;
        Node _next = _foot.next;

        //_prev和_next关联
        //切断头元素和其上一个元素节点关联
        if (null != _prev) {
            _prev.next = null;
            _head.prev = null;
            _prev.next = _next;
        }
        //切断尾元素和其下一个元素节点关联
        if (null != _next) {
            _next.prev = null;
            _foot.next = null;
            _next.prev = _prev;
        }

        Node<T> curr = _head.next, prev;
        //切断头尾之间两两节点之间的关联
        do {
            prev = curr.prev;
            prev.next = null;
            curr.prev = null;
        } while (_foot != (curr = curr.next));

        //切断尾元素和其前一个元素之间的关联
        prev = curr.prev;
        prev.next = null;
        curr.prev = null;
        size -= toIndex - fromIndex + 1;
        return ts;
    }

    @Override
    public boolean remove(T t) {
        Iterator<Node<T>> it = new _Itr(true);
        Node<T> node = null, curr = null;
        //根据参数t找到其对应的节点
        while (it.hasNext()) {
            curr = it.next();
            if (curr.t.equals(t)) {
                node = curr;
                break;
            }
        }
        //如果t对应的节点存在，则删除并返回true
        if (null != node) {
            _remove(node);
            return true;
        }
        return false;
    }

    @Override
    public void remove(List<T> list) {
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            remove(it.next());
        }
    }

    @Override
    public void retain(List<T> list) {
        //删除在list中不存在的链表Node<T>
        Iterator<Node<T>> it = new _Itr(true);
        Node<T> node;
        while (it.hasNext()) {
            node = it.next();
            //证明节点中的t是否存在于参数list中
            if (!list.contains(node.t)) {
                //不存在则删除node
                it.remove();
            }
        }
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        validateIndex(fromIndex, toIndex);
        return toList(_get(fromIndex), _get(toIndex));
    }

    @Override
    public void sort(Comparator<T> comparator) {
        if (size <= 1) return;
        Node<T>[] nodes = new Node[size];
        int _size = 0;
        Iterator<Node<T>> it = new _Itr(true);
        while (it.hasNext()) {
            nodes[_size++] = it.next();
        }
        //匿名内部类创建接口对象
        Comparator<Node<T>> _comp = new Comparator<Node<T>>() {
            @Override
            public int compare(Node<T> o1, Node<T> o2) {
                return comparator.compare(o1.t, o2.t);
            }
        };
        if (size <= 7) {
            Arrays.selectSort(nodes, 0, nodes.length - 1, _comp, true);
        } else {
            Arrays.quickSort(nodes, 0, nodes.length - 1, _comp, true);
        }
        head = nodes[0];
        foot = nodes[nodes.length - 1];
        for (int i = 0; i + 1 < nodes.length; i++) {
            nodes[i].next = nodes[i + 1];
            nodes[i + 1].prev = nodes[i];
            nodes[i] = null;
        }
        //排序后，最后一个节点若非foot，必须置空其next属性
        nodes[nodes.length - 1].next = null;
        nodes[nodes.length - 1] = null;
    }

    @Override
    public Object[] toArray() {
        Iterator<Node<T>> it = new _Itr(true);
        Object[] objs = new Object[size];
        int _size = 0;
        while (it.hasNext()) {
            objs[_size++] = it.next().t;
        }
        return objs;
    }

    @Override
    public void clear() {
        size = 0;
        Iterator<Node<T>> it = new _Itr(true);
        head = null;
        foot = null;
        Node<T> node, prev;
        while (it.hasNext()) {
            node = it.next();
            node.t = null;
            prev = node.prev;
            if (null != prev) {
                prev.next = null;
                node.prev = null;
            }
        }
    }

    @Override
    public int capacity() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<T> {
        private Iterator<Node<T>> it;

        public Itr() {
            it = new _Itr(true);
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public T next() {
            return it.next().t;
        }

        @Override
        public void remove() {
            it.remove();
        }
    }

    private class _Itr implements Iterator<Node<T>> {
        boolean forward;
        boolean first;
        Node<T> curr;

        public _Itr(boolean forward) {
            this.first = true;
            this.forward = forward;
            this.curr = forward ? head : foot;
        }

        @Override
        public boolean hasNext() {
            return null != (first ? curr : (forward ? curr.next : curr.prev));
        }

        @Override
        public Node<T> next() {
            if (first) {
                first = false;
                return curr;
            }
            return curr = forward ? curr.next : curr.prev;
        }

        @Override
        public void remove() {
            Node<T> node = forward ? curr.next : curr.prev;
            _remove(curr);
            curr = node;
            first = true;
        }
    }
}
