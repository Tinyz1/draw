<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK" contentType="text/html; charset=UTF-8"%>
<script src="resources/js/jquery-1.10.2.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>抽奖配置界面</title>
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
			<h3>操作菜单</h3>
			<button id="配置" onclick="config()">参奖人员</button>
			<br/>
			<br/>
			<button id="新增" onclick="addp()">奖项新增</button>
			<br/>
			<br/>
			<button id="环节" onclick="linkcontrl()">环节控制</button>
			<br/>
			<br/>
			<button id="用户" onclick="addnew1()">新增用户</button>
			<br/>
			<br/>
			<button id="提交" onclick="tijiao()">参赛人员提交</button>
			<br/>
			<br/>
		</div>
	</td>
    <td width="79%" valign="top">
		<div id = "配置2" align = "center">
			<form action="center/pick/num" method="post">
			<h2  align = "center">参奖人员</h2>
      	参奖人员个数：<input type="text" id="123456" name="partnum" />
			<br />
			<br />
			<input type="submit"  value="生成" />
			</form>
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
</body>
</html>
<script type="text/javascript">
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