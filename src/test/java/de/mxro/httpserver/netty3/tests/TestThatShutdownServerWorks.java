package de.mxro.httpserver.netty3.tests;

import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;

@SuppressWarnings("all")
public class TestThatShutdownServerWorks {
  @Test
  public void test() {
    throw new Error("Unresolved compilation problems:"
      + "\nType mismatch: cannot convert from (ValueCallback<Netty3ServerComponent>)=>void to Operation<ServerComponent>"
      + "\nType mismatch: cannot convert from (ValueCallback<Netty3ServerComponent>)=>void to Operation<Object>"
      + "\nThere is no context to infer the closure\'s argument types from. Consider typing the arguments or use the closures in a more specific context."
      + "\nThere is no context to infer the closure\'s argument types from. Consider typing the arguments or use the closures in a more specific context.");
  }
  
  public static String getString(final InputStream is) {
    Scanner _scanner = new Scanner(is);
    final Scanner s = _scanner.useDelimiter("\\A");
    return s.next();
  }
}
