/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3.internal;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;

import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.server.ComponentConfiguration;
import de.mxro.server.ComponentContext;

public class InternalNettyRestServer implements Netty3ServerComponent {

    protected final Channel channel;
    protected final int port;
    protected final ServerBootstrap bootstrap;

    @Override
    public Channel getChannel() {

        return channel;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void stop(final SimpleCallback callback) {
        try {
            destroy(callback);
        } catch (final Throwable t) {
            callback.onFailure(t);
        }

    }

    public InternalNettyRestServer(final Channel channel, final int port, final ServerBootstrap bootstrap) {
        super();
        this.channel = channel;
        this.port = port;
        this.bootstrap = bootstrap;
    }

    @Override
    public void start(final SimpleCallback callback) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public void injectConfiguration(final ComponentConfiguration conf) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public void injectContext(final ComponentContext context) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public ComponentConfiguration getConfiguration() {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public void destroy(final SimpleCallback callback) {

        channel.close().awaitUninterruptibly(1000 * 20);
        bootstrap.releaseExternalResources();

        callback.onSuccess();

    }

}
