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

public class ArrayQueueModule {
    private static Object[] elements = new Object[10];
    private static int head = -1;
    private static int tail = -1;
    private static int size = 0;

    /*
        Pred: e != null
        Post: n == n' + 1 && forall i = 1..n' a'[i] == a[i] && a[n] == e
     */

    public static void enqueue(Object e) {
        Objects.requireNonNull(e);
        size++;
        ensureCapacity();
        if (head == -1) {
            head = 0;
        }
        tail = (tail + 1) % elements.length;
        elements[tail] = e;
    }

    /* Pred: size > 0
       Post: result = a[1]
     */

    public static Object element() {
        assert !isEmpty();
        return elements[head];
    }

    /* Pred: size > 0
       Post: n = n' - 1 && forall i = 2..n' a'[i] == a[i] && a[1] == null
     */

    public static Object dequeue() {
        assert !isEmpty();
        size--;
        Object result = elements[head];
        elements[head] = null;
        if (head == tail) {
            head = -1;
            tail = -1;
        } else {
            head = (head + 1) % elements.length;
        }
        return result;
    }

    /* Pred: true
       Post: result == n && forall i = 1..n a'[i] == a[i]
     */

    public static int size() {
        return size;
    }

    /* Pred: true
       Post: (result == false && size > 0) && (result == false && size == 0) && forall i = 1..n a'[i] == a[i]
     */

    public static boolean isEmpty() {
        return head == -1;
    }

    /*
    Pred: index < size && index >= 0
    Post: result == a[index + 1] && forall i = 1..n a'[i] == a[i]
     */

    public static Object get(int index) {
        assert index < size();
        assert index >= 0;
        return elements[(head + index) % elements.length];
    }

    /*
    Pred: substitute != null && index < size && index >= 0
    Post: a[index + 1] == substitute && forall i = 1..index + 1..n a'[i] == a[i]
     */

    public static void set(int index, Object substitute) {
        Objects.requireNonNull(substitute);
        assert index < size();
        assert index >= 0;
        elements[(head + index) % elements.length] = substitute;
    }

    private static void ensureCapacity() {
        if (head == (tail + 1) % elements.length) {
            if (tail == elements.length - 1) {
                elements = Arrays.copyOf(elements, elements.length * 2);
            } else {
                Object[] a = new Object[elements.length];
                System.arraycopy(elements, head, a, 0, elements.length - head);
                System.arraycopy(elements, 0, a, elements.length - head, tail + 1);
                tail = elements.length - 1;
                elements = Arrays.copyOf(a, elements.length * 2);
                head = 0;
            }
        }
    }

    /*
        Pred: true
        Post: forall i = 1..n: a[i] == null
     */

    public static void clear() {
        Arrays.fill(elements, null);
        head = -1;
        tail = -1;
    }
}
