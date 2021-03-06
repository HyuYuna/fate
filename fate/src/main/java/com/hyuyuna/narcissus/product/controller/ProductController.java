package com.hyuyuna.narcissus.product.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hyuyuna.narcissus.common.SessionManager;
import com.hyuyuna.narcissus.common.vo.FileVO;
import com.hyuyuna.narcissus.product.service.ProductService;
import com.hyuyuna.narcissus.product.vo.ProductVO;
import com.hyuyuna.narcissus.reply.vo.ReplyVO;

@Controller
public class ProductController {
	
	@Resource(name="productService")
	ProductService service;
	
	@Value("#{directory['globals.filesDir']}")
	private String filePath;

	
	// 제품 등록 화면
	@RequestMapping(value="/productReg.do")
	public String productReg(Model model) throws Exception {
		return "product/product_reg.main";
	}
	
	// 제품 목록
	@RequestMapping(value = "/productList.do")
	public String productList(ProductVO vo, Model model, HttpServletRequest request,
			@RequestParam(required=false, defaultValue = "1") int range) throws Exception {
		
		int page = request.getParameter("page") == null ? 1 : Integer.parseInt("page");
		int listCnt = service.productCnt(vo);
		
		vo.setPage(page);
		vo.setRange(range);
		vo.setListCnt(listCnt);
		vo.pageInfo(page, range, listCnt);
		
		List<ProductVO> list = service.selectProductList(vo);
		
		model.addAttribute("vo", vo);
		model.addAttribute("cnt", listCnt);
		model.addAttribute("list", list);
		
		return "product/product_list.main";
	}
	
	// 제품 정보화면
	@RequestMapping(value="/productView.do")
	public String productView(ProductVO vo, Model model) throws Exception {
		
		ProductVO detail = service.selectProduct(vo.getProductIdx());
		
		// 제품 첨부파일 목록
		List<Map<String,Object>> map = service.selectFileList(vo.getProductIdx());
		
		model.addAttribute("detail", detail);
		model.addAttribute("map", map);
		model.addAttribute("replyVO", new ReplyVO());
		
		return "product/product_view.main";
	}
		
	// 제품 상세
	@RequestMapping(value="/productDtl.do")
	public String productDtl(ProductVO vo, Model model) throws Exception {

		ProductVO detail = service.selectProduct(vo.getProductIdx());
		
		// 제품 첨부파일 목록
		List<Map<String,Object>> map = service.selectFileList(vo.getProductIdx());
		
		model.addAttribute("detail", detail);
		model.addAttribute("map", map);
		
		return "product/product_dtl.main";
	}
	
	// 제품 등록
	@RequestMapping(value="/productInsert.do")
	public String productInsert(@RequestParam Map<String,Object> map, HttpServletRequest request) throws Exception  {
		
		service.insertProduct(map, request);
		
		return "redirect:productList.do";
	}
	
	// 제품 수정
	@RequestMapping(value="/productUpdate.do")
	public String productUpdate(@RequestParam Map<String,Object> map, HttpServletRequest request) throws Exception  {
		
		service.updateProduct(map, request);
		
		return "redirect:productList.do";
	}
	
	// 제품 삭제
	@RequestMapping(value="/deleteProduct.do")
	public String deleteProduct(ProductVO vo, HttpServletRequest request) {
			
		int productIdx = vo.getProductIdx();
		String realPath = request.getSession().getServletContext().getRealPath("/upfile/");
		
			String fname = request.getParameter("fname");
			File file = new File(realPath+ "/"+ fname);
			file.delete();
			System.out.println(realPath+fname);
		
		service.deleteProduct(productIdx);
		
		return "redirect:productList.do";
	}
	
	// 파일 삭제
	@RequestMapping(value="deleteFile.do")
	public void deleteFile(HttpServletRequest request,
				@RequestParam("filename") String filename) {
		String realPath = request.getSession().getServletContext().getRealPath("/upfile/");
		File file = new File(realPath+ "/" + filename);
		file.delete();
	}
	
	// 파일 다운로드
	@RequestMapping(value="/downloadFile.do")
	public void downloadFile(HttpServletResponse response, @RequestParam("fileIdx") int fileIdx ) throws Exception {
		
		// 제품 첨부파일 정보
		FileVO file = service.selectFileInfo(fileIdx);
		
		String storedFileName = file.getStoredFileName();
		String originalFileName  = file.getOriginalFileName();
		
		byte fileByte[] = FileUtils.readFileToByteArray(new File(filePath + "/" +storedFileName));
		
		response.setContentType("application/octet-stream"); 
		response.setContentLength(fileByte.length); 
		
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName,"UTF-8")+"\";"); 
		response.setHeader("Content-Transfer-Encoding", "binary"); 
		
		response.getOutputStream().write(fileByte); 
		response.getOutputStream().flush(); 
		response.getOutputStream().close(); 
		
	}
	
	
}
