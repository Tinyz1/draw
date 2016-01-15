<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>CMC&BDX上海年会</title>
	<script src="resources/wechat-showinfo/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="resources/wechat-showinfo/css/style.css">
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
		setInterval("redirect()",1000);
		function redirect(){
			$.get('center/getCommand', function(data){
				if(data.type == 'NULL'){
					// 空指令，不做任何事情
				}else if(data.type == 'REDIRECT'){
					window.open(data.url,"_self");
				}
			});
		}
	
	</script>
</body>
</html>
    