package queue;

import java.util.Arrays;
import java.util.Objects;
/*
    Model:
        [a1, a2, ... an]
        n -- размер очереди
    Inv:
        n >= 0 (размер очереди)
        forall i = 1..n: a[i] != null
     */

public class ArrayQueueADT {
    private int size = 0;
    private Object[] elements = new Object[10];
    private int head = -1;
    private int tail = -1;

    /*
        Pred: true
        Post R.n == 0 && R - новый
     */

    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    /*
        Pred: e != null && queue != null
        Post: n == n' + 1 && forall i = 1..n' a'[i] == a[i] && a[n] == e
     */

    public static void enqueue(ArrayQueueADT queue, Object e) {
        assert queue != null;
        Objects.requireNonNull(e);
        queue.size++;
        ensureCapacity(queue);
        if (queue.head == -1) {
            queue.head = 0;
        }
        queue.tail = (queue.tail + 1) % queue.elements.length;
        queue.elements[queue.tail] = e;
    }

    /* Pred: size > 0 && queue != null
       Post: result = a[1]
     */

    public static Object element(ArrayQueueADT queue) {
        assert !isEmpty(queue);
        return queue.elements[queue.head];
    }

    /* Pred: size > 0 && queue != null
       Post: n = n' - 1 && forall i = 2..n' a'[i] == a[i] && a[1] == null
     */

    public static Object dequeue(ArrayQueueADT queue) {
        assert !isEmpty(queue);
        queue.size--;
        Object result = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        if (queue.head == queue.tail) {
            queue.head = -1;
            queue.tail = -1;
        } else {
            queue.head = (queue.head + 1) % queue.elements.length;
        }
        return result;
    }

    /* Pred: queue != null
       Post: result == n && forall i = 1..n a'[i] == a[i]
     */

    public static int size(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }

    /*
       Pred: queue != null
       Post: (result == false && size > 0) && (result == false && size == 0) && forall i = 1..n a'[i] == a[i]
     */

    public static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null;
        return queue.head == -1;
    }

    /*
    Pred: index < size && index >= 0 && queue != null
    Post: result == a[index + 1] && forall i = 1..n a'[i] == a[i]
    */

    public static Object get(ArrayQueueADT queue, int index) {
        assert queue != null;
        assert index < size(queue);
        assert index >= 0;
        return queue.elements[(queue.head + index) % queue.elements.length];
    }

    /*
    Pred: substitute != null && index < size && index >= 0 && queue != null
    Post: a[index + 1] == substitute && forall i = 1..index + 1..n a'[i] == a[i]
     */

    public static void set(ArrayQueueADT queue, int index, Object substitute) {
        assert queue != null;
        assert index < size(queue);
        assert index >= 0;
        Objects.requireNonNull(substitute);
        queue.elements[(queue.head + index) % queue.elements.length] = substitute;

        }

    private static void ensureCapacity(ArrayQueueADT queue) {
        if (queue.head == (queue.tail + 1) % queue.elements.length) {
            if (queue.tail == queue.elements.length - 1) {
                queue.elements = Arrays.copyOf(queue.elements, queue.elements.length * 2);
            } else {
                Object[] a = new Object[queue.elements.length];
                System.arraycopy(queue.elements, queue.head, a, 0, queue.elements.length - queue.head);
                System.arraycopy(queue.elements, 0, a, queue.elements.length - queue.head, queue.tail + 1);
                queue.tail = queue.elements.length - 1;
                queue.elements = Arrays.copyOf(a, queue.elements.length * 2);
                queue.head = 0;
            }
        }
    }

    /*
        Pred: queue != null
        Post: forall i = 1..n: a[i] == null
     */

    public static void clear(ArrayQueueADT queue) {
        assert queue != null;
        Arrays.fill(queue.elements, null);
        queue.head = -1;
        queue.tail = -1;
    }
}
