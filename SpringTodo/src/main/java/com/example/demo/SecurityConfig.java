package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity//セキュリティ設定用クラス
@Configuration//セキュリティ用Beanクラス
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {//PasswordEncoderはパスワードを暗号化したり複合するインターフェース
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private DataSource dataSource;//SQL実行のため

	//ユーザIDとパスワードを取得するSQL ユーザID,パスワード、使用可否(全部true)
	private static final String USER_SQL = "SELECT id, pass, true FROM users WHERE id=?";

	//ユーザのロールを取得するSQL
	private static final String ROLE_SQL = "SELECT id, role FROM users WHERE id=?";

	public void configure(WebSecurity web) throws Exception {
		//静的リソースへのACCESSにはセキュリティを適用しない
		web.ignoring().antMatchers("/webjars/**", "/css/**");
	}

	protected void configure(HttpSecurity http) throws Exception {
		//直リンク禁止(ログイン不要ページの設定)　※上から順番に設定されるのでanyRequest().authenticated()を最初にしないこと
		http.authorizeRequests()
		.antMatchers("/webjars/**").permitAll()//webjarsへアクセス許可(アスタリスク2つ=いずれかのファイル)
		.antMatchers("/css/**").permitAll()//cssアクセス許可
		.antMatchers("/login").permitAll()//ログインページへは直リンクOK
		.anyRequest().authenticated();//それ以外は直リンク禁止

		//ログイン処理
		http.formLogin()
		.loginProcessingUrl("/login")//ログイン処理のパス(formタグのaction="/login"一致させる)
		.loginPage("/login")//ログインページの指定(これを指定しないとSpringのデフォルトログイン画面になる。コントローラークラスの@GetMapping("/login")に一致させる)
		.failureUrl("/login")//ログイン失敗時の遷移
		.usernameParameter("id")//ログインページのユーザID(ログイン画面のname="*{userId}"に一致させる)
		.passwordParameter("pass")//ログインページのパスワード
		.defaultSuccessUrl("/todoList", true);//ログイン成功後の遷移先

		//ログアウト処理(ログアウトボタンを押すと、ユーザセッションが破棄される)
		http.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//GETメソッドで送るため(デフォルトではPOSTメソッド)
		.logoutUrl("/logout")
		.logoutSuccessUrl("/login");

		//CSRFを無効にするURLを設定
		//RequestMatcher csrfMatcher = new RestMatcher("/rest/**");

		//RESTのみCSRF対策を無効に設定
		//http.csrf().requireCsrfProtectionMatcher(csrfMatcher);

		http.csrf().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		//ログイン処理時のユーザ情報をDBから取得する
		auth.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery(USER_SQL)
		.authoritiesByUsernameQuery(ROLE_SQL)
		.passwordEncoder(passwordEncoder());
	}
}
