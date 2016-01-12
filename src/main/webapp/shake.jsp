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
	if(participantName === null){
		window.open("mobileLuckDraw.jsp","_self");
	}
	</script>
</head>
<body>
	<audio src="resources/music/5018.mp3" class="audio" loop></audio>
	<audio src="resources/music/5012.mp3" class="audio" ></audio>
	<div class="nav">
		<span class="username exit"></span>
	</div>
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
				<!-- <button id="shakeBtn">摇一摇</button> -->
			</div>
			<p>CMC&BDX上海年会</p>
		</div>
	</div>
	
	<script type="text/javascript">
		(function($){
			
			$(".username").html(participantName);
			
			var SHAKE_THRESHOLD = 3000;
	 	    var last_update = 0;
	 	    var x = y = z = last_x = last_y = last_z = 0;

	 	    if (window.DeviceMotionEvent) {
	 	        window.addEventListener('devicemotion', deviceMotionHandler, false);
	 	    } else {
	 	        alert('本设备不支持devicemotion事件');
	 	    }

	 	    function deviceMotionHandler(e) {
	 	        var acceleration = e.accelerationIncludingGravity;
	 	        var curTime = new Date().getTime();

	 	        if ((curTime - last_update) > 100) {
	 	            var diffTime = curTime - last_update;
	 	            last_update = curTime;
	 	            x = acceleration.x;
	 	            y = acceleration.y;
	 	            z = acceleration.z;
	 	            
	 	            var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

	 				if (speed > SHAKE_THRESHOLD) {
	 					SHAKE_THRESHOLD = 200000;
	 					shake();
	 	            }
	 				
	 	            last_x = x;
	 	            last_y = y;
	 	            last_z = z;
	 	        }
	 	    }
			
			function shake() {
				$(".tips2").hide();
				$(".tips3").hide();
				$(".sshake").addClass("shake");
				$(".audio")[0].play();
				setTimeout(function() {
					record();
					$(".audio")[0].pause();
					$(".sshake").removeClass("shake");
					SHAKE_THRESHOLD = 3000;
				},1500);
			}
			
			function record(){
				$.post("draw/pick",{participantName: participantName},function(data){
					$(".tips").hide();
					$(".audio")[1].play();
					if(data.type == 1){
			 			$('.prize').text(data.spec);
			 			$('.money').text(data.mess);
			 			$(".tips3").show();
			 		}else{
			 			$('.ptip').text(data.spec);
		 				$('.pptip').text(data.mess);
			 			$(".tips2").show();
			 		}
			 	});
			}
			
			$(".exit").click(function(){
				localStorage.removeItem('participantName');
				window.open("mobileLuckDraw.jsp","_self");
			})
			
			/* $('#shakeBtn').click(function(){
				shake();
			}); */
		})(jQuery);
	</script>
</body>
</html>
