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
import de.mxro.service.callbacks.ShutdownCallback;

public class InternalShutdownServer implements Netty3ServerComponent {

	protected final int port;
	protected final Channel channel;
	protected final ShutdownRequestHandler handler;
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
		if (!channel.isBound()) {
			callback.onSuccess();
			return;
		}
		this.handler.shutdownOperations.stop(new ShutdownCallback() {

			@Override
			public void onSuccess() {
				destroy(callback);
			}

			@Override
			public void onFailure(final Throwable t) {
				callback.onFailure(t);
			}

		});
	}

	public InternalShutdownServer(final int port, final Channel channel,
			final ShutdownRequestHandler handler,
			final ServerBootstrap bootstrap) {
		super();
		this.port = port;
		this.channel = channel;
		this.handler = handler;
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
		channel.close().awaitUninterruptibly(20000);
		bootstrap.releaseExternalResources();
		callback.onSuccess();
	}

}
