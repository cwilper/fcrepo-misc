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
  <img src="static/logo.png" alt="Fedora CloudSync"/>
</div>

<div id="logout">
  <span id="whoami">Logged in as <span id="username">...</span></span><br/>
  <button id="button-Logout">Logout</button>
</div>

<!-- Tabs -->

<div id="tabs">

  <ul>
    <li><a href="#tasks">Tasks</a></li>
    <li><a href="#sets">Sets</a></li>
    <li><a href="#stores">Stores</a></li>
  </ul>

  <div id="tasks" class="tab">
    <div class="tab-header">
      <button id="button-NewTask">Add</button>
<!--      <button class="button-Refresh">Refresh</button> -->
    </div>
    <div class="tab-body">
      <div class="tab-section">
        <h2>Active</h2>
        <div id="tasks-active" class="tab-section-body">
          ...
        </div>
      </div>
      <div class="tab-section">
        <h2>Idle</h2>
        <div id="tasks-idle" class="tab-section-body">
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
      <button id="button-NewSet">Add</button>
    </div>
    <div class="tab-body">
      <div class="tab-section">
        <h2>PID Patterns</h2>
        <div id="sets-pidpatterns" class="tab-section-body">
          ...
        </div>
      </div>
      <div class="tab-section">
        <h2>PID Lists</h2>
        <div id="sets-pidlists" class="tab-section-body">
          ...
        </div>
      </div>
      <div class="tab-section">
        <h2>Queries</h2>
        <div id="sets-queries" class="tab-section-body">
          ...
        </div>
      </div>
    </div>
  </div>

  <div id="stores" class="tab">
    <div class="tab-header">
      <button id="button-NewStore">Add</button>
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

<!-- Tasks -->

<div class="ui-helper-hidden" id="dialog-NewTask" title="New Task">
  <p>
    What kind of Task do you want to add?
  </p>
  <p>
    <button id="button-NewListTask">List Objects</button>
  </p>
  <p>
    <button id="button-NewCopyTask">Copy Objects</button>
  </p>
</div>

<div class="ui-helper-hidden" id="dialog-NewListTask" title="New List Task">
  <table>
    <tr>
      <td>List</td>
      <td><select id="NewListTask-setId">
        ...
      </select></td>
    </tr>
    <tr>
      <td>in</td>
      <td><select id="NewListTask-storeId">
        ...
      </select></td>
    </tr>
    <tr>
      <td>Task Name</td>
      <td>
        <input id="NewListTask-name" type="text"/>
      </td>
    </tr>
    <tr>
      <td>Run Now</td>
      <td>
        <input type="checkbox" id="NewListTask-runNow" checked="checked"/>
      </td>
    </tr>
  </table>
</div>

<div class="ui-helper-hidden" id="dialog-NewCopyTask" title="New Copy Task">
  <table>
    <tr>
      <td>Copy</td>
      <td><select id="NewCopyTask-setId">
        ...
      </select></td>
    </tr>
    <tr>
      <td>in</td>
      <td><select id="NewCopyTask-queryStoreId">
        ...
      </select></td>
    </tr>
    <tr>
      <td>from</td>
      <td><select id="NewCopyTask-sourceStoreId">
        ...
      </select></td>
    <tr>
      <td>to</td>
      <td><select id="NewCopyTask-destStoreId">
        ...
      </select></td>
    </tr>
    <tr>
      <td>Task Name</td>
      <td>
        <input id="NewCopyTask-name" type="text"/>
      </td>
    </tr>
    <tr>
      <td>Overwrite</td>
      <td>
        <input type="checkbox" id="NewCopyTask-overwrite" checked="checked"/>
      </td>
    </tr>
    <tr>
      <td>Run Now</td>
      <td>
        <input type="checkbox" id="NewCopyTask-runNow" checked="checked"/>
      </td>
    </tr>
  </table>
</div>

<!-- Sets -->

<div class="ui-helper-hidden" id="dialog-NewSet" title="New Set">
  <p>
    What kind of Object Set do you want to add?
  </p>
  <p>
    <button id="button-NewPidPattern">PID Pattern</button>
  </p>
  <p>
    <button id="button-NewPidList">PID List</button>
  </p>
  <p>
    <button id="button-NewQuery">Query</button>
  </p>
</div>

<div class="ui-helper-hidden" id="dialog-NewPidPattern" title="New PID Pattern">
  <table>
    <tr>
      <td>Name</td>
      <td><input id="NewPidPattern-name" type="text" value=""/></td>
    </tr>
    <tr>
      <td>Pattern (* is wildcard)</td>
      <td><input id="NewPidPattern-data" type="text" value=""/></td>
    </tr>
  </table>
</div>

