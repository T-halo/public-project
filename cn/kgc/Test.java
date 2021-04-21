package cn.kgc;


import cn.kgc.util.Stack;
import cn.kgc.util.impl.LinkedList;

public class Test {

    static class A extends Object {

    }

    public static void main(String[] args) {
        /*List<Integer> list = new LinkedList<>();
        list.add(2);
        list.add(Arrays.asList(5,11,7,11,9));

        System.out.println(list.indexOf(11));
        System.out.println(list.lastIndexOf(11));
        System.out.println(list.indexOf(100));
        list.add(2,100);
        list.add(2,Arrays.asList(100,200,300));
        list.set(2,100);
        System.out.println(list.get(list.size()));
        System.out.println(list.remove(2));
        Iterator<Integer> _it = list.remove(2, 4).iterator();
        while (_it.hasNext()) {
            System.out.println(_it.next());
        }
        Integer del = 11;
        System.out.println(list.remove(del));
        list.remove(Arrays.asList(2,11,11,3));
        list.retain(Arrays.asList(2,11,11,3));
        Iterator<Integer> _it = list.subList(0, list.size()-1).iterator();
        while (_it.hasNext()) {
            System.out.println(_it.next());
        }
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1-o2;
            }
        });
        for (Object o : list.toArray()) {
            System.out.println(o);
        }
        System.out.println(list.capacity());

        list.clear();
        System.out.println(list.size());

        System.out.println("==============");
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }*/

        //obj = null;
        //finalize()
        //System.gc();

        /*Queue<String> queue = new LinkedList<>();
        queue.offer("aaa");
        queue.offer("aab");
        queue.offer("aac");
        queue.offer("aba");
        queue.offer("abc");

        String item = null;
        while (null != (item=queue.poll())){
            System.out.println(item);
        }
        System.out.println(queue.peek());*/

        /*Deque<String> deque = new LinkedList<>();
        deque.offerFirst("aaa");
        deque.offerFirst("bbb");
        deque.offerFirst("ccc");
        deque.offerLast("eee");
        deque.offerLast("fff");
        deque.offerLast("ggg");

        while (true){
            String h = deque.pollFirst();
            if(null==h){
                break;
            }
            System.out.println(h);
            String f = deque.pollLast();
            if(null==f){
                break;
            }
            System.out.println(f);
        }*/

        Stack<String> stack = new LinkedList<>();
        stack.push("aaa");
        stack.push("bbb");
        stack.push("ccc");

        String item = null;
        while (null != (item = stack.pop())) {
            System.out.println(item);
        }


    }
}
