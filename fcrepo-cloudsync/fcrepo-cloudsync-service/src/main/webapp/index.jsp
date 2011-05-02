<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>

<title>Fedora CloudSync</title>

<meta charset="UTF-8"/>
<meta name="application-name" content="Fedora CloudSync"/>

<link rel="shortcut icon" href="static/favicon.ico"/>
<link rel="stylesheet" type="text/css" href="static/style.css"/>
<link rel="stylesheet" type="text/css" href="static/jquery-ui-1.8.12.custom.css"/>

<script type="text/javascript" src="static/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="static/jquery-ui-1.8.12.custom.min.js"></script>
<script type="text/javascript" src="static/json2.js"></script>
<script type="text/javascript" src="static/cloudsync-client.js"></script>
<script type="text/javascript" src="static/cloudsync-ui.js"></script>

</head>

<body>
<div id="title">
  <a href="test.jsp">
    <img src="static/logo.png" alt="Fedora CloudSync"/>
  </a>
</div>

<div id="logout">
  <span id="whoami">Logged in as <span id="username">...</span></span><br/>
  <button id="button-Logout">Logout</button>
</div>

<div id="tabs">

  <ul>
    <li><a href="#tasks">Tasks</a></li>
    <li><a href="#sets">Sets</a></li>
    <li><a href="#stores">Stores</a></li>
  </ul>

  <div id="tasks" class="tab">
    <div class="tab-header">
      <button id="button-NewTask">New</button>
      <button class="button-Refresh">Refresh</button>
    </div>
    <div class="tab-body">
      <div class="tab-section">
        <h2>On-demand</h2>
        <div id="tasks-ondemand" class="tab-section-body">
          ...
        </div>
      </div>
      <div class="tab-section">
        <h2>Scheduled</h2>
        <div id="tasks-scheduled" class="tab-section-body">
          ...
        </div>
      </div>
      <div class="tab-section">
        <h2>Completed</h2>
        <div id="tasks-completed" class="tab-section-body">
          ...
        </div>
      </div>
    </div>
  </div>

  <div id="sets" class="tab">
    <div class="tab-header">
      <button id="button-NewSet">New</button>
      <button class="button-Refresh">Refresh</button>
    </div>
    <div class="tab-body">
      <div class="tab-section">
        <h2>Built-in</h2>
        <div id="sets-built-in" class="tab-section-body">
          ...
        </div>
      </div>
      <div class="tab-section">
        <h2>Custom</h2>
        <div id="sets-custom" class="tab-section-body">
          ...
        </div>
      </div>
    </div>
  </div>

  <div id="stores" class="tab">
    <div class="tab-header">
      <button id="button-NewStore">New</button>
      <button class="button-Refresh">Refresh</button>
    </div>
    <div class="tab-body">
      <div class="tab-section">
        <h2>DuraCloud</h2>
        <div id="stores-duracloud" class="tab-section-body">
          ...
        </div>
      </div>
      <div class="tab-section">
        <h2>Fedora</h2>
        <div id="stores-fedora" class="tab-section-body">
          ...
        </div>
      </div>
    </div>
  </div>

</div>

<div class="ui-helper-hidden" id="dialog-NewTask">
  Here's where you would create a new Task.
</div>

<div class="ui-helper-hidden" id="dialog-NewSet">
  Here's where you would create a new Object Set.
</div>

<div class="ui-helper-hidden" id="dialog-NewStore" title="New Store">
  <p>
    What kind of Store do you want to add?
  </p>
  <p>
    <button id="button-NewDuraCloudStore">DuraCloud</button>
  </p>
  <p>
    <button id="button-NewFedoraStore">Fedora</button>
  </p>
</div>

<div class="ui-helper-hidden" id="dialog-NewDuraCloudStore" title="New DuraCloud Store">
  <table>
    <tr>
      <td>DuraStore URL</td>
      <td><input type="text" value="https://"/></td>
    </tr>
    <tr>
      <td>DuraCloud Username</td>
      <td><input type="text"/></td>
    </tr>
    <tr>
      <td>DuraCloud Password</td>
      <td><input type="password"/></td>
    </tr>
  </table>
</div>

<div class="ui-helper-hidden" id="dialog-NewFedoraStore" title="New Fedora Store">
  <table>
    <tr>
      <td>Fedora Base URL</td>
      <td><input type="text" value="https://"/></td>
    </tr>
    <tr>
      <td>Fedora Username</td>
      <td><input type="text"/></td>
    </tr>
    <tr>
      <td>Fedora Password</td>
      <td><input type="password"/></td>
    </tr>
  </table>
</div>

</body>
</html>
