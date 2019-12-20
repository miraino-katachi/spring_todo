package com.example.demo.login.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.login.domain.model.GroupOrder;
import com.example.demo.login.domain.model.TodoItem;
import com.example.demo.login.domain.model.TodoItemForm;
import com.example.demo.login.domain.model.Users;
import com.example.demo.login.domain.service.TodoItemService;
import com.example.demo.login.domain.service.UsersService;

@Controller
public class HomeController {

	@Autowired
	private TodoItemService todoItemService;

	@Autowired
	private UsersService userService;

	/*
	 * 作業登録画面のGET用メソッド
	 */
	@GetMapping("/add")
	public String getTodoAdd(@ModelAttribute TodoItemForm form, Model model) {
		//コンテンツ部分にユーザ一覧を表示するための文字列を登録
		model.addAttribute("contents", "login/addTodoList :: addTodoList_contents");

		//ユーザ一覧の生成
		List<Users> userList = userService.selectMany();

		//Modelに作業一覧リストを登録
		model.addAttribute("userList", userList);

		//作業登録画面に遷移
		return "login/homeLayout";

	}

	/*
	 * 作業登録画面のPOST用メソッド
	 */
	@PostMapping("/add")
	public String postTodoAdd(@ModelAttribute @Validated(GroupOrder.class)TodoItemForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

		//入力チェックに引っかかった場合、作業登録画面に戻る
		if(bindingResult.hasErrors()) {
			//GETリクエスト用のメソッドを呼び出して、作業登録画面に戻ります
			return getTodoAdd(form, model);
		}

		//formの中をコンソールに出力して確認
		System.out.println(form);

		//現在日時を取得
		Date d = new Date();

		//insert用変数
		TodoItem todoItem = new TodoItem();
		todoItem.setUserId(form.getSelectUser());
		todoItem.setItemName(form.getItemName());
		todoItem.setRegistrationDate(d);
		todoItem.setExpireDate(form.getExpireDate());
		todoItem.setFinishedDate(null);
		todoItem.setIsDelete(0);
		todoItem.setCreateDateTime(d);
		todoItem.setUpdateDateTime(d);

		//作業登録処理
		boolean result = todoItemService.insert(todoItem);

		//登録結果判定処理
		if(result) {
			System.out.println("登録成功");
		}else {
			redirectAttributes.addFlashAttribute("result", "登録失敗");
			//getErrorPageにリダイレクト
			return "redirect:/getErrorPage";
		}
		//todoListにリダイレクト
		return "redirect:/todoList";
	}

	//作業一覧画面用GETメソッド
	@GetMapping("/todoList")
	public String getTodoList(Model model) {
		//コンテンツ部分に作業一覧を表示するための文字列を登録
		model.addAttribute("contents", "login/todoList :: todoList_contents");

		//ログインユーザ名取得
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		int id = Integer.parseInt(auth.getName());
		//ユーザ情報を取得
		Users user = userService.selectOne(id);
		model.addAttribute("loginUser", user);

		//作業一覧の生成
		List<TodoItem> todoItemList = todoItemService.selectMany();

		//Modelに作業一覧リストを登録
		model.addAttribute("todoItemList", todoItemList);

		return "login/homeLayout";
	}

	/*
	 * 作業更新画面のGET用メソッド
	 */
	@GetMapping("/editTodoList/{id}")
	public String getTodoEdit(@ModelAttribute TodoItemForm form, Model model, @PathVariable("id") String itemId) {
		//コンテンツ部分に編集画面を表示するための文字列を登録
		model.addAttribute("contents", "login/editTodoList :: editTodoList_contents");

		//UserId確認(デバッグ)
		System.out.println("itemID = " + itemId);

		//ユーザ一覧の生成
		List<Users> userList = userService.selectMany();

		//Modelに作業一覧リストを登録
		model.addAttribute("userList", userList);

		//ユーザIDのチェック
		if(itemId != null && itemId.length() > 0) {
			//ユーザ情報を取得
			TodoItem item = todoItemService.selectOne(itemId);

			//TodoItemクラスをフォームクラスに変換
			form.setId(item.getId());
			form.setItemName(item.getItemName());
			form.setUserName(item.getUserName());
			form.setExpireDate(item.getExpireDate());
			form.setFinishedDate(item.getFinishedDate());

			if(item.getFinishedDate() != null) {
				form.setFinishedCheck(true);
			}else {
				form.setFinishedCheck(false);
			}
			form.setIsDelete(item.getIsDelete());

			//Modelに登録
			model.addAttribute("todoItemForm", form);
		}

		//作業登録画面に遷移
		return "login/homeLayout";

	}