<div class="ui-helper-hidden" id="dialog-NewPidList" title="New PID List">
  <table>
    <tr>
      <td>Name</td>
      <td><input id="NewPidList-name" type="text" value=""/></td>
    </tr>
    <tr>
      <td>PIDs (one per line)</td>
      <td>
        <textarea id="NewPidList-data" rows="10" cols="20"></textarea>
      </td>
    </tr>
  </table>
</div>

<div class="ui-helper-hidden" id="dialog-NewQuery" title="New Query">
  <table>
    <tr>
      <td>Name</td>
      <td><input id="NewQuery-name" type="text" value=""/></td>
    </tr>
    <tr>
      <td>Query Type</td>
      <td><select id="NewQuery-queryType">
        <option>iTQL</option>
        <option selected="selected">SPARQL</option>
      </select></td>
    </tr>
    <tr>
      <td>Query Text</td>
      <td>
        <textarea id="NewQuery-queryText" rows="10" cols="40"></textarea>
      </td>
    </tr>
  </table>
</div>

<!-- Stores -->

<div class="ui-helper-hidden" id="dialog-NewStore" title="New Store">
  <p>
    What kind of Object Store do you want to add?
  </p>
  <p>
    <button id="button-NewDuraCloudStore">DuraCloud-based</button>
  </p>
  <p>
    <button id="button-NewFedoraStore">Fedora-based</button>
  </p>
</div>

<div class="ui-helper-hidden" id="dialog-NewDuraCloudStore" title="New DuraCloud-based Store">
  <table>
    <tr>
      <td>Hostname or URL</td>
      <td><input id="NewDuraCloudStore-url" type="text"/></td>
    </tr>
    <tr>
      <td>DuraCloud Username</td>
      <td><input id="NewDuraCloudStore-username" type="text"/></td>
    </tr>
    <tr>
      <td>DuraCloud Password</td>
      <td><input id="NewDuraCloudStore-password" type="password"/></td>
    </tr>
  </table>
</div>

<div class="ui-helper-hidden" id="dialog-NewDuraCloudStoreStep2" title="New DuraCloud-based Store">
  <p style="text-align: center">
    Storage Provider:<br/>
    <select id="NewDuraCloudStore-providerId">
      <option>...</option>
    </select>
  </p>
  <p style="text-align: center">
    Space:<br/>
    <select id="NewDuraCloudStore-space">
      <option>...</option>
    </select>
  </p>
  <p style="text-align: center">
    Content Id Prefix (Optional):<br/>
    <input id="NewDuraCloudStore-prefix" type="text"/>
  </p>
</div>

<div class="ui-helper-hidden" id="dialog-NewDuraCloudStoreStep3" title="New DuraCloud-based Store">
  <table>
    <tr>
      <td>URL</td>
      <td id="NewDuraCloudStoreStep3-url">...</td>
    </tr>
    <tr>
      <td>Username</td>
      <td id="NewDuraCloudStoreStep3-username">...</td>
    </tr>
    <tr>
      <td>Password</td>
      <td>(Not shown)</td>
    </tr>
    <tr>
      <td>Storage Provider</td>
      <td id="NewDuraCloudStoreStep3-providerName">...</td>
    </tr>
    <tr>
      <td>Space</td>
      <td id="NewDuraCloudStoreStep3-space">...</td>
    </tr>
    <tr>
      <td>Content Id Prefix</td>
      <td id="NewDuraCloudStoreStep3-prefix">...</td>
    </tr>
    <tr>
      <td>Store Name</td>
      <td><input id="NewDuraCloudStoreStep3-name" type="text" size="40" value="..."/></td>
    </tr>
  </table>
</div>

<div class="ui-helper-hidden" id="dialog-NewFedoraStore" title="New Fedora-based Store">
  <table>
    <tr>
      <td>Base URL</td>
      <td><input id="NewFedoraStore-url" type="text" value=""/></td>
    </tr>
    <tr>
      <td>Fedora Username</td>
      <td><input id="NewFedoraStore-username" type="text"/></td>
    </tr>
    <tr>
      <td>Fedora Password</td>
      <td><input id="NewFedoraStore-password" type="password"/></td>
    </tr>
  </table>
</div>

<div class="ui-helper-hidden" id="dialog-NewFedoraStoreStep2" title="New Fedora-based Store">
  <table>
    <tr>
      <td>URL</td>
      <td id="NewFedoraStoreStep2-url">...</td>
    </tr>
    <tr>
      <td>Username</td>
      <td id="NewFedoraStoreStep2-username">...</td>
    </tr>
    <tr>
      <td>Password</td>
      <td>(Not shown)</td>
    </tr>
    <tr>
      <td>Store Name</td>
      <td><input id="NewFedoraStoreStep2-name" type="text" size="40" value="..."/></td>
    </tr>
  </table>
</div>

<!-- Misc -->

<div class="ui-helper-hidden" id="dialog-confirm" title="Are you sure?">
  ...
</div>

</body>
</html>
