package kr.co.paperwaffle.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.paperwaffle.vo.MemberVO;

@Mapper
@Repository
public interface MemberDAO {
	
	public MemberVO selectByMb_id(String mb_id);

}
