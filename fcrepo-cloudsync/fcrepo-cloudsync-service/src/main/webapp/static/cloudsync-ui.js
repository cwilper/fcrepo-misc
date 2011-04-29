var service = new CloudSyncClient(document.location.href + "api/rest/");

function refreshAll() {
  refreshTasks();
  refreshSets();
  refreshStores();
}

function refreshTasks() {
  service.listTasks(
    function(data) {
      refreshActiveTasks(data.tasks);
      refreshScheduledTasks(data.tasks);
      refreshCompletedTasks(data.tasks);
    }
  );
}

function refreshActiveTasks(tasks) {
  var html = "";
  var count = 0;
  $.each(tasks, function(index, item) {
    count++;
    var task = item.task;
    html += getExpandable(
        "Name of Active Task #" + task.id,
        getActiveTaskHtml(task)
        );
  });
  if (count > 0) {
    $("#tasks-active").html(html);
    $("#tasks-active .expandable").accordion({collapsible: true, active: false});
  } else {
    $("#tasks-active").html("None.");
  }
}

function refreshScheduledTasks(tasks) {
  var html = "";
  var count = 0;
  $.each(tasks, function(index, item) {
    count++;
    var task = item.task;
    html += getExpandable(
      "Name of Scheduled Task #" + task.id,
      getScheduledTaskHtml(task)
    );
  });
  if (count > 0) {
    $("#tasks-scheduled").html(html);
    $("#tasks-scheduled .expandable").accordion({collapsible: true, active: false});
  } else {
    $("#tasks-scheduled").html("None.");
  }
}

function getActiveTaskHtml(task) {
  return "This area will allow you to pause, cancel, and view details about this active task";
}

function getScheduledTaskHtml(task) {
  return "This area will allow you to modify, delete, and view details about this scheduled task";
}

function getExpandable(title, body) {
  var html = "";
  html += "<div class='expandable'>";
  html += "  <h3><a href='#'>" + title + "</a></h3>";
  html += "  <div class='expandable-body'>" + body + "</div>";
  html += "</div>";
  return html;
}

function refreshCompletedLogs(taskList, taskLogList) {
}

function refreshSets() {
  var objectSetList = service.listObjectSets();
  refreshBuiltInSets(objectSetList);
  refreshCustomSets(objectSetList);
}

function refreshBuiltInSets(objectSetList) {

}

function refreshCustomSets(objectSetList) {

}

function refreshStores() {
  var objectStoreList = service.listObjectStores();
  refreshDuraCloudStores(objectStoreList);
  refreshFedoraStores(objectStoreList);
}

function refreshDuraCloudStores(objectStoreList) {

}

function refreshFedoraStores(objectStoreList) {

}

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
            //JSON.stringify(data[0]));
  });

  refreshAll();

});