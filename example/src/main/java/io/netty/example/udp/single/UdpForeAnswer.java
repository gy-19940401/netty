package io.netty.example.udp.single;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @author: GanYang
 * @Date: 2022/9/14 22:12
 */
public class UdpForeAnswer {
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
                        System.out.println(s);

                        ctx.channel().writeAndFlush(new DatagramPacket(
                                Unpooled.copiedBuffer("answer : teacher : question", StandardCharsets.UTF_8),
                                msg.sender()));

                        //注意 应答端的 通道不能关闭
                        //ctx.close();
                    }
                });
        ChannelFuture sync = bootstrap.bind(8080).sync();
        System.out.println("answer 启动");
        sync.channel().closeFuture().sync();
    }
}
