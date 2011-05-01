var service = new CloudSyncClient(document.location.href + "api/rest/");

function refreshTasks() {
  service.listTasks(function(data) {
//    doSection(data.tasks, "Active Task", "tasks-active", getActiveTaskHtml);
    doSection(data.tasks, "On-demand Task", "tasks-ondemand", getOnDemandTaskHtml);
    doSection(data.tasks, "Scheduled Task", "tasks-scheduled", getScheduledTaskHtml);
  });
  service.listTaskLogs(function(data) {
    doSection(data.tasklogs, "Completed Task Record", "tasks-completed", getTaskLogHtml);
  });
}

function refreshSets() {
  service.listObjectSets(function(data) {
    doSection(data.objectsets, "Built-in Set", "sets-built-in", getBuiltInSetHtml);
    doSection(data.objectsets, "Custom Set", "sets-custom", getCustomSetHtml);
  });
}

function refreshStores() {
  service.listObjectStores(function(data) {
    doSection(data.objectstores, "DuraCloud-based Store", "stores-duracloud", getDuraCloudStoreHtml);
    doSection(data.objectstores, "Fedora-based Store", "stores-fedora", getFedoraStoreHtml);
  });
}

function getActiveTaskHtml(item) {
  var html = "";
  html += "<div class='item-actions'>";
  html += "  <button>Pause</button>";
  html += "  <button>Abort</button>";
  html += "</div>";
  html += "<div class='item-attributes'>Attributes:";
  $.each(item, function(key, value) {
    html += "<br/>" + key + ": " + value;
  });
  html += "</div>";
  return html;
}

function getOnDemandTaskHtml(item) {
  var html = "";
  html += "<div class='item-actions'>";
  html += "  <button class='button-pauseTask'>Pause</button>";
  html += "  <button class='button-abortTask'>Abort</button>";
  html += "  <button class='button-runTask'>Run</button>";
  html += "  <button class='button-editTask'>Edit</button>";
  html += "  <button class='button-deleteTask'>Delete</button>";
  html += "</div>";
  html += "<div class='item-attributes'>Attributes:";
  $.each(item, function(key, value) {
    html += "<br/>" + key + ": " + value;
  });
  html += "</div>";
  return html;
}

function getScheduledTaskHtml(item) {
  var html = "";
  html += "<div class='item-actions'>";
  html += "  <button class='button-pauseTask'>Pause</button>";
  html += "  <button class='button-abortTask'>Abort</button>";
  html += "  <button class='button-runTask'>Run</button>";
  html += "  <button class='button-editTask'>Edit</button>";
  html += "  <button class='button-deleteTask'>Delete</button>";
  html += "</div>";
  html += "<div class='item-attributes'>Attributes:";
  $.each(item, function(key, value) {
    html += "<br/>" + key + ": " + value;
  });
  html += "</div>";
  return html;
}

function getTaskLogHtml(item) {
  var html = "";
  html += "<div class='item-actions'>";
  html += "  <button>View Log</button>";
  html += "</div>";
  html += "<div class='item-attributes'>Attributes:";
  $.each(item, function(key, value) {
    html += "<br/>" + key + ": " + value;
  });
  html += "</div>";
  return html;
}

function getBuiltInSetHtml(item) {
  var html = "";
  html += "<div class='item-actions'>";
  html += "  <button>Test</button>";
  html += "</div>";
  html += "<div class='item-attributes'>Attributes:";
  $.each(item, function(key, value) {
    html += "<br/>" + key + ": " + value;
  });
  html += "</div>";
  return html;
}

function getCustomSetHtml(item) {
  var html = "";
  html += "<div class='item-actions'>";
  html += "  <button>Test</button>";
  html += "  <button>Edit</button>";
  html += "  <button>Delete</button>";
  html += "</div>";
  html += "<div class='item-attributes'>Attributes:";
  $.each(item, function(key, value) {
    html += "<br/>" + key + ": " + value;
  });
  html += "</div>";
  return html;
}

