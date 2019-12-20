package com.example.demo.login.domain.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.example.demo.login.domain.model.TodoItem;

public interface TodoItemDao {

	//TodoItemテーブルの件数を取得
	public int count() throws DataAccessException;

	//TodoItemテーブルにデータを1件insert
	public int insertOne(TodoItem todoItem) throws DataAccessException;

	//TodoItemテーブルにデータを1件取得
	public TodoItem selectOne(String id) throws DataAccessException;

	//TodoItemテーブルのデータを全て取得
	public List<TodoItem> selectMany() throws DataAccessException;

	//TodoItemテーブルを1件更新
	public int updateOne(TodoItem todoItem) throws DataAccessException;

	//TodoItemテーブルを1件削除
	public int deleteOne(TodoItem todoItem) throws DataAccessException;

	//完了ボタン用SQL
	public int updateFinishDate(TodoItem todoItem) throws DataAccessException;

	//TodoItem検索用SQL
	public List<TodoItem> selectFind(String str) throws DataAccessException;

}
