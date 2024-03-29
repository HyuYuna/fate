<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri = "http://www.springframework.org/tags/form" %> 
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/summernote/summernote-bs4.min.css" />
<script src="${pageContext.request.contextPath}/resources/summernote/summernote-bs4.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/summernote/lang/summernote-ko-KR.min.js"></script>
<section>
<script>
	$(document).ready(function(){
		$('#summernote').summernote({
	        minHeight: 370,
	        maxHeight: null,
	        lang : 'ko-KR',
	        callbacks : {
	        	onImageUpload : function(files, editor, welEditable) {
	                // 파일 업로드(다중업로드를 위해 반복문 사용)
	                for (var i = files.length - 1; i >= 0; i--) {
	            	    uploadSummernoteImageFile(files[i],this);
          			}
	        	}
			}
  		});
		
		$("div.note-editable").on('drop',function(e){
	         for(i=0; i< e.originalEvent.dataTransfer.files.length; i++){
	         	uploadSummernoteImageFile(e.originalEvent.dataTransfer.files[i],$("#summernote")[0]);
	         }
	        e.preventDefault();
	   })
	   
	   
		let mode = '<c:out value="${mode}" />';
		if (mode == 'edit'){
			$("input:hidden[name='customerIdx']").val('<c:out value="${detail.customerIdx}" />');
			$("input:hidden[name='mode']").val('<c:out value="${mode}" />');
			$("#customerName").val('<c:out value="${detail.customerName}"/>');
			$("#phone").val('<c:out value="${detail.phone}"/>');
			$("#address").val('<c:out value="${detail.address}"/>');
			$("#joindate").val('<c:out value="${detail.joindate}"/>');
			$("#grade").val('<c:out value="${detail.grade}"/>');
			$("#city").val('<c:out value="${detail.city}"/>');
			//$("#summernote").val('<c:out value="${detail.content}" escapeXml="false" />');
		}
	});
	
	function uploadSummernoteImageFile(file, editor) {
		data = new FormData();
		data.append("file", file);
		$.ajax({
			data : data,
			type : "POST",
			url : "/uploadSummernoteImageFile.do",
			contentType : false,
			processData : false,
			enctype : 'multipart/form-data',
			success : function(data) {
				$(editor).summernote('insertImage', '/getImage.do?fileNm='+data.url);
			}
		});
	}
	
</script>
<div class="container mt-50" role="main">
	<div align="center">
		<c:if test="${mode ne 'edit' }">
			<font size=5><strong>고객 등록</strong></font>
		</c:if>
		<c:if test="${mode eq 'edit' }">
			<font size=5><strong>고객 정보 수정하기</strong></font>
		</c:if>
	</div> 
	<br><br><br>
	<form name="form" id="form" action="customerSave.do">
		<c:if test="${mode eq 'edit'}">
			<input type="hidden" name="customerIdx" />
		</c:if>
		<input type="hidden" name="mode" />
		<div class="mb-3">
			<label for="customerName">고객이름</label>
			<input name="customerName" id="customerName" class="form-control" placeholder="제목을 입력해 주세요" required />
		</div>
		<div class="mb-3">
			<label for="phone">고객전화</label>
			<input name="phone" id="phone" class="form-control" placeholder="번호을 입력해 주세요" />
		</div>
		<div class="mb-3">
			<label for="address">고객주소</label>
			<input name="address" id="address" class="form-control" placeholder="주소를 입력해 주세요" />
		</div>
		<div class="mb-3">
			<label for="joindate">가입일자</label>
			<input name="joindate" id="joindate" class="form-control" placeholder="날짜를 입력해 주세요" />
		</div>
		<div class="mb-3">
			<label for="grade">고객등급<br>[A:VIP,B:일반,C:직원]</label>
			<input name="grade" id="grade" class="form-control" placeholder="등급을 입력해 주세요" />
		</div>
		<div class="mb-3">
			<label for="city">도시코드</label>
			<input name="city" id="city" class="form-control" placeholder="코드를 입력해 주세요" />
		</div>
		<div class="mb-3">
			<label for="content">내용</label>
			<textarea id="summernote" name="content"><c:out value="${detail.content}" escapeXml="false" /></textarea>
		</div>
		<br/>
		<div>
			<button type="submit" class="btn btn-sm btn-primary" id="btnSave">저장</button>
			<button type="button" class="btn btn-sm btn-primary" onClick="location.href='customerList.do'" id="btnList">목록</button>
			<button type="reset" class="btn btn-sm btn-primary" value="다시 입력">초기화</button>
		</div>
	</form>
</div>
</section>
