/**
 * Data Transfer Object classes for the core Fedora persistence entities.
 * <p>
 * The classes in this package are intended primarily to aid in the
 * programmatic construction and inspection of Fedora objects and their
 * constituent entities (datastreams, etc.)
 *
 * <h2><a name="working">Working With DTO Classes</a></h2>
 * All DTO classes follow a few important design principles that developers
 * should be aware of:
 *
 * <h3>Setter Chaining and Getter/Setter Naming</h3>
 * Setters generally return a reference to the object against which the call
 * is made.  In addition, getter and setter method names are not prefixed
 * with <code>get</code> or <code>set</code>.  This style tends to lead to
 * shorter, more writable/readable code, particularly when programmatically
 * constructing DTO instances. Compare:
 * <pre>
 *     //
 *     // Abbreviated/Chaining Style (used by this package)
 *     //
 *     FedoraObject obj = new FedoraObject()
 *             .pid("example:1")
 *             .state(State.ACTIVE)
 *             .label("My Label")
 *             .createdDate(new Date())
 *             .ownerId("bob");
 *     System.out.println(obj.pid() + " - " + obj.label());
 *
 *     //
 *     // Java Bean Style (NOT used by this package)
 *     //
 *     FedoraObject obj = new FedoraObject();
 *     obj.setPid("example:1");
 *     obj.setState(State.ACTIVE);
 *     obj.setLabel("My Label");
 *     obj.setCreatedDate(new Date());
 *     obj.setOwnerId("bob");
 *     System.out.println(obj.getPid() + " - " + obj.getLabel());
 * </pre>
 *
 * <h3>Null Values and String Normalization</h3>
 * All DTO instances generally start with minimal state: most fields begin
 * with <code>null</code> values (or are empty, in the case of collections).
 * Any field that is settable can generally be set to <code>null</code>, and
 * any collection can have all elements removed.  In addition, all string
 * fields are auto-normalized when set, as follows:
 * <ul>
 *   <li> Whitespace is removed from the beginning and end of the string</li>
 *   <li> If the resulting value is the empty string, the value will be
 *        set to <code>null</code>.</li>
 * </ul>
 * Together, these conventions ensure that there is exactly one way of
 * determining that a field has no value (or in the case of collections,
 * members).  This helps to simplify consuming code considerably -- it just
 * has to check for <code>null</code> or <code>.size() == 0</code> as
 * appropriate.
 *
 * <h3>Minimal Validation</h3>
 * This package is generally not concerned with validation of field values.
 * This is a concern best implemented elsewhere (higher level code, xml schema,
 * etc.)
 *
 * <h3>Collection Ordering</h3>
 * Where collections (sets or maps) are used, they are generally sorted to
 * to allow for reasonably deterministic iteration.  For example,
 * <code>FedoraObject.datastreams().keySet()</code> is ordered by datastream
 * id (ascending), and <code>Datastream.versions()</code> is ordered by
 * creation date (descending), then id (ascending).
 */
package com.github.cwilper.fcrepo.dto.core;