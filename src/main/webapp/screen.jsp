<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>CMC&BDX上海年会</title>
	<script src="resources/wechat-showinfo/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="resources/wechat-showinfo/css/web_1.css">
	<script src="resources/js/login.js"></script>
</head>
<body>
	<div class="wrap">
		<div class="header">
			<img src="resources/wechat-showinfo/img/bg.png">
		</div>
		<div class="footer">
			<img src="resources/wechat-showinfo/img/foot.png">
		</div>
		<div class="content">
			<img src="resources/wechat-showinfo/img/w2016.png">
			<p>CMC&BDX上海年会</p>
			<h1>万众一信 · 筑梦起航</h1>
		</div>
	</div>
	<script type="text/javascript">
	
		var storage = getLocalStorage();
		var identity = storage.getItem('identity');
		
		var command = false;
		if(identity === null){
			var identity = prompt('您的身份?');
			if(identity === ''){
				identity = 'guest';
			}else{
				storage.setItem('identity', identity);
			}
			command = true;
		}else{
			command = true;
		}
	
		setInterval("redirect()",1000);
		function redirect(){
			if(command){
				$.get('center/getCommand/' + identity, function(data){
					if(data.type == 'NULL'){
						// 空指令，不做任何事情
					}else if(data.type == 'REDIRECT'){
						window.open(data.url,"_self");
					}
				});
			}
		}
	
	</script>
</body>
</html>
    