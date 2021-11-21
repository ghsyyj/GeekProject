package geek.week3.kimmking.gateway.outbound.netty4;//package io.github.kimmking.gateway.outbound;
//
import geek.week3.gateway.NettyClientHandler;
import geek.week3.gateway.SimpleNettyHttpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

public class NettyHttpClient {
    public void connect(String host, int port, String message) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        NettyClientHandler handler = new NettyClientHandler();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            //禁止使用nagle算法，用于小数据即时传输
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
//                     客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
                    //自定义的handler
                    ch.pipeline().addLast(handler);
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            f.channel().writeAndFlush(message);
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        NettyHttpClient client = new NettyHttpClient();
        client.connect("127.0.0.1", 8802, "netty请求信息");
    }
}