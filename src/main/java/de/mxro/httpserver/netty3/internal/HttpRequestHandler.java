/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3.internal;

import java.io.ByteArrayOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;

import de.mxro.httpserver.netty3.ByteStreamHandler;

/**
 * 
 * @author <a href="http://www.mxro.de/">Max Rohde</a>
 * 
 */
public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

    protected final ByteStreamHandler byteStreamHandler;

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
        throw new RuntimeException(e.getCause());
    }

    @Override
    public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        System.out.println("connected!");
        super.channelConnected(ctx, e);
    }

    @Override
    public void channelClosed(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        System.out.println("closed!");
        super.channelClosed(ctx, e);
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        System.out.println("msg processed");
        e.getChannel().getConfig().setConnectTimeoutMillis(3000);

        // if (e.getChannel().getConfig() instanceof SocketChannelConfig) {
        // final SocketChannelConfig config = (SocketChannelConfig)
        // e.getChannel().getConfig();
        // config.setKeepAlive(true);

        // }

        final HttpRequest request = (HttpRequest) e.getMessage();

        if (request.isChunked()) {
            HttpUtils.sendHttpError(e, this.getClass().getName() + ": Cannot process chunked requests.");
            return;
        }
        final ByteArrayOutputStream receivedData = new ByteArrayOutputStream();
        final ChannelBuffer buffer = request.getContent();

        if (buffer.readable()) {
            final byte[] ar = new byte[buffer.readableBytes()];
            buffer.readBytes(ar);
            receivedData.write(ar);
            byteStreamHandler.processRequest(receivedData, e);
            return;
        } else {
            byteStreamHandler.processRequest(new ByteArrayOutputStream(), e);
            return;
        }

    }

    public HttpRequestHandler(final ByteStreamHandler byteStreamHandler) {
        super();
        this.byteStreamHandler = byteStreamHandler;

    }

}
