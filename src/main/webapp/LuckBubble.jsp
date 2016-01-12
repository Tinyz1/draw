<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>获奖信息展示</title>
	<script src="resources/wechat-showinfo/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="resources/wechat-showinfo/css/style.css">
</head>
<body>
	<div class="wrap">
		<div class="header">
			<img src="resources/wechat-showinfo/img/bg.png">	
			<div class="header-div">
			<h1 class="header-h1"><h1>
			</div>		
		</div>
		<div class="footer">
			<img src="resources/wechat-showinfo/img/foot.png">
		</div>
		<div class="content">
			<p class="subTitle">CMC&BDX上海年会</p>
			<h1 class="title">万众一信 · 筑梦起航</h1>
			<div class="luckyPerson">
				<div >
					<ul id="d1">
					</ul>
				</div>
				<!-- <div class="btn-next">
					下轮抽奖 >
				</div> -->
			</div>			
		</div>
	</div>
	<script type="text/javascript">
		init();
		var linkId = "";
		function init(){
			$.post("link/current",function (data){
				//获取当前环节的环节ID
				linkId = data.linkId;
				linkId = 1;
			});
		}
	</script>
	
	
	
</body>
<script type="text/javascript" src="resources/wechat-showinfo/js/lucky.js"></script>
</html>
