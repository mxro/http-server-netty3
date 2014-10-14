/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3;

import org.jboss.netty.channel.Channel;

import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.server.ServerComponent;

public interface Netty3ServerComponent extends ServerComponent {

	public Channel getChannel();

	public int getPort();

	@Override
	public void stop(SimpleCallback callback);

	/**
	 * Releases the resources of this server without going through graceful
	 * shutdown procedures.
	 * 
	 * @param callback
	 */
	public void destroy(SimpleCallback callback);

}
