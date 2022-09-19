package io.netty.example.udp.single;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @author: GanYang
 * @Date: 2022/9/14 21:59
 */
public class UdpForQuestion {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup udpEvent = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(udpEvent)
                .channel(NioDatagramChannel.class)
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    /**
                     * Is called for each message of type {@link DatagramPacket}.
                     *
                     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
                     *            belongs to
                     * @param msg the message to handle
                     * @throws Exception is thrown if an error occurred
                     */
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
                        String s = msg.content().toString(Charset.defaultCharset());
                        System.out.println("question get msg : " + s);
                        ctx.close();
                    }
                });
        Channel channel = bootstrap.bind(0).sync().channel();

        channel.writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer("question : yichuanyanyu", StandardCharsets.UTF_8),
                        new InetSocketAddress("127.0.0.1", 8080)))
                .sync();
        System.out.println("00000");
        if (!channel.closeFuture().await(10000)) {
            System.out.println("请求结束");
        }

    }
}
