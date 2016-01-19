<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = pageContext.getServletContext().getContextPath();
%>
<html>
<head>
	<meta charset="utf-8">
    <!-- Loading Bootstrap -->
    <link href="<%=contextPath %>/resources/Flat-UI/css/vendor/bootstrap.min.css" rel="stylesheet">
    <!-- Loading Flat UI -->
    <link href="<%=contextPath %>/resources/Flat-UI/css/flat-ui.min.css" rel="stylesheet">
    
    <!-- HTML5 shim, for IE6-8 support of HTML5 elements. All other JS at the end of file. -->
    <!--[if lt IE 9]>
      <script src="<%=contextPath %>/resources/Flat-UI/js/vendor/html5shiv.js"></script>
      <script src="<%=contextPath %>/resources/Flat-UI/js/vendor/respond.min.js"></script>
    <![endif]-->
    
    <title>抽奖控制</title>
</head>
	<body>
	
		<div class="container">
			<!-- 显示抽奖环节信息 -->
			<div class="row">
				<div class="col-md-12">
					<p class="bg-primary" style="padding: 10px;">抽奖环节名称：<strong id="linkName"></strong></p>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
					<form role="form">
		            	<legend>1、设置/添加抽奖环节参与人员</legend>
		            	<div class="form-group">
		               		<label class="sr-only" for="exampleInputAmount">人员个数</label>
		              		<input type="text" class="form-control" name="partnum" id="partnum" placeholder="请输入人员个数">
		            	</div>
		            	<button id="partBtn" type="submit" class="btn btn-default">确定</button>
		          	</form>
		          	
		          	<hr>
		        </div><!-- /.col-md-12 -->
		    </div><!-- /.row -->

			<div class="row">
				<div class="col-md-12">
					<legend>2、开始抽取</legend>
					<button id="startBtn" class="btn btn-default">开始</button>
					<hr>
					<legend>3、结束抽取</legend>
					<button id="endBtn" class="btn btn-default">结束</button>
					<hr>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
					<legend class="text-danger">4、确认人员并开始抽奖</legend>
					<button id="commitBtn" class="btn btn-default">确认开始</button>
					<hr>
				</div>
			</div>

		</div>
		
		<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
	   	<script src="<%=contextPath %>/resources/Flat-UI/js/vendor/jquery.min.js"></script>
	   	<!-- Include all compiled plugins (below), or include individual files as needed -->
	   	<script src="<%=contextPath %>/resources/Flat-UI/js/flat-ui.min.js"></script>

		<script type="text/javascript">
		
			(function($){
				
				var contextPath = '<%=contextPath %>';
				console.log('contextPath->' + contextPath);
				
				var initCurrntLink = function(){
					var url = contextPath + '/link/current';
					$.get(url, function(data){
						if(data.linkName){
							$('#linkName').text(data.linkName);
						}
					});
				};
				
				// 初始化当前环节
				initCurrntLink();

				// 点击按钮后执行的回调函数
				function callBack($this, data){
					var text = $this.text();
	    			var changeClass = '';
	    			if(data.returnCode === 1){
	    				changeClass = 'btn-success';
	    			}else{
	    				changeClass = 'btn-danger';
	    			}
	    			$this.text(data.resultMsg).removeClass('btn-default').addClass(changeClass);
	    			setTimeout(function(){$this.text(text).removeClass(changeClass).addClass('btn-default');}, 500);
				}
				
				// 选择抽奖人数
				$('#partBtn').click(function(e){
		    		e.preventDefault();
		    		var $this = $(this);
		    		var partnum = $('#partnum').val();
		    		var url = contextPath + '/center/pick/num';
		    		$.post(url, {partnum: partnum}, function(data){
		    			callBack($this, data);
		    		});
		    	});
		    	
				// 开始抽奖
		    	$('#startBtn').click(function(e){
		    		var $this = $(this);
		    		var url = contextPath + '/center/pick/start';
		    		$.post(url, function(data){
		    			callBack($this, data);
		    		});
		    	});
		    	
				// 结束抽奖
		    	$('#endBtn').click(function(e){
		    		var $this = $(this);
		    		var url = contextPath + '/center/pick/end';
		    		$.post(url, function(data){
		    			callBack($this, data);
		    		});
		    	});
		    	
				// 确认抽奖人员，并开始抽奖
		    	$('#commitBtn').click(function(e){
		    		var $this = $(this);
		    		var url = contextPath + '/center/pick/commit';
		    		if(confirm("确认提交后，就可以开始摇奖了。是否确认？")){
		        		$.post(url, function(data){
		        			callBack($this, data);
		        		});
		    		}
		    	});
				
			})(jQuery);
		
		</script>
   	</body>
</html>
