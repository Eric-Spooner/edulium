package com.at.ac.tuwien.sepm.ss15.edulium;

import org.junit.Test;
import junit.framework.Assert;

public class HelloWorldTest {
    @Test
    public void helloWorldConcatenation() {
        // GIVEN
        String hello = "hello";
        String world = "world";

        // WHEN
        String helloWorld = hello + world;

        // THEN
        Assert.assertEquals(helloWorld, "helloworld");
    }
}
