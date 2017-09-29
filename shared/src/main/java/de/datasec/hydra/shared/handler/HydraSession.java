package de.datasec.hydra.shared.handler;

import de.datasec.hydra.shared.protocol.HydraProtocol;
import de.datasec.hydra.shared.protocol.packets.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by DataSec on 29.09.2017.
 */
public class HydraSession extends SimpleChannelInboundHandler<Packet> {

    private SocketChannel channel;

    private HydraProtocol protocol;

    public HydraSession(SocketChannel channel, HydraProtocol protocol) {
        this.channel = channel;
        this.protocol = protocol;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) throws Exception {
        // Use packetListener from
    }

    public void send(Packet packet) {
        channel.writeAndFlush(packet);
    }
}
