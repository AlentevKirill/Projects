package queue;

import java.util.Objects;

public abstract class AbstractQueue {
    protected static int size = 0;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(Object e) {
        Objects.requireNonNull(e);
        size++;
        enqueueImpl(e);
    }

    protected abstract void enqueueImpl(Object e);

    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    public abstract Object element();

    public Object get(int index) {
        assert index < size;
        assert index >= 0;
        return toArray()[index];
    }

    public void set(int index,  Object substitute) {
        Objects.requireNonNull(substitute);
        assert index < size;
        assert index >= 0;
        Object[] save = new Object[size];
        int temp = size;
        for (int i = 0; i < temp; i++) {
            if (i == index) {
                save[i] = substitute;
                dequeue();
                continue;
            }
            save[i] = dequeue();
        }
        for (int i = 0; i < temp; i++) {
            enqueue(save[i]);
        }
    }

    public Object[] toArray() {
        if (size == 0) {
            return new Object[0];
        }
        int temp = size;
        Object[] a = new Object[size];
        for (int i = 0; i < temp; i++) {
            a[i] = dequeue();
        }
        for (int i = 0; i < temp; i++) {
            enqueue(a[i]);
        }
        return a;
    }

}
