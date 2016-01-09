<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="utf-8">
    <!-- Loading Bootstrap -->
    <link href="resources/Flat-UI/css/vendor/bootstrap.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="resources/Flat-UI/css/flat-ui.min.css" rel="stylesheet">
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-12">
				<form class="form-horizontal" id="login-form" action="participant/auth" method="post">
					<div class="control-group">
						<label class="sr-only" for="login-num">用户编号</label>
		              	<input class="form-control" name="participantNum" placeholder="输入你的编号" id="login-num" type="text">
		            </div>
		
		            <div class="control-group">
		            	<label class="sr-only" for="login-num">用户名字</label>
		              	<input class="form-control" name="participantName" placeholder="输入你的名字" id="login-name" type="text">
		            </div>
		            <button class="btn btn-primary btn-large btn-block" type="submit">确定</button>
		       </form>
			</div>
		</div>
	</div>
	<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
    <script src="resources/Flat-UI/js/vendor/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="resources/Flat-UI/js/flat-ui.min.js"></script>
</body>
</html>