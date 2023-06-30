package queue;

/*
    Model:
        [a1, a2, ... an]
        n -- размер очереди
    Inv:
        n >= 0 (размер очереди)
        forall i = 1..n: a[i] != null
     */

public interface Queue {

    /* Pred: true
       Post: result = n && forall i = 1..n a'[i] == a[i]
     */

    int size();

    /* Pred: true
       Post: result == (size == 0) && forall i = 1..n a'[i] == a[i]
     */

    boolean isEmpty();

    /*
        Pred: e != null
        Post: n == n' + 1 && forall i = 1..n' a'[i] == a[i] && a[n] == e
     */

    void enqueue(Object e);

    /* Pred: size > 0
       Post: n = n' - 1 && forall i = 2..n' a'[i-1] == a[i] && a[1] == null
     */

    Object dequeue();

    /* Pred: size > 0
       Post: result = a[1] && forall i = 1..n a'[i] == a[i]
     */

    Object element();

    /*
        Pred: true
        Post: forall i = 1..n: a[i] == null
     */

    /*
    Pred: size > 0
    Post: forall i = 1..n a[i] == a'[i] && result == a
     */

    Object[] toArray();

    /*
    Pred: substitute != null && index < size && index >= 0
    Post: a[index + 1] == substitute && forall i = 1..index + 1..n a'[i] == a[i]
     */

    void set(int index, Object substitute);

    /*
    Pred: index < size && index >= 0
    Post: result == a[index + 1] && forall i = 1..n a'[i] == a[i]
    */

    Object get(int index);

    void clear();
}
