package de.mxro.httpserver.netty3.examples.post;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class StartPostServer {
  public void main(final String[] args) {
    throw new Error("Unresolved compilation problems:"
      + "\nType mismatch: cannot convert from new ValueCallback(){} to ValueCallback<Netty3ServerComponent>");
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
