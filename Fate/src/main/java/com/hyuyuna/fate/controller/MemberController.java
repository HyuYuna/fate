package com.hyuyuna.fate.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyuyuna.narcissus.common.FileVO;
import com.hyuyuna.narcissus.common.MemberVO;
import com.hyuyuna.narcissus.common.MoneyVO;
import com.hyuyuna.narcissus.service.MemberService;

@Controller
public class MemberController {
	
	@Resource(name="memberService")
	MemberService service;
	
	@Autowired
	private SessionManager sessionManager;
	
	@RequestMapping(value="/form.do")
	public String form(Model model) throws Exception {
		return "form";
	}
	
	@RequestMapping(value="/fileForm.do")
	public String fileForm(Model model) throws Exception {
		return "fileForm";
	}
	
	@RequestMapping(value="/example.do")
	public String example(Model model, HttpServletRequest request) throws Exception {
		
		MemberVO member = (MemberVO)sessionManager.getSession(request);
		
		return "example";
	}
	
	@RequestMapping(value="/grid.do")
	public String grid(Model model) throws Exception {
		return "grid";
	}
	
	@RequestMapping(value="/save.do", method= {RequestMethod.GET, RequestMethod.POST})
	public String insert(MemberVO vo,
				@RequestParam("mode") String mode) throws Exception {

		if(mode.equals("edit")) {
			service.updateMember(vo);
		} else {
			service.insertMember(vo);
		}
		
		return "redirect:memberList.do";
	}
	
	@RequestMapping(value="/memberList.do")
	public String list(MemberVO vo, Model model) throws Exception{
		List<MemberVO> list = service.selectAllMember(vo);
		int cnt = service.memberCnt(vo);
		
		model.addAttribute("cnt", cnt);
		model.addAttribute("list", list);
		return "member_list";
	}
	
	/*@RequestMapping(value="/memberListJson.do")
	public ModelAndView memberListJson(HttpSession session,
					@ModelAttribute("MemberVO") MemberVO vo){
		
		ModelAndView mnv = new ModelAndView("jsonView");
		
		List<MemberVO> memberList = service.selectAllMember(vo);
		//int cnt = service.memberCnt(vo);
		
		mnv.addObject("list",memberList);
		
		return mnv;
	} */

	@RequestMapping(value="/memberListJson.do", method=RequestMethod.POST)
	@ResponseBody
	public MemberVO memberListJson(
						@RequestParam(value="page", required=false) String page,
						@RequestParam(value="rows", required=false) String rows,
						@RequestParam(value="sidx", required=false) String sidx,
						@RequestParam(value="sord", required=false) String sord) {
		
		List<Map<String,Object>> memberList = service.selectAllMemberJson();
		MemberVO obj = new MemberVO();
		obj.setRows(memberList);

		return obj;
	} 

	@RequestMapping(value = "/memberFileList.do")
	public String fileList(MemberVO vo, Model model) throws Exception {
		vo.setFlag("f");
		List<MemberVO> list = service.selectAllMember(vo);
		int cnt = service.memberCnt(vo);
		
		model.addAttribute("cnt", cnt);
		model.addAttribute("list", list);
		
		return "member_file_list";
	}
	
	@RequestMapping(value="/delete.do")
	public String delete(MemberVO vo) throws Exception {
		service.deleteMember(vo.getCustno());
		return "redirect:memberList.do";
	}
	
	@RequestMapping(value="deleteFile.do")
	public void deleteFile(HttpServletRequest request,
				@RequestParam("filename") String filename) {
		String realPath = request.getSession().getServletContext().getRealPath("/upfile/");
		File file = new File(realPath+ "/" + filename);
		file.delete();
	}
	
	@RequestMapping(value="/deleteFileMember.do")
	public String deleteFile(MemberVO vo, HttpServletRequest request) {
			
		int custno = vo.getCustno();
		String realPath = request.getSession().getServletContext().getRealPath("/upfile/");
		
		
			String fname = request.getParameter("fname");
			File file = new File(realPath+ "/"+ fname);
			file.delete();
			System.out.println(realPath+fname);
		
		service.deleteFileMember(custno);
		
		return "redirect:memberFileList.do";

	}
	
	@RequestMapping(value="/memberView.do")
	public String memberView(MemberVO vo, Model model) throws Exception {
		
		MemberVO detail = service.selectMember(vo.getCustno());
		
		model.addAttribute("detail", detail);
		
		return "member_view";
	}
	
	@RequestMapping(value="/memberFileView.do")
	public String memberFileView(MemberVO vo, Model model) throws Exception {
		
		MemberVO detail = service.selectFileMember(vo.getCustno());
		
		List<Map<String,Object>> map = service.selectFileList(vo.getCustno());
		
		model.addAttribute("detail", detail);
		model.addAttribute("map", map);
		
		return "member_file_view";
	}
		
	
	@RequestMapping(value="/memberDtl.do")
	public String detail(MemberVO vo, Model model, @RequestParam("mode") String mode) throws Exception {

		MemberVO detail = service.selectMember(vo.getCustno());
		
		List<Map<String,Object>> map = service.selectFileList(vo.getCustno());
		
		model.addAttribute("detail", detail);
		model.addAttribute("map", map);
		model.addAttribute("memberVO", new MemberVO());
		model.addAttribute("mode",mode);

		
		return "member_dtl";
	}
	
	@RequestMapping(value="/memberFileDtl.do")
	public String Filedetail(MemberVO vo, Model model, @RequestParam("mode") String mode) throws Exception {

		MemberVO detail = service.selectFileMember(vo.getCustno());
		model.addAttribute("detail", detail);
		model.addAttribute("memberVO", new MemberVO());
		model.addAttribute("mode",mode);
		
		
		return "member_file_dtl";
	}
	
	@RequestMapping(value="/editUser.do")
	public String editUser(MemberVO vo, Model model) throws Exception {

		try {
			String shapass = SHA256.getSHA256(vo.getPwd());
			vo.setPwd1(shapass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		service.editUser(vo);
		
		return "redirect:memberList.do";
	}
	
	@RequestMapping(value="/moneyList.do")
	public String moneyList(MoneyVO vo , Model model)  throws Exception {
		
		List<MoneyVO> list = service.moneylist();
		model.addAttribute("moneylist",list);
		
		return "money_list";
	}
	
	@RequestMapping(value="/fileInsert.do")
	public String fileInsert(MemberVO vo, HttpServletRequest request) throws Exception  {
		
		String mode = request.getParameter("mode") == null ? "" : request.getParameter("mode");
		
		
		if (mode == "edit") {
			service.updateFileMember(vo);
		} else {
			service.insertFileMember(vo, request);
		}
		return "redirect:memberFileList.do";
	}
	
	@RequestMapping(value="/downloadFile.do")
	public void downloadFile(HttpServletResponse response, @RequestParam("num") int num ) throws Exception {
		
		FileVO file = service.selectFileInfo(num);
		String storedFileName = file.getStoredFileName();
		String originalFileName  = file.getOriginalFileName();
		
		byte fileByte[] = FileUtils.readFileToByteArray(new File("C:\\Fate\\sts-3.9.15.RELEASE\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\Fate\\upfile\\"+storedFileName));
		
		response.setContentType("application/octet-stream"); 
		response.setContentLength(fileByte.length); 
		
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName,"UTF-8")+"\";"); 
		response.setHeader("Content-Transfer-Encoding", "binary"); 
		
		response.getOutputStream().write(fileByte); 
		response.getOutputStream().flush(); 
		response.getOutputStream().close(); 
		
	}


}