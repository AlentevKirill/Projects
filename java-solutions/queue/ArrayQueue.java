package queue;

import java.util.Arrays;

/*
    Model:
        [a1, a2, ... an]
        n -- размер очереди
    Inv:
        n >= 0 (размер очереди)
        forall i = 1..n: a[i] != null
     */

public class ArrayQueue extends AbstractQueue implements Queue {
    private Object[] elements = new Object[10];
    private int head = -1;
    private int tail = -1;

    /*
        Pred: e != null
        Post: forall i = 1..n' a'[i] == a[i] && a[n] == e
     */

    protected void enqueueImpl(Object e) {
        ensureCapacity();
        if (this.head == -1) {
            this.head = 0;
        }
        tail = (tail + 1) % elements.length;
        elements[tail] = e;
    }

    /* Pred: size > 0
       Post: result = a[1] && forall i = 1..n a'[i] == a[i]
     */

    public Object element() {
        assert size > 0;
        return elements[head];
    }

    /* Pred: size > 0
       Post: forall i = 2..n a'[i] == a[i] && a[1] == null
     */

    protected Object dequeueImpl() {
        Object result = elements[head];
        elements[head] = null;
        if (head == this.tail) {
            head = -1;
            tail = -1;
        } else {
            head = (head + 1) % elements.length;
        }
        return result;
    }

    private void ensureCapacity() {
        if (head == (tail + 1) % elements.length) {
            head = 0;
            tail = size;
            elements = Arrays.copyOf(toArray(), size * 2);
        }
    }

    /*
        Pred: true
        Post: forall i = 1..n: a[i] == null
     */

    public void clear() {
        Arrays.fill(elements, null);
        head = -1;
        tail = -1;
        size = 0;
    }

}
