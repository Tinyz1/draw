$(function() {
	
	//后去当前环节数
	var luck = "第一环节";
	$(".header-h1").html(luck + "获奖人名单");
	
	// 获取用户名称数组
	var arrs = ["赵林刚", "赵林刚", "赵林刚", "赵林刚", "赵林刚", "赵林刚", "赵林刚", "赵林刚", "赵林刚","赵林刚" ];
	
	//$("#listUl").append('<li><span style="color:red;">'+ arrs[0]+'</span></li>');
	

	var html = "";
	// 遍历用户数组数据
	for (var i = 0; i < arrs.length; i++) {
		var arr = arrs[i];
		html += '<li><span style="color:red;">'+ arr +'</span></li>';
	}

	$("#listUl").append(html);
});