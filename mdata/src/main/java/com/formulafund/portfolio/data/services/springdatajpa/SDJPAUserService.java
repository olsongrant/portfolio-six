package com.formulafund.portfolio.data.services.springdatajpa;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.ApplicationUser;
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
	public Set<ApplicationUser> findAll() {
		return StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
				.collect(Collectors.toSet());
	}

	@Override
	public ApplicationUser findById(Long id) {
		return this.userRepository.findById(id).orElse(null);
	}

	@Override
	public ApplicationUser save(ApplicationUser object) {
		return this.userRepository.save(object);
	}

	@Override
	public void delete(ApplicationUser object) {
		this.userRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		this.userRepository.deleteById(id);
	}

}
