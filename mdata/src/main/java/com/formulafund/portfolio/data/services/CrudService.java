package com.formulafund.portfolio.data.services;

import java.util.Set;

import com.formulafund.portfolio.data.model.BaseEntity;

public interface CrudService<T extends BaseEntity> {
	Set<T> findAll();
	T findById(Long id);
	T save(T object);
	void delete(T object);
	void deleteById(Long id);
	
}
