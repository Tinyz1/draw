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

	if (prize == 1) {
		wh = 220 + "px";
		lh=60+"px";
		delay = 20000;
	} else if (prize == 2) {
		wh = 200 + 'px';
		delay = 18000;
		lh=40+"px";
	} else if (prize == 3) {
		wh = 180 + "px";
		delay = 16000;
		lh=30+"px";
	} else {
		wh = 160 + "px";
		delay = 14000;
		lh=20+"px";
	}
	var html = '<div class="bubble" style="display:block" id=' + number 
				+ '><p class="name">'+name +'</p><p class="prizeName"> 获得：' 
				+ prizeName + '</p></div>';
	
	var li2p="<li><p>"+name+"</p><p>获得： "+ prizeName+"</p></li>";
	
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

$(".btnOk").click(function(){
	//后去当前环节数
	$(".header-h1").html("第一环节");
	var i=parseInt($(".pNumber").val());
	var timer = setInterval(function(){
		i -= 1;
		var prize = Math.floor(Math.random() * 4) + 1;
		newDiv(1, prize, i,prizeName);
		if(i<1){
			clearInterval(timer);
		}
},3000);
})


