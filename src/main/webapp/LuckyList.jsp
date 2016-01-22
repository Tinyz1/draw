<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>获奖人员展示</title>
	<script src="resources/wechat-showinfo/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="resources/wechat-showinfo/css/web.css">
	<script src="resources/js/login.js"></script>
</head>
<body>
	<div class="wrap">
	
		<div class="header">
			<img src="resources/image/bg-prize2.jpg">	
			<div class="header-div">
			</div>		
		</div>
		
		<div class="content list" id="hahh">
			<ul class="listUl" id="listUl">
			</ul>
			<!-- <div class="btn-s" id="btn">
				开始抽奖
			</div> -->
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
		
		init();
		
		var linkId ="";
		var storeData = [];
		
		var arrs = [];
		
		var flag = true;
		
		setInterval("redirect()",1000);
		function redirect(){
			if(command){
				$.get('center/getCommand/' + identity, function(data){
					if(data.type == 'NULL'){
						// 空指令，不做任何事情
					}else if(data.type == 'REDIRECT'){
						//跳转到指定页面
						window.open(data.url,"_self");
					}
				});	
			}
			if(flag){
				$.post("link/currentHits", function (data){
					flag = false;
					try{
						for(var i = 0, len = data.length; i < len; i++){
							var name = data[i].participantName;
							var prizeName = data[i].prizeName;
							var prizeType = data[i].prizeType;
							if(!contains(storeData, name)){
								storeData.push(name);
								// <strong style="color: yellow;display: inline-block;text-indent:17px;line-height: 36px;width: 125px;font-size: 25px;">'+prizeName+'</strong>
								// 每一行显示四个字符
								var str = prizeName.split(" ");
								var prize = "";
								if(str.length){
									var len = arrs.length;
									if(len === 2){
										for(var j = 0; j < str.length; j++){
											prize += '<p style="color: yellow;line-height: 45px;text-indent:13px;width: 145px;font-size: 30px;">'+ str[j] +'</p>'
										}
									}else if(len === 3){
										for(var j = 0; j < str.length; j++){
											prize += '<p style="color: yellow;line-height: 35px;text-indent:-5px;width: 125px;font-size: 26px;">'+ str[j] +'</p>'
										}
									}else if(len === 4){
										for(var j = 0; j < str.length; j++){
											prize += '<p style="color: yellow;line-height: 32px;text-indent:-15px;width: 130px;font-size: 25px;">'+ str[j] +'</p>'
										}
									}else if(len === 8){
										for(var j = 0; j < str.length; j++){
											prize += '<p style="color: yellow;line-height: 28px;text-indent:-10px;width: 125px;font-size: 23px;">'+ str[j] +'</p>'
										}
									}else{
										for(var j = 0; j < str.length; j++){
											prize += '<p style="color:yellow;line-height: 23px;text-indent:-15px;width: 115px;font-size: 20px;">'+ str[j] +'</p>'
										}
									}
								}
								$("#"+name).html('<div>'+prize+'</div>');
								$("#"+name).next().next("img.shake").hide();
							}
						}
					}catch(err){
						console.log(err);
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
				arrs = data;
				var html = "";
				if(arrs.length === 2){
					for (var i = 0; i < arrs.length; i++) {
						var arr = arrs[i];
						html += '<li style="width:40%;left:45px"><p id='+arr.participantName+' style="width:320px; height:95px;'
						+'background:url(resources/image/sred.png) no-repeat;background-size:50%">'
						+'</p><p style="color:#000;width:150px; height:30px; text-align:center;letter-space:5px">' + arr.participantName 
						+ '</p><img src="resources/image/sshake.png" class="li-img shake" style="width:110px" /></li>';
					}
					$("#listUl").addClass("martop-3");
				}else if(arrs.length === 3){
					for (var i = 0; i < arrs.length; i++) {
						var arr = arrs[i];
						html += '<li style="width:25%;left:28px"><p id='+arr.participantName+' style="width:260px; height:95px;'
						+'background:url(resources/image/sred.png) no-repeat;background-size:50%">'
						+'</p><p style="color:#000;width:120px; height:30px; text-align:center;letter-space:5px">' + arr.participantName 
						+ '</p><img src="resources/image/sshake.png" class="li-img shake" style="width:87px" /></li>';
					}
					$("#listUl").addClass("martop-3");
				}else if(arrs.length === 4){
					for (var i = 0; i < arrs.length; i++) {
						var arr = arrs[i];
						html += '<li style="width:22%;left:"><p id='+arr.participantName+' style="width:235px; height:80px;'
						+'background:url(resources/image/sred.png) no-repeat;background-size:50%">'
						+'</p><p style="color:#000;width:120px; height:30px; text-align:center;letter-space:5px">' + arr.participantName 
						+ '</p><img src="resources/image/sshake.png" class="li-img shake" style="width:80px" /></li>';
					}
					$("#listUl").addClass("martop-4");
				}else if(arrs.length === 8){
					for (var i = 0; i < arrs.length; i++) {
						var arr = arrs[i];
						html += '<li style="width:22%;margin-bottom:100px;"><p id='+arr.participantName+' style="width:235px; height:60px;'
						+'background:url(resources/image/sred.png) no-repeat;background-size:50%">'
						+'</p><p style="color:#000;width:120px; height:30px">' + arr.participantName 
						+ '</p><img src="resources/image/sshake.png" class="li-img shake" style="width:80px;"></li>';
					}
					$("#listUl").addClass("martop-8");
				}else if(arrs.length === 10){
					for (var i = 0; i < arrs.length; i++) {
						var arr = arrs[i];
						html += '<li style="margin-bottom: 100px;"><p id='+arr.participantName+' style="width:207px; height:50px;'
						+'background:url(resources/image/sred.png) no-repeat;background-size:50%">'
						+'</p><p style="color:#000;width:100px; height:40px">' + arr.participantName 
						+ '</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					}
					$("#listUl").addClass("martop-10");
				}else{
					for (var i = 0; i < arrs.length; i++) {
						var arr = arrs[i];
						html += '<li><p id='+arr.participantName+' style="width:207px; height:50px;'
						+'background:url(resources/image/sred.png) no-repeat;background-size:50%">'
						+'</p><p style="color:#000;width:100px; height:30px;">' + arr.participantName 
						+ '</p><img src="resources/image/sshake.png" class="li-img shake"/></li>';
					}
				}
				$("#listUl").append(html);
			});
		}
	
	</script>
	
</body>

</html>
