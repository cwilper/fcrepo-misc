<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<title>Fedora CloudSync</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<link rel="shortcut icon" href="static/favicon.ico"/>

<link rel="stylesheet" type="text/css" href="static/style.css"/>
<link rel="stylesheet" type="text/css" href="static/jquery-ui-1.8.12.custom.css"/>

<script type="text/javascript" src="static/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="static/jquery-ui-1.8.12.custom.min.js"></script>

<script type="text/javascript" src="static/json2.js"></script>

<script type="text/javascript" src="static/cloudsync-client.js"></script>

<script type="text/javascript"><!--

var service = new CloudSyncClient(document.location.href + "api/rest/");

$(function() {

  // initialize ui elements

  $(".expandable").accordion({collapsible: true, active: false});

  $("#button-Logout").button({
    icons: { primary: "ui-icon-power" }
  });

  $("#button-Logout").click(
    function() {
      document.location = 'j_spring_security_logout';
    }
  );


  $("#tabs" ).tabs();

  $("#button-NewTask").button({
    icons: { primary: "ui-icon-plus" }
  });

  $("#button-NewTask").click(
    function() {
      $("#dialog-NewTask").dialog("open");
    }
  );

  $("#button-NewObjectSet").button({
    icons: { primary: "ui-icon-plus" }
  });

  $("#button-NewObjectSet").click(
    function() {
      $("#dialog-NewObjectSet").dialog("open");
    }
  );

  $("#button-NewObjectStore").button({
    icons: { primary: "ui-icon-plus" }
  });

  $("#button-NewObjectStore").click(
    function() {
      $("#dialog-NewObjectStore").dialog("open");
    }
  );

  $("#dialog-NewTask").dialog({
    autoOpen: false,
    width: 550,
    modal: true,
    buttons: {
      Ok: function() {
        $(this).dialog("close");
      }
    }
  });
  $("#dialog-NewObjectSet").dialog({
    autoOpen: false,
    width: 550,
    modal: true,
    buttons: {
      Ok: function() {
        $(this).dialog("close");
      }
    }
  });
  $("#dialog-NewObjectStore").dialog({
    autoOpen: false,
    width: 550,
    modal: true,
    buttons: {
      Ok: function() {
        $(this).dialog("close");
      }
    }
  });

  service.getCurrentUser(function(data, status, x) {
    $("#username").text(data.user.name);
            //JSON.stringify(data[0]));
  });

});

//--></script>

</head>
<body>

<div id="title">
  <a href="test.jsp"><img border="0" src="static/logo.png"/></a>
</div>

<div id="whoami">
  <em>Logged in as <span id="username">..</span></em><br/>
  <button id="button-Logout">Logout</button>
</div>

<div id="tabs">

  <ul>
    <li><a href="#tab-Tasks">Tasks</a></li>
    <li><a href="#tab-ObjectSets">Sets</a></li>
    <li><a href="#tab-ObjectStores">Stores</a></li>
  </ul>
  <div id="tab-Tasks">
    <button id="button-NewTask" style="float:right">New Task</button>
    <div style="clear: both; padding-top: 10px">
      <hr size="1" style="color: #dddddd"/>
        <h3>Active</h3>
        <dir>
            None.
        </dir>
        <h3>Scheduled</h3>
        <dir>
            None.
        </dir>
        <h3>Completed Logs</h3>
        <dir>
            None.
        </dir>
    </div>
    <p style="text-align: right;">
        <input type="checkbox"/>
        Auto-delete completed logs; keep the latest
        <select>
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
            <option value="100">100</option>
        </select>
    </p>
  </div>
  <div id="tab-ObjectSets">
    <button id="button-NewObjectSet" style="float:right">New Set</button>
    <div style="clear: both; padding-top: 10px; padding-bottom: 10px;">
      <hr size="1" style="color: #dddddd"/>
      <h3>Built-in</h3>
        <dir>
        <div class="expandable">
            <h3><a href="#">All objects, including inline XML (X) datastreams only</a></h3>
            <div>Here's the previously hidden text!</div>
        </div>
        <div class="expandable">
            <h3><a href="#">All objects, including inline XML (X) and managed (M) datastreams</a></h3>
            <div>Here's the previously hidden text!</div>
        </div>
      <div class="expandable">
          <h3><a href="#">All objects, including inline XML (X), managed (M), and external (E) datastreams</a></h3>
          <div>Here's the previously hidden text!</div>
      </div>
        </dir>
      <h3>Custom</h3>
        <dir>
        None.
        </dir>
    </div>
  </div>
  <div id="tab-ObjectStores">
    <button id="button-NewObjectStore" style="float:right">New Store</button>
    <div style="clear: both; padding-top: 10px; padding-bottom: 10px;">
      <hr size="1" style="color: #dddddd"/>
      <h3>DuraCloud</h3>
        <dir>None.</dir>
      <h3>Fedora</h3>
        <dir>None.</dir>
    </div>
  </div>

</div>

<div id="bottom">
</div>

<div class="ui-helper-hidden" id="dialog-NewTask">
Here's where you would create a new Task.
</div>

<div class="ui-helper-hidden" id="dialog-NewObjectSet">
Here's where you would create a new Object Set.
</div>

<div class="ui-helper-hidden" id="dialog-NewObjectStore">
Here's where you would create a new Object Store.
</div>

</body>
</html>
