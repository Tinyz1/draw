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
    
    <title>环节控制</title>
</head>
	<body>
	
		<div class="container">
			<!-- 显示抽奖环节信息 -->
			<div class="row">
				<div class="col-md-12">
					<p class="bg-primary" style="padding: 10px;">抽奖环节控制</p>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>环节名称</th>
								<th>状态</th>
								<th colspan="3">操作</th>
							</tr>
						</thead>
						
						<tbody id="allLink">
						
						</tbody>
					</table>
		          	<hr>
		        </div><!-- /.col-md-12 -->
		    </div><!-- /.row -->
		    
		    <div class="row">
				<div class="col-md-12">
					<p class="text-warning">注意：启动新的环节，会结束当前环节！</p>
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
				
				var stateName = function(state){
					var map = {
						'1': '<em class="text-primary">未开始</em>',
						'2': '<strong class="text-info">进行中...</strong>',
						'3': '<span class="text-muted">已结束</span>'
					};
					return map[state];
				}
				
				var btn = function(link, text){
					switch (link.linkState){
					case 1:{
						if(text === '启动'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-info init">启动</button>';
						}else if(text === '结束'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-danger disabled finish">结束</button>';
						}else if(text === '重置'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-warning disabled reset">重置</button>';
						}
						break;
					}
					case 2:{
						if(text === '启动'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-info disabled init">启动</button>';
						}else if(text === '结束'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-danger finish">结束</button>';
						}else if(text === '重置'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-warning disabled reset">重置</button>';
						}
						break;
					}
					case 3:{
						if(text === '启动'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-info disabled init">启动</button>';
						}else if(text === '结束'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-danger disabled finish">结束</button>';
						}else if(text === '重置'){
							return '<button type="button" data-link="'+ link.linkId +'" class="btn btn-warning reset">重置</button>';
						}
						break;
					}
					}
				}
				
				
				var initAllLink = function(){
					var url = contextPath + '/link/all';
					$.get(url, function(data){
						var html = "";
						console.log(data);
						$.each(data, function(i, n){
							var tr = '<tr id="allLink">'+
										'<td><button type="button" class="btn btn-link">'+ n.linkName +'</button></td>'+
										'<td><p>'+ stateName(n.linkState) +'</p></td>'+
										'<td>'+ btn(n, '启动') +'</td>'+
										'<td>'+ btn(n, '结束') +'</td>'+
										'<td>'+ btn(n, '重置') +'</td>'+
									 '</tr>';
							html += tr;
						});
						$('#allLink').html(html);
						
						// 点击按钮后执行的回调函数
						function callBack($this, defaultClass, data){
							var text = $this.text();
							console.log(text);
			    			var changeClass = '';
			    			if(data.returnCode === 1){
			    				changeClass = 'btn-success';
			    			}else{
			    				changeClass = 'btn-danger';
			    			}
			    			$this.text(data.resultMsg).removeClass(defaultClass).addClass(changeClass);
			    			setTimeout(function(){$this.text(text).removeClass(changeClass).addClass(defaultClass);}, 500);
			    			setTimeout(function(){initAllLink();}, 900);
						}
						
						function doChangeLink($this, defaultClass, method){
							$this.addClass('disabled');
							var linkId = $this.data('link');
							var url = contextPath + '/link/' + method;
							$.post(url, {linkId: linkId}, function(data){
								$this.removeClass('disabled');
								callBack($this, defaultClass, data);
							})
						}
						
						$('.init').click(function(){
							doChangeLink($(this), 'btn-info', 'init');
						});
						
						$('.finish').click(function(){
							doChangeLink($(this), 'btn-danger', 'finish');
						});
						
						$('.reset').click(function(){
							doChangeLink($(this), 'btn-warning', 'reset');
						});
						
					});
				};
				
				// 初始化当前环节
				initAllLink();
				
				
				$('#partBtn').click(function(e){
		    		e.preventDefault();
		    		var $this = $(this);
		    		var partnum = $('#partnum').val();
		    		var url = contextPath + '/center/pick/num';
		    		$.post(url, {partnum: partnum}, function(data){
		    			callBack($this, data);
		    		});
		    	});
		    	
		    	$('#startBtn').click(function(e){
		    		var $this = $(this);
		    		var url = contextPath + '/center/pick/start';
		    		$.post(url, function(data){
		    			callBack($this, data);
		    		});
		    	});
		    	
		    	$('#endBtn').click(function(e){
		    		var $this = $(this);
		    		var url = contextPath + '/center/pick/end';
		    		$.post(url, function(data){
		    			callBack($this, data);
		    		});
		    	});
		    	
		    	$('#commitBtn').click(function(e){
		    		var $this = $(this);
		    		var url = contextPath + '/center/pick/commit';
		    		if(confirm("确认提交后，本环节将不能继续加人。是否确认？")){
		        		$.post(url, function(data){
		        			callBack($this, data);
		        		});
		    		}
		    	});
		    	
		    	$('#linkBtn').click(function(e){
		    		var $this = $(this);
		    		var url = contextPath + '/link/start';
		    		$.post(url, function(data){
		    			callBack($this, data);
		    		});
		    	});
				
				
			})(jQuery);
		
		
		</script>

   	</body>
</html>
