package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.User;
import com.formulafund.portfolio.data.repositories.UserRepository;
import com.formulafund.portfolio.data.services.UserService;

@Service
@Profile("springdatajpa")
public class SDJPAUserService implements UserService {
	
	private UserRepository userRepository;
	
	public SDJPAUserService(UserRepository uRepository) {
		this.userRepository = uRepository;
	}

	@Override
	public Set<User> findAll() {
		return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
				.collect(Collectors.toSet());
	}

	@Override
	public User findById(Long id) {
		return this.userRepository.findById(id).orElse(null);
	}

	@Override
	public User save(User object) {
		return this.userRepository.save(object);
	}

	@Override
	public void delete(User object) {
		this.userRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.userRepository.deleteById(id);
	}

}
