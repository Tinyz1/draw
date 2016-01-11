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
	<audio src="resources/music/5018.mp3" class="audio" loop></audio> 
	<div class="wrap">
		<div class="header">
			<img src="resources/image/sheader.jpg">
			<img class="sshake" src="resources/image/sshake.png">
		</div>
		<div class="footer">
			<img src="resources/image/sfooter.png">
		</div>
		<div class="content">
			<div class="form form1">
				<div class="tips tips1 tip">
					<p>摇手机抽奖啦</p>
				</div>
				<div class="tips tips2 hidden tip">
					<p class="ptip">很遗憾您没有中奖哦，</p>
					<p class="pptip">下一轮再接再厉吧。</p>
				</div>
				<div class="tips3 hidden tip">
					<div class="redPackage">
						<p class="prize">一等奖</p>
						<p class="money">￥5000</p>
					</div>
				</div>
			</div>
			<p>CMC&BDX上海年会</p>
		</div>
		<button class="btns" onclick="shake()">
			摇一摇
		</button>
	</div>
</body>
<script src="resources/js/login.js"></script>
</html>
