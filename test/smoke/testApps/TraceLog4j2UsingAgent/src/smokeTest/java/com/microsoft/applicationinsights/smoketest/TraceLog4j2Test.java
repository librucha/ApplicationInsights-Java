package com.microsoft.applicationinsights.smoketest;

import java.util.Comparator;
import java.util.List;

import com.microsoft.applicationinsights.internal.schemav2.Data;
import com.microsoft.applicationinsights.internal.schemav2.Envelope;
import com.microsoft.applicationinsights.internal.schemav2.ExceptionData;
import com.microsoft.applicationinsights.internal.schemav2.ExceptionDetails;
import com.microsoft.applicationinsights.internal.schemav2.MessageData;
import com.microsoft.applicationinsights.internal.schemav2.RequestData;
import com.microsoft.applicationinsights.internal.schemav2.SeverityLevel;

import org.junit.Test;

import static org.junit.Assert.*;

@UseAgent
public class TraceLog4j2Test extends AiSmokeTest {

    @Test
    @TargetUri("/traceLog4j2")
    public void testTraceLog4j2() throws Exception {
        List<Envelope> rdList = mockedIngestion.waitForItems("RequestData", 1);
        List<Envelope> mdList = mockedIngestion.waitForItems("MessageData", 3);

        Envelope rdEnvelope = rdList.get(0);
        Envelope mdEnvelope1 = mdList.get(0);
        Envelope mdEnvelope2 = mdList.get(1);
        Envelope mdEnvelope3 = mdList.get(2);

        RequestData rd = (RequestData) ((Data) rdEnvelope.getData()).getBaseData();

        List<MessageData> logs = mockedIngestion.getTelemetryDataByType("MessageData");
        logs.sort(new Comparator<MessageData>() {
            @Override
            public int compare(MessageData o1, MessageData o2) {
                return o1.getSeverityLevel().compareTo(o2.getSeverityLevel());
            }
        });

        MessageData md1 = logs.get(0);
        MessageData md2 = logs.get(1);
        MessageData md3 = logs.get(2);

        assertEquals("This is log4j2 warn.", md1.getMessage());
        assertEquals(SeverityLevel.Warning, md1.getSeverityLevel());
        assertEquals("Logger", md1.getProperties().get("SourceType"));
        assertEquals("WARN", md1.getProperties().get("LoggingLevel"));
        assertParentChild(rd, rdEnvelope, mdEnvelope1);

        assertEquals("This is log4j2 error.", md2.getMessage());
        assertEquals(SeverityLevel.Error, md2.getSeverityLevel());
        assertEquals("Logger", md2.getProperties().get("SourceType"));
        assertEquals("ERROR", md2.getProperties().get("LoggingLevel"));
        assertParentChild(rd, rdEnvelope, mdEnvelope2);

        assertEquals("This is log4j2 fatal.", md3.getMessage());
        assertEquals(SeverityLevel.Critical, md3.getSeverityLevel());
        assertEquals("Logger", md3.getProperties().get("SourceType"));
        assertEquals("FATAL", md3.getProperties().get("LoggingLevel"));
        assertParentChild(rd, rdEnvelope, mdEnvelope3);
    }

    @Test
    @TargetUri("/traceLog4j2WithException")
    public void testTraceLog4j2WithException() throws Exception {
        List<Envelope> rdList = mockedIngestion.waitForItems("RequestData", 1);
        List<Envelope> edList = mockedIngestion.waitForItems("ExceptionData", 1);

        Envelope rdEnvelope = rdList.get(0);
        Envelope edEnvelope = edList.get(0);

        RequestData rd = (RequestData) ((Data) rdEnvelope.getData()).getBaseData();
        ExceptionData ed = (ExceptionData) ((Data) edEnvelope.getData()).getBaseData();

        List<ExceptionDetails> details = ed.getExceptions();
        ExceptionDetails ex = details.get(0);

        assertEquals("Fake Exception", ex.getMessage());
        assertEquals(SeverityLevel.Error, ed.getSeverityLevel());
        assertEquals("This is an exception!", ed.getProperties().get("Logger Message"));
        assertEquals("Logger", ed.getProperties().get("SourceType"));
        assertEquals("ERROR", ed.getProperties().get("LoggingLevel"));
        assertParentChild(rd, rdEnvelope, edEnvelope);
    }

    private static void assertParentChild(RequestData rd, Envelope rdEnvelope, Envelope childEnvelope) {
        String operationId = rdEnvelope.getTags().get("ai.operation.id");
        assertNotNull(operationId);
        assertEquals(operationId, childEnvelope.getTags().get("ai.operation.id"));

        String operationParentId = rdEnvelope.getTags().get("ai.operation.parentId");
        assertNull(operationParentId);

        assertEquals(rd.getId(), childEnvelope.getTags().get("ai.operation.parentId"));
    }
}