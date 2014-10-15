package de.mxro.httpserver.netty3.internal;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

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

    public static void sendFullHttpResponse(final MessageEvent event, final byte[] bytes, final int responseCode,
            final Map<String, String> headerFields) {

        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(responseCode));

        final ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(bytes);
        response.setContent(buffer);

        if (headerFields != null) {
            for (final Entry<String, String> header : headerFields.entrySet()) {

                if (header.getKey() != null) {
                    response.setHeader(header.getKey(), header.getValue());
                }
            }
        }

        response.setHeader(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        // response.setHeader(CONTENT_TYPE, contentType);

        final ChannelFuture future = event.getChannel().write(response);
        future.addListener(ChannelFutureListener.CLOSE);

    }

}
