package de.mxro.httpserver.netty3.examples.post;

import de.mxro.async.Async;
import de.mxro.async.callbacks.SimpleCallback;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;
import de.mxro.async.promise.Deferred;
import de.mxro.fn.Success;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.services.Services;
import java.util.HashMap;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;

@SuppressWarnings("all")
public class StartPostServer {
  public static void main(final String[] args) {
    try {
      final HashMap<String, HttpService> services = new HashMap<String, HttpService>();
      HttpService _echo = Services.echo();
      services.put("/service", _echo);
      byte[] _bytes = StartPostServer.PAGE.getBytes();
      HttpService _data = Services.data(_bytes, "text/html");
      services.put("*", _data);
      final Deferred<Netty3ServerComponent> _function = new Deferred<Netty3ServerComponent>() {
        public void get(final ValueCallback<Netty3ServerComponent> cb) {
          HttpService _dispatcher = Services.dispatcher(services);
          Netty3Server.start(_dispatcher, 8081, cb);
        }
      };
      final Netty3ServerComponent server = AsyncJre.<Netty3ServerComponent>waitFor(_function);
      InputOutput.<String>println("Open page at http://localhost:8081");
      InputOutput.<String>println("Press key to stop server");
      System.in.read();
      final Deferred<Success> _function_1 = new Deferred<Success>() {
        public void get(final ValueCallback<Success> cb) {
          SimpleCallback _wrap = Async.wrap(cb);
          server.stop(_wrap);
        }
      };
      AsyncJre.<Success>waitFor(_function_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private final static String PAGE = new Function0<String>() {
    public String apply() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.newLine();
      _builder.append("<html>");
      _builder.newLine();
      _builder.newLine();
      _builder.append("<body>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js\"></script>");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<script>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("var text = \"\";");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("for (var i=0;i<20;i++) {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("text += \"\"+Math.random();");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("setInterval(function() {");
      _builder.newLine();
      _builder.append("\t\t ");
      _builder.append("$.ajax({");
      _builder.newLine();
      _builder.append("\t\t \t        ");
      _builder.append("type: \"POST\",");
      _builder.newLine();
      _builder.append("                    ");
      _builder.append("url: \"/service\",");
      _builder.newLine();
      _builder.append("                    ");
      _builder.append("data:  \"Some big chunk of data follows: \"+text,");
      _builder.newLine();
      _builder.append("                    ");
      _builder.append("processData: false,");
      _builder.newLine();
      _builder.append("                    ");
      _builder.append("contentType: \'text\',");
      _builder.newLine();
      _builder.append("                    ");
      _builder.append("dataType: \'text\',");
      _builder.newLine();
      _builder.append("                    ");
      _builder.append("success: function(data, textStatus, rew) {");
      _builder.newLine();
      _builder.append("                        ");
      _builder.append("$(\"body\").append(data);");
      _builder.newLine();
      _builder.append("                        ");
      _builder.newLine();
      _builder.newLine();
      _builder.append("                       ");
      _builder.newLine();
      _builder.append("                    ");
      _builder.append("}");
      _builder.newLine();
      _builder.append("                    ");
      _builder.newLine();
      _builder.append("                ");
      _builder.append("});");
      _builder.newLine();
      _builder.append("                ");
      _builder.append("}, 500);");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("</script>");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("</body>");
      _builder.newLine();
      _builder.newLine();
      _builder.newLine();
      _builder.append("</html>");
      _builder.newLine();
      _builder.newLine();
      return _builder.toString();
    }
  }.apply();
}
