package de.mxro.httpserver.netty3.tests;

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
          Netty3Server.start(Services.echo(), 12322, cb);
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
      final URLConnection connection = new URL("http://localhost:12321/mysecret").openConnection();
      connection.connect();
      final InputStream is = connection.getInputStream();
      Assert.assertTrue(TestThatShutdownServerWorks.getString(is).contains("successful"));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static String getString(final InputStream is) {
    final Scanner s = new Scanner(is).useDelimiter("\\A");
    return s.next();
  }
}
