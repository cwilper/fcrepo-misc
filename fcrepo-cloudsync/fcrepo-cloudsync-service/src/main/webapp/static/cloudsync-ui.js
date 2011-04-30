var service = new CloudSyncClient(document.location.href + "api/rest/");

function refreshTasks() {
  service.listTasks(function(data) {
    doSection(data.tasks, "task", "tasks-active", getActiveTaskHtml);
    doSection(data.tasks, "task", "tasks-scheduled", getScheduledTaskHtml);
  });
  service.listTaskLogs(function(data) {
    doSection(data.tasklogs, "tasklog", "tasks-completed", getTaskLogHtml);
  });
}

function refreshSets() {
  service.listObjectSets(function(data) {
    doSection(data.objectsets, "objectset", "sets-built-in", getBuiltInSetHtml);
    doSection(data.objectsets, "objectset", "sets-custom", getCustomSetHtml);
  });
}

function refreshStores() {
  service.listObjectStores(function(data) {
    doSection(data.objectstores, "objectstore", "stores-duracloud", getDuraCloudStoreHtml);
    doSection(data.objectstores, "objectstore", "stores-fedora", getFedoraStoreHtml);
  });
}

function getActiveTaskHtml(task) {
  return "This area will allow you to pause, cancel, and view details about this active task";
}

function getScheduledTaskHtml(task) {
  return "This area will allow you to modify, delete, and view details about this scheduled task";
}

function getTaskLogHtml(taskLog) {
  return "This area will allow you to delete and view details about this completed task";
}

function getBuiltInSetHtml(set) {
  return "This area will allow you to view details about this built-in set";
}

function getCustomSetHtml(set) {
  return "This area will allow you to modify, delete, and view details about this custom set";
}

function getDuraCloudStoreHtml(set) {
  return "This area will allow you to modify, delete, and view details about this DuraCloud-based store";
}

function getFedoraStoreHtml(set) {
  return "This area will allow you to modify, delete, and view details about this Fedora-based store";
}

function doSection(items, itemType, sectionName, itemHtmlGetter) {
  var html = "";
  var count = 0;
  $.each(items, function(index, item) {
    count++;
    var itemData = item[itemType];
    html += getExpandable(
        "Name of " + itemType + " #" + count,
        itemHtmlGetter(itemData));
  });
  if (count > 0) {
    $("#" + sectionName).html(html);
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

  $(".button-Reload").button({
    icons: { primary: "ui-icon-arrowrefresh-1-e" }//,
  });

  $("#tasks .button-Reload").click(function() { refreshTasks(); });
  $("#sets .button-Reload").click(function() { refreshSets(); });
  $("#stores .button-Reload").click(function() { refreshStores(); });

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