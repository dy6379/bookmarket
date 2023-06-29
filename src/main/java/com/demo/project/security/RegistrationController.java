package com.demo.project.security;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.project.entity.User;
import com.demo.project.repository.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping
	public String register(User user) {
		return "register";
	}
	
	@PostMapping
	public String register(@Valid User user, BindingResult bindingResult, Model model ) {
		
		if(bindingResult.hasErrors()) { // 입력 validation 에러시 다시 되돌아감(에러 메세지 포함)
			return "register";
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			model.addAttribute("passwordNotMatch", "패스워드확인이 틀릴때");
			return "register";
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword())); //패스워드 암호화해서 입력
		userRepo.save(user); 	//DB에 새 유저 저장
		
		return "redirect:/login";
	}
}
