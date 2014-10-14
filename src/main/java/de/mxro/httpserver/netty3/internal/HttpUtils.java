package de.mxro.httpserver.netty3.internal;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.UnsupportedEncodingException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class HttpUtils {

    public static void sendHttpError(final MessageEvent event, final String message) {
        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
    
        ChannelBuffer buffer;
        try {
            buffer = ChannelBuffers.wrappedBuffer(message.getBytes("UTF-8"));
    
            response.setContent(buffer);
            response.setHeader(CONTENT_TYPE, "text/plain");
    
            final ChannelFuture future = event.getChannel().write(response);
            future.addListener(ChannelFutureListener.CLOSE);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    
    }

}
