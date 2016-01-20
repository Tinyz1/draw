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
    <title>摇一摇抽奖控制界面</title>
</head>
	<body>
	
		<nav class="container" style="margin-top: 30px;">
			<div class="row">
				<!-- 菜单 -->
				<div class="col-md-3">
					<ul class="nav nav-pills nav-stacked">
					  <li role="presentation" class="active"><a href="#linkCtr">环节控制</a></li>
					  <li role="presentation"><a href="#draw">抽奖控制</a></li>
					  <li role="presentation"><a href="#linkSet">环节配置</a></li>
					  <li role="presentation"><a href="#user">人员配置</a></li>
					  <li role="presentation"><a href="#prizeuser">中奖记录</a></li>
					  <li role="presentation"><a href="#person">环节人员</a></li>
					</ul>
				</div>
				
				<!-- 内容 -->
				<div class="col-md-9">
					<iframe width="100%" frameborder="0" scrfrolling="no" id="external-frame"></iframe>
				</div>
			</div>
		</nav>
	
		<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
	   	<script src="<%=contextPath %>/resources/Flat-UI/js/vendor/jquery.min.js"></script>
	   	<!-- Include all compiled plugins (below), or include individual files as needed -->
	   	<script src="<%=contextPath %>/resources/Flat-UI/js/flat-ui.min.js"></script>
	   	
	   	<script type="text/javascript">
	   	
	   		(function($){
	   			
	   			// 界面初始化为抽奖控制
	   			setIframeSrc('linkCtr.html', 900);

	   			function setIframeSrc(src, height) {   
	   				var $iframe = $('#external-frame');
	   				$iframe.attr('src', src);
	   				$iframe.height(height);
	   			}
	   			
	   			$('li[role=presentation]').click(function(){
	   				var $this = $(this);
	   				$this.siblings().removeClass('active');
	   				$this.addClass('active');
	   				
	   				var href= $(this).find('a').attr('href').substring(1);
	   				console.log(href);
	   				switch (href){
						case 'linkCtr':{
							console.log('环节控制');
							setIframeSrc('linkCtr.html');
							break;
						}
						case 'draw':{
		   					console.log('抽奖控制');
		   					setIframeSrc('draw.html');
		   					break;
		   				}
						case 'linkSet':{
							console.log('环节配置');
							setIframeSrc('linkSet.html');
							break;
						}
						case 'user':{
							console.log('人员配置');
							setIframeSrc('user.html');
							break;
						}
						case 'prizeuser':{
							console.log('中奖查询');
							setIframeSrc('prizeuser.html');
							break;
						}
						case 'person':{
							console.log('环节人员查询');
							setIframeSrc('person.html');
							break;
						}
	   				}
	   			});
	   			
	   		})(jQuery);
	   	
	   	</script>
   	</body>
</html>
