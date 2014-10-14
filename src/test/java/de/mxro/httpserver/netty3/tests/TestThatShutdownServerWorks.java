package de.mxro.httpserver.netty3.tests;

import de.mxro.async.Deferred;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.services.Services;
import java.net.URL;
import java.net.URLConnection;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Test;

@SuppressWarnings("all")
public class TestThatShutdownServerWorks {
  @Test
  public void test() {
    try {
      final Deferred<Netty3ServerComponent> _function = new Deferred<Netty3ServerComponent>() {
        public void get(final ValueCallback<Netty3ServerComponent> cb) {
          HttpService _echoService = Services.echoService();
          Netty3Server.start(_echoService, 12322, cb);
        }
      };
      final Netty3ServerComponent server = AsyncJre.<Netty3ServerComponent>waitFor(_function);
      final Deferred<Netty3ServerComponent> _function_1 = new Deferred<Netty3ServerComponent>() {
        public void get(final ValueCallback<Netty3ServerComponent> cb) {
          Netty3Server.startShutdownServer(12321, "mysecret", server, cb);
        }
      };
      final Netty3ServerComponent shutdownServer = AsyncJre.<Netty3ServerComponent>waitFor(_function_1);
      URL _uRL = new URL("http://localhost:12321/mysecret");
      final URLConnection connection = _uRL.openConnection();
      connection.connect();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
