package jp.co.example.ecommerce_b.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    UserDetailsService userDetailsService;
	
	
	//セキュリティ対象外を指定
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers( "/css/**"
						, "/img_curry/**"
						, "/js/**"
						, "/fonts/**");
		
	}
	
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()// 認可に関する設定
		
		//ログイン前に閲覧できないページを指定する
	    .antMatchers("/order/confirm", "/order/history").authenticated()
	    // それ以外のパスは認証不要
		.anyRequest().permitAll();

		
	http.formLogin() // ログインに関する設定
		.loginPage("/user/") // ログイン画面に遷移させるパス(ログイン認証が必要なパスを指定してかつログインされていないとこのパスに遷移される)
		.loginProcessingUrl("/user/login") // ログインボタンを押した際に遷移させるパス(ここに遷移させれば自動的にログインが行われる)
        .usernameParameter("email") // 認証時に使用するユーザ名のリクエストパラメータ名(今回はメールアドレスを使用)
	    .passwordParameter("password")// 認証時に使用するパスワードのリクエストパラメータ名
	    .defaultSuccessUrl("/itemList");//ログイン成功時の遷移先
	
	http.logout() // ログアウトに関する設定
	    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // ログアウトさせる際に遷移させるパス
		.logoutSuccessUrl("/itemList") // ログアウト後に遷移させるパス(ここではログイン画面を設定)
		.deleteCookies("JSESSIONID") // ログアウト後、Cookieに保存されているセッションIDを削除
		.invalidateHttpSession(true);
	
        
        
    }
	
	
	
	
	/**
     * bcryptアルゴリズムでハッシュ化する実装を返します.
     * @return bcryptアルゴリズムでハッシュ化する実装オブジェクト
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
    		return new BCryptPasswordEncoder();
    }
    
    /**
	 * 「認証」に関する設定.<br>
	 * 認証ユーザを取得する「UserDetailsService」の設定や<br>
	 * パスワード照合時に使う「PasswordEncoder」の設定
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		
	}
    

}
