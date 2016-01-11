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

	<!-- login page -->
	<div class="container-fluid" id="login-container">
		<img alt="" src="">
		<form class="form-horizontal" id="login-form">
			<div class="control-group">
				<label class="sr-only" for="login-num">用户编号</label>
              	<input class="form-control" name="participantNum" placeholder="输入你的编号" id="login-num" type="text">
            </div>

            <div class="control-group">
            	<label class="sr-only" for="login-num">用户名字</label>
              	<input class="form-control" name="participantName" placeholder="输入你的名字" id="login-name" type="text">
            </div>
            <button class="btn btn-primary btn-large btn-block" type="submit" id="subBtn">确定</button>
       </form>
	</div>
	
	<div class="container-fluid" id="hand-container">
		<audio style="display:none" id="music" controls src="resources/wechat-h5/images/5018.mp3">  
		</audio>  
		<div id="hand" class="hand hand-animate"></div>
		<div id="loading" class="loading"></div>
		<div id="result" class="result">
			<div class="pic"></div>
			<div class="con"><strong id="spec"></strong><br/><div id="mess"></div></div>
		</div>
	</div>
	
	
	<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
    <script src="resources/Flat-UI/js/vendor/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="resources/Flat-UI/js/flat-ui.min.js"></script>
 
 	<script type="text/javascript">
 
 	(function($){
 		
 		function getLocalStorage(){
 			if(typeof localStorage == 'object'){
 				return localStorage;
 			}else if(typeof globalStorage == 'object'){
 				return globalStorage[location.host];
 			}else{
 				throw new Error('Local storage not available.')
 			}
 		}
 		
 		// 从本地缓存中获取用户编号（已成功登陆的用户才会保存用户编号）
 		var storage = getLocalStorage();
 		var participantNum = storage.getItem('participantNum');
 		var participantNum = storage.getItem('participantNum');
 		// 如果存在用户编号，说明用户已经验证过了，可以直接进入摇一摇抽奖界面
 		if(participantNum === undefined){
 			showLogin();
 		}else{
 			showHand();
 		}
 		
 		// 验证
 		$('#login-form').submit(function(event){
    		event.preventDefault();
    		var participantNum = $('#login-num').val();
    		var participantName = $('#login-name').val();
    		$('#subBtn').showLoading();
    		$.post("participant/auth", {participantNum: participantNum, participantName: participantName},function(data){
    			$('#subBtn').hideLoading();
    			if(data.returnCode == 1){
    				// 验证成功时，保存参与人员编号到本地数据缓存中
    				var storage = getLocalStorage();
    				storage.setItem('participantNum', participantNum);
    				showHand();
    			}else{
    				alert(data.resultMsg);
    			}
    		});
    	});
 		
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

 				if(speed > 400){
 					$('#hand').addClass('hand-animate');
 				}
 				
 				if(speed <=400){
 					$('#hand').removeClass('hand-animate');
 				}
 				
 				if (speed > SHAKE_THRESHOLD) {
 						pick();
 	            }
 				
 	            last_x = x;
 	            last_y = y;
 	            last_z = z;
 	        }
 	    }
 	    
 	    function pick() {
 	    	playMusic();
 	    	loading();
 	        $.post("draw/pick", {participantNum: participantNum},function(data){
 	        	$('#spec').text(data.spec);
 				$('#mess').text(data.mess);
 				show();
 			});
 	    }
 	    
 	    function playMusic(){
 	    	$('#music')[0].play();
 	    }
 	    
 	    function loading(){
 	    	$('#loading').addClass('loading-show');
 	    	$('#result').removeClass('result-show');
 	    }
 	    
 	    function show(){
 	    	$('#loading').removeClass('loading-show');
 	    	$('#result').addClass('result-show');
 	    }
 		
 		// 显示验证
 		function showLogin(){
 			$('#login-container').show();
 			$('#hand-container').hide();
 		}
 		
 		// 显示摇奖
 		function showHand(){
 			$('#login-container').hide();
 			$('#hand-container').show();
 		}
 		
 	})(jQuery);
 	
  </script>
</body>
</html>