<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="utf-8">
<!-- Loading Bootstrap -->
<link href="resources/Flat-UI/css/vendor/bootstrap.min.css"
	rel="stylesheet">
<!-- Loading Flat UI -->
<link href="resources/Flat-UI/css/flat-ui.min.css" rel="stylesheet">

<%
	// 当前选取人员个数
	String partnum = request.getParameter("partnum");
	// 总的可以被选择的人数
	String allow = request.getParameter("allow");
%>

</head>
<body>
	<div class="container-fluid">
		<h1>
			当前选择参与人员：<%=partnum %>，最大可参与人员：<%=allow %>
		</h1>
		<div id="p1">
			<table id="tr1" class="table">
				<tr>
					<th>编号</th>
					<th>能够参与摇一摇的人员</th>
				</tr>
			</table>
		</div>
	</div>

	<!-- jQuery (necessary for Flat UI's JavaScript plugins) -->
	<script src="resources/Flat-UI/js/vendor/jquery.min.js"></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="resources/Flat-UI/js/flat-ui.min.js"></script>

	<script type="text/javascript">
		
		var partnum = parseInt(<%=partnum %>, 10)
		var allow = parseInt(<%=allow %>, 10)
		
		// 当前环节可参与抽奖的人数
		var _num = partnum > allow ? allow : partnum;
	
		init();
		
		function init() {
			$.post("participant/current/participants", function(data) {
				var html = "";
				for (var i = 0; i < data.length; i++) {
					var obj = {};
					obj.participantId = data[i].participantId;
					obj.participantName = data[i].participantName;
					list.push(obj);
					html += '<tr id="'+ obj.participantId +'"><td>'
							+ obj.participantId + '</td><td>'
							+ obj.participantName + '</td></tr>';
				}
				$('#tr1').append(html);
			});
		}

		setInterval("redirect()", 1000);
		function redirect() {
			$.get('center/getCommand', function(data) {
				if (data.type == 'NULL') {
					// 空指令，不做任何事情
				} else if (data.type == "INIT_POOL") {
					$.post("link/initPool");
				} else if (data.type == 'REDIRECT') {
					window.open(data.url, "_self");
				} else if (data.type == 'PICK_START') {
					startCommand();
				} else if (data.type == 'PICK_END') {
					endCommand();
				}
			});
		}
		
		var list = [];
		var list2 = [];

		function startCommand() {
			for (var i = 0; i < _num; i++) {
				var num = Math.floor(Math.random() * (list.length));
				var obj = {};
				obj.participantId = list[num].participantId;
				obj.participantName = list[num].participantName;
				list2.push(obj);
				list.pop(list[num]);
			}
		}
		function endCommand() {
			var ids = "";
			for (var i = 0; i < list2.length; i++) {
				var obj = list2[i];
				var id = obj.participantId;
				$('#' + id).addClass('info');
				ids += list2[i].participantId + ",";
			}
			console.log(ids);
			$.post("participant/current/addPickParticipant", {
				"ids" : ids
			}, function(data) {
			});
		}
	</script>


</body>
</html>