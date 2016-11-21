package de.mxro.httpserver.netty3.tests;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Snapshot;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.services.Services;
import de.mxro.metrics.jre.Metrics;
import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.async.properties.PropertyNode;
import delight.async.properties.PropertyOperation;
import delight.functional.Closure;
import delight.functional.Success;
import delight.promise.Promise;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class TestParallelAccess {
  @Test
  public void test_parallel_possible() {
    try {
      final HashMap<String, HttpService> serviceMap = new HashMap<String, HttpService>();
      HttpService _delayedEcho = Services.delayedEcho(100);
      serviceMap.put("/one", _delayedEcho);
      HttpService _delayedEcho_1 = Services.delayedEcho(1);
      serviceMap.put("/two", _delayedEcho_1);
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
            12422, cb);
        }
      };
      final Netty3ServerComponent server = Async.<Netty3ServerComponent>waitFor(_function_1);
      ArrayList<Object> _arrayList = new ArrayList<Object>();
      final List<Object> list = Collections.<Object>synchronizedList(_arrayList);
      final Runnable _function_2 = new Runnable() {
        @Override
        public void run() {
          try {
            IntegerRange _upTo = new IntegerRange(1, 3);
            for (final Integer i : _upTo) {
              {
                list.add("1");
                HttpRequestWithBody _post = Unirest.post("http://localhost:12422/one");
                RequestBodyEntity _body = _post.body("Hello");
                HttpResponse<String> _asString = _body.asString();
                _asString.getBody();
                list.add("7");
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      final Thread t1 = new Thread(_function_2);
      t1.start();
      final Runnable _function_3 = new Runnable() {
        @Override
        public void run() {
          try {
            IntegerRange _upTo = new IntegerRange(1, 300);
            for (final Integer i : _upTo) {
              {
                list.add("6");
                HttpRequestWithBody _post = Unirest.post("http://localhost:12422/two");
                RequestBodyEntity _body = _post.body("Hello");
                HttpResponse<String> _asString = _body.asString();
                _asString.getBody();
                list.add("8");
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      final Thread t2 = new Thread(_function_3);
      t2.start();
      t1.join();
      t2.join();
      final String order = IterableExtensions.join(list, "");
      final int startLong = order.indexOf("1");
      final int endLong = order.indexOf("7");
      Assert.assertTrue(((endLong - startLong) > 5));
      final Operation<Success> _function_4 = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(cb);
          server.stop(_asSimpleCallback);
        }
      };
      Async.<Success>waitFor(_function_4);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void test_queue_overflow() {
    try {
      final HashMap<String, HttpService> serviceMap = new HashMap<String, HttpService>();
      HttpService _delayedEcho = Services.delayedEcho(100);
      serviceMap.put("/one", _delayedEcho);
      HttpService _delayedEcho_1 = Services.delayedEcho(1);
      serviceMap.put("/two", _delayedEcho_1);
      HttpService _dispatcher = Services.dispatcher(serviceMap);
      final HttpService service = Services.withParallelWorkerThreads("test", 2, 230000, _dispatcher);
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
            12428, cb);
        }
      };
      final Netty3ServerComponent server = Async.<Netty3ServerComponent>waitFor(_function_1);
      ArrayList<Object> _arrayList = new ArrayList<Object>();
      final List<Object> list = Collections.<Object>synchronizedList(_arrayList);
      final Runnable _function_2 = new Runnable() {
        @Override
        public void run() {
          try {
            IntegerRange _upTo = new IntegerRange(1, 5);
            for (final Integer i : _upTo) {
              {
                list.add("1");
                HttpRequestWithBody _post = Unirest.post("http://localhost:12428/one");
                RequestBodyEntity _body = _post.body("Hello");
                HttpResponse<String> _asString = _body.asString();
                _asString.getBody();
                list.add("7");
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      final Thread t1 = new Thread(_function_2);
      t1.start();
      final Runnable _function_3 = new Runnable() {
        @Override
        public void run() {
          try {
            IntegerRange _upTo = new IntegerRange(1, 300);
            for (final Integer i : _upTo) {
              {
                list.add("6");
                HttpRequestWithBody _post = Unirest.post("http://localhost:12428/two");
                RequestBodyEntity _body = _post.body("Hello");
                HttpResponse<String> _asString = _body.asString();
                _asString.getBody();
                list.add("8");
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      final Thread t2 = new Thread(_function_3);
      t2.start();
      t1.join();
      t2.join();
      final String order = IterableExtensions.join(list, "");
      final int startLong = order.indexOf("1");
      final int endLong = order.indexOf("7");
      Assert.assertTrue(((endLong - startLong) > 5));
      final Operation<Success> _function_4 = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(cb);
          server.stop(_asSimpleCallback);
        }
      };
      Async.<Success>waitFor(_function_4);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void test_task_delayed() {
    try {
      final HashMap<String, HttpService> serviceMap = new HashMap<String, HttpService>();
      HttpService _delayedEcho = Services.delayedEcho(400);
      HttpService _withParallelWorkerThreads = Services.withParallelWorkerThreads("slow", 2, 5000, _delayedEcho);
      serviceMap.put("/slow", _withParallelWorkerThreads);
      HttpService _delayedEcho_1 = Services.delayedEcho(1);
      HttpService _withParallelWorkerThreads_1 = Services.withParallelWorkerThreads("fast", 2, 5000, _delayedEcho_1);
      serviceMap.put("/fast", _withParallelWorkerThreads_1);
      HttpService _dispatcher = Services.dispatcher(serviceMap);
      final HttpService service = Services.withParallelWorkerThreads("dispatcher", 2, 230000, _dispatcher);
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
            12628, cb);
        }
      };
      final Netty3ServerComponent server = Async.<Netty3ServerComponent>waitFor(_function_1);
      ArrayList<Object> _arrayList = new ArrayList<Object>();
      final List<Object> list = Collections.<Object>synchronizedList(_arrayList);
      final PropertyNode metrics = Metrics.createUnsafe();
      final Runnable _function_2 = new Runnable() {
        @Override
        public void run() {
          IntegerRange _upTo = new IntegerRange(1, 5);
          for (final Integer i : _upTo) {
            final Runnable _function = new Runnable() {
              @Override
              public void run() {
                try {
                  list.add("1");
                  HttpRequestWithBody _post = Unirest.post("http://localhost:12628/slow");
                  RequestBodyEntity _body = _post.body("Hello");
                  HttpResponse<String> _asString = _body.asString();
                  _asString.getBody();
                  list.add("7");
                } catch (Throwable _e) {
                  throw Exceptions.sneakyThrow(_e);
                }
              }
            };
            Thread _thread = new Thread(_function);
            _thread.start();
          }
        }
      };
      final Thread t1 = new Thread(_function_2);
      t1.start();
      final Runnable _function_3 = new Runnable() {
        @Override
        public void run() {
          IntegerRange _upTo = new IntegerRange(1, 50);
          for (final Integer i : _upTo) {
            final Runnable _function = new Runnable() {
              @Override
              public void run() {
                try {
                  final long time = System.currentTimeMillis();
                  list.add("6");
                  HttpRequestWithBody _post = Unirest.post("http://localhost:12628/fast");
                  RequestBodyEntity _body = _post.body("Hello");
                  HttpResponse<String> _asString = _body.asString();
                  _asString.getBody();
                  list.add("8");
                  long _currentTimeMillis = System.currentTimeMillis();
                  long _minus = (_currentTimeMillis - time);
                  PropertyOperation<Long> _value = Metrics.value("time", _minus);
                  metrics.<Long>record(_value);
                } catch (Throwable _e) {
                  throw Exceptions.sneakyThrow(_e);
                }
              }
            };
            Thread _thread = new Thread(_function);
            _thread.start();
          }
        }
      };
      final Thread t2 = new Thread(_function_3);
      t2.start();
      t1.join();
      t2.join();
      while ((list.size() < 110)) {
        Thread.sleep(10);
      }
      final String order = IterableExtensions.join(list, "");
      final int startLong = order.indexOf("1");
      final int endLong = order.indexOf("7");
      Assert.assertTrue(((endLong - startLong) > 20));
      final Operation<Success> _function_4 = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          Promise<Object> _retrieve = metrics.retrieve("time");
          final Closure<Object> _function = new Closure<Object>() {
            @Override
            public void apply(final Object time) {
              final Histogram hist = ((Histogram) time);
              final Snapshot snap = hist.getSnapshot();
              double _stdDev = snap.getStdDev();
              boolean _greaterThan = (_stdDev > 300);
              if (_greaterThan) {
                Exception _exception = new Exception("Std Dev of operations too big.");
                cb.onFailure(_exception);
                return;
              }
              cb.onSuccess(Success.INSTANCE);
            }
          };
          _retrieve.get(_function);
        }
      };
      Async.<Success>waitFor(_function_4);
      final Operation<Success> _function_5 = new Operation<Success>() {
        @Override
        public void apply(final ValueCallback<Success> cb) {
          SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(cb);
          server.stop(_asSimpleCallback);
        }
      };
      Async.<Success>waitFor(_function_5);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
