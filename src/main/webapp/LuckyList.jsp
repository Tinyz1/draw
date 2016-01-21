<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
			</div>		
		</div>
		<div class="footer">
			<img src="resources/wechat-showinfo/img/foot.png">
		</div>
		
		<div class="content list" id="hahh">
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
		
		var linkId ="";
		var storeData = [];
		var flag = true;
		
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
			if(flag){
				$.post("link/currentHits", function (data){
					flag = false;
					for(var i = 0, len = data.length; i < len; i++){
						var name = data[i].participantName;
						var prizeName = data[i].prizeName;
						var prizeType = data[i].prizeType;
						if(!contains(storeData, name)){
							storeData.push(name);
							$("#"+name).html('<div><strong style="color: yellow;display: inline-block;text-align: center;line-height: 36px;width: 125px;font-size: 25px;">'+prizeName+'</strong></div>');
							$("#"+name).next("img.shake").hide();
						}
					}
					flag = true;
				});
			}
		}
		
		function contains(arr, item){
			if(arr.length){
				for(var i = 0, len = arr.length; i < len; i++){
					if(arr[i] == item){
						return true;
					}
				}
			}
			return false;
		}
		function init(){
			$.post("link/current",function (data){
				// 后去当前环节数
				linkId = data.linkId;
				var link = data.linkName;
				$(".header-h1").html(link + "参与人名单");
			});
			$.post("participant/current/pickParticipants",function (data){
				// 获取用户名称数组
				var arrs = data;
				var html = "";
				if(arrs.length === 2){
					var arr = arrs[0];
					html += '<li style="width:100%;margin:90px 42%; 80px 0;"><p style="color:#000;width:150px; height:30px">' + arr.participantName 
					+ '</p><p id='+arr.participantName+' style="width:150px; height:75px;'
					+'background:url(resources/image/sred.png) no-repeat;background-size:cover">'
					+'</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					
					var arr = arrs[1];
					html += '<li style="width:100%;margin:90px 42%; 80px 0;"><p style="color:#000;width:150px; height:30px">' + arr.participantName 
					+ '</p><p id='+arr.participantName+' style="width:150px; height:75px;'
					+'background:url(resources/image/sred.png) no-repeat;background-size:cover">'
					+'</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					
				}else if(arrs.length === 3){
					
					var arr = arrs[0];
					html += '<li style="width:100%;margin:90px 42%; 90px 0;"><p style="color:#000;width:150px; height:30px">' + arr.participantName 
					+ '</p><p id='+arr.participantName+' style="width:150px; height:75px;'
					+'background:url(resources/image/sred.png) no-repeat;background-size:cover">'
					+'</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					
					var arr = arrs[1];
					html += '<li style=";margin:50px 100px 80px 26%;"><p style="color:#000;width:150px; height:30px">' + arr.participantName 
					+ '</p><p id='+arr.participantName+' style="width:150px; height:75px;'
					+'background:url(resources/image/sred.png) no-repeat;background-size:cover">'
					+'</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					
					var arr = arrs[2];
					html += '<li style="margin:50px 10% 80px 0;"><p style="color:#000;width:150px; height:30px">' + arr.participantName 
					+ '</p><p id='+arr.participantName+' style="width:150px; height:75px;'
					+'background:url(resources/image/sred.png) no-repeat;background-size:cover">'
					+'</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					
				}else if(arrs.length === 10){
					for (var i = 0; i < arrs.length; i++) {
						var arr = arrs[i];
						html += '<li style="margin:90px 0 80px 0;"><p style="color:#000;width:150px; height:30px">' + arr.participantName 
						+ '</p><p id='+arr.participantName+' style="width:150px; height:75px;'
						+'background:url(resources/image/sred.png) no-repeat;background-size:cover">'
						+'</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					}
				}else{
					for (var i = 0; i < arrs.length; i++) {
						var arr = arrs[i];
						html += '<li style="margin:10px 0 80px 0;"><p style="color:#000;width:150px; height:30px">' + arr.participantName 
						+ '</p><p id='+arr.participantName+' style="width:150px; height:75px;'
						+'background:url(resources/image/sred.png) no-repeat;background-size:cover">'
						+'</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					}
				}
				$("#listUl").append(html);
			});
		}
	
	</script>
	
</body>

</html>
