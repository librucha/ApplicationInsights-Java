package com.microsoft.applicationinsights.smoketest;

import org.junit.Test;

@UseAgent
public class NettyTest extends AiSmokeTest {

    @Test
    @TargetUri("/test")
    public void doMostBasicTest() throws Exception {
        mockedIngestion.waitForItems("RequestData", 1);
    }
}
