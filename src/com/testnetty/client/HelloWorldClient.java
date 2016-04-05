package com.testnetty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * DateTime: 2015年1月5日 上午9:56:22
 *
 */
public class HelloWorldClient {
	static final boolean SSL = System.getProperty("ssl") != null;
	static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer
			.parseInt(System.getProperty("port", "8007"));

	public static Bootstrap bootstrap = getBootstrap();
	public static Channel channel = getChannel(PORT, HOST);
	
	private static Bootstrap getBootstrap() {
		Bootstrap b = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		b.group(group)
		 .channel(NioSocketChannel.class)
		 .option(ChannelOption.TCP_NODELAY, true)
		 .handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch)
					throws Exception {
				ChannelPipeline p = ch.pipeline();
				p.addLast(new ObjectEncoder(),
						new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
						new HelloWorldClientHandler());
			}
		}).option(ChannelOption.SO_KEEPALIVE, true); 
		return b;
	}

	private static Channel getChannel(int port, String host) {
		Channel channel = null;
		try {
			channel = bootstrap.connect(host, port).sync().channel();
		} catch (Exception e) {
			System.out.println(String.format("连接Server(IP[%s],PORT[%s])失败",
					host, port));
		}
		return channel;
	}

	public static void sendMsg(String msg) throws Exception {  
        if(channel!=null){  
            channel.writeAndFlush(msg).sync();  
        }else{  
        	System.out.println("消息发送失败,连接尚未建立!");  
        }  
    } 

	public static void main(String[] args) throws Exception {
		long t0 = System.nanoTime();
		long s = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {  
        	HelloWorldClient.sendMsg(i+"你好1");  
        }  
        long t1 = System.nanoTime();  
        long e = System.currentTimeMillis();
        System.out.println((t1-t0)/1000000.0); 
        System.out.println(e-s);
	}
		
}

class HelloWorldClientHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println(msg);
	}
}