	//作業更新用処理
	@PostMapping(value="/edit")
	public String userDetailUpdate(@ModelAttribute @Validated(GroupOrder.class)TodoItemForm form, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("更新ボタンの処理");

		//入力チェックに引っかかった場合、作業登録画面に戻る
		if(bindingResult.hasErrors()) {
			//GETリクエスト用のメソッドを呼び出して、作業更新画面に戻ります
			return getTodoEdit(form, model, Integer.toString(form.getId()));
		}

		//TodoItemインスタンス生成
		TodoItem todoItem = new TodoItem();
		//フォームクラスをTodoItemクラスに変換(form=画面取得用, TodoItem=データ保存用)
		//現在日時を取得
		Date d = new Date();
		todoItem.setId(form.getId());
		todoItem.setUserId(form.getSelectUser());
		todoItem.setItemName(form.getItemName());
		todoItem.setExpireDate(form.getExpireDate());

		if(form.getFinishedCheck()) {
			todoItem.setFinishedDate(d);
		}else {
			todoItem.setFinishedDate(null);
		}
		//todoItem.setIsDelete(0);
		todoItem.setUpdateDateTime(d);

		try {
			//更新処理
			boolean result = todoItemService.updateOne(todoItem);
			if(result) {
				System.out.println("更新成功");
			}else{
				redirectAttributes.addFlashAttribute("result", "更新失敗");
				//getErrorPageにリダイレクト
				return "redirect:/getErrorPage";
			}
		}catch(DataAccessResourceFailureException e) {
			model.addAttribute("result", "更新失敗");
		}
		//ユーザ一覧画面表示
		return getTodoList(model);
	}

	/*
	 * 作業削除画面のGET用メソッド
	 */
	@GetMapping("/deleteTodoList/{id}")
	public String getTodoDelete(@ModelAttribute TodoItemForm form, Model model, @PathVariable("id") String itemId) {
		//コンテンツ部分に編集画面を表示するための文字列を登録
		model.addAttribute("contents", "login/deleteTodoList :: deleteTodoList_contents");

		//UserId確認(デバッグ)
		System.out.println("itemID = " + itemId);

		//ユーザIDのチェック
		if(itemId != null && itemId.length() > 0) {
			//ユーザ情報を取得
			TodoItem item = todoItemService.selectOne(itemId);

			//TodoItemクラスをフォームクラスに変換
			form.setId(item.getId());
			form.setItemName(item.getItemName());
			//Modelに登録
			model.addAttribute("todoItemList", form);
		}

		//作業登録画面に遷移
		return "login/homeLayout";

	}

	//ユーザ削除用処理
	@PostMapping(value="/delete")
	public String userDetailDelete(@ModelAttribute TodoItemForm form, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("削除ボタンの処理");

		//TodoItemインスタンス生成
		TodoItem todoItem = new TodoItem();
		//フォームクラスをTodoItemクラスに変換(form=画面取得用, TodoItem=データ保存用)
		//現在日時を取得
		Date d = new Date();
		todoItem.setId(form.getId());
		todoItem.setUpdateDateTime(d);

		try {
			//更新処理
			boolean result = todoItemService.deleteOne(todoItem);
			if(result) {
				System.out.println("削除成功");
			}else{
				redirectAttributes.addFlashAttribute("result", "削除失敗");
				//getErrorPageにリダイレクト
				return "redirect:/getErrorPage";

			}
		}catch(DataAccessResourceFailureException e) {
			model.addAttribute("result", "削除失敗");
		}
		//ユーザ一覧画面表示
		return getTodoList(model);
	}

	//エラー画面用GETメソッド
	@GetMapping("/getErrorPage")
	public String getErrorPage(Model model) {
		//コンテンツ部分に作業一覧を表示するための文字列を登録
		String result = (String)model.asMap().get("result");
		model.addAttribute("result", result);
		model.addAttribute("contents", "login/error :: error_contents");

		return "login/homeLayout";
	}

