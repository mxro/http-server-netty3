package de.mxro.httpserver.netty3.examples.post;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class StartPostServer {
  public static void main(final String[] args) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field Async is undefined for the type StartPostServer"
      + "\nThe method or field Async is undefined for the type StartPostServer"
      + "\nThe method or field AsyncCommon is undefined for the type StartPostServer"
      + "\nThere is no context to infer the closure\'s argument types from. Consider typing the arguments or put the closures into a typed context."
      + "\nThere is no context to infer the closure\'s argument types from. Consider typing the arguments or put the closures into a typed context."
      + "\nwaitFor cannot be resolved"
      + "\nwaitFor cannot be resolved"
      + "\nstop cannot be resolved"
      + "\nwrap cannot be resolved");
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
