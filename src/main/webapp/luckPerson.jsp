<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>人员选择</title>
	<script src="resources/js/jquery-1.10.2.min.js"></script>
	<link rel="stylesheet" href="resources/css/style.css">
<%
	// 当前选取人员个数
	String partnum = request.getParameter("partnum");
	// 总的可以被选择的人数
	String allow = request.getParameter("allow");
%>
</head>
<body>
	<div class="wrap">
		<div class="header">
			<img src="resources/image/bg.png">
		</div>
		<div class="footer">
			<img src="resources/image/foot.png">
		</div>
		<div class="content list">
			<img class="smallImg" src="resources/image/w2016.png">
			<ul class="listUl">
			</ul>
			<div class="btn-s">
				开始抽奖
			</div>
		</div>
			<p class="footP">CMC&BDX上海年会</p>
			<h1 class="footH">万众一信 · 筑梦起航</h1>
	</div>
</body>
<script type="text/javascript">

var partnum = <%=partnum %>;

var arr = [];
// 保存抽中的用户信息
var personArr = [];
setInterval("redirect()", 1000);
var timer=[];
// 界面初始化
init();

function redirect() {
	$.get('center/getCommand', function(data) {
		if (data.type == 'NULL') {
			// 空指令，不做任何事情
		} else if (data.type == "INIT_POOL") {
			$.post("link/initPool");
		} else if (data.type == 'REDIRECT') {
			window.open(data.url, "_self");
		} else if (data.type == 'PICK_START') {
			start();
		} else if (data.type == 'PICK_END') {
			endCommand();
		}
	});
}
function endCommand() {
	for(var i=0;i<arr.length;i++){
	clearInterval(timer[i]);
	}
	lotteryDraw(arr, partnum);
	var ids = "";
	for (var i = 0; i < personArr.length; i++) {
		var id = personArr[i].participantId;
		ids += id + ",";
	}
	ids = ids.substring(0,ids.lastIndexOf(','));
	$.post("participant/current/addPickParticipant", {"ids" : ids});
}
function lotteryDraw(arr, number) {
	while(personArr.length<number) {
		var j = Math.floor(Math.random() * arr.length);
		var con = arr[j];
		console.log(con)
		var i=0;
		for(;i<personArr.length;i++){
			if(personArr[i].participantId==con.participantId){
				break;
			}
		}
		if(i==personArr.length){
			arr.splice(arr.indexOf(con), 1);
			personArr.push(con);
		}
	}
	personArr.forEach(function(item, i) {
		var k = i % 5;
		var lefts = ["0", "20%", "40%", "60%", "80%"];
		var tops = ["10%", "30%", "50%", "70%"]
		var left = lefts[k];
		var n = Math.floor(i / 5);
		var top = tops[n];
		item = "#" + item.index;
		$(item).animate({
			left: left,
			top: top
		}, 500);
		$(item).css({
			fontSize: 22+"px",
    		color:"#000"
		});
	})
	arr.forEach(function(item) {
		var str = "#" + item.index;
		$(str).css("display", "none");
	})
};

function start(){
	if(isInit){
		for (var i = 0; i < arr.length; i++) {
			var html = "<li class='ran' id=" + arr[i].index + ">" + arr[i].participantName + "</li>";
			$(".listUl").append(html);
		}
		$(".ran").each(function(i) {
			var t = Math.random() + 1;
			$(this).css({
				top: 3 * (++i) + "px",
				left: t * 110 + "%"
			});
		});
		arr.forEach(function(item) {
			var str = "#" + item.index;
			var color = "rgb(" + Math.floor(Math.random() * 256) + "," + Math.floor(Math.random() * 256) + "," + Math.floor(Math.random() * 256) + ")";
			var fontSize = Math.floor(Math.random() * 30) + 21;
			$(str).css({
				"color": color,
				fontSize: fontSize + "px"
			});
			var left = parseInt($(str).css("left"));
			var mleft = left;
			timer[item.index] = setInterval(function() {
				left -= 5;
				if (left == -300) {
					left = mleft;
				}
				$(str).css("left", left + "px")
			}, 5);
		});
	}
}

// 页面是否已初始化完毕
var isInit = false;
// 初始化人员数据
// 初始化界面
function init() {
	// 未初始化时，才需要初始化
	if(!isInit){
		$.post("participant/current/participants", function(data) {
			for(var i = 0; i< data.length ;i++){
				var a = {};
				a.participantId = data[i].participantId;
				a.participantName = data[i].participantName;
				a.index=i;
				arr.push(a);
			}
			isInit = true;
			for (var i = 0; i < arr.length; i++) {
				var html = "<li class='ran' id=" + arr[i].index + ">" + arr[i].participantName + "</li>";
				$(".listUl").append(html);
			}
			$(".ran").each(function(i) {
				var t = Math.random()-0.1;
				
				if(i>100){
					i=i%100;
				}
				if(i%5==0){
					$(this).css({
						top: 3 * (++i) + "px",
						left: t * 90 + "%"
					});
				}
			});
		});
	}
}
 $(".btn-s").click(function() {
	 endCommand();
}); 
</script>
</html>
