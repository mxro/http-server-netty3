/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3.internal;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;

import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.NxServerNetty;
import de.mxro.server.ServerComponent;
import de.mxro.service.callbacks.ShutdownCallback;

public class ShutdownRequestHandler extends SimpleChannelUpstreamHandler {

    private final String secret;
    final ServerComponent shutdownOperations;
    Netty3Server thisServer;

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
                        NxServerNetty
                                .sendHttpResponse(
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
                            NxServerNetty.sendHttpSuccess(e, "Shutdown successful.".getBytes("UTF-8"), "text/plain");

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

                        NxServerNetty.sendHttpSuccess(e, t.getMessage().getBytes(), "text/plain");

                    }

                });
            }

        };

        t.start();

    }

    public void setThisServer(final Netty3Server server) {
        this.thisServer = server;
    }

    public ShutdownRequestHandler(final String secret, final ServerComponent shutdownOperations) {
        super();

        this.shutdownOperations = shutdownOperations;
        this.secret = secret;

    }

}
