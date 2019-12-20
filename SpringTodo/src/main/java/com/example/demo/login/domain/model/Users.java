package com.example.demo.login.domain.model;

import java.util.Date;

import lombok.Data;

@Data
public class Users {

	private int userId;

	private String user;

	private String pass;

	private String familyName;

	private String firstName;

	private String role; //権限

	private int isDeleted;

	private Date createDateTime;

	private Date updateDateTime;
}
