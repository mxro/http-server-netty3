package de.mxro.httpserver.netty3.examples.staticfile;

import de.mxro.async.Async;
import de.mxro.async.Deferred;
import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;
import de.mxro.fn.Success;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.resources.ResourceProvider;
import de.mxro.httpserver.resources.Resources;
import de.mxro.httpserver.services.Services;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class StartStaticFileServer {
  public static void main(final String[] args) {
    try {
      ResourceProvider _fromClasspath = Resources.fromClasspath(StartStaticFileServer.class);
      ResourceProvider _forWeb = Resources.forWeb(_fromClasspath);
      final ResourceProvider source = Resources.cache(_forWeb);
      final HttpService service = Services.resources(source);
      final Deferred<Success> _function = new Deferred<Success>() {
        public void get(final ValueCallback<Success> cb) {
          SimpleCallback _wrap = Async.wrap(cb);
          service.start(_wrap);
        }
      };
      AsyncJre.<Success>waitFor(_function);
      final Deferred<Netty3ServerComponent> _function_1 = new Deferred<Netty3ServerComponent>() {
        public void get(final ValueCallback<Netty3ServerComponent> cb) {
          Netty3Server.start(service, 8081, cb);
        }
      };
      final Netty3ServerComponent server = AsyncJre.<Netty3ServerComponent>waitFor(_function_1);
      InputOutput.<String>println("Download file from at http://localhost:8081/bigfile.js");
      InputOutput.<String>println("Press key to stop server");
      System.in.read();
      final Deferred<Success> _function_2 = new Deferred<Success>() {
        public void get(final ValueCallback<Success> cb) {
          SimpleCallback _wrap = Async.wrap(cb);
          server.stop(_wrap);
        }
      };
      AsyncJre.<Success>waitFor(_function_2);
      final Deferred<Success> _function_3 = new Deferred<Success>() {
        public void get(final ValueCallback<Success> cb) {
          SimpleCallback _wrap = Async.wrap(cb);
          service.stop(_wrap);
        }
      };
      AsyncJre.<Success>waitFor(_function_3);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}