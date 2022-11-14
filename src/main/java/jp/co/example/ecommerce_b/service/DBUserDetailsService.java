package jp.co.example.ecommerce_b.service;

import java.util.ArrayList;
import java.util.Collection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.LoginUser;
import jp.co.example.ecommerce_b.domain.Users;
import jp.co.example.ecommerce_b.repository.UserRepository;

/**
 * ユーザー権限情報の操作処理の設定
 * 
 * @author ishida fuya
 *
 */

@Service
public class DBUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userrepository;
	
	@Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
		Users user = userrepository.searchPassword(email);
		
		if(user == null) {
			throw new UsernameNotFoundException("そのEmailは登録されていません。");
		}
		
	     // 権限付与
			Collection<GrantedAuthority> authorityList = new ArrayList<>();
			authorityList.add(new SimpleGrantedAuthority("ROLE_USER")); // ユーザ権限付与
//			if(member.isAdmin()) {
//				authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // 管理者権限付与
//			}
			return new LoginUser(user,authorityList);
		}
    }
		
        
    

   


