package com.timvisee.rumor.protocol.packet;

import com.timvisee.rumor.server.DisconnectReason;

public class PacketFactory {

    public static Packet createDisconnectPacket(DisconnectReason reason) {
        Packet p = new Packet(PacketType.DISCONNECT);
        p.appendInteger(reason.id());
        return p;
    }

    public static Packet createHeartbeatPacket() {
        return new Packet(PacketType.HEARTBEAT);
    }

    public static Packet createHandshakePacket() {
        return new Packet(PacketType.HANDSHAKE);
    }

    public static Packet createClientInfoPacket() {
        return new Packet(PacketType.CLIENT_INFO);
    }

    public static Packet createServerInfoPacket() {
        return new Packet(PacketType.SERVER_INFO);
    }

    public static Packet createAuthenticationPacket(String user) {
        Packet p = new Packet(PacketType.AUTH);
        p.appendString(user);
        return p;
    }

    public static Packet createAuthenticationResultPacket(boolean succeed) {
        Packet p = new Packet(PacketType.AUTH_RESULT);
        p.appendBoolean(succeed);
        p.appendInteger(0);
        return p;
    }
}
