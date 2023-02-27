package kr.co.paperwaffle.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.paperwaffle.dao.MemberDAO;
import kr.co.paperwaffle.vo.MemberVO;


@Service
public class SecurityUserService implements UserDetailsService {

	@Autowired
	private MemberDAO dao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 해당 사용자 있는지 확인
		MemberVO member = dao.selectByMb_id(username);
		
		if(member == null) {
			throw new UsernameNotFoundException(username); // 예외처리(해당 사용자 없음)
		}
		
		UserDetails userDts = MyUserDetails.builder()
								.member(member)
								.build();

		return userDts;
	}

}
