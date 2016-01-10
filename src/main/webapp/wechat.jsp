<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="utf-8">
    <!-- Loading Bootstrap -->
    <link href="resources/Flat-UI/css/vendor/bootstrap.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="resources/Flat-UI/css/flat-ui.min.css" rel="stylesheet">
    <style>
    * { margin: 0; padding: 0; }
    body { background: #292D2E; }
    .hand { width: 190px; height: 300px; background: url(resources/wechat-h5/images/hand.png) no-repeat; position: absolute; top: 50px; left: 50%; margin-left: -95px; }
    .hand-animate { -webkit-animation: hand_move infinite 2s; }
    .result { background: #393B3C; border: #2C2C2C 1px solid; box-shadow: inset #4D4F50 0 0 0 1px; border-radius: 10px; color: #fff; padding: 10px; width: 300px; position: absolute; top: 300px; left: 50%; margin-left: -161px; opacity: 0;
        -webkit-transition: all 1s;
           -moz-transition: all 1s;
            -ms-transition: all 1s;
             -o-transition: all 1s;
                transition: all 1s; }
    .result .pic { width: 50px; height: 50px; float: left; background: url('resources/wechat-h5/images/asiainfo.PNG') no-repeat; }
    .result .con { overflow: hidden; zoom: 1; padding-left: 10px; line-height: 24px; }
    .result-show { opacity: 1; margin-top: 50px; }
    .loading { position: absolute; top: 240px; left: 50%; margin-left: -50px; width: 100px; height: 100px; background: url(resources/wechat-h5/images/spinner.png) no-repeat; background-size: 100px 100px; opacity: 0;
        -webkit-animation: loading infinite linear .5s;
           -moz-animation: loading infinite linear .5s;
            -ms-animation: loading infinite linear .5s;
             -o-animation: loading infinite linear .5s;
                animation: loading infinite linear .5s;
        -webkit-transition: all .5s;
           -moz-transition: all .5s;
            -ms-transition: all .5s;
             -o-transition: all .5s;
                transition: all .5s; }
    .loading-show { opacity: 1; }
    
    @-webkit-keyframes hand_move {
        0% {
            -webkit-transform: rotate(0);
               -moz-transform: rotate(0);
                -ms-transform: rotate(0);
                 -o-transform: rotate(0);
                    transform: rotate(0); }
        50% {
            -webkit-transform: rotate(15deg);
               -moz-transform: rotate(15deg);
                -ms-transform: rotate(15deg);
                 -o-transform: rotate(15deg);
                    transform: rotate(15deg); }
        100% {
            -webkit-transform: rotate(0);
               -moz-transform: rotate(0);
                -ms-transform: rotate(0);
                 -o-transform: rotate(0);
                    transform: rotate(0); }
    }

    @-webkit-keyframes loading {
        0% {
            -webkit-transform: rotate(0);
               -moz-transform: rotate(0);
                -ms-transform: rotate(0);
                 -o-transform: rotate(0);
                    transform: rotate(0); }
        100% {
            -webkit-transform: rotate(360deg);
               -moz-transform: rotate(360deg);
                -ms-transform: rotate(360deg);
                 -o-transform: rotate(360deg);
                    transform: rotate(360deg); }
    }

    </style>
</head>
<body>

	<div id="show2">
		<audio style="display:none" id="music1"  controls src="resources/wechat-h5/images/5018.mp3">  
		</audio>  
		<div id="hand" class="hand hand-animate"></div>
		<div id="loading" class="loading"></div>
		<div id="result" class="result">
			<div class="pic"></div>
			<div class="con">摇晃结果<br/><div id="reshtml">大奖一个</div></div>
		</div>
	</div>
	
	<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
    <script src="resources/Flat-UI/js/vendor/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="resources/Flat-UI/js/flat-ui.min.js"></script>
 <script>
	init();
    var SHAKE_THRESHOLD = 3000;
    var last_update = 0;
    var x = y = z = last_x = last_y = last_z = 0;

    if (window.DeviceMotionEvent) {
        window.addEventListener('devicemotion', deviceMotionHandler, false);
    } else {
        alert('本设备不支持devicemotion事件');
    }

    function deviceMotionHandler(eventData) {
        var acceleration = eventData.accelerationIncludingGravity;
        var curTime = new Date().getTime();

        if ((curTime - last_update) > 100) {
            var diffTime = curTime - last_update;
            last_update = curTime;
            x = acceleration.x;
            y = acceleration.y;
            z = acceleration.z;
            var speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
            var status = document.getElementById("status");
			if(SHAKE_THRESHOLD == 1000000){
				document.getElementById("hand").className = "hand";
				return ;
			}
			if(speed > 400){
				 document.getElementById("hand").className = "hand hand-animate";
			}
			if(speed > 1000){
				if ($("#show1").is(":visible") == false){
					document.getElementById('music1').play(); 
				}
			}
			if(speed <=400){
				document.getElementById("hand").className = "hand";
			}
			if (speed > SHAKE_THRESHOLD) {
				if ($("#show1").is(":visible") == false){
					doResult();
				}
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }
    function doResult() {
		var arr = ['恭喜，摇得一等奖：保时捷卡宴','恭喜，摇得二等奖：奔驰 320','恭喜，摇得三等奖：奇瑞QQ','恭喜，摇得优秀奖：iphone6s','悲伤逆流成河,继续努力']; 
        var num = Math.floor(Math.random()*5); 
		if(num <1){
			SHAKE_THRESHOLD = 1000000;
		}
       var html = arr[num]+"！";
    }
	
  </script>
</body>
</html>