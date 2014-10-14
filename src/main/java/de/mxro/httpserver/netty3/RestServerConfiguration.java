package de.mxro.httpserver.netty3;

import mx.sslutils.SslKeyStoreData;
import de.mxro.httpserver.HttpService;

public abstract class RestServerConfiguration {

	public abstract int getPort();

	public abstract boolean getUseSsl();

	public abstract SslKeyStoreData getSslKeyStore();

	public abstract HttpService getService();

}
