/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3.internal;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 
 * 
 * @author <a href="http://www.mxro.de/">Max Rohde</a>
 * 
 */
public class ShutdownServerPipelineFactory implements ChannelPipelineFactory {

	protected final ShutdownRequestHandler shutdownHandler;

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = pipeline();

		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("handler", shutdownHandler);

		return pipeline;
	}

	public ShutdownServerPipelineFactory(
			final ShutdownRequestHandler shutdownHandler) {
		super();
		this.shutdownHandler = shutdownHandler;
	}

}
