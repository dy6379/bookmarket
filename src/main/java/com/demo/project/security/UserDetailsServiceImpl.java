package com.demo.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.project.entity.Admin;
import com.demo.project.entity.User;
import com.demo.project.repository.AdminRepository;
import com.demo.project.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 인증에 필요한 유저정보를 읽어온다.(username 파라메타)
		User user = userRepo.findByUsername(username);
		Admin admin = adminRepository.findByUsername(username);

		if (admin != null)
			return admin;
		if (user != null)
			return user;

		throw new UsernameNotFoundException("유저 " + username + "이 없습니다");
	}

}
