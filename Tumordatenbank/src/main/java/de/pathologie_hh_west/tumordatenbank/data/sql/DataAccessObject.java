package de.pathologie_hh_west.tumordatenbank.data.sql;

import de.pathologie_hh_west.tumordatenbank.logic.exceptions.DataAccessException;

interface DataAccessObject<K, T> {
	
	T get(K key) throws DataAccessException;
	
	void insert(T obj) throws DataAccessException;
	
	void update(T obj) throws DataAccessException;
	
	void upsert(T obj) throws DataAccessException;
	
	void delete(T obj) throws DataAccessException;
	
}
