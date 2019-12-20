package com.example.demo.login.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	@Around("execution(* *..*.*Controller.*(..))")
	public Object startLog(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("メソッド開始: " + jp.getSignature());

		try {
			Object result = jp.proceed();
			System.out.println("メソッド終了: " + jp.getSignature());
			return result;
		}catch(Exception e){
			System.out.println("メソッド以上終了: " + jp.getSignature());
			e.printStackTrace();
			throw e;
		}
	}

	//TodoItemDaoクラスのログ出力
	@Around("execution(* *..*.*TodoItemDao.*(..))")
	public Object todoItemDaoLog(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("メソッド開始: " + jp.getSignature());

		try {
			Object result = jp.proceed();
			System.out.println("メソッド終了: " + jp.getSignature());
			return result;
		}catch(Exception e){
			System.out.println("メソッド以上終了: " + jp.getSignature());
			e.printStackTrace();
			throw e;
		}
	}

	//TodoItemDaoクラスのログ出力
	@Around("execution(* *..*.*UsersDao.*(..))")
	public Object usersDaoLog(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("メソッド開始: " + jp.getSignature());

		try {
			Object result = jp.proceed();
			System.out.println("メソッド終了: " + jp.getSignature());
			return result;
		}catch(Exception e){
			System.out.println("メソッド以上終了: " + jp.getSignature());
			e.printStackTrace();
			throw e;
		}
	}

}
