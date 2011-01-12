package com.github.cwilper.fcrepo.dto.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.SortedSet;

/**
 * Unit tests for <code>Datastream</code>.
 */
public class DatastreamTest extends FedoraDTOTest {

    @Override
    Object[] getEqualInstances() {
        return new Object[] {
                new Datastream("a"),
                new Datastream("a"),
                new Datastream("a").controlGroup(ControlGroup.MANAGED),
                new Datastream("a").controlGroup(ControlGroup.MANAGED)
        };
    }

    @Override
    Object[] getNonEqualInstances() {
        return new Object[] {
                new Datastream("a"),
                new Datastream("b")
        };
    }

    @Test
    public void idFieldNormal() {
        Assert.assertEquals("a", new Datastream("a").id());
    }

    @Test
    public void idFieldNormalization() {
        Assert.assertEquals("a", new Datastream(" a").id());
        Assert.assertEquals("a", new Datastream("a ").id());
    }

    @Test (expected=NullPointerException.class)
    public void idFieldNull() {
        new Datastream(null);
    }

    @Test (expected=NullPointerException.class)
    public void idFieldEmpty() {
        new Datastream("");
    }

    @Test (expected=NullPointerException.class)
    public void idFieldSpace() {
        new Datastream(" ");
    }

    @Test
    public void stateField() {
        Datastream ds = new Datastream("a");
        Assert.assertNull(ds.state());
        ds.state(State.ACTIVE);
        Assert.assertEquals(State.ACTIVE, ds.state());
    }

    @Test
    public void controlGroupField() {
        Datastream ds = new Datastream("a");
        Assert.assertNull(ds.controlGroup());
        ds.controlGroup(ControlGroup.MANAGED);
        Assert.assertEquals(ControlGroup.MANAGED,
                ds.controlGroup());
    }

    @Test
    public void versionableField() {
        Datastream ds = new Datastream("a");
        Assert.assertNull(ds.versionable());
        ds.versionable(true);
        Assert.assertEquals(true, ds.versionable());
    }

    @Test
    public void addVersion() {
        Datastream ds = new Datastream("a");
        // starts empty
        Assert.assertEquals(0, ds.versions().size());
        DatastreamVersion dsv = ds.addVersion(null);
        Assert.assertNotNull(dsv);
        // now has one version
        Assert.assertEquals(1, ds.versions().size());
        // ..and it's this one
        Assert.assertEquals(dsv, ds.versions().first());
        // then add another directly, skipping a version id
        ds.versions().add(new DatastreamVersion("a.2", null));
        // finally add another with a generated id
        // and ensure it has the expected id
        DatastreamVersion dsvLast = ds.addVersion(null);
        Assert.assertEquals("a.3", dsvLast.id());
    }

    @Test
    public void addVersionViaSet() {
        Datastream ds = new Datastream("a");
        DatastreamVersion dsv = new DatastreamVersion("a.0", null);
        ds.versions().add(dsv);
        // now has one version
        Assert.assertEquals(1, ds.versions().size());
        // ..and it's this one
        Assert.assertEquals(dsv, ds.versions().first());
    }
    
    @Test
    public void datastreamVersionOrdering() {
        Datastream ds = new Datastream("a");
        DatastreamVersion dsv1 = new DatastreamVersion("a.1", null);
        DatastreamVersion dsv2 = new DatastreamVersion("a.2", null);
        DatastreamVersion dsv3 = new DatastreamVersion("a.3", new Date(2));
        DatastreamVersion dsv4 = new DatastreamVersion("a.4", new Date(1));
        ds.versions().add(dsv4);
        ds.versions().add(dsv2);
        ds.versions().add(dsv1);
        ds.versions().add(dsv3);
        // should be in this order: a.2, a.1, a.3, a.4
        Assert.assertEquals(dsv2, ds.versions().first());
        Assert.assertEquals(dsv4, ds.versions().last());
        SortedSet<DatastreamVersion> subset = ds.versions().subSet(dsv1, dsv4);
        Assert.assertEquals(dsv1, subset.first());
        Assert.assertEquals(dsv3, subset.last());
    }

}
