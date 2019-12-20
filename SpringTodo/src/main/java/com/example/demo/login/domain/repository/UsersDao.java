package com.example.demo.login.domain.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.example.demo.login.domain.model.Users;

public interface UsersDao {

	//Usersテーブルのデータを1件取得
	public Users selectOne(int userId) throws DataAccessException;

	//Usersテーブルのidを1件取得
	public Users selectId(String userName) throws DataAccessException;

	//Usersテーブルの全データを取得
	public List<Users> selectMany() throws DataAccessException;

}
