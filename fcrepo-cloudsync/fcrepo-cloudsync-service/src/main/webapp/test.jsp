<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>Fedora CloudSync: Test Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<link rel="stylesheet" type="text/css" href="static/style.css"/>
<link rel="stylesheet" type="text/css" href="static/jquery-ui-1.8.12.custom.css"/>

<script type="text/javascript" src="static/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="static/jquery-ui-1.8.12.custom.min.js"></script>

<script type="text/javascript" src="static/json2.js"></script>

<script type="text/javascript" src="static/cloudsync-client.js"></script>

<script type="text/javascript"><!--

var service = new CloudSyncClient("/cloudsync/api/rest/");

$(function() {

});

//
// callbacks for successful ajax calls; for testing only
//

function show(data, status, x) {
  alert("Success!\n\nResponse Code: " + x.status + "\n\n" + x.getAllResponseHeaders() + "\n" + JSON.stringify(data));
}

function showText(data, status, x) {
  alert("Success!\n\nResponse Code: " + x.status + "\n\n" + x.getAllResponseHeaders() + "\n" + data);
}

//--></script>

</head>
<body>

<div>
    <h2>REST API Testing</h2>
    <a href="/cloudsync">Go back</a>
    <p>
    <em>Click a button below to test a REST API operation.</em>
    </p>
    <hr size="1"/>
    <p>
    Configuration:
    <ul>
      <li> <button onclick="service.getConfiguration(show);">getConfiguration</button></li>
      <li> <button onclick="service.updateConfiguration({'configuration':{'keepSysLogDays':10, 'keepTaskLogDays':20}}, show);">updateConfiguration</button></li>
    </ul>
    Users:
    <ul>
      <li> <button onclick="service.createUser({'user':{'name':'value'}}, show);">createUser</button></li>
      <li> <button onclick="service.listUsers(show);">listUsers</button></li>
      <li> <button onclick="service.getUser(2, show);">getUser</button></li>
      <li> <button onclick="service.getCurrentUser(show);">getCurrentUser</button></li>
      <li> <button onclick="service.updateUser(2, {'user':{'name':'value'}}, show);">updateUser</button></li>
      <li> <button onclick="service.deleteUser(2, show);">deleteUser</button></li>
    </ul>
    Tasks:
    <ul>
      <li> <button onclick="service.createTask({'task':{'name':'value'}}, show);">createTask</button></li>
      <li> <button onclick="service.listTasks(show);">listTasks</button></li>
      <li> <button onclick="service.getTask(2, show);">getTask</button></li>
      <li> <button onclick="service.updateTask(2, {'task':{'name':'value'}}, show);">updateTask</button></li>
      <li> <button onclick="service.deleteTask(2, show);">deleteTask</button></li>
    </ul>
    Object Sets:
    <ul>
      <li> <button onclick="service.createObjectSet({'objectset':{'name':'value'}}, show);">createObjectSet</button></li>
      <li> <button onclick="service.listObjectSets(show);">listObjectSets</button></li>
      <li> <button onclick="service.getObjectSet(2, show);">getObjectSet</button></li>
      <li> <button onclick="service.updateObjectSet(2, {'objectset':{'name':'value'}}, show);">updateObjectSet</button></li>
      <li> <button onclick="service.deleteObjectSet(2, show);">deleteObjectSet</button></li>
    </ul>
    Object Stores:
    <ul>
      <li> <button onclick="service.createObjectStore({'objectstore':{'name':'My Object Store', 'type':'fedora', 'data': '{&quot;url&quot;: &quot;urn:example.org&quot;, &quot;username&quot;: &quot;bob&quot, &quot;password&quot;: &quot;pass&quot;}'}}, show);">createObjectStore</button></li>
      <li> <button onclick="service.listObjectStores(show);">listObjectStores</button></li>
      <li> <button onclick="service.getObjectStore(2, show);">getObjectStore</button></li>
      <li> <button onclick="service.queryObjectStore(2, 2, -1, 0, show);">queryObjectStore</button></li>
      <li> <button onclick="service.updateObjectStore(2, {'objectstore':{'name':'value'}}, show);">updateObjectStore</button></li>
      <li> <button onclick="service.deleteObjectStore(2, show);">deleteObjectStore</button></li>
    </ul>
    System Logs:
    <ul>
      <li> <button onclick="service.listSystemLogs(show);">listSystemLogs</button></li>
      <li> <button onclick="service.getSystemLog(2, show);">getSystemLog</button></li>
      <li> <button onclick="service.getSystemLogContent(2, showText)">getSystemLogContent</button></li>
      <li> <button onclick="service.deleteSystemLog(2, show);">deleteSystemLog</button></li>
    </ul>
    Task Logs:
    <ul>
      <li> <button onclick="service.listTaskLogs(show);">listTaskLogs</button></li>
      <li> <button onclick="service.getTaskLog(2, show);">getTaskLog</button></li>
      <li> <button onclick="service.getTaskLogContent(2, showText)">getTaskLogContent</button></li>
      <li> <button onclick="service.deleteTaskLog(2, show);">deleteTaskLog</button></li>
    </ul>
    DuraCloud:
    <ul>
      <li> <button onclick="service.listProviderAccounts('https://example.org/durastore', 'exampleuser', 'examplepass', show);">listProviderAccounts</button></li>
      <li> <button onclick="service.listSpaces('https://example.org/durastore', 'exampleuser', 'examplepass', '1', show);">listSpaces</button></li>
    </ul>
    </p>
</div>

</body>
</html>
