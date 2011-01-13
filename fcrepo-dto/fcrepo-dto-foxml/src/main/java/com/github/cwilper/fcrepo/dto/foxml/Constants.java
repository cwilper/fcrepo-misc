package com.github.cwilper.fcrepo.dto.foxml;

/** Package-private constants. */
abstract class Constants {

    static final String XML_VERSION = "1.0";
    static final String CHAR_ENCODING = "UTF-8";

    static final int BASE64_LINE_LENGTH = 74;
    static final String LINE_FEED = "\n";

    static final String FOXML_VERSION = "1.1";

    static final String xmlns = "info:fedora/fedora-system:def/foxml#";

    static final String digitalObject = "digitalObject";
    static final String VERSION = "VERSION";
    static final String PID = "PID";

    static final String objectProperties = "objectProperties";
    
    static final String property = "property";
    static final String NAME = "NAME";
    static final String MODEL = "info:fedora/fedora-system:def/model#";
    static final String VIEW = "info:fedora/fedora-system:def/view#";
    static final String STATE_URI = MODEL + "state";
    static final String LABEL_URI = MODEL + "label";
    static final String OWNERID_URI = MODEL + "ownerId";
    static final String CREATEDDATE_URI = MODEL + "createdDate";
    static final String LASTMODIFIEDDATE_URI = VIEW + "lastModifiedDate";
    static final String VALUE = "VALUE";

    static final String datastream = "datastream";
    static final String ID = "ID";
    static final String STATE = "STATE";
    static final String CONTROL_GROUP = "CONTROL_GROUP";
    static final String VERSIONABLE = "VERSIONABLE";

    static final String datastreamVersion = "datastreamVersion";
    static final String ALT_IDS = "ALT_IDS";
    static final String LABEL = "LABEL";
    static final String CREATED = "CREATED";
    static final String MIMETYPE = "MIMETYPE";
    static final String FORMAT_URI = "FORMAT_URI";
    static final String SIZE = "SIZE";

    static final String contentDigest = "contentDigest";
    static final String TYPE = "TYPE";
    static final String DIGEST = "DIGEST";
    
    static final String contentLocation = "contentLocation";
    static final String INTERNALREF_SCHEME = "internal";
    static final String INTERNALREF_TYPE = "INTERNAL_REF";
    static final String URL_TYPE = "URL";
    static final String REF = "REF";

    static final String xmlContent = "xmlContent";

    static final String binaryContent = "binaryContent";
}
