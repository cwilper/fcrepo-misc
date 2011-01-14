package com.github.cwilper.fcrepo.dto.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Unit tests for <code>FedoraObject</code>.
 */
public class FedoraObjectTest extends FedoraDTOTest {

    @Override
    Object[] getEqualInstances() {
        return new Object[] {
                new FedoraObject().createdDate(new Date(0)),
                new FedoraObject().createdDate(new Date(0)),
                new FedoraObject().lastModifiedDate(new Date(1)),
                new FedoraObject().lastModifiedDate(new Date(1))
        };
    }

    @Override
    Object[] getNonEqualInstances() {
        return new Object[] {
                new FedoraObject(),
                new FedoraObject().createdDate(new Date(0)),
                new FedoraObject().createdDate(new Date(0)),
                new FedoraObject().createdDate(new Date(1)),
                new FedoraObject().lastModifiedDate(new Date(0)),
                new FedoraObject().lastModifiedDate(new Date(1))
        };
    }

    @Test
    public void copy() {
        FedoraObject o1 = new FedoraObject()
                .createdDate(new Date(0))
                .lastModifiedDate(new Date(0))
                .putDatastream(new Datastream("a"));
        FedoraObject o2 = o1.copy();
        Assert.assertEquals(o1, o2);
        Assert.assertNotSame(o1, o2);
        o1.createdDate(new Date(1));
        System.out.println(o1.createdDate().getTime());
        System.out.println(o2.createdDate().getTime());
        Assert.assertFalse(o1.equals(o2));
        o2 = o1.copy();
        Assert.assertEquals(o1, o2);
        o1.lastModifiedDate(new Date(1));
        Assert.assertFalse(o1.equals(o2));
        o2 = o1.copy();
        Assert.assertEquals(o1, o2);
        o1.putDatastream(new Datastream("b"));
        Assert.assertFalse(o1.equals(o2));
        o2 = o1.copy();
        Assert.assertEquals(o1, o2);
        o1.datastreams().put("c", new Datastream("c"));
        Assert.assertFalse(o1.equals(o2));
    }

    @Test
    public void pidField() {
        checkStringField(new FedoraObject(), "pid");
    }

    @Test
    public void labelField() {
        checkStringField(new FedoraObject(), "label");
    }

    @Test
    public void ownerIdField() {
        checkStringField(new FedoraObject(), "ownerId");
    }

    @Test
    public void stateField() {
        FedoraObject o = new FedoraObject();
        Assert.assertNull(o.state());
        o.state(State.ACTIVE);
        Assert.assertEquals(State.ACTIVE, o.state());
    }

    @Test
    public void createdDateField() {
        checkDateField(new FedoraObject(), "createdDate");
    }

    @Test
    public void lastModifiedDateField() {
        checkDateField(new FedoraObject(), "lastModifiedDate");
    }

    @Test
    public void putDatastream() {
        FedoraObject o = new FedoraObject();
        // starts empty
        Assert.assertEquals(0, o.datastreams().keySet().size());
        Datastream ds = new Datastream("a");
        o.putDatastream(ds);
        // now has one datastream
        Assert.assertEquals(1, o.datastreams().keySet().size());
        // ...and it's this one
        Assert.assertEquals(ds, o.datastreams().get(ds.id()));
    }

    @Test (expected=NullPointerException.class)
    public void putDatastreamNull() {
        new FedoraObject().putDatastream(null);
    }

    @Test
    public void putDatastreamViaMap() {
        FedoraObject o = new FedoraObject();
        Datastream ds = new Datastream("a");
        o.datastreams().put(ds.id(), ds);
        // now has one datastream
        Assert.assertEquals(1, o.datastreams().keySet().size());
        // ...and it's this one
        Assert.assertEquals(ds, o.datastreams().get(ds.id()));
    }

    @Test (expected=NullPointerException.class)
    public void putDatastreamViaMapNullKey() {
        new FedoraObject().datastreams().put(null, new Datastream("a"));
    }

    @Test (expected=NullPointerException.class)
    public void putDatastreamViaMapNullValue() {
        new FedoraObject().datastreams().put("a", null);
    }

    @Test (expected=IllegalArgumentException.class)
    public void putDatastreamViaMapWrongId() {
        FedoraObject o = new FedoraObject();
        Datastream ds = new Datastream("a");
        o.datastreams().put("b", ds);
    }

    @Test
    public void datastreamOrdering() {
        FedoraObject o = new FedoraObject();
        Datastream ds1 = new Datastream("a");
        Datastream ds2 = new Datastream("b");
        Datastream ds3 = new Datastream("c");
        o.putDatastream(ds3);
        o.putDatastream(ds1);
        o.putDatastream(ds2);
        Assert.assertEquals(3, o.datastreams().keySet().size());
        Assert.assertEquals(ds1.id(), o.datastreams().firstKey());
        Assert.assertEquals(ds3.id(), o.datastreams().lastKey());
    }

}
