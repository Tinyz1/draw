<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
	<meta charset="utf-8">
    <!-- Loading Bootstrap -->
    <link href="resources/Flat-UI/css/vendor/bootstrap.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="resources/Flat-UI/css/flat-ui.min.css" rel="stylesheet">
    
    <title>中央控制</title>
    
</head>

<body>
<table id="tb1" width="100%" border="1" cellspacing="1" cellpadding="1" height="639">
  <tr>
    <td height="135" colspan="2">
		<h1>&nbsp;&nbsp;配置页面</h1>
	</td>
  </tr>
  <tr>
    <td width="21%" height="490" valign="top">
		<div align = "center">
			<br>
			<br>
			<button id="配置" class="btn btn-default" onclick="config()">正常流程</button>
			<br>
			<br>
			<button id="新增" class="btn btn-default" onclick="addp()">加奖流程</button>
			<br>
			<br>
			<button id="环节" class="btn btn-default" onclick="linkcontrl()">环节控制</button>
			<br>
			<br>
			<button id="用户" class="btn btn-default" onclick="addnew1()">新增用户</button>
			<br>
			<br>
		</div>
	</td>
    <td width="79%" valign="top">
		<div id = "配置2" align = "center">
			
			<div class="container">
				<div class="row" style="margin-top: 20px;">
					<div class="col-md-12">
						<form class="form-inline">
						  <div class="form-group">
						    <label class="sr-only" for="exampleInputAmount">参与人员个数</label>
						    <div class="input-group">
						      <input type="text" class="form-control" name="partnum" id="partnum" placeholder="输入参与人员个数">
						    </div>
						  </div>
						  <button type="submit" id="partBtn" class="btn btn-primary">确定并进入抽人界面</button>
						</form>						
					</div>
				</div>
				<div class="row" style="margin-top: 20px;">
					<div class="col-md-12">
						  <button id="startBtn" class="btn btn-primary">开始</button>
						  <button id="endBtn" class="btn btn-primary">结束</button>
					</div>
				</div>
				<div class="row" style="margin-top: 20px;">
					<div class="col-md-12">
						  <button id="commitBtn" class="btn btn-primary">确认提交抽奖人员并进入人员确认界面</button>
					</div>
				</div>
				<div class="row" style="margin-top: 20px;">
					<div class="col-md-12">
						  <button id="linkBtn" class="btn btn-primary">启动抽奖环节并开始抽奖</button>
					</div>
				</div>
			</div>
		</div>
		<div id="新增2"  align = "center">
			<form action="prize/addprize" method="post">
			<h2>奖项新增</h2>
			环节名字：<input type="text" name="linkname" />&nbsp;&nbsp;&nbsp;
			奖品数量：<input type="text" name="prizenum" />&nbsp;&nbsp;&nbsp;
			参赛人数：<input type="text" name="provider" />&nbsp;&nbsp;&nbsp;
			 是否对所有人开放<select name="course">
				<option value="2">否</option>
				<option value="1" >是</option>
	  			</select>
			<br />
			<br />
			<input type="submit"  value="发奖"/>&nbsp;&nbsp;&nbsp;
			<input type="reset" id="button1" value="重置" />
			</form>
		</div>
		<div id="环节2" align = "center">
		<h1>环节控制</h1>
			<input type="submit"  value="环节开始" onclick="startlink()"/>&nbsp;&nbsp;&nbsp;
			<input type="submit"  value="环节终止"/>&nbsp;&nbsp;&nbsp;
			<input type="submit"  value="重新加载环节"/>&nbsp;&nbsp;&nbsp;
		</div>
		<div id="用户2"  align = "center">
			<form action="<%=request.getContextPath()%>/participant/addnewpart" method="post">
				<h2>用户注册</h2>
				用户昵称：<input type="text" name="name" /> <br />
	  			<input type="submit" value="提交">&nbsp;&nbsp;&nbsp;
	  			<input type="reset" value="重置"></input>
			</form>
		</div>
		<div id="提交2" align = "center">
			<input type="submit"  value="人员提交" style="width: 200px;height: 40px"/>&nbsp;&nbsp;&nbsp;
		</div>
	</td>
  </tr>
</table>

	<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
    <script src="resources/Flat-UI/js/vendor/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="resources/Flat-UI/js/flat-ui.min.js"></script>
    
    <script type="text/javascript">
    
    (function($){
    	$('#partBtn').click(function(e){
    		e.preventDefault();
    		var partnum = $('#partnum').val();
    		$.post('center/pick/num', {partnum: partnum}, function(data){
    			alert(data.resultMsg);
    		});
    	});
    	
    	$('#startBtn').click(function(e){
    		$.post('center/pick/start', function(data){
    			alert(data.resultMsg);
    		});
    	});
    	
    	$('#endBtn').click(function(e){
    		$.post('center/pick/end', function(data){
    			alert(data.resultMsg);
    		});
    	});
    	
    	$('#commitBtn').click(function(e){
    		if(confirm("确认提交后，本环节将不能继续加人。是否确认？")){
        		$.post('center/pick/commit', function(data){
        			alert(data.resultMsg);
        		});
    		}
    	});
    	
    	$('#linkBtn').click(function(e){
    		$.post('link/start', function(data){
    			alert(data.resultMsg);
    		});
    	});
    	
    	
    })(jQuery);
    
	//初始化，不展示页面
	window.onload = function(){
		document.getElementById("配置2").style.display="none";
		document.getElementById("新增2").style.display="none";
		document.getElementById("用户2").style.display="none";
		document.getElementById("环节2").style.display="none";
		document.getElementById("提交2").style.display="none";
	}
	//展示配置界面
	function config(){
		document.getElementById("配置2").style.display="block";
		document.getElementById("新增2").style.display="none";
		document.getElementById("用户2").style.display="none";
		document.getElementById("环节2").style.display="none";
		document.getElementById("提交2").style.display="none";
	}
	//展示新增界面
	function addp(){
		document.getElementById("配置2").style.display="none";
		document.getElementById("新增2").style.display="block";
		document.getElementById("用户2").style.display="none";
		document.getElementById("环节2").style.display="none";
		document.getElementById("提交2").style.display="none";
	 }
	
	
	//用户新增
	function addnew1(){
		document.getElementById("配置2").style.display="none";
		document.getElementById("新增2").style.display="none";
		document.getElementById("用户2").style.display="block";
		document.getElementById("环节2").style.display="none";
		document.getElementById("提交2").style.display="none";
	}
	
	//环节控制
	function linkcontrl(){
		document.getElementById("配置2").style.display="none";
		document.getElementById("新增2").style.display="none";
		document.getElementById("用户2").style.display="none";
		document.getElementById("环节2").style.display="block";
		document.getElementById("提交2").style.display="none";
	}
	
	//人员提交控制
	function tijiao(){
		document.getElementById("配置2").style.display="none";
		document.getElementById("新增2").style.display="none";
		document.getElementById("用户2").style.display="none";
		document.getElementById("环节2").style.display="none";
		document.getElementById("提交2").style.display="block";
	}
	//环节开始
	function startlink(){
		location.href="/draw/link/start";
	}
	
	</script>
</body>
</html>
