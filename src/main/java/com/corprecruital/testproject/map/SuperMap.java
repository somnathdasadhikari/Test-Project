package com.corprecruital.testproject.map;

import com.corprecruital.testproject.exception.*;


public class SuperMap<K, V> {
	
	private Entry<K, V>[] table; // Array of Entry.
	private int capacity; // Initial capacity of HashMap
	private int size = 0;
	private double lf = 0.75;

	static class Entry<K, V> {
		K key;
		V value;
		Entry<K, V> next;

		public Entry(K key, V value, Entry<K, V> next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
	}

	public SuperMap() {
		this(16);
	}

	@SuppressWarnings("unchecked")
	public SuperMap(int capacity) {
		this.capacity = capacity;
		this.table = new Entry[this.capacity];
	}
	
	 public int size() {
		return size;
	 }

	@SuppressWarnings("unchecked")
	public void put(K newKey, V data) {
		if (newKey == null)
			throw new NullKeyException("Null key instertion is not allowed"); // does not allow to store null.

		if (size == lf * capacity) {
            // rehash
            Entry<K, V>[] old = table;

            capacity *= 2;
            size = 0;
            table = new Entry[capacity];

            for (Entry<K,V> e: old) {
                while (e != null) {
                    put(e.key, e.value);
                    e = e.next;
                }
            }
        }

		// calculate hash of key.
		int hash = hash(newKey);
		// create new entry.
		Entry<K, V> newEntry = new Entry<K, V>(newKey, data, null);

		// if table location does not contain any entry, store entry there.
		if (table[hash] == null) {
			table[hash] = newEntry;
			size++;
		} else {
			Entry<K, V> previous = null;
			Entry<K, V> current = table[hash];

			while (current != null) { // we have reached last entry of table.
				if (current.key.equals(newKey)) {
					if (previous == null) { // node has to be insert on first of table.
						newEntry.next = current.next;
						table[hash] = newEntry;
						return;
					} else {
						newEntry.next = current.next;
						previous.next = newEntry;
						return;
					}
				}
				previous = current;
				current = current.next;
			}
			previous.next = newEntry;
			size++;
		}
	}
	
	
	public V get(K key) {
		if(key == null)
			throw new NullKeyException("Null key is not allowed");
		int hash = hash(key);
		if (table[hash] == null) {
			throw new NoDataFoundException("No data found with given key");
		} else {
			Entry<K, V> temp = table[hash];
			while (temp != null) {
				if (temp.key.equals(key))
					return temp.value;
				temp = temp.next; // return value corresponding to key.
			}
			throw new NoDataFoundException("No data found with given key"); // throws exception if key is not found.
		}
	}

	public K remove(K deleteKey) {
		
		if(deleteKey == null)
			throw new NullKeyException("Null key is not allowed");

		int hash = hash(deleteKey);

		if (table[hash] == null) {
			throw new NoDataFoundException("No data found with given key");
		} 
		else {
			Entry<K, V> previous = null;
			Entry<K, V> current = table[hash];

			while (current != null) { // we have reached last entry node of bucket.
				if (current.key.equals(deleteKey)) {
					if (previous == null) { // delete first entry node.
						table[hash] = table[hash].next;
						size --;
						return current.key;
					} else {
						previous.next = current.next;
						size --;
						return current.key;
					}
				}
				previous = current;
				current = current.next;
			}
			throw new NoDataFoundException("No data found with given key");
		}

	}
	
	public void display() {

		for (int i = 0; i < capacity; i++) {
			if (table[i] != null) {
				Entry<K, V> entry = table[i];
				while (entry != null) {
					System.out.print("{" + entry.key + "=" + entry.value + "}" + " ");
					entry = entry.next;
				}
			}
		}

	}
	
	
	private int hash(K key) {
		return Math.abs(key.hashCode()) % capacity;
	}

}
