package de.mxro.httpserver.netty3.internal;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpMessage;

public class CustomHttpContentCompressor extends HttpContentCompressor {

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        super.messageReceived(ctx, e);

        final HttpMessage httpMessage = (HttpMessage) e.getMessage();
        final int length = httpMessage.getContent().array().length;

        httpMessage.addHeader(arg0, arg1);

    }

}
