//4中气泡样式
var colors = ["#f6366d", "#19a3dc", "#9057cb", "#f47119"];

/*
name 名字
prize 奖项
number 唯一标识
*/
function newDiv(name,prizeName,number) {
	var wh;
	var delay;
	var prize = number % 3 + 1;
	var color = colors[prize - 1];
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
	var html = '<div class="bubble" style="display:block" id=' + number + '><p class="number">'+name +'</p><p class="money"> ' + prizeName + '</p></div>';
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

var storeData = [];

var flag = true;

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

setInterval("redirect()",1000);
function redirect(){
	if(flag){
		$.post("link/hitPrize?linkId="+linkId,function (data){
			flag = false;
			for(var i = 0, len = data.length; i < len; i++){
				var name = data[i].participantName;
				var prizeName = data[i].prizeName;
				if(!contains(storeData, name)){
					storeData.push(name);
					newDiv(name, prizeName, storeData.length)
				}
			}
			flag = true;
		});
	}
	$.get('center/getCommand', function(data){
		if(data.type == 'NULL'){
			// 空指令，不做任何事情
		}else if(data.type == 'REDIRECT'){
			//跳转到指定页面
			window.open(data.url,"_self");
		}
	});		
}




$(function() {
	//后去当前环节数
	var luck = "第一环节";
	$(".header-h1").html(luck + "中奖信息");
});



