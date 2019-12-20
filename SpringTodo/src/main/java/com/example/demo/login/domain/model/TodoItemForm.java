package com.example.demo.login.domain.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TodoItemForm {

	private int id;

	private int userId;

	private String userName;

	@NotBlank(groups=ValidGroup1.class)
	@Length(min = 1, max = 100, groups=ValidGroup2.class)
	private String itemName;

	@DateTimeFormat(pattern="yyyy/MM/dd")
	private Date registrationDate;

	@NotNull(groups=ValidGroup1.class)
	@DateTimeFormat(pattern="yyyy/MM/dd")
	private Date expireDate;

	@DateTimeFormat(pattern="yyyy/MM/dd")
	private Date finishedDate;

	private int isDelete;
	private Date createDateTime;
	private Date updateDateTime;

	@Pattern(regexp="checked")
	private Boolean finishedCheck;

	private int selectUser;
}
