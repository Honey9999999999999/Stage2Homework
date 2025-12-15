package org.example;

import java.util.*;
import java.util.function.Consumer;

public class MyHashMap<K,V> implements Iterable<KeyValuePair<K,V>>{
    private class MyIterator implements Iterator<KeyValuePair<K,V>> {
        private KeyValuePair<K,V>[] array = toArray();
        private int index = -1;

        @Override
        public boolean hasNext() {
            return index + 1 < array.length;
        }

        @Override
        public KeyValuePair<K,V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return array[++index];
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

    public KeyValuePair<K,V> getByIndex(int index){
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

    public V getByKey(K key) {
        KeyValuePair<K,V> pair = findPair(key);
        if(pair != null){
            return pair.value;
        }

        throw new NullPointerException("Key is not found in list");
    }

    private MyLinkedList<KeyValuePair<K,V>> getBucket(K key) {
        int hash = getHashKey(key);
        int index = getIndex(hash);
        return buckets.get(index);
    }

    private KeyValuePair<K,V> findPair(K key) {
        int hash = getHashKey(key);
        MyLinkedList<KeyValuePair<K,V>> list = getBucket(key);

        for (KeyValuePair<K,V> item : list) {
            if (isEquals(hash, key, item.key)) {
                return item; // Найдена нужная пара
            }
        }
        return null;
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
        MyLinkedList<KeyValuePair<K,V>> bucket = getBucket(key);
        KeyValuePair<K,V> pair = findPair(key);

        if(pair != null){
            if(bucket.remove(pair)){
                bucket.add(new KeyValuePair<>(key, value));

                return true;
            }
            else {
                return  false;
            }
        }

        bucket.add(new KeyValuePair<>(key, value));

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
        MyLinkedList<KeyValuePair<K,V>> bucket = getBucket(key);
        KeyValuePair<K,V> pair = findPair(key);

        if(pair != null){
            bucket.remove(pair);
            size--;

            return true;
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
        if(obj == null){
            return 0;
        }

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
