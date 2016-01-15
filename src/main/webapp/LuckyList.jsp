<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>获奖人员展示</title>
	<script src="resources/wechat-showinfo/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="resources/wechat-showinfo/css/web.css">
</head>
<body>
	<div class="wrap">
	
		<div class="header">
			<img src="resources/wechat-showinfo/img/bg.png">	
			<div class="header-div">
				<h1 class="header-h1"></h1>
			</div>		
		</div>
		
		<div class="footer">
			<img src="resources/wechat-showinfo/img/foot.png">
		</div>
		
		<div class="content list">
			<ul class="listUl" id="listUl">
			</ul>
			<!-- <div class="btn-s" id="btn">
				开始抽奖
			</div> -->
		</div>
		
		<p class="footP">CMC&BDX上海年会</p>
		<h1 class="footH">万众一信 · 筑梦起航</h1>
		
	</div>
	<script type="text/javascript">
		init();
		setInterval("redirect()",1000);
		function redirect(){
			$.get('center/getCommand', function(data){
				if(data.type == 'NULL'){
					// 空指令，不做任何事情
				}else if(data.type == 'REDIRECT'){
					//跳转到指定页面
					window.open(data.url,"_self");
				}
			});		
		}
		
		function init(){
			$.post("link/current",function (data){
				// 后去当前环节数
				var link = data.linkName;
				$(".header-h1").html(link + "参与人名单");
			});
			$.post("participant/current/pickParticipants",function (data){
				// 获取用户名称数组
				var arrs = data;
				// $("#listUl").append('<li><span style="color:red;">'+
				// arrs[0]+'</span></li>');
				var html = "";
				// 遍历用户数组数据
				for (var i = 0; i < arrs.length; i++) {
					var arr = arrs[i];
					html += '<li><span style="color:red;">' + arr.participantName + '</span></li>';
				}

				$("#listUl").append(html);
			});
		}
	
	</script>
	
</body>

</html>
