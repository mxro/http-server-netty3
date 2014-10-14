package de.mxro.httpserver.netty3.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.http.HttpRequest;

import de.mxro.fn.Closure;
import de.mxro.fn.SuccessFail;
import de.mxro.httpserver.Address;
import de.mxro.httpserver.HttpMethod;
import de.mxro.httpserver.HttpServer;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.IPVersion;
import de.mxro.httpserver.Request;
import de.mxro.httpserver.Response;
import de.mxro.httpserver.netty3.ByteStreamHandler;

public class SocketWrapper implements ByteStreamHandler {

    private final HttpService service;

    @Override
    public void processRequest(final ByteArrayOutputStream receivedData, final MessageEvent e) {
        final HttpRequest request = (HttpRequest) e.getMessage();
        final Response response = HttpServer.createResponse();

        final InetSocketAddress inetSocketAddress = (InetSocketAddress) e.getChannel().getRemoteAddress();
        final InetAddress inetAddress = inetSocketAddress.getAddress();

        final Address address = new Address() {

            @Override
            public IPVersion getVersion() {
                if (inetAddress instanceof Inet4Address) {
                    return IPVersion.IPv4;
                } else if (inetAddress instanceof Inet6Address) {
                    return IPVersion.IPv6;
                } else {
                    throw new RuntimeException("SocketWrapper supports only IPv4 and IPv6 addresses and not "
                            + inetAddress);
                }

            }

            @Override
            public byte[] getAddress() {
                return inetAddress.getAddress();
            }
        };

        final String requestUri;
        if (request.getUri() != null) {
            requestUri = request.getUri();
        } else {
            requestUri = "";
        }
        final HashMap<String, String> headers = new HashMap<String, String>();

        for (final Entry<String, String> en : request.getHeaders()) {
            headers.put(en.getKey(), en.getValue());
        }

        service.process(new Request() {

            @Override
            public String getRequestUri() {
                return requestUri;
            }

            InputStream inputStream = null;

            @Override
            public InputStream getInputStream() {
                if (inputStream != null) {
                    return inputStream;
                } else {
                    inputStream = new ByteArrayInputStream(getData());
                    return inputStream;
                }

            }

            @Override
            public HttpMethod getMethod() {
                if (request.getMethod().equals(org.jboss.netty.handler.codec.http.HttpMethod.POST)) {
                    return HttpMethod.POST;
                } else if (request.getMethod().equals(org.jboss.netty.handler.codec.http.HttpMethod.GET)) {
                    return HttpMethod.GET;
                } else if (request.getMethod().equals(org.jboss.netty.handler.codec.http.HttpMethod.PUT)) {
                    return HttpMethod.PUT;
                } else if (request.getMethod().equals(org.jboss.netty.handler.codec.http.HttpMethod.DELETE)) {
                    return HttpMethod.DELETE;
                }
                throw new IllegalStateException("Http method not supported: " + request.getMethod());

            }

            @Override
            public Map<String, String> getHeaders() {

                return headers;
            }

            @Override
            public String getHeader(final String key) {
                return headers.get(key);
            }

            byte[] byteArray = null;

            @Override
            public byte[] getData() {
                if (byteArray != null) {
                    return byteArray;
                } else {
                    byteArray = receivedData.toByteArray();
                    return byteArray;
                }

            }

            @Override
            public Address getSourceAddress() {
                return address;
            }
        }, response, new Closure<SuccessFail>() {

            @Override
            public void apply(final SuccessFail o) {

                if (o.isFail()) {
                    HttpUtils.sendHttpError(e, o.getException().getMessage());
                    return;
                }

                HttpUtils.sendFullHttpResponse(e, response.getContent(), response.getResponseCode(),
                        response.getHeaders());

            }
        });

    }

    /**
     * Note: service will not be started by socket wrapper; must be started
     * externally.
     * 
     * @param service
     */
    public SocketWrapper(final HttpService service) {
        super();
        this.service = service;

    }

}
