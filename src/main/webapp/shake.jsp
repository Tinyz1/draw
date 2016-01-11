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
	</div>
	
	<script type="text/javascript">
		(function($){
			
			// 获取本地缓存中的用户名称
			var storage = getLocalStorage();
			var participantName = storage.getItem('participantName');
			
			var SHAKE_THRESHOLD = 2000;
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
				},1500);
			}
			
			function record(){
				$.post("draw/pick",{participantName: participantName},function(data){
					$(".tips").hide();
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
		})(jQuery);
	</script>
</body>
</html>
