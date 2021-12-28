package com.hyuyuna.narcissus.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hyuyuna.narcissus.common.FileVO;
import com.hyuyuna.narcissus.common.MemberVO;
import com.hyuyuna.narcissus.common.MoneyVO;

@Repository("memberDao")
public class MemberDao extends AbstractDao {
	
	String flag;

	public void insertMember(MemberVO vo) {
		insert("memberDao.insertMember" , vo);
	}

	@SuppressWarnings("unchecked")
	public List<MemberVO> selectAllmember(MemberVO vo) {
		flag = vo.getFlag();
		if (flag == "f") {
			return (List<MemberVO>)selectList("memberDao.selectAllFilemember", vo);
		} else {
			return (List<MemberVO>)selectList("memberDao.selectAllmember", vo);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectAllMemberJson() {
		return (List<Map<String, Object>>)selectList("memberDao.selectAllMemberJson");
	}
	
	public int memberCnt(MemberVO vo) {
		if (flag == "f") {
			return (Integer)selectOne("memberDao.memberFileCnt");
		} else {
			return (Integer)selectOne("memberDao.memberCnt");
		}
	}
	
	public void deleteMember(int custno) {
		delete("memberDao.deleteMember", custno);
	}

	public void updateMember(MemberVO vo) {
		int k = (Integer)update("memberDao.updateMember", vo);
		System.out.println("으으윽"+ k);
	}
	
	public void editUser(MemberVO vo) {
		update("memberDao.editUser", vo);
	}

	public MemberVO selectMember(int custno) {
		return (MemberVO)selectOne("memberDao.selectMember", custno);
	}
	
	public MemberVO selectFileMember(int custno) {
		return (MemberVO)selectOne("memberDao.selectFileMember", custno);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectFileList(int custno) {
		return (List<Map<String, Object>>)selectList("memberDao.selectFileList", custno);
	}
	
	public FileVO selectFileInfo(int num) {
		return (FileVO)selectOne("memberDao.selectFileInfo", num);
	}

	@SuppressWarnings("unchecked")
	public List<MoneyVO> moneylist() {
		return (List<MoneyVO>)selectList("memberDao.moneylist");
	}

	public void insertFileMember(MemberVO vo) {
		insert("memberDao.insertFileMember", vo);
	}
	
	public void updateFileMember(MemberVO vo) {
		insert("memberDao.updateFileMember", vo);
	}

	public void deleteFileMember(int custno) {
		delete("memberDao.deleteFileMember", custno);
	}
	
	public void deleteFile(int custno) {
		delete("memberDao.deleteFile", custno);
	}
	
	public void insertFile(Map<String,Object> map) {
		insert("memberDao.insertFile", map);
	}
	 
}