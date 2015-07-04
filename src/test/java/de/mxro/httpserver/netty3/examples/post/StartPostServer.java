package de.mxro.httpserver.netty3.examples.post;

import de.mxro.fn.Success;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.services.Services;
import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
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
      final Operation<Netty3ServerComponent> _function = new Operation<Netty3ServerComponent>() {
        public void apply(final ValueCallback<Netty3ServerComponent> cb) {
          HttpService _dispatcher = Services.dispatcher(services);
          Netty3Server.start(_dispatcher, 8081, cb);
        }
      };
      final Netty3ServerComponent server = Async.<Netty3ServerComponent>waitFor(_function);
      InputOutput.<String>println("Open page at http://localhost:8081");
      InputOutput.<String>println("Press key to stop server");
      System.in.read();
      final Operation<Success> _function_1 = new Operation<Success>() {
        public void apply(final ValueCallback<Success> cb) {
          SimpleCallback _wrap = AsyncCommon.wrap(cb);
          server.stop(_wrap);
        }
      };
      Async.<Success>waitFor(_function_1);
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
