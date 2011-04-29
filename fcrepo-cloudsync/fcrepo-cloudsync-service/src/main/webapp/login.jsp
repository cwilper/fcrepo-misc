<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>Fedora CloudSync: Login</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<link rel="shortcut icon" href="static/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="static/style.css"/>
<link rel="stylesheet" type="text/css" href="static/jquery-ui-1.8.12.custom.css"/>

<script type="text/javascript" src="static/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="static/jquery-ui-1.8.12.custom.min.js"></script>
<script type="text/javascript"><!--

$(function() {
  $("#button-login").button();
  document.f.j_username.focus();
});

//--></script>

</head>

<body>

<form name='f' action='/cloudsync/j_spring_security_check' method='POST'>
<center>
<p></p>
<div id="login">
  <p></p>
  <table cellpadding="10">
    <tr><td><img src="static/logo.png"/></td></tr>
    <tr><td align="middle">
      <font color="#999999" size="+1">
        For demo purposes, you may login as admin, password admin.
      </font>
    </td></tr>
    <tr><td align="middle">
      <table cellpadding="5" style="font-size: 16px;">
        <tr><td>Username:</td><td><input type='text' name='j_username' value=''></td></tr>
        <tr><td>Password:</td><td><input type='password' name='j_password'/></td></tr>
      </table>
    </td></tr>
    <tr><td align="middle">
      <button id="button-login" onclick="document.f.submit();">Login &gt;</button>
    </td></tr>
  </table>
</div>
</center>
</form>

</body>
</html>