<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <title>관리자 페이지</title>
    <script>
    
    function fn_popup1(){
        
        var url = "naverLogin1.do";
        window.open(url,"adminAccountSubmit","width=400, height=200");
    }
    $(function(){     
    	if (self.name != 'reload') {
	        self.name = 'reload';
	        self.location.reload(true);
	    }
	    else self.name = ''; 
    })
    
    </script>
</head>
<body>
	<a onclick="fn_popup1(); return false;"><img src="/images/href.png"></a>
</body>
</html>
