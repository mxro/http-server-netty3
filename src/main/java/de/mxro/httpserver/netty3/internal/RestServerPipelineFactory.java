/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3.internal;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.util.Timer;

import de.mxro.sslutils.SslKeyStoreData;
import de.mxro.sslutils.SslUtils;

/**
 * 
 * 
 * @author <a href="http://www.mxro.de/">Max Rohde</a>
 * 
 */
public final class RestServerPipelineFactory implements ChannelPipelineFactory {

    protected final boolean useSsl;
    protected final BytesHandler handler;
    protected SslKeyStoreData sslKeyStore;

    @Override
    public ChannelPipeline getPipeline() throws Exception {

        final ChannelPipeline pipeline = Channels.pipeline();

        if (useSsl) {
            final SSLEngine engine = SslUtils.createContextForCertificate(sslKeyStore).createSSLEngine();

            engine.setUseClientMode(false);
            final SslHandler sslHandler = new SslHandler(engine);

            pipeline.addLast("ssl", sslHandler);
        }

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());

        pipeline.addLast("deflater", new CustomHttpContentCompressor());
        pipeline.addLast("aggregator", new HttpChunkAggregator(5242880));

        pipeline.addLast("handler", new HttpRequestHandler(handler));

        return pipeline;
    }

    public RestServerPipelineFactory(final BytesHandler handler, final boolean useSsl,
            final SslKeyStoreData sslKeyStore, final Timer timer) {
        super();
        this.useSsl = useSsl;
        this.handler = handler;
        this.sslKeyStore = sslKeyStore;

    }

}
