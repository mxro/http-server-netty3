/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3.internal;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.server.ServerComponent;
import de.mxro.service.callbacks.ShutdownCallback;

public class ShutdownRequestHandler extends SimpleChannelUpstreamHandler {

    private final String secret;
    final ServerComponent shutdownOperations;
    Netty3ServerComponent thisServer;

    private static void sendHttpResponse(final MessageEvent event, final byte[] bytes, final int responseCode,
            final String contentType) {

        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(responseCode));

        final ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(bytes);
        response.setContent(buffer);
        response.setHeader(CONTENT_TYPE, contentType);

        final ChannelFuture future = event.getChannel().write(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendHttpSuccess(final MessageEvent event, final byte[] bytes, final String contentType) {
        sendHttpResponse(event, bytes, OK.getCode(), contentType);
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {

        final Thread t = new Thread() {

            @Override
            public void run() {

                // System.out.println("doing shutdown ...");
                final HttpRequest request = (HttpRequest) e.getMessage();

                final String requestUri = request.getUri();

                if (!requestUri.replace("/", "").equals(secret)) {

                    try {
                        sendHttpResponse(
                                e,
                                "Access denied. You must supply the master secret for this server as part of the url, eg: http://myserver.com:8900/[your secret]"
                                        .getBytes("UTF-8"), 403, "text/plain");

                        return;
                    } catch (final Throwable t) {
                        throw new RuntimeException(t);
                    }

                }

                shutdownOperations.stop(new ShutdownCallback() {

                    @Override
                    public void onSuccess() {

                        try {
                            // new Exception("Shutdown successful")
                            // .printStackTrace();
                            Netty3Server.sendHttpSuccess(e, "Shutdown successful.".getBytes("UTF-8"), "text/plain");

                            final TimerTask stopShutdownServer = new TimerTask() {

                                @Override
                                public void run() {
                                    assert thisServer != null : "setThisServer() must be specified for this shutdown server.";

                                    thisServer.stop(new ShutdownCallback() {

                                        @Override
                                        public void onSuccess() {
                                            // all ok

                                        }

                                        @Override
                                        public void onFailure(final Throwable t) {
                                            throw new RuntimeException(t);
                                        }
                                    });
                                }

                            };

                            new Timer().schedule(stopShutdownServer, 150);

                        } catch (final UnsupportedEncodingException e1) {
                            throw new RuntimeException(e1);
                        }

                    }

                    @Override
                    public void onFailure(final Throwable t) {

                        System.out.println("Failure while shutting down server: " + t.getMessage());
                        t.printStackTrace();

                        sendHttpSuccess(e, t.getMessage().getBytes(), "text/plain");

                    }

                });
            }

        };

        t.start();

    }

    public void setThisServer(final Netty3ServerComponent server) {
        this.thisServer = server;
    }

    public ShutdownRequestHandler(final String secret, final ServerComponent shutdownOperations) {
        super();

        this.shutdownOperations = shutdownOperations;
        this.secret = secret;

    }

}
