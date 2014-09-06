package com.timvisee.rumor.protocol.packet;

public class PacketFactory {

    public static Packet createDisconnectPacket() {
        return new Packet(PacketType.DISCONNECT);
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
