//4中气泡样式
var colors = ["#f6366d", "#19a3dc", "#9057cb", "#f47119"];

/*
	name 用户姓名
	prize 奖项等级
	prizeName 奖项名称
	
	wh 设置气泡宽度大小
	lh 设置气泡高度
	delay 设置气泡浮动速度
 */
function newDiv(name,prize,number,prizeName) {
	var color = colors[prize - 1];
	var wh;
	var delay;
	var prizes;
	if (prize == 1) {
		wh = 220 + "px";
		lh=60+"px";
		delay = 7500;	
	} else if (prize == 2) {
		wh = 200 + 'px';
		delay = 7000;
		lh=40+"px";
	} else if (prize == 3) {
		wh = 180 + "px";
		delay = 6500;
		lh=30+"px";
	} else {
		wh = 160 + "px";
		delay = 6000;
		lh=20+"px";
	}
	var html = '<div class="bubble" style="display:block" id=' + number + '><p class="number">'+name +
						 '</p><p class="money"> ' + prizeName + '</p></div>';
	var li2p="<li><p>"+name+"</p><p> "+ prizeName+"</p></li>";
	$("#d1").append(li2p);
	$("body").append(html);
	var lf = Math.random() * 80 + "%";
	var str = "#" + number;
	$(str).css({
		"left": lf,
		"width": wh,
		"height": wh,
		"background": color,
		"lineHeight":lh,
		"zIndex":100
	});
	bubble($(str), delay);
}
function bubble(ele, delay) {
	ele.animate({
		"top": -220
	}, delay);
	$(".bubble").fadeOut("slow");
}

var i = 0;

var timer = setInterval(function(){
	
	i += 1;
	var prize = Math.floor(Math.random() * 4) + 1;
	//var name = request.getParameter("name");
	var name = "汪小峰";var prizeName = "无人机";
	
	//var prize = request.getParameter("prize");
	//var prizeName = request.getParameter("prizeName");
	newDiv(name, prize, i, prizeName)
	if(i>19){
		clearInterval(timer);
	}
},3000);

$(function() {
	//后去当前环节数
	var luck = "第一环节";
	$(".header-h1").html(luck + "中奖信息");
});



