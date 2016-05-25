package de.pathologie_hh_west.tumordatenbank.data.sql;

interface DataAccessObject<K, T> {
	
	T get(K key) throws DataAccessException;
	
	void insert(T obj) throws DataAccessException;
	
	void update(T obj) throws DataAccessException;
	
	void upsert(T obj) throws DataAccessException;
	
}
