/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import de.mxro.async.callbacks.ValueCallback;
import de.mxro.httpserver.netty3.internal.InternalNettyRestServer;
import de.mxro.httpserver.netty3.internal.RestServerPipelineFactory;
import de.mxro.httpserver.netty3.internal.SocketWrapper;
import de.mxro.httpserver.services.Services;

public class NettyHttpServer {

    public static void startRestServer(final RestServerConfiguration conf,
            final ValueCallback<Netty3ServerComponent> callback) {

        final NioServerSocketChannelFactory socketChannelFactory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

        final ServerBootstrap bootstrap = new ServerBootstrap(socketChannelFactory);

        bootstrap.setOption("child.keepAlive", true);

        final ByteStreamHandler messageHandler = new SocketWrapper(Services.safeShutdown(conf.getService()));

        bootstrap.setPipelineFactory(new RestServerPipelineFactory(messageHandler, conf.getUseSsl(), conf
                .getSslKeyStore()));

        // Bind and start to accept incoming connections.
        final Channel server = bootstrap.bind(new InetSocketAddress(conf.getPort()));

        callback.onSuccess(new InternalNettyRestServer(server, conf.getPort(), bootstrap));

    }

}
