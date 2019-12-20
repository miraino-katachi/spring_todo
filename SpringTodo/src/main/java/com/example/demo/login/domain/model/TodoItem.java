package com.example.demo.login.domain.model;

import java.util.Date;

import lombok.Data;

@Data
public class TodoItem {

	private int id;

	private int userId;

	private String userName;

	private String itemName;

	private Date registrationDate;

	private Date expireDate;

	private Date finishedDate;

	private int isDelete;

	private Date createDateTime;

	private Date updateDateTime;
}
