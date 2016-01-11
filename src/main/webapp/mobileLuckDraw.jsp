<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no" />
	<title>lucky</title>
	<script src="resources/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="resources/css/style.css">
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
				<input type="text" class="username" placeholder="请输入姓名">
				<button onclick="login()">确定</button>
				<div class="tips">
					小猴温馨提示 :
					<p>为了您能顺利临到奖品，请输入正确的姓名和编号</p>
				</div>
			</div>
			<p>CMC&BDX上海年会</p>
		</div>
	</div>
</body>
<script src="resources/js/login.js"></script>
</html>
