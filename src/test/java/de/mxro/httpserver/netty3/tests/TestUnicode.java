package de.mxro.httpserver.netty3.tests;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.netty3.tests.JsonService;
import de.mxro.httpserver.services.Services;
import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.functional.Success;
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
      HttpService _delayedEcho = Services.delayedEcho(1);
      serviceMap.put("/one", _delayedEcho);
      JsonService _jsonService = new JsonService();
      serviceMap.put("/two", _jsonService);
      HttpService _dispatcher = Services.dispatcher(serviceMap);
      final HttpService service = Services.withParallelWorkerThreads("test", 10, 230000, _dispatcher);
      final Operation<Success> _function = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(cb);
          service.start(_asSimpleCallback);
        }
      };
      Async.<Success>waitFor(_function);
      final Operation<Netty3ServerComponent> _function_1 = new Operation<Netty3ServerComponent>() {
        @Override
        public void apply(final ValueCallback<Netty3ServerComponent> cb) {
          Netty3Server.start(service, 
            12442, cb);
        }
      };
      final Netty3ServerComponent server = Async.<Netty3ServerComponent>waitFor(_function_1);
      HttpRequestWithBody _post = Unirest.post("http://localhost:12442/one");
      RequestBodyEntity _body = _post.body("this and that and … and fällen");
      HttpResponse<String> _asString = _body.asString();
      String _body_1 = _asString.getBody();
      Assert.assertEquals("this and that and … and fällen", _body_1);
      HttpRequestWithBody _post_1 = Unirest.post("http://localhost:12442/two");
      RequestBodyEntity _body_2 = _post_1.body("{\"client\":\"hello …\"}");
      HttpResponse<String> _asString_1 = _body_2.asString();
      final String json = _asString_1.getBody();
      boolean _contains = json.contains("…");
      Assert.assertTrue(_contains);
      boolean _contains_1 = json.contains("ä");
      Assert.assertTrue(_contains_1);
      final Operation<Success> _function_2 = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(cb);
          server.stop(_asSimpleCallback);
        }
      };
      Async.<Success>waitFor(_function_2);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
