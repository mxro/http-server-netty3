package de.mxro.httpserver.netty3.tests;

import com.mashape.unirest.http.Unirest;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.netty3.tests.JsonService;
import de.mxro.httpserver.services.Services;
import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import java.util.HashMap;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class TestUnicode {
  @Test
  public void test() {
    try {
      final HashMap<String, HttpService> serviceMap = new HashMap<String, HttpService>();
      serviceMap.put("/one", Services.delayedEcho(1));
      JsonService _jsonService = new JsonService();
      serviceMap.put("/two", _jsonService);
      final HttpService service = Services.withParallelWorkerThreads("test", 10, 230000, Services.dispatcher(serviceMap));
      final Operation<Object> _function = new Operation<Object>() {
        @Override
        public void apply(final ValueCallback<Object> cb) {
          service.start(AsyncCommon.<Object>asSimpleCallback(cb));
        }
      };
      Async.<Object>waitFor(_function);
      final Operation<Netty3ServerComponent> _function_1 = new Operation<Netty3ServerComponent>() {
        @Override
        public void apply(final ValueCallback<Netty3ServerComponent> cb) {
          Netty3Server.start(service, 
            12442, cb);
        }
      };
      final Netty3ServerComponent server = Async.<Netty3ServerComponent>waitFor(_function_1);
      Assert.assertEquals("this and that and … and fällen", Unirest.post("http://localhost:12442/one").body("this and that and … and fällen").asString().getBody());
      final String json = Unirest.post("http://localhost:12442/two").body("{\"client\":\"hello …\"}").asString().getBody();
      Assert.assertTrue(json.contains("…"));
      Assert.assertTrue(json.contains("ä"));
      final Operation<Object> _function_2 = new Operation<Object>() {
        @Override
        public void apply(final ValueCallback<Object> cb) {
          server.stop(
            AsyncCommon.<Object>asSimpleCallback(cb));
        }
      };
      Async.<Object>waitFor(_function_2);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
