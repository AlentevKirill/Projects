package javaAdvanced.info.kgeorgiy.ja.alentev.arrayset;

import java.util.*;


public class ArraySet<E> extends AbstractSet<E> implements NavigableSet<E> {
    private ArrayList<E> arraySet = new ArrayList<>();
    private final Comparator<? super E> comparator;
    private boolean isComparatorNull;
    private boolean isReversed = false;

    public ArraySet() {
        this.comparator = null;
    }

    // :NOTE: Можно унифицировать с ArraySet(Collection<? extends E>, Comparator<? super E>)
    public ArraySet(Collection<? extends E> collection) {
        //this.comparator = null;
        // :NOTE: лишняя аллокация, можно объединить с сортировкой
        //this.arraySet.addAll(new HashSet<>(collection));
        //this.arraySet.sort(null);
        ArraySet<E> a = new ArraySet<>(collection, null);
        this.arraySet = a.arraySet;
        this.comparator = null;
        isComparatorNull = true;
    }

    public ArraySet(Collection<? extends E> collection, Comparator<? super E> comparator) {
        this.comparator = comparator;
        TreeSet<E> set = new TreeSet<>(comparator);
        set.addAll(collection);
        this.arraySet.addAll(set);
        isComparatorNull = comparator == null;
    }

    public ArraySet(Collection<? extends E> collection, Comparator<? super E> comparator, boolean isReversed) {
        this.comparator = comparator;
        TreeSet<E> set = new TreeSet<>(comparator);
        set.addAll(collection);
        this.arraySet.addAll(set);
        isComparatorNull = comparator == null;
    }

    @Override
    public E lower(E o) {
        int i = binSearch(arraySet, o, comparator);
        return i > 0 ? arraySet.get(i - 1) : null;
    }

    @Override
    public E floor(E o) {
        int i = binSearch(arraySet, o, comparator);
        if (arraySet.size() == 0) {
            return null;
        }
        // :NOTE: не читаемо
        return i == arraySet.size() ?
                arraySet.get(i - 1) :
                compare(arraySet.get(i), o) == 0 ?
                        arraySet.get(i) :
                        i == 0 ?
                                null :
                                arraySet.get(i - 1);
    }

    @Override
    public E ceiling(E o) {
        int i = 0;
        try {
            i = binSearch(arraySet, o, comparator);
        } catch (ClassCastException e) {
            System.out.println(e.getLocalizedMessage() + e);
        }
        return i == arraySet.size() ? null : arraySet.get(i);
    }

    @Override
    public E higher(E o) {
        int i = binSearch(arraySet, o, comparator);
        return i == arraySet.size() ? null :
                compare(arraySet.get(i), o) == 0 ?
                        i + 1 == arraySet.size() ?
                                null :
                                arraySet.get(i + 1) :
                        arraySet.get(i);
    }

