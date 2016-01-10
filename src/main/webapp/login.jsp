<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="utf-8">
    <!-- Loading Bootstrap -->
    <link href="resources/Flat-UI/css/vendor/bootstrap.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="resources/Flat-UI/css/flat-ui.min.css" rel="stylesheet">
	<link href="resources/showloading/css/loading.css" rel="stylesheet">
	
	<script type="text/javascript">
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
	// 如果存在用户编号，说明用户已经验证过了，可以直接进入摇一摇抽奖界面
	if(participantNum){
		location.href = 'wechat.jsp';
	}
	</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-12">
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
		</div>
	</div>
	<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
    <script src="resources/Flat-UI/js/vendor/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="resources/Flat-UI/js/flat-ui.min.js"></script>
    <script src="resources/showloading/js/loading.js"></script>
    
    <script type="text/javascript">
    	
    (function($){
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
    				// 跳转到摇一摇界面
    				location.href = 'wechat.jsp';
    			}else{
    				alert(data.resultMsg);
    			}
    		});
    	});
    })(jQuery);
    
    </script>
</body>
</html>