FCRepo Flexport
===============

A flexible export utility for Fedora Commons Repositories.

*This project is in the early prototyping stage.  It doesn't really work yet!*

Object Selection
----------------

Users can control the list of objects to export in three ways:

* By listing PIDs verbatim
* By specifying a simple PID wildcard (e.g. demo:\*)
* By specifying an RDF query that returns object URIs.

Datastream Export Rules
-----------------------

For each set of selected objects, users can optionally specify a list of rules
that control how each datastream's content is to be exported.

The default behavior is to export each object such that *Inline XML* and *Managed
Content* are included within the object export package, and all other datastream
content remains as a reference to the original location (a URL).

### Rule Structure ###

Each rule specifies a set of conditions that serve to select the datastreams
to which the rule applies, followed by a list of actions to take with those
datastreams.

The following datastream characteristics may be used for selection:

* ID
* CONTROL\_GROUP
* MIME\_TYPE
* FORMAT\_URI
* MIN\_SIZE
* MAX\_SIZE

The following actions may be specified:

* save-as PATH-TEMPLATE
* use-url URL-TEMPLATE
* use-fedora-url

### Rule Evaluation ###

Rules will be evaluated in order for each datastream until one matches.
If none match, the default behavior will be followed for that datastream.

Usage
-----

    fedora-flexport flexport.conf flexport.spec [ list | dryrun | export ]

Configuration
-------------

### flexport.conf

    fedora.url  = http://localhost:8080/fedora
    fedora.user = fedoraAdmin
    fedora.pass = fedoraAdmin

    export.dir  = /tmp/flexport-test/objects

    # Define any other variables to be used in rules

    my.dir = /tmp/flexport-text/datastreams

### flexport.spec

    # Any number of static PIDs, one per line
    demo:pid1
    demo:pid2

    # Followed by any number of pid patterns, one per line
    demo:*

    # Followed by any number of SPARQL queries, each starting with [sparql-query]
    [sparql-query]
    PREFIX model:  <info:fedora/fedora-system:def/model#>
    PREFIX system: <info:fedora/fedora-system:>
    SELECT ?object
    WHERE {
      ?object
        model:hasModel
          <info:fedora/fedora-system:ContentModel-3.0>.
    }

    # Followed by any number of Datastream Rules, each starting with [ds-rule]
    [ds-rule]
    filter ID            DS or DS2 or DS3
    filter CONTROL_GROUP M
    filter MIME_TYPE     text/plain
    filter FORMAT_URI    urn:pronom:1
    filter MIN_SIZE      0
    filter MAX_SIZE      100000
    action DROP
    action DROP-OLD
    action SAVE-TO       ${my.dir}/${pid.fn}/${ID}.${VERSION_ID}.txt
    action SET-REF       file://${my.dir}/

    # Actions:
    #
    # SAVE-TO: Saves the content of the datastream(s) to the given file
    #          Note: Directories will be created if they don't exist yet.
    #                If the template does not include ${VERSION_ID}, then
    #                only the most recent version of the datastream will be
    #                written.
    # SET-REF: Sets the location of the datastream to the given file:// or
    #          http:// URL.
    #
    # Variables you can use:
    #
    # ${anything}   - Any variable defined in flexport.conf
    # ${pid.fn}     - A filename-safe encoding of the PID
    # ${REF}        - Original datastream location REF value
    # ${REF.n}      - Original datastream location REF value minus the first n chars
    # ${ID}         - Datastream ID
    # ${VERSION_ID} - Datastream Version ID
    #
    # ${var.fn}
