package queue;

public class MainArrayQueue {
    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        fill(queue1);
        //fill(queue2);

        dump(queue1);
        //dump(queue2);
        fill(queue1);
        System.out.println(queue1.get(3));
        queue1.set(3, 2);
        System.out.println(queue1.element());
        queue1.clear();
        System.out.println(queue1.isEmpty());
        System.out.println(queue1.size());
        dump(queue1);
    }
    private static void fill(ArrayQueue queue) {
        for (int i = 0; i < 5; i++) {
            queue.enqueue(i * 10);
        }
        System.out.println(queue.size());
    }

    private static void dump(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
    }
}
