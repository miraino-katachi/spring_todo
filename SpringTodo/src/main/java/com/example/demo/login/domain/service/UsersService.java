package com.example.demo.login.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.login.domain.model.Users;
import com.example.demo.login.domain.repository.UsersDao;

@Service
public class UsersService {

	@Autowired
	//@Qualifier("UsersDaoJdbcImpl")
	UsersDao dao;

	//1件取得用メソッド
	public Users selectOne(int id) {
		return dao.selectOne(id);

	}

	//全件取得用メソッド
	public List<Users> selectMany(){
		return dao.selectMany();
	}

	//id取得用メソッド
	public Users selectId(String userName) {
		return dao.selectId(userName);

	}
}