	//TOP画面用GETメソッド
	@PostMapping(value="/topPage")
	public String getTopPage(Model model) {

		//コンテンツ部分に作業一覧を表示するための文字列を登録
		model.addAttribute("contents", "login/todoList :: todoList_contents");

		//ログインユーザ名取得
		Authentication auth = SecurityContextHolder.getContext(
				).getAuthentication();
		int id = Integer.parseInt(auth.getName());
		//ユーザ情報を取得
		Users user = userService.selectOne(id);
		model.addAttribute("loginUser", user);

		//作業一覧の生成
		List<TodoItem> todoItemList = todoItemService.selectMany();


		//Modelに作業一覧リストを登録
		model.addAttribute("todoItemList", todoItemList);

		return "login/homeLayout";
	}

	/*
	 * 作業完了のGET用メソッド
	 */
	@GetMapping("/complete/{id}")
	public String getComplete(@ModelAttribute TodoItemForm form, Model model, @PathVariable("id") String itemId, RedirectAttributes redirectAttributes) {

		//ItemId確認(デバッグ)
		System.out.println("itemID = " + itemId);

		//TodoItemインスタンス生成
		TodoItem todoItem = new TodoItem();
		//フォームクラスをTodoItemクラスに変換(form=画面取得用, TodoItem=データ保存用)
		//現在日時を取得
		Date d = new Date();
		todoItem.setId(form.getId());
		todoItem.setFinishedDate(d);
		todoItem.setUpdateDateTime(d);

		try {
			//更新処理
			boolean result = todoItemService.updateFinishedDate(todoItem);
			if(result) {
				System.out.println("更新成功");
			}else{
				redirectAttributes.addFlashAttribute("result", "更新失敗");
				//getErrorPageにリダイレクト
				return "redirect:/getErrorPage";

			}
		}catch(DataAccessResourceFailureException e) {
			model.addAttribute("result", "更新失敗");
		}
		//作業一覧画面表示
		return getTodoList(model);
	}

	/*
	 * 作業未完了のGET用メソッド
	 */
	@GetMapping("/incomplete/{id}")
	public String getInComplete(@ModelAttribute TodoItemForm form, Model model, @PathVariable("id") String itemId, RedirectAttributes redirectAttributes) {

		//ItemId確認(デバッグ)
		System.out.println("itemID = " + itemId);

		//TodoItemインスタンス生成
		TodoItem todoItem = new TodoItem();
		//フォームクラスをTodoItemクラスに変換(form=画面取得用, TodoItem=データ保存用)
		//現在日時を取得
		Date d = new Date();
		todoItem.setId(form.getId());
		todoItem.setFinishedDate(null);
		todoItem.setUpdateDateTime(d);

		try {
			//更新処理
			boolean result = todoItemService.updateFinishedDate(todoItem);
			if(result) {
				System.out.println("更新成功");
			}else{
				redirectAttributes.addFlashAttribute("result", "更新失敗");
				//getErrorPageにリダイレクト
				return "redirect:/getErrorPage";

			}
		}catch(DataAccessResourceFailureException e) {
			model.addAttribute("result", "更新失敗");
		}
		//ユーザ一覧画面表示
		return getTodoList(model);
	}

	//検索処理
	@PostMapping(value="/find")
	public String search(@ModelAttribute TodoItemForm form, HttpServletRequest request,  Model model) {

		//コンテンツ部分に作業一覧を表示するための文字列を登録
		model.addAttribute("contents", "login/searchTodoList :: searchTodoList_contents");
		//ログインユーザ名取得
		Authentication auth = SecurityContextHolder.getContext(
				).getAuthentication();
		int id = Integer.parseInt(auth.getName());
		//ユーザ情報を取得
		Users user = userService.selectOne(id);
		model.addAttribute("loginUser", user);

        String param = request.getParameter("str");

        if(param == null) {
        	return "redirect:/todoList";
        }else {
        	model.addAttribute("value", param);
        	List<TodoItem> list = todoItemService.find(param);

        	model.addAttribute("todoList", list);
        }
        return "login/homeLayout";
    }
}
