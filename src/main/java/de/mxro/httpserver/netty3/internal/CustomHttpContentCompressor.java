package de.mxro.httpserver.netty3.internal;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpMessage;

public class CustomHttpContentCompressor extends HttpContentCompressor {

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {

        super.messageReceived(ctx, e);

    }

    @Override
    public void writeRequested(final ChannelHandlerContext ctx, final MessageEvent e) throws Exception {

        super.writeRequested(ctx, e);

        if (e.getMessage() instanceof HttpMessage) {
            final HttpMessage httpMessage = (HttpMessage) e.getMessage();
            final int length = httpMessage.getContent().readableBytes();

            httpMessage.headers().add(HttpHeaders.Names.CONTENT_LENGTH, length);
            System.out.println("add length " + length);
        }

    }

}
