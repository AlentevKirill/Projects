package queue;

public class MainArrayQueueADT {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = ArrayQueueADT.create();
        ArrayQueueADT queue2 = ArrayQueueADT.create();
        fill(queue1);
        //fill(queue2);

        dump(queue1);
        //dump(queue2);
        fill(queue1);
        System.out.println(ArrayQueueADT.get(queue1, 3));
        ArrayQueueADT.set(queue1, 3, 2);
        System.out.println(ArrayQueueADT.element(queue1));
        ArrayQueueADT.clear(queue1);
        System.out.println(ArrayQueueADT.isEmpty(queue1));
        System.out.println(ArrayQueueADT.size(queue1));
        dump(queue1);
    }
    private static void fill(ArrayQueueADT queue) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue,i * 10);
        }
        System.out.println(ArrayQueueADT.size(queue));
    }

    private static void dump(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println(ArrayQueueADT.dequeue(queue));
        }
    }
}
