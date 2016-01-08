<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="utf-8">
    <!-- Loading Bootstrap -->
    <link href="resources/Flat-UI/css/vendor/bootstrap.min.css" rel="stylesheet">

    <!-- Loading Flat UI -->
    <link href="resources/Flat-UI/css/flat-ui.min.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements. All other JS at the end of file. -->
    <!--[if lt IE 9]>
      <script src="resources/Flat-UI/js/vendor/html5shiv.js"></script>
      <script src="resources/Flat-UI/js/vendor/respond.min.js"></script>
    <![endif]-->

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

	<script>
		function init(){
			var userid = getCookie("userid");
			if(userid == null || "" == userid || "undefined" == typeof userid){
				 document.getElementById("show1").style.visibility='visible';
				 document.getElementById("show2").style.visibility='hidden';
				 document.getElementById("hand").style.visibility='hidden';
				 document.getElementById('music1').pause();
			}else{
				 document.getElementById("show1").style.display='none';
				 document.getElementById("show2").style.visibility='visible';
			}
	}
	//设置cookie的值，根据名字，value,time
	function setCookie(name,value,time){ 
		var str = name + "=" + escape(value);
		if(time > 0){
			var date = new Date();
			var ms = time*1000*3600;
			date.setTime(date.getTime() + ms);
			str += "; expires=" + date.toGMTString();
    }
		document.cookie = str;
	}
	//获取cookie  
	function getCookie(name){  
    //cookie中的数据都是以分号加空格区分开  
    var arr = document.cookie.split("; ");  
    for(var i=0; i<arr.length; i++){  
        if(arr[i].split("=")[0] == name){  
            return arr[i].split("=")[1];  
        }
    }
    //未找到对应的cookie则返回空字符串  
    return '';  
	}
	//删除cookie  
	function removeCookie(name){   
		document.cookie = name+"=;expires="+(new Date(0)).toGMTString();
	}
	function submitbtn(){
		var userid = document.getElementById("userid").value;
		setCookie("userid",userid,24);
		document.getElementById("form").submit();
/*		$.ajax({
                cache: true,
                type: "POST",
                url:ajaxCallUrl,
                data:$('#form').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("Connection error");
                },
                success: function(data) {
                   var userid =  $("#userid").val();
				   setCookie("userid",userid,24);
				   window.location.reload();
                }
            });*/
	}	
	</script>
</head>
<body>
	<div id="show1" class="text-center">
	<div style="padding: 60px 60px 40px;">
   <form class="bs-example bs-example-form" role="form"  id="form">
      <div class="input-group">
         <span class="input-group-addon">姓名</span>
         <input type="text" class="form-control"  placeholder="如：马云">
      </div>
      <br>

      <div class="input-group">
		 <span class="input-group-addon">编号</span>
         <input type="text" class="form-control" placeholder="如：0001" id="userid">
      </div>
      <br>
	  <div class="control-group" >
          <div class="controls">
            <button class="btn btn-success" onclick="submitbtn()">提交</button>
          </div>
        </div>
   </form>
</div>
	</div>
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
		$("#reshtml").html(html);
	/*	$.ajax({
                cache: true,
                type: "POST",
                url:ajaxCallUrl,
                data:$('#form').serialize(),// 你的formid
                async: false,
                error: function(request) {
                    alert("没有中奖哦，加油！");
                },
                success: function(data) {
					判断是否中奖，中奖信息是什么
					var html ="恭喜您，获得"+data; 
                    $("#reshtml").html(html);
                }
            });*/
        document.getElementById("result").className = "result";
        document.getElementById("loading").className = "loading loading-show";
        setTimeout(function(){
            document.getElementById("result").className = "result result-show";
            document.getElementById("loading").className = "loading";
        }, 1000);
    }
	
  </script>
</body>
</html>