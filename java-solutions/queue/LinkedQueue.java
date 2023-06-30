package queue;

/*
    Model:
        [a1, a2, ... an]
        n -- размер очереди
    Inv:
        n >= 0 (размер очереди)
        forall i = 1..n: a[i] != null
     */

import java.util.Objects;

public class LinkedQueue extends AbstractQueue implements Queue {
    private List tail;
    private List head;


    private static class List {
        private Object value;
        private List post;

        public List(List post, Object value) {
            this.post = post;
            this.value = value;
        }

        public List(Object value) {
            this.value = value;
        }
    }

    /*
        Pred: e != null
        Post: forall i = 1..n' a'[i] == a[i] && a[n] == e
     */

    protected void enqueueImpl(Object e) {
        if (size == 1) {
            head = new List(e);
            tail = head;
        } else {
            tail.post = new List(e);
            tail = tail.post;
        }
    }

    /* Pred: size > 0
       Post: result = a[head] && forall i = 1..n a'[i] == a[i]
     */

    public Object element() {
        assert size > 0;
        return head.value;
    }

    /* Pred: size > 0
       Post: result = a[1] && forall i = 2..n a'[i] == a[i] && a[1] == null
     */

    public Object dequeueImpl() {
        Object result = head.value;
        head = head.post;
        return result;
    }

    /*
        Pred: true
        Post: forall i = 0..limit - 1: a[i] == null
     */

    public void clear() {
        size = 0;
        head = null;
        tail = null;
    }

}
