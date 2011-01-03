package com.github.cwilper.fcrepo.dto.core;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Date;

/**
 * Common tests and convenience methods for Fedora DTO classes.
 */
public abstract class FedoraDTOTest {

    @Test
    public void equality() {
        Object[] instances = getEqualInstances();
        for (int i = 0; i < instances.length; i+=2) {
            Assert.assertEquals(instances[i], instances[i+1]);
            Assert.assertEquals(instances[i+1].hashCode(),
                    instances[i+1].hashCode());
        }
    }

    @Test
    public void inequality() {
        Object[] instances = getNonEqualInstances();
        Assert.assertFalse(instances[0].equals(""));
        for (int i = 0; i < instances.length; i+=2) {
            Assert.assertFalse(instances[i].equals(instances[i+1]));
        }
    }

    /**
     * Get 1 or more pairs of equal instances of this class.
     *
     * @return the equal instance pairs to test.
     */
    abstract Object[] getEqualInstances();

    /**
     * Get 1 or more pairs of non-equal instances of this class.
     *
     * @return the non-equal instance pairs to test.
     */
    abstract Object[] getNonEqualInstances();

    static void checkStringField(Object o, String field) {
        try {
            Method setter = o.getClass().getMethod(field, String.class);
            Method getter = o.getClass().getMethod(field);
            // value starts null
            Assert.assertNull(getter.invoke(o));
            // set value, get same value
            setter.invoke(o, "a");
            Assert.assertEquals("a", getter.invoke(o));
            // set " a", get normalized "a"
            setter.invoke(o, " a");
            Assert.assertEquals("a", getter.invoke(o));
            // set "a ", get normalized "a"
            setter.invoke(o, "a ");
            Assert.assertEquals("a", getter.invoke(o));
            // set "", get normalized null
            setter.invoke(o, "");
            Assert.assertNull(getter.invoke(o));
            // set " ", get normalized null
            setter.invoke(o, " ");
            Assert.assertNull(getter.invoke(o));
            // set null, get null
            setter.invoke(o, new Object[] { null });
            Assert.assertNull(getter.invoke(o));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static void checkDateField(Object o, String field) {
        Date date;
        try {
            Method setter = o.getClass().getMethod(field, Date.class);
            Method getter = o.getClass().getMethod(field);
            // value starts null
            Assert.assertNull(getter.invoke(o));
            // set value, get same value
            date = new Date(0);
            setter.invoke(o, date);
            Assert.assertEquals(date, getter.invoke(o));
            // changing the original date object after setting it
            // doesn't affect the value stored
            date.setTime(1);
            Assert.assertEquals(0, ((Date) getter.invoke(o)).getTime());
            // changing the retrieved date object after getting it
            // shouldn't affect the value stored
            date = (Date) getter.invoke(o);
            date.setTime(1);
            Assert.assertEquals(0, ((Date) getter.invoke(o)).getTime());
            // set null, get null
            setter.invoke(o, new Object[] { null });
            Assert.assertNull(getter.invoke(o));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    static void checkURIField(Object o, String field) {
        try {
            Method setter = o.getClass().getMethod(field, URI.class);
            Method getter = o.getClass().getMethod(field);
            // value starts null
            Assert.assertNull(getter.invoke(o));
            // set value, get same value
            setter.invoke(o, URI.create("urn:a"));
            Assert.assertEquals(URI.create("urn:a"), getter.invoke(o));
            // set null, get null
            setter.invoke(o, new Object[] { null });
            Assert.assertNull(getter.invoke(o));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}