    //Непонятно как реализовать эти методы с учётом того, что нельзя изменять массив
    // :NOTE: так и нужно, как вы сделали
    //----------------------------------
    @Override
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException();
    }
    //----------------------------------

    @Override
    public Iterator<E> iterator() {
        // :NOTE: реализация итератора не эффективная
        return arraySet.iterator();
    }

    // :NOTE: тут не должно быть unchecked cast
    // :NOTE: Асимптотически неэффективно
    @Override
    public NavigableSet<E> descendingSet() {
        return new ArraySet<>(arraySet, comparator.reversed());
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    //@SuppressWarnings("unchecked")
    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    // :NOTE: тут не должно быть unchecked cast
    // :NOTE: Слишком сложно
    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        int i = binSearch(arraySet, fromElement, comparator);
        int j = binSearch(arraySet, toElement, comparator);
        if (i == arraySet.size()) {
            return new ArraySet<>();
        }
        if (compare(fromElement, toElement) == 0 && compare(arraySet.get(i), fromElement) == 0) {
            return toInclusive && fromInclusive ?
                    new ArraySet<>(Set.of(arraySet.get(i)), comparator) :
                    new ArraySet<>();
        }
        if (i == j) {
            return toInclusive && compare(arraySet.get(j), toElement) == 0 ?
                    new ArraySet<>(Set.of(arraySet.get(i)), comparator) :
                    new ArraySet<>();
        }
        if (!fromInclusive) {
            i = compare(arraySet.get(i), fromElement) == 0 ? i + 1 : i;
        }
        if (toInclusive) {
            // :NOTE: Лишние копирования
            return j == arraySet.size() ?
                    new ArraySet<>(arraySet.subList(i, j), comparator) :
                    compare(arraySet.get(j), toElement) == 0 ?
                            new ArraySet<>(arraySet.subList(i, j + 1), comparator) :
                            new ArraySet<>(arraySet.subList(i, j), comparator);
        }
        // :NOTE: Лишние копирования
        return new ArraySet<>(arraySet.subList(i, j), comparator);
    }

    // :NOTE: тут не должно быть unchecked cast
    // :NOTE: Слишком сложно
    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        int i = binSearch(arraySet, toElement, comparator);
        if (i == arraySet.size()) {
            return new ArraySet<>(arraySet, comparator);
        }
        if (compare(arraySet.get(i), toElement) == 0) {
            // :NOTE: Лишние копирования
            return inclusive ? new ArraySet<>(arraySet.subList(0, i + 1), comparator) :
                    new ArraySet<>(arraySet.subList(0, i), comparator);
        } else {
            return new ArraySet<>(arraySet.subList(0, i), comparator);
        }
        //return arraySet.length == 0 ? new ArraySet<>() : compare(toElement, arraySet[0]) >= 0 ? subSet(arraySet[0], true, toElement, inclusive) : new ArraySet<>();
    }

    // :NOTE: тут не должно быть unchecked cast
    // :NOTE: Слишком сложно
    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        int i = binSearch(arraySet, fromElement, comparator);
        if (i == arraySet.size()) {
            return new ArraySet<>();
        }
        if (compare(arraySet.get(i), fromElement) == 0) {
            // :NOTE: Лишние копирования
            return inclusive ?
                    new ArraySet<>(arraySet.subList(i, arraySet.size()), comparator) :
                    new ArraySet<>(arraySet.subList(i + 1, arraySet.size()), comparator);
        } else {
            return new ArraySet<>(arraySet.subList(i, arraySet.size()), comparator);
        }
        //return arraySet.length == 0 ? new ArraySet<>() : compare(fromElement, arraySet[0]) > 0 ? new ArraySet<>() : subSet(fromElement, inclusive, arraySet[arraySet.length - 1], true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    // :NOTE: тут не должно быть unchecked cast
    @Override
    public E first() {
        if (arraySet.size() == 0) {
            throw new NoSuchElementException();
        }
        return arraySet.get(0);
    }

    // :NOTE: тут не должно быть unchecked cast
    @Override
    public E last() {
        if (arraySet.size() == 0) {
            throw new NoSuchElementException();
        }
        return arraySet.get(arraySet.size() - 1);
    }

    @Override
    public int size() {
        return arraySet.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        int i = binSearch(arraySet, (E) o, comparator);
        return i != arraySet.size() && compare(arraySet.get(i), o) == 0;
    }

    /*public static class ArrayIterator<T> implements Iterator<T> {
        private T[] array;
        private int pos = 0;

        public ArrayIterator(ArrayList<T> arrayList) {
            array = (T[]) arrayList.toArray();
        }

        public boolean hasNext() {
            return pos < array.length;
        }

        public T next() throws NoSuchElementException {
            if (hasNext())
                return array[pos++];
            else
                throw new NoSuchElementException();
        }

        // :NOTE: не нужно
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }*/

    // :NOTE: Почему не Collections.binarySearch / Arrays.binarySearch?
    private int binSearch(ArrayList<E> array, E key, Comparator<? super E> comparator) {
        int i = Collections.binarySearch(array, key, comparator);
        return i < 0 ? Math.abs(i) - 1 : i;
        /*int l = -1;
        int r = array.length;
        while (l < r - 1) {
            int m = (l + r) / 2;
            if (compare(array[m], key) < 0) {
                l = m;
            } else {
                r = m;
            }
        }
        return r;*/
    }

    // :NOTE: этот метод вызывается слишком часто, чтобы позволить себе проверять в нем comparator на null
    @SuppressWarnings("unchecked")
    private int compare(Object k1, Object k2) {
        return isComparatorNull ? ((Comparable<? super E>) k1).compareTo((E) k2)
                : comparator.compare((E) k1, (E) k2);
    }

}