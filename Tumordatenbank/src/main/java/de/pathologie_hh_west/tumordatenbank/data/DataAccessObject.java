package de.pathologie_hh_west.tumordatenbank.data;

interface DataAccessObject<K, T> {
	
	T get(K key);
	
	void insert(T obj);
	
	void update(T obj);
	
	void upsert(T obj);
	
}
