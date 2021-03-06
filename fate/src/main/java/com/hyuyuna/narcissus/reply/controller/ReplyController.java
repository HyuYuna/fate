package com.hyuyuna.narcissus.reply.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyuyuna.narcissus.reply.service.ReplyService;
import com.hyuyuna.narcissus.reply.vo.ReplyVO;

@Controller
public class ReplyController {
	
	@Resource(name="replyService")
	ReplyService service;
	
	// 댓글 목록
	@RequestMapping(value="/getReplyList.do", method=RequestMethod.POST)
	@ResponseBody
	public List<ReplyVO> getReplyList(@RequestParam("boardIdx") int boardIdx) throws Exception {
		List<ReplyVO> replyList = service.selectReplyList(boardIdx);
		
		return replyList;
	} 
	
	// 댓글 저장
	@RequestMapping(value="/insertReply.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> insertReply(@RequestBody ReplyVO replyVO) throws Exception {
		Map<String, Object> result = new HashMap();
		
		try {
			service.insertReply(replyVO);
			result.put("status", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "False");
		}
		
		return result;
	}
	
	// 댓글 수정
	@RequestMapping(value="/updateReply.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateReply(@RequestBody ReplyVO replyVO) throws Exception {
		Map<String, Object> result = new HashMap();
		try {
			service.updateReply(replyVO);
			result.put("status", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "False");
		}
		
		return result;
	
	}
	
	// 댓글 삭제
	@RequestMapping(value="/deleteReply.do", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteReply(@RequestParam("replyIdx") int replyIdx) throws Exception {
		Map<String, Object> result = new HashMap();
		try {
			service.deleteReply(replyIdx);
			result.put("status", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "False");
		}
		
		return result;
	}
	
}