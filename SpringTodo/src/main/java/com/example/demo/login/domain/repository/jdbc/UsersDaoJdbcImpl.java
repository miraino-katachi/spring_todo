package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.Users;
import com.example.demo.login.domain.repository.UsersDao;

@Repository
public class UsersDaoJdbcImpl implements UsersDao{

	@Autowired
	JdbcTemplate jdbc;

	@Override
	public Users selectOne(int id) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM users WHERE id = ?", id);

		//結果返却用の変数
		Users user = new Users();
		//取得したデータを結果返却用の変数にセット
		user.setUserId((Integer)map.get("id"));//ユーザID
		user.setUser((String)map.get("user"));//ユーザ名
		user.setPass((String)map.get("pass"));//パスワード
		user.setFamilyName((String)map.get("family_name"));//ユーザ姓
		user.setFirstName((String)map.get("first_name"));//ユーザ名
		user.setIsDeleted((Integer)map.get("is_deleted"));//削除フラグ
		user.setRole((String)map.get("role"));//権限
		user.setCreateDateTime((Date)map.get("create_date_time"));
		user.setUpdateDateTime((Date)map.get("update_date_time"));
		return user;
	}

	@Override
	public List<Users> selectMany() throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM users");

		//結果返却用の変数
		List<Users> userList = new ArrayList<Users>();
		//取得したデータを結果返却用Listに格納する
		for(Map<String, Object> map: getList) {
			Users users = new Users();
			users.setUserId((Integer)map.get("id"));
			users.setUser((String)map.get("user"));
			userList.add(users);
		}
		return userList;
	}

	@Override
	public Users selectId(String userName) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM users WHERE user = ?", userName);

		//結果返却用の変数
		Users user = new Users();
		//取得したデータを結果返却用の変数にセット
		user.setUserId((Integer)map.get("id"));//ユーザID
		user.setUser((String)map.get("user"));//ユーザ名
		return user;
	}

}
