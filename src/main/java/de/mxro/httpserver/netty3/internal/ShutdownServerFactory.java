/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3.internal;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import mx.jreutils.MxJREUtils;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.server.ComponentConfiguration;
import de.mxro.server.ComponentContext;
import de.mxro.server.ServerComponent;

/**
 * 
 * @author <a href="http://www.mxro.de/">Max Rohde</a>
 * 
 */
public class ShutdownServerFactory {

    private final static boolean ENABLE_DEBUG = false;

    public static Netty3ServerComponent startNettyShutdownServer(final int port, final String secret,
            final ServerComponent shutdownOperations) {
        final ThreadFactory threadFactory = new ThreadFactory() {

            @Override
            public Thread newThread(final Runnable arg0) {

                return new Thread(arg0, "shutdown-server-" + new Random().nextInt(2000));
            }
        };
        final ExecutorService newFixedThreadPool = Executors.newCachedThreadPool(threadFactory);

        final ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(newFixedThreadPool,
                newFixedThreadPool));

        final ShutdownRequestHandler shutdownRequestHandler = new ShutdownRequestHandler(secret, shutdownOperations);

        bootstrap.setPipelineFactory(new ShutdownServerPipelineFactory(shutdownRequestHandler));

        if (ENABLE_DEBUG) {
            System.out.println(ShutdownServerFactory.class + ": Binding shutdown server on port: " + port);
        }

        if (!MxJREUtils.portAvailable(port)) {
            throw new IllegalStateException("Cannot start shutdown server on port: " + port + "\n"
                    + "  The port is already in use.");
        }

        // Bind and start to accept incoming connections.

        final Channel server = bootstrap.bind(new InetSocketAddress(port));

        if (ENABLE_DEBUG) {
            System.out.println(ShutdownServerFactory.class + ": Shutdown server started on port: " + port);
        }

        final Netty3ServerComponent nettyServer = new Netty3ServerComponent() {

            @Override
            public int getPort() {

                return port;
            }

            @Override
            public Channel getChannel() {

                return server;
            }

            @Override
            public void stop(final SimpleCallback callback) {
                getChannel().close().awaitUninterruptibly(3000);
                bootstrap.releaseExternalResources();
                callback.onSuccess();

            }

            @Override
            public void start(final SimpleCallback callback) {
                throw new RuntimeException("Operation not supported!");
            }

            @Override
            public void injectConfiguration(final ComponentConfiguration conf) {
                throw new RuntimeException("Operation not supported!");
            }

            @Override
            public void injectContext(final ComponentContext context) {
                throw new RuntimeException("Operation not supported!");
            }

            @Override
            public ComponentConfiguration getConfiguration() {
                throw new RuntimeException("Operation not supported!");
            }

            @Override
            public void destroy(final SimpleCallback callback) {
                getChannel().close().awaitUninterruptibly(3000);
                bootstrap.releaseExternalResources();
                callback.onSuccess();

            }
        };

        shutdownRequestHandler.setThisServer(nettyServer);

        return new InternalShutdownServer(port, server, shutdownRequestHandler, bootstrap);

    }

}
