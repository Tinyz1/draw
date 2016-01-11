function login() {
	var username = $(".username").val();
	user = {
		"participantName": username
	};
	localStorage.setItem('participantName', username);
	$.post("participant/auth", user,function(data){
		if(data.returnCode === 1){
			window.open("shake.jsp","_self");
		}else{
			alert("请输入正确的姓名");
		}
	});
}

function getLocalStorage() {
	if (typeof localStorage == 'object') {
		return localStorage;
	} else if (typeof globalStorage == 'object') {
		return globalStorage[location.host];
	} else {
		throw new Error('Local storage not available.')
	}
}

// 从本地缓存中获取用户编号（已成功登陆的用户才会保存用户编号）
var storage = getLocalStorage();
var participantName = storage.getItem('participantName');
var user = {"participantName": participantName};
$(".username").val(participantName);

function shake() {
	$(".sshake").addClass("shake");
	$(".audio")[0].play();
	setTimeout(function() {
		$(".sshake").removeClass("shake");
		$(".audio")[0].pause();
		record();
	},2000);
}
function record(){
	 $.post("participant/auth",user,function(data){
	 	if(data.type == 1){
	 		$(".tips").hide();
	 		console.log(data.mess);
	 		$(".tips3").show();
	 	}else if(data.type == 2){
	 		$(".tips").hide();
	 		$(".tips2").show();
	 	}else if(data.type== 3){
	 		$(".tips").hide();
	 		$(".ptip").html("当前抽奖环节已结束");
	 		$(".tips2").show();
	 	}else{
	 		$(".tips").hide();
	 		$(".ptip").html("当前抽奖环节未开始");
	 		$(".tips2").show();
	 	}
	 });
		/*var types=["中奖了","未中奖","当前抽奖环节已结束","当前抽奖环节未开始"];
		var i=Math.floor(Math.random()*4);
		var type=types[i];
		$(".tip").addClass("hidden");
		if(type =="中奖了"){	
			$(".tips3").removeClass("hidden");
		}else if(type =="未中奖"){
			$(".tips2").removeClass("hidden");
		}else if(type=="当前抽奖环节已结束"){
			$(".ptip").html("当前抽奖环节已结束");
			$(".pptip").html("");
			$(".tips2").removeClass("hidden");
		}else{
			$(".ptip").html("当前抽奖环节未开始");
			$(".pptip").html("");
			$(".tips2").removeClass("hidden");
		}*/
}