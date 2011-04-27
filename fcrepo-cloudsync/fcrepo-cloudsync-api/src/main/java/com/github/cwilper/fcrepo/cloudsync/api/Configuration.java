package com.github.cwilper.fcrepo.cloudsync.api;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="configuration")
public class Configuration {

    public Integer keepSysLogDays;
    public Integer keepTaskLogDays;

}
