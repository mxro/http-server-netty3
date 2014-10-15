package de.mxro.httpserver.netty3;

import de.mxro.httpserver.HttpService;
import de.mxro.sslutils.SslKeyStoreData;

public abstract class Netty3ServerConfiguration {

    public abstract int getPort();

    public abstract boolean getUseSsl();

    public abstract SslKeyStoreData getSslKeyStore();

    public abstract HttpService getService();
}
