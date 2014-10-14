/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3.internal;

import java.io.ByteArrayOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.SocketChannelConfig;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpRequest;

import de.mxro.httpserver.netty3.ByteStreamHandler;

/**
 * 
 * @author <a href="http://www.mxro.de/">Max Rohde</a>
 * 
 */
public class HttpRequestAggregator extends SimpleChannelUpstreamHandler {

    protected final ByteStreamHandler byteStreamHandler;

    private final ByteArrayOutputStream receivedData;
    private boolean chunked;

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception {
        throw new RuntimeException(e.getCause());
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {

        e.getChannel().getConfig().setConnectTimeoutMillis(15000);

        if (e.getChannel().getConfig() instanceof SocketChannelConfig) {
            final SocketChannelConfig config = (SocketChannelConfig) e.getChannel().getConfig();
            config.setKeepAlive(true);
        }

        if (!chunked) {
            final HttpRequest request = (HttpRequest) e.getMessage();

            final ChannelBuffer buffer = request.getContent();
            receivedData.write(buffer.array());

            if (!request.isChunked()) {
                byteStreamHandler.processRequest(receivedData, e);
            } else {
                chunked = true;

            }

        } else {
            final HttpChunk chunk = (HttpChunk) e.getMessage();
            final ChannelBuffer buffer = chunk.getContent();
            receivedData.write(buffer.array());

            if (chunk.isLast()) {
                byteStreamHandler.processRequest(receivedData, e);
            }
        }

    }

    public HttpRequestAggregator(final ByteStreamHandler byteStreamHandler) {
        super();
        this.byteStreamHandler = byteStreamHandler;

        this.chunked = false;
        this.receivedData = new ByteArrayOutputStream();
    }

}
