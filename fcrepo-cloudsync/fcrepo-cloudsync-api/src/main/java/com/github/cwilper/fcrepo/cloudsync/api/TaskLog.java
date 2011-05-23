package com.github.cwilper.fcrepo.cloudsync.api;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement(name="tasklog")
public class TaskLog {

    public static final String INCOMPLETE = "Incomplete";
    public static final String SUCCEEDED = "Succeeded";
    public static final String FAILED = "Failed";
    public static final String CANCELED = "Canceled";

    private String id;
    private String taskId;
    private String resultType;
    private Date startDate;
    private Date finishDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
