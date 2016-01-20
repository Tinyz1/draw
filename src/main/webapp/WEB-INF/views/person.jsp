<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String contextPath = pageContext.getServletContext().getContextPath();
%>
<html>
<head>
<meta charset="utf-8">
<!-- Loading Bootstrap -->
<link
	href="<%=contextPath%>/resources/Flat-UI/css/vendor/bootstrap.min.css"
	rel="stylesheet">
<!-- Loading Flat UI -->
<link href="<%=contextPath%>/resources/Flat-UI/css/flat-ui.min.css"
	rel="stylesheet">

<title>参与人员查询</title>
</head>
<body>

	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<p class="bg-primary" style="padding: 10px;">参与人员查询</p>
			</div>
		</div>
		<div>
			<form class="form-inline">
				<div class="form-group">
					<label class="checkbox-inline"> 
					<select class="form-control" name="prizeType" id="select">
							<option selected value="请选择环节">请选择环节</option>
					</select>
					</label>
				</div>
				<button type="button" class="btn btn-warning" id="submit">查询</button>
			</form>
		</div>
		<br>
		<div class="row">
			<div class="col-md-12">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>环节名称</th>
							<th>姓名</th>
							<th>状态</th>
						</tr>
					</thead>
						
					<tbody id="allLink"></tbody>
				</table>
				<hr>
			</div>
		</div>
	</div>

	<script
		src="<%=contextPath%>/resources/Flat-UI/js/vendor/jquery.min.js"></script>
	<script src="<%=contextPath%>/resources/Flat-UI/js/flat-ui.min.js"></script>

	<script type="text/javascript">
		(function($) {
			init();
			function init() {
				var url = '<%=contextPath%>' + '/link/all';
				$.post(url, function(data) {
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<option value="'+data[i].linkId+'">'
								+ data[i].linkName + '</option><br>';
					}
					$("#select").append(html);
				});
			}
			
			
			$('#submit').click(function(){
				var name = $("#name").val();
				var select = $("#select").val() == '请选择环节' ? '' :$("#select").val();
				var url = '<%=contextPath%>' + '/record/query';
				var html = "";
				$.post(url,{"partcipantName":name,"linkId":select}, function(data) {
					if(data.length > 0){
						$.each(data, function(i, n){
							html += '<tr>'+
										'<td>'+n.linkName+'</td>'+
										'<td>'+n.participantName+'</td>'+
										'<td>'+n.state+'</td>'+
										'</tr>';
						});
						$('#allLink').html(html);
					}else{
						alert("没有相关信息");
					}
				})
				
			});
		})(jQuery);
	</script>

</body>
</html>
