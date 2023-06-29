package com.demo.project.entity;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Size(min = 2, message = "유저네임은 최소한 2 이상")
	private String username;

	@Size(min = 2, message = "비번은 최소한 4 이상")
	private String password;

	@Transient
	private String confirmPassword; // 실제 테이블에는 없음

	@Email(message = "이메일 양식에 맞게 입력")
	private String email;

	@Column(name = "phone_number") // 테이블의 열이름과 같게 매칭
	@Size(min = 6, message = "전화번호는 6 이상")
	private String phoneNumber;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한 목록을 리턴 ( 유저 권한 )
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public boolean isAccountNonExpired() {
		// 계정이 만료 되지 않았음?
		return true; // 만료 안됨
	}

	@Override
	public boolean isAccountNonLocked() {
		// 계정이 잠겨 있지 않음?
		return true; // 잠기지 않음
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// 비밀번호가 만료되지 않음?
		return true; // 만료안됨
	}

	@Override
	public boolean isEnabled() {
		// 사용가능한 계정?
		return true; // 사용가능
	}

}
