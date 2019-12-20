package com.example.demo.login.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.login.domain.model.TodoItem;
import com.example.demo.login.domain.repository.TodoItemDao;

@Service
public class TodoItemService {

	@Autowired
	TodoItemDao dao;

	//insert用メソッド
	public boolean insert(TodoItem item) {

		//insert実行
		int rowNumber = dao.insertOne(item);

		//判定用変数
		boolean result = false;
		if(rowNumber > 0) {
			result = true;
		}
		return result;

	}

	//カウント用メソッド
	public int count() {
		return dao.count();
	}

	//全件取得用メソッド
	public List<TodoItem> selectMany(){
		return dao.selectMany();
	}

	//1件更新
	public boolean updateOne(TodoItem todoItem) {
		//1件更新
		int rowNumber = dao.updateOne(todoItem);
		//判定用変数
		boolean result = false;
		if(rowNumber > 0) {
			//update成功
			result = true;
		}
		return result;
	}

	//1件取得用メソッド
	public TodoItem selectOne(String itemId) {
		//selectOne実行
		return dao.selectOne(itemId);
	}

	//1件削除メソッド
	public boolean deleteOne(TodoItem todoItem) {
		//1件更新
		int rowNumber = dao.deleteOne(todoItem);
		//判定用変数
		boolean result = false;
		if(rowNumber > 0) {
			//update成功
			result = true;
		}
		return result;
	}

	//完了ボタン用メソッド
	public boolean updateFinishedDate(TodoItem todoItem) {
		//1件更新
		int rowNumber = dao.updateFinishDate(todoItem);
		//判定用変数
		boolean result = false;
		if(rowNumber > 0) {
			//update成功
			result = true;
		}
		return result;
	}

	//検索用メソッド
	public List<TodoItem> find(String str){
		return dao.selectFind(str);
	}
}
