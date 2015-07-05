package de.mxro.httpserver.netty3.tests;

import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.services.Services;
import delight.async.Operation;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class TestThatShutdownServerWorks {
  @Test
  public void test() {
    try {
      final Operation<Netty3ServerComponent> _function = new Operation<Netty3ServerComponent>() {
        @Override
        public void apply(final ValueCallback<Netty3ServerComponent> cb) {
          HttpService _echo = Services.echo();
          Netty3Server.start(_echo, 12322, cb);
        }
      };
      final Netty3ServerComponent server = Async.<Netty3ServerComponent>waitFor(_function);
      final Operation<Netty3ServerComponent> _function_1 = new Operation<Netty3ServerComponent>() {
        @Override
        public void apply(final ValueCallback<Netty3ServerComponent> cb) {
          Netty3Server.startShutdownServer(12321, "mysecret", server, cb);
        }
      };
      Async.<Netty3ServerComponent>waitFor(_function_1);
      URL _uRL = new URL("http://localhost:12321/mysecret");
      final URLConnection connection = _uRL.openConnection();
      connection.connect();
      final InputStream is = connection.getInputStream();
      String _string = TestThatShutdownServerWorks.getString(is);
      boolean _contains = _string.contains("successful");
      Assert.assertTrue(_contains);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static String getString(final InputStream is) {
    Scanner _scanner = new Scanner(is);
    final Scanner s = _scanner.useDelimiter("\\A");
    return s.next();
  }
}