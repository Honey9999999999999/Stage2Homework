package org.example;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MyLinkedList<E> implements Iterable<E> {
    private class Node<T>{
        public Node(Node<T> preElement, T current, Node<T> nextElement){
            previous = preElement;
            item = current;
            next = nextElement;
        }

        public Node<T> previous;
        public T item;
        public Node<T> next;
    }

    private class MyIterator implements Iterator<E> {
        private Node<E> current = new Node<>(null, null, firstElement);

        @Override
        public boolean hasNext() {
            return current.next != null;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = current.next;

            return current.item;
        }

        @Override
        public void remove() {
            // Необязательная реализация
            throw new UnsupportedOperationException();
        }
    }

    private int size;
    private Node<E> firstElement;
    private Node<E> lastElement;

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Iterable.super.spliterator();
    }

    public void addAll(Collection<? extends E> c) {
        addAll(size, c);
    }
    public void addAll(int index, Collection<? extends E> c) {
        @SuppressWarnings("unchecked") E[] array = (E[])c.toArray();

        for(E element : array){
            insert(index, element);
        }

        size += array.length;
    }
    public void add(E element){
        addLast(element);
    }

    public void insert(int index, E element){
        Node<E> next = getNode(index);
        Node<E> prev = null;

        if(next != null){
            prev = next.previous;
        }

        Node<E> current = new Node<>(prev, element, next);

        if(prev == null){
            firstElement = current;
        }
        else{
            prev.next = current;
        }
        if(next == null){
            lastElement = current;
        }
        else{
            next.previous = current;
        }

        size++;
    }

    public void addFirst(E element){
        linkFirst(element);
    }
    public void addLast(E element){
        linkLast(element);
    }

    private void linkFirst(E element){
        final Node<E> f = firstElement;
        final Node<E> newNode = new Node<>(null, element, f);
        firstElement = newNode;
        if (f == null)
            lastElement = newNode;
        else
            f.previous = newNode;
        size++;
    }
    private void linkLast(E element){
        final Node<E> l = lastElement;
        final Node<E> newNode = new Node<>(l, element, null);
        lastElement = newNode;
        if (l == null)
            firstElement = newNode;
        else
            l.next = newNode;
        size++;
    }

    public E get(int index){
        return getNode(index).item;
    }
    private Node<E> getNode(int index){
        checkPositionIndex(index);

        Node<E> x;
        if (index < (size >> 1)) {
            x = firstElement;
            for (int i = 0; i < index; i++)
                x = x.next;
        } else {
            x = lastElement;
            for (int i = size - 1; i > index; i--)
                x = x.previous;
        }
        return x;
    }
    private Node<E> find(Object element){
        Node<E> x = firstElement;

        for (int i = 0; i < size; i++){
            if(x.item.equals(element)){
                return  x;
            }
            x = x.next;
        }

        return  null;
    }

    public boolean remove(Object element){
        Node<E> current = find(element);

        if(current == null){
            return  false;
        }
        else{
            Node<E> prev = current.previous;
            Node<E> next = current.next;

            if(prev == null){
                firstElement = next;
            }
            else{
                prev.next = next;
            }

            if(next == null){
                lastElement = prev;
            }
            else{
                next.previous = prev;
            }

            size--;

            return true;
        }
    }

    public int getSize(){
        return  size;
    }

    public void clear(){
        if(firstElement == null){
            return;
        }

        Node<E> current = firstElement;

        while (true){

            current.previous = null;
            current = current.next;

            if(current != null){
                current.previous.next = null;
            }
            else {
                break;
            }
        }

        firstElement = null;
        lastElement = null;
    }

    private void checkPositionIndex(int index) {
        if (!isPositionIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    private boolean isPositionIndex(int index) {
        return index >= 0 && index <= size;
    }
    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    @Override
    public String toString(){
        if(size > 0){
            Node<E> node = firstElement;
            StringBuilder strBuffer = new StringBuilder("[");

            while (true){
                strBuffer.append(node.item.toString());
                node = node.next;

                if(node != null){
                    strBuffer.append(", ");
                }
                else {
                    strBuffer.append("]");
                    break;
                }
            }

            return new String(strBuffer);
        }

        return "Empty";
    }
}
