/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package de.mxro.httpserver.netty3;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.OK;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import de.mxro.httpserver.netty3.internal.ShutdownServerFactory;
import de.mxro.server.ServerComponent;

public class NxServerNetty {

    /**
     * Secret must be supplied as URI path.
     * 
     * @param port
     * @param secret
     * @param operations
     * @return
     */
    public static Netty3Server startShutdownServer(final int port, final String secret, final ServerComponent operations) {
        return ShutdownServerFactory.startNettyShutdownServer(port, secret, operations);
    }

    public static void sendHttpResponse(final MessageEvent event, final byte[] bytes, final int responseCode,
            final Map<String, List<String>> headerFields) {

        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(responseCode));

        final ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(bytes);
        response.setContent(buffer);

        for (final Entry<String, List<String>> header : headerFields.entrySet()) {
            if (header.getKey() != null) {
                response.setHeader(header.getKey(), header.getValue());
            }
        }

        response.setHeader("Connection", "keep-alive");
        // response.setHeader(CONTENT_TYPE, contentType);

        final ChannelFuture future = event.getChannel().write(response);
        future.addListener(ChannelFutureListener.CLOSE);

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

    public static void sendHttpResponse(final MessageEvent event, final byte[] bytes, final int responseCode,
            final String contentType) {

        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(responseCode));

        final ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(bytes);
        response.setContent(buffer);
        response.setHeader(CONTENT_TYPE, contentType);

        final ChannelFuture future = event.getChannel().write(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendHttpSuccess(final MessageEvent event, final byte[] bytes, final String contentType) {
        sendHttpResponse(event, bytes, OK.getCode(), contentType);
    }

    public static void sendHttpSuccessWithCache(final int cacheMin, final MessageEvent event, final byte[] bytes,
            final String contentType) {
        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);

        final ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(bytes);
        response.setContent(buffer);
        response.setHeader(CONTENT_TYPE, contentType);

        final int maxCache = cacheMin * 60000; // 30 min
        final long now = System.currentTimeMillis();

        response.setHeader(HttpHeaders.Names.EXPIRES, now + maxCache);
        response.setHeader(HttpHeaders.Names.LAST_MODIFIED, now);
        response.setHeader(HttpHeaders.Names.CACHE_CONTROL, "max-age=" + maxCache + ", public");

        final ChannelFuture future = event.getChannel().write(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    public static final int HTTP_CACHE_SECONDS = 60;

    private static void setDateHeader(final HttpResponse response) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        final Calendar time = new GregorianCalendar();
        response.setHeader(HttpHeaders.Names.DATE, dateFormatter.format(time.getTime()));
    }

    public static void sendNotModified(final MessageEvent event) {

        final HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.NOT_MODIFIED);
        setDateHeader(response);

        // Close the connection as soon as the error message is sent.
        event.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);

    }

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
