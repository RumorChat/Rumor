package com.timvisee.rumor.protocol.packet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example test case.
 */
public class PacketTest {

    /**
     * This is an example test.
     */
    @Test
    public void testExample() {
        Packet packet = new Packet(PacketType.AUTH);
        assertEquals(PacketType.AUTH.id(), packet.getPacketId());
    }

}
