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
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import de.mxro.async.Value;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.internal.BytesHandler;
import de.mxro.httpserver.netty3.internal.Netty3ServerComponentImpl;
import de.mxro.httpserver.netty3.internal.RestServerPipelineFactory;
import de.mxro.httpserver.services.Services;
import de.mxro.server.ServerComponent;
import de.mxro.sslutils.SslKeyStoreData;

public class Netty3Server {

    /**
     * Server which triggers a shutdown for another server, if the correct
     * secret is supplied as the URI request URL.
     * 
     * @param port
     * @param secret
     * @param operations
     * @return
     */
    public static void startShutdownServer(final int port, final String secret, final ServerComponent operations,
            final ValueCallback<Netty3ServerComponent> callback) {

        final Value<ServerComponent> ownServer = new Value<ServerComponent>(null);

        final Netty3ServerConfiguration conf = new Netty3ServerConfiguration() {

            @Override
            public boolean getUseSsl() {
                return false;
            }

            @Override
            public SslKeyStoreData getSslKeyStore() {
                return null;
            }

            @Override
            public HttpService service() {

                return Services.shutdown(secret, operations, ownServer);
            }

            @Override
            public int port() {
                return port;
            }
        };

        start(conf, new ValueCallback<Netty3ServerComponent>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final Netty3ServerComponent value) {
                ownServer.set(value);
                callback.onSuccess(value);
            }
        });

    }

    public static void start(final Netty3ServerConfiguration conf, final ValueCallback<Netty3ServerComponent> callback) {

        final NioServerSocketChannelFactory socketChannelFactory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

        final ServerBootstrap bootstrap = new ServerBootstrap(socketChannelFactory);

        // bootstrap.setOption("child.keepAlive", true);

        final BytesHandler messageHandler = new BytesHandler(Services.safeShutdown(conf.service()));

        final Timer timer = new HashedWheelTimer();

        bootstrap.setPipelineFactory(new RestServerPipelineFactory(messageHandler, conf.getUseSsl(), conf
                .getSslKeyStore(), timer));

        // Bind and start to accept incoming connections.
        final Channel server = bootstrap.bind(new InetSocketAddress(conf.port()));

        System.out.println("start netty server");

        callback.onSuccess(new Netty3ServerComponentImpl(server, conf.port(), bootstrap, timer));

    }

    public static void start(final HttpService service, final int port,
            final ValueCallback<Netty3ServerComponent> callback) {
        final Netty3ServerConfiguration configuration = new Netty3ServerConfiguration() {

            @Override
            public boolean getUseSsl() {
                return false;
            }

            @Override
            public SslKeyStoreData getSslKeyStore() {
                return null;
            }

            @Override
            public HttpService service() {
                return service;
            }

            @Override
            public int port() {
                return port;
            }
        };

        start(configuration, callback);
    }

}
