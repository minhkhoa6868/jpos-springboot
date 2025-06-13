package com.example.jpos_springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jpos.iso.ISOMsg;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpos_springboot.client.ISOTestClient;

@SpringBootTest
public class ISOTestClientTests {
    
    private final ISOTestClient client = new ISOTestClient("localhost", 10000);

    @Test
    public void testEcho1() throws Exception {
        ISOMsg respMsg = client.sendMessage("testcases/echo_test_1.xml");
        assertNotNull(respMsg);
        assertEquals("0810", respMsg.getMTI());
        assertEquals("00", respMsg.getString(39));
    }

    @Test
    public void testEcho2() throws Exception {
        ISOMsg respMsg = client.sendMessage("testcases/echo_test_2.xml");
        assertNotNull(respMsg);
        assertEquals("0210", respMsg.getMTI());
        assertEquals("00", respMsg.getString(39));
    }

    @Test
    public void testEcho3() throws Exception {
        ISOMsg respMsg = client.sendMessage("testcases/echo_test_3.xml");
        assertNotNull(respMsg);
        assertEquals("0210", respMsg.getMTI());
        assertEquals("01", respMsg.getString(39));
    }

    @Test
    public void testEcho4() throws Exception {
        ISOMsg respMsg = client.sendMessage("testcases/echo_test_4.xml");
        assertNotNull(respMsg);
        assertEquals("0210", respMsg.getMTI());
        assertEquals("00", respMsg.getString(39));
    }

    @Test
    public void testEcho5() throws Exception {
        ISOMsg respMsg = client.sendMessage("testcases/echo_test_5.xml");
        assertNotNull(respMsg);
        assertEquals("0810", respMsg.getMTI());
        assertEquals("00", respMsg.getString(39));
    }

    @Test
    public void testEcho6() throws Exception {
        ISOMsg respMsg = client.sendMessage("testcases/echo_test_6.xml");
        assertNotNull(respMsg);
        assertEquals("0410", respMsg.getMTI());
        assertEquals("00", respMsg.getString(39));
    }
}
