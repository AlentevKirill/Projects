package queue;

public class MainQueueModule {
    public static void main(String[] args) {
        fill();
        //fill(queue2);

        dump();
        //dump(queue2);
        fill();
        System.out.println(ArrayQueueModule.get(3));
        ArrayQueueModule.set(3, 2);
        System.out.println(ArrayQueueModule.element());
        ArrayQueueModule.clear();
        System.out.println(ArrayQueueModule.isEmpty());
        System.out.println(ArrayQueueModule.size());
        dump();
    }
    private static void fill() {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue(i * 10);
        }
        System.out.println(ArrayQueueModule.size());
    }

    private static void dump() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.dequeue());
        }
    }
}
