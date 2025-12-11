package org.example;

import java.util.*;

public class MyHashMap<K,V> implements Iterable<KeyValuePair<K,V>>{
    private class MyIterator implements Iterator<KeyValuePair<K,V>> {
        private KeyValuePair<K,V>[] array = toArray();
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index + 1 < array.length;
        }

        @Override
        public KeyValuePair<K,V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return array[index++];
        }

        @Override
        public void remove() {
            // Необязательная реализация
            throw new UnsupportedOperationException();
        }
    }

    public MyHashMap(){
        createBuckets();
    }

    private static final int BASE_CAPACITY = 16;
    private int capacity = 16;
    private ArrayList<MyLinkedList<KeyValuePair<K,V>>> buckets;
    private int size;

    @Override
    public MyIterator iterator() {
        return new MyIterator();
    }

    public KeyValuePair<K,V> get(int index){
        return toArray()[index];
    }
    public KeyValuePair<K,V>[] toArray() {
        KeyValuePair<K,V>[] array = (KeyValuePair<K,V>[])new KeyValuePair[size];

        int index = 0;
        for (MyLinkedList<KeyValuePair<K,V>> linkList : buckets){
            for (KeyValuePair<K,V> item : linkList){
                array[index++] = item;
            }
        }

        return array;
    }

    public boolean addAll(Collection<KeyValuePair<K, V>> c) {
        for (KeyValuePair<K, V> obj : c){
            if(!add(obj.key, obj.value)){
                return false;
            }
        }
        return true;
    }
    public boolean add(K key, V value) {
        int hash = getHashKey(key);
        int index = getIndex(hash);
        MyLinkedList<KeyValuePair<K,V>> list = buckets.get(index);

        for (KeyValuePair<K,V> item : list){
            if(isEquals(hash, key, item.key)){
                if(list.remove(item)){
                    list.add(new KeyValuePair<>(key, value));

                    return true;
                }
                else {
                    return  false;
                }
            }
        }

        list.add(new KeyValuePair<>(key, value));

        if(capacity * .75f <= ++size){
            resize();
        }

        return true;
    }

    private void resize(){
        KeyValuePair<K,V>[] pairs = toArray();
        clear(capacity * 2);

        for(KeyValuePair<K, V> pair : pairs){
            add(pair.key, pair.value);
        }
    }


    public void clear() {
        clear(BASE_CAPACITY);
    }
    private void clear(int capacity){
        for (MyLinkedList<KeyValuePair<K,V>> linkList : buckets){
            linkList.clear();
        }

        this.capacity = capacity;
        createBuckets();
        size = 0;
    }
    private void createBuckets(){
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++){
            buckets.add(i, new MyLinkedList<>());
        }
    }

    public boolean removeAll(Collection<K> c) {
        for (K key : c){
            if(!remove(key)){
                return false;
            }
        }

        return true;
    }
    public boolean remove(K key) {
        int hash = getHashKey(key);
        int index = getIndex(hash);
        MyLinkedList<KeyValuePair<K,V>> list = buckets.get(index);

        for (KeyValuePair<K,V> item : list){
            if(isEquals(hash, key, item.key)) {
                list.remove(item);
                size--;

                return true;
            }
        }

        return false;
    }

    public boolean containsAllKeys(Collection<K> c) {
        for(K comparableKey : c){
            if(!containsKey(comparableKey)){
                return false;
            }
        }

        return true;
    }
    public boolean containsKey(K key) {
        for (MyLinkedList<KeyValuePair<K, V>> linkList : buckets){
            for (KeyValuePair<K, V> item : linkList){
                if(key.equals(item.key)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isEquals(int hA, Object a, Object b){
        return hA == getHashKey(a) && a.equals(b);
    }

    private int getHashKey(Object obj){
        int h = obj.hashCode();
        return  h ^ h >>> 16;
    }
    private int getIndex(int hash){
        return  ((capacity) - 1) & hash;
    }

    public int getSize() {
        return size;
    }
    public boolean isEmpty() {
        return size <= 0;
    }

    @Override
    public String toString(){
        if(isEmpty()){
            return "Empty";
        }
        else {
            StringBuilder str = new StringBuilder();
            int counter = 0;

            for(MyLinkedList<KeyValuePair<K,V>> bucket : buckets){
                str.append("[Bucket #").append(counter).append("]\n{\n");

                for(KeyValuePair<K, V> pair : bucket){
                    str.append("\t[").append(pair.key).append("|").append(pair.value).append("]");
                }

                str.append("\n}\n\n");
                counter++;
            }

            return new String(str);
        }
    }
}
