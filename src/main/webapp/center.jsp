<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK" contentType="text/html; charset=UTF-8"%>
<script src="resources/js/jquery-1.10.2.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�齱���ý���</title>
</head>

<body>
<table id="tb1" width="100%" border="1" cellspacing="1" cellpadding="1" height="639">
  <tr>
    <td height="135" colspan="2">
		<h1>&nbsp;&nbsp;����ҳ��</h1>
	</td>
  </tr>
  <tr>
    <td width="21%" height="490" valign="top">
		<div align = "center">
			<h3>�����˵�</h3>
			<button id="����" onclick="config()">�ν���Ա</button>
			<br/>
			<br/>
			<button id="����" onclick="addp()">��������</button>
			<br/>
			<br/>
			<button id="����" onclick="linkcontrl()">���ڿ���</button>
			<br/>
			<br/>
			<button id="�û�" onclick="addnew1()">�����û�</button>
			<br/>
			<br/>
			<button id="�ύ" onclick="tijiao()">������Ա�ύ</button>
			<br/>
			<br/>
		</div>
	</td>
    <td width="79%" valign="top">
		<div id = "����2" align = "center">
			<form action="center/pick/num" method="post">
			<h2  align = "center">�ν���Ա</h2>
      	�ν���Ա������<input type="text" id="123456" name="partnum" />
			<br />
			<br />
			<input type="submit"  value="����" />
			</form>
			</div>
		<div id="����2"  align = "center">
			<form action="prize/addprize" method="post">
			<h2>��������</h2>
			�������֣�<input type="text" name="linkname" />&nbsp;&nbsp;&nbsp;
			��Ʒ������<input type="text" name="prizenum" />&nbsp;&nbsp;&nbsp;
			����������<input type="text" name="provider" />&nbsp;&nbsp;&nbsp;
			 �Ƿ�������˿���<select name="course">
				<option value="2">��</option>
				<option value="1" >��</option>
	  			</select>
			<br />
			<br />
			<input type="submit"  value="����"/>&nbsp;&nbsp;&nbsp;
			<input type="reset" id="button1" value="����" />
			</form>
		</div>
		<div id="����2" align = "center">
		<h1>���ڿ���</h1>
			<input type="submit"  value="���ڿ�ʼ" onclick="startlink()"/>&nbsp;&nbsp;&nbsp;
			<input type="submit"  value="������ֹ"/>&nbsp;&nbsp;&nbsp;
			<input type="submit"  value="���¼��ػ���"/>&nbsp;&nbsp;&nbsp;
		</div>
		<div id="�û�2"  align = "center">
			<form action="<%=request.getContextPath()%>/participant/addnewpart" method="post">
				<h2>�û�ע��</h2>
				�û��ǳƣ�<input type="text" name="name" /> <br />
	  			<input type="submit" value="�ύ">&nbsp;&nbsp;&nbsp;
	  			<input type="reset" value="����"></input>
			</form>
		</div>
		<div id="�ύ2" align = "center">
			<input type="submit"  value="��Ա�ύ" style="width: 200px;height: 40px"/>&nbsp;&nbsp;&nbsp;
		</div>
	</td>
  </tr>
</table>
</body>
</html>
<script type="text/javascript">
//��ʼ������չʾҳ��
window.onload = function(){
	document.getElementById("����2").style.display="none";
	document.getElementById("����2").style.display="none";
	document.getElementById("�û�2").style.display="none";
	document.getElementById("����2").style.display="none";
	document.getElementById("�ύ2").style.display="none";
}
//չʾ���ý���
function config(){
	document.getElementById("����2").style.display="block";
	document.getElementById("����2").style.display="none";
	document.getElementById("�û�2").style.display="none";
	document.getElementById("����2").style.display="none";
	document.getElementById("�ύ2").style.display="none";
}
//չʾ��������
function addp(){
	document.getElementById("����2").style.display="none";
	document.getElementById("����2").style.display="block";
	document.getElementById("�û�2").style.display="none";
	document.getElementById("����2").style.display="none";
	document.getElementById("�ύ2").style.display="none";
 }


//�û�����
function addnew1(){
	document.getElementById("����2").style.display="none";
	document.getElementById("����2").style.display="none";
	document.getElementById("�û�2").style.display="block";
	document.getElementById("����2").style.display="none";
	document.getElementById("�ύ2").style.display="none";
}

//���ڿ���
function linkcontrl(){
	document.getElementById("����2").style.display="none";
	document.getElementById("����2").style.display="none";
	document.getElementById("�û�2").style.display="none";
	document.getElementById("����2").style.display="block";
	document.getElementById("�ύ2").style.display="none";
}

//��Ա�ύ����
function tijiao(){
	document.getElementById("����2").style.display="none";
	document.getElementById("����2").style.display="none";
	document.getElementById("�û�2").style.display="none";
	document.getElementById("����2").style.display="none";
	document.getElementById("�ύ2").style.display="block";
}
//���ڿ�ʼ
function startlink(){
	location.href="/draw/link/start";
}

</script>