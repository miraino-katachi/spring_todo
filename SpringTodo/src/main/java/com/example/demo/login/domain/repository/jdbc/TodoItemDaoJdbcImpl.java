package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.TodoItem;
import com.example.demo.login.domain.model.Users;
import com.example.demo.login.domain.repository.TodoItemDao;
import com.example.demo.login.domain.service.UsersService;

@Repository
public class TodoItemDaoJdbcImpl implements TodoItemDao{

	@Autowired
	JdbcTemplate jdbc;

	@Autowired
	UsersService usersService;

	@PersistenceContext
	private EntityManager entityManager;

	//全件取得してカウント
	@Override
	public int count() throws DataAccessException {
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM todo_items", Integer.class);
		return count;
	}

	//TodoItemテーブルのデータを1件追加
	@Override
	public int insertOne(TodoItem todoItem) throws DataAccessException {
		int rowNumber = jdbc.update("INSERT INTO todo_items VALUES(?,?,?,?,?,?,?,?,?)",
				todoItem.getId(),
				todoItem.getUserId(),
				todoItem.getItemName(),
				todoItem.getRegistrationDate(),
				todoItem.getExpireDate(),
				todoItem.getFinishedDate(),
				todoItem.getIsDelete(),
				todoItem.getCreateDateTime(),
				todoItem.getUpdateDateTime());
		return rowNumber;
	}

	@Override
	public TodoItem selectOne(String itemId) throws DataAccessException {
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM todo_items WHERE id = ?", itemId);

		//結果返却用の変数
		TodoItem item = new TodoItem();
		//取得したデータを結果返却用の変数にセット
		item.setId((Integer)map.get("id"));
		item.setUserId((Integer)map.get("user_id"));
		item.setItemName((String)map.get("item_name"));
		item.setRegistrationDate((Date)map.get("registration_date"));
		item.setExpireDate((Date)map.get("expire_date"));
		item.setFinishedDate((Date)map.get("finished_date"));
		item.setIsDelete((Integer)map.get("is_deleted"));
		item.setCreateDateTime((Date)map.get("create_date_time"));
		item.setUpdateDateTime((Date)map.get("update_date_time"));
		return item;
	}

	//TodoItemテーブルの全データを取得
	@Override
	public List<TodoItem> selectMany() throws DataAccessException {
		List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM todo_items ORDER BY expire_date");

		//結果返却用の変数
		List<TodoItem> todoList = new ArrayList<TodoItem>();
		//取得したデータを結果返却用Listに格納する
		for(Map<String, Object> map: getList) {

			Users users = usersService.selectOne((Integer)map.get("user_id"));

			TodoItem item = new TodoItem();
			item.setId((Integer)map.get("id"));
			item.setItemName((String)map.get("item_name"));
			item.setUserId((Integer)map.get("user_id"));
			item.setUserName(users.getUser());
			item.setIsDelete((Integer)map.get("is_deleted"));
			item.setExpireDate((Date)map.get("expire_date"));
			item.setFinishedDate((Date)map.get("finished_date"));
			todoList.add(item);
		}
		return todoList;

	}

	@Override
	public int updateOne(TodoItem todoItem) throws DataAccessException {
		// 1件更新
		String sql = "UPDATE todo_items SET user_id=?, item_name=?, expire_date=?, finished_date=?, is_deleted=?, update_date_time=? WHERE id=?";

		int rowNumber = jdbc.update(sql, todoItem.getUserId(), todoItem.getItemName(), todoItem.getExpireDate(), todoItem.getFinishedDate(), todoItem.getIsDelete(), todoItem.getUpdateDateTime(), todoItem.getId());

		return rowNumber;
	}

	@Override
	public int deleteOne(TodoItem todoItem) throws DataAccessException {
		// 削除フラグを１に設定
		String sql = "UPDATE todo_items SET is_deleted=1, update_date_time=? WHERE id=?";

		int rowNumber = jdbc.update(sql, todoItem.getUpdateDateTime(), todoItem.getId());
		return rowNumber;
	}

	@Override
	public int updateFinishDate(TodoItem todoItem) throws DataAccessException {
		// finishedDateを現在日時に設定
		String sql = "UPDATE todo_items SET finished_date=?, update_date_time=? WHERE id=?";

		int rowNumber = jdbc.update(sql, todoItem.getFinishedDate(), todoItem.getUpdateDateTime(), todoItem.getId());
		return rowNumber;
	}

	@Override
	public List<TodoItem> selectFind(String str) throws DataAccessException {

		//検索用SQL
		String sql = "SELECT t.* FROM todo_items t, users u WHERE t.user_id = u.id AND CONCAT(t.item_name, u.user, IFNULL(t.expire_date, \"\"), t.registration_date, IFNULL(t.finished_date, \"\")) LIKE ? ORDER BY t.expire_date";

		List<Map<String, Object>> getList = jdbc.queryForList(sql, "%" + str + "%");

		//結果返却用の変数
		List<TodoItem> todoList = new ArrayList<TodoItem>();
		//取得したデータを結果返却用Listに格納する
		for(Map<String, Object> map: getList) {

			Users users = usersService.selectOne((Integer)map.get("user_id"));

			TodoItem item = new TodoItem();
			item.setId((Integer)map.get("id"));
			item.setItemName((String)map.get("item_name"));
			item.setUserId((Integer)map.get("user_id"));
			item.setUserName(users.getUser());
			item.setIsDelete((Integer)map.get("is_deleted"));
			item.setExpireDate((Date)map.get("expire_date"));
			item.setFinishedDate((Date)map.get("finished_date"));
			todoList.add(item);
		}
		return todoList;

	}

}
