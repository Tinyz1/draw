<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no" />
	<title>摇一摇抽奖</title>
	<script src="resources/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="resources/css/style.css">
	<script src="resources/js/login.js"></script>
	<script type="text/javascript">
	// 从本地缓存中获取用户编号（已成功登陆的用户才会保存用户编号）
	var storage = getLocalStorage();
	var participantName = storage.getItem('participantName');
	if(participantName == null){
		window.open("mobileLuckDraw.jsp","_self");
	}
	</script>
</head>
<body>
	<div class="wrap">
		<div class="header">
			<img src="resources/image/sheader.jpg">
		</div>
		<div class="footer">
			<img src="resources/image/sfooter.png">
		</div>
		<div class="content">
			<img src="resources/image/s2016.png">
			<div class="form">
				<input type="text" id="linkNum" class="linkNum" placeholder="请输入该环节验证码">
				<button id="loginBtn">确定</button>
				<div class="tips">
					小猴温馨提示 :
					<p>为了您能顺利领到奖品，请输入正确的验证码</p>
					<p>主持人会告诉你验证码哦！！！</p>
				</div>
			</div>
			<p>CMC&BDX上海年会</p>
		</div>
	</div>
	
	<script type="text/javascript">
		(function($){
			$('#loginBtn').click(function(){
				var enterNumber = $("#linkNum").val();
				if(!enterNumber.length){
					alert('请输入验证码！');
					return;
				}
				$.post("link/authLinkNumber", {enterNumber: enterNumber},function(data){
					if(data.returnCode === 1){
						storage.setItem('enterNumber', enterNumber);
						window.open("shake.jsp","_self");
					}else{
						alert("请输入正确的环节验证码!");
					}
				});
			});
		})(jQuery);
	</script>
</body>
</html>
