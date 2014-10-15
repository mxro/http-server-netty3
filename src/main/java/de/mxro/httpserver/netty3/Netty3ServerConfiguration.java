package de.mxro.httpserver.netty3;

import de.mxro.httpserver.HttpsServerConfiguration;
import de.mxro.sslutils.SslKeyStoreData;

public abstract class Netty3ServerConfiguration implements HttpsServerConfiguration {

    public abstract boolean getUseSsl();

    public abstract SslKeyStoreData getSslKeyStore();

}
