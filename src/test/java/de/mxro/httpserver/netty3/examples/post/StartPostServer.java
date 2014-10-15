package de.mxro.httpserver.netty3.examples.post;

import de.mxro.async.Deferred;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.async.jre.AsyncJre;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.netty3.Netty3Server;
import de.mxro.httpserver.netty3.Netty3ServerComponent;
import de.mxro.httpserver.services.Services;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class StartPostServer {
  public void main(final String[] args) {
    final Deferred<Netty3ServerComponent> _function = new Deferred<Netty3ServerComponent>() {
      public void get(final ValueCallback<Netty3ServerComponent> cb) {
        HttpService _echo = Services.echo();
        Netty3Server.start(_echo, 8080, cb);
      }
    };
    AsyncJre.<Netty3ServerComponent>waitFor(_function);
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
      _builder.append("\t");
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
      _builder.append("data:  \"Hello!\",");
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
