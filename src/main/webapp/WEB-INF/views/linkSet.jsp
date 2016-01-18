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
					<p class="bg-primary" style="padding: 10px;">添加抽奖环节</p>
				</div>
			</div>
			
			<div class="row">
				<div class="col-md-12">
					<form role="form" id="link-form">
					
		            	<div class="form-group">
		               		<label class="checkbox-inline">
		              			<input type="text" class="form-control" name="linkName" id="linkName" placeholder="输入抽奖环节名称">
		            		</label>
		            		<label class="checkbox-inline">
							  <input class="form-control" type="text" name="enterNumber" placeholder="进入环节验证码">
							</label>
		            	</div>
		            	
		            	<hr>
		            	
		            	<div class="form-group">
		               		<label class="checkbox-inline">
							  <input class="form-control" type="text" name="prizeName" placeholder="输入奖品名称">
							</label>
							<label class="checkbox-inline">
							  <select class="form-control" name="prizeType">
				                <option selected value="一等奖">一等奖</option>
				                <option value="二等奖">二等奖</option>
				                <option value="三等奖">三等奖</option>
				                <option value="四等奖">四等奖</option>
				                <option value="五等奖">五等奖</option>
				                <option value="六等奖">六等奖</option>
				                <option value="阳光普照奖">阳光普照奖</option>
				                <option value="礼品">礼品</option>
				                <option value="现金奖">现金奖</option>
				              </select>
							</label>
							<label class="checkbox-inline">
							  <input class="form-control" type="number" name="size" placeholder="选择奖品数量">
							</label>
							<label class="checkbox-inline">
							  <a class="btn btn-default btn-add">添加</a>
							</label>
		            	</div>
		            	<button id="btn-commit" type="submit" class="btn btn-primary">确定</button>
		          	</form>
		          	
		          	<hr>
		        </div><!-- /.col-md-12 -->
		    </div><!-- /.row -->

		</div>
		
		<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
	   	<script src="<%=contextPath %>/resources/Flat-UI/js/vendor/jquery.min.js"></script>
	   	<!-- Include all compiled plugins (below), or include individual files as needed -->
	   	<script src="<%=contextPath %>/resources/Flat-UI/js/flat-ui.min.js"></script>

		<script type="text/javascript">
		
			(function($){
				
				var contextPath = '<%=contextPath %>';
				console.log('contextPath->' + contextPath);
				
				$('.btn-add').click(function(){
					var html = '<div class="form-group">'+
				               		'<label class="checkbox-inline">'+
									  '<input class="form-control" type="text" name="prizeName" placeholder="输入奖品名称">'+
									'</label>'+
									'<label class="checkbox-inline">'+
									  '<select class="form-control" name="prizeType">'+
						                '<option selected value="一等奖">一等奖</option>'+
						                '<option value="二等奖">二等奖</option>'+
						                '<option value="三等奖">三等奖</option>'+
						                '<option value="四等奖">四等奖</option>'+
						                '<option value="五等奖">五等奖</option>'+
						                '<option value="六等奖">六等奖</option>'+
						                '<option value="阳光普照奖">阳光普照奖</option>'+
						                '<option value="礼品">礼品</option>'+
						                '<option value="现金奖">现金奖</option>'+
						              '</select>'+
									'</label>'+
									'<label class="checkbox-inline">'+
									  '<input class="form-control" type="number" name="size" placeholder="选择奖品数量">'+
									'</label>'+
				          		'</div>'
			        $(this).parent().parent().before(html);
				});

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
				
				$('#link-form').submit(function(e){
		    		e.preventDefault();
		    		var $this = $(this);
		    		$this.addClass('disabled');
		    		var url = contextPath + '/link/add';
		    		var linkItem = {
		    			linkName: $this.find('[name=linkName]').val(),
		    			enterNumber: $this.find('[name=enterNumber]').val()
		    		};
		    		//console.log(param);
		    		var prizeNames = $this.find('[name=prizeName]');
		    		var prizeItems = []
		    		$.each(prizeNames, function(i, n){
		    			var $n = $(n);
		    			var $type = $n.parent().nextAll().find('[name=prizeType]');
		    			var $num = $n.parent().nextAll().find('[name=size]');
		    			var prizeItem = {
		    				prizeName: $n.val(),
		    				prizeType: $type.val(),
		    				size: $num.val()
		    			}
		    			prizeItems.push(prizeItem);
		    		});
		    		linkItem.prizeItems = prizeItems;
		    		console.log(linkItem);
		    		
		    		$.post(url, {linkItem: JSON.stringify(linkItem)}, function(data){
		    			$this.removeClass('disabled');
		    			callBack($this.find('#btn-commit'), data);
		    		});
		    	
		    	});
		    	
			})(jQuery);
		
		</script>

   	</body>
</html>