function getDuraCloudStoreHtml(item) {
  var html = "";
  html += "<div class='item-actions'>";
  html += "  <button>Count Objects</button>";
  html += "  <button>Edit</button>";
  html += "  <button>Delete</button>";
  html += "</div>";
  html += "<div class='item-attributes'>Attributes:";
  $.each(item, function(key, value) {
    html += "<br/>" + key + ": " + value;
  });
  html += "</div>";
  return html;
}

function getFedoraStoreHtml(item) {
  var html = "";
  html += "<div class='item-actions'>";
  html += "  <button>Count Objects</button>";
  html += "  <button>Edit</button>";
  html += "  <button>Delete</button>";
  html += "</div>";
  html += "<div class='item-attributes'>Attributes:";
  $.each(item, function(key, value) {
    html += "<br/>" + key + ": " + value;
  });
  html += "</div>";
  return html;
}

function doSection(items, itemType, sectionName, itemHtmlGetter) {
  var html = "";
  var count = 0;
  $.each(items, function(index, item) {
    count++;
    html += getExpandable(
        "Name of " + itemType + " #" + count,
        itemHtmlGetter(item));
  });
  if (count > 0) {
    $("#" + sectionName).html(html);
    $("#" + sectionName + " .item-actions button").button();
    $("#" + sectionName + " .item-actions .button-pauseTask").button({disabled: true});
    $("#" + sectionName + " .item-actions .button-abortTask").button({disabled: true});
    $("#" + sectionName + " .expandable").accordion({collapsible: true, active: false});
  } else {
    $("#" + sectionName).html("None.");
  }
}

function getExpandable(title, body) {
  var html = "";
  html += "<div class='expandable'>";
  html += "  <h3><a href='#'>" + title + "</a></h3>";
  html += "  <div class='expandable-body'>" + body + "</div>";
  html += "</div>";
  return html;
}

var loadedTasks = false;
var loadedSets = false;
var loadedStores = false;

$(function() {

  // initialize ui elements

  $(".button-Refresh").button({
    icons: { primary: "ui-icon-arrowrefresh-1-e" }//,
  });

  $("#tasks .button-Refresh").click(function() { refreshTasks(); });
  $("#sets .button-Refresh").click(function() { refreshSets(); });
  $("#stores .button-Refresh").click(function() { refreshStores(); });

  $("#button-Logout").button({
    icons: { primary: "ui-icon-power" }
  });

  $("#button-Logout").click(
    function() {
      document.location = 'j_spring_security_logout';
    }
  );


  $("#tabs").tabs({
    show: function(event, ui) {
      if (ui.index == 0 && !loadedTasks) {
        refreshTasks();
        loadedTasks = true;
      } else if (ui.index == 1 && !loadedSets) {
        refreshSets();
        loadedSets = true;
      } else if (ui.index == 2 && !loadedStores) {
        loadedStores = true;
        refreshStores();
      }
    }
  });

  $("#button-NewTask").button({
    icons: { primary: "ui-icon-plus" }
  });

  $("#button-NewTask").click(
    function() {
      $("#dialog-NewTask").dialog("open");
    }
  );

  $("#button-NewSet").button({
    icons: { primary: "ui-icon-plus" }
  });

  $("#button-NewSet").click(
    function() {
      $("#dialog-NewSet").dialog("open");
    }
  );

  $("#button-NewStore").button({
    icons: { primary: "ui-icon-plus" }
  });

  $("#button-NewStore").click(
    function() {
      $("#dialog-NewStore").dialog("open");
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
  $("#dialog-NewSet").dialog({
    autoOpen: false,
    width: 550,
    modal: true,
    buttons: {
      Ok: function() {
        $(this).dialog("close");
      }
    }
  });
  $("#dialog-NewStore").dialog({
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
  });

  //refreshAll();

});