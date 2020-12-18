package com.formulafund.portfolio.data.services.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.BaseEntity;
import com.formulafund.portfolio.data.services.CrudService;

@Service
public class BaseMapService<T extends BaseEntity> implements CrudService<T> {
	protected static AtomicLong nextId = new AtomicLong(0L);
	
	protected static Long getNextId() {
		return nextId.getAndIncrement();
	}
	protected Map<Long, T> map = new HashMap<>();
	
	public Set<T> findAll() {
		return new HashSet<T>(map.values());
	}
	
	public T findById(Long id) {
		return map.get(id);
	}
	
	public T save(T object) {
		if (object.getId() == null) {
			object.setId(BaseMapService.getNextId());
		} 
		map.put(object.getId(), object);
		return object;
	}
	public void deleteById(Long id) {
		map.remove(id);
	}
	public void delete(T object) {
		map.entrySet().removeIf(entry -> entry.getValue().equals(object));
	}

}
