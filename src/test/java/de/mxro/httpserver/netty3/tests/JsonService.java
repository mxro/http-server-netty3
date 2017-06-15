package de.mxro.httpserver.netty3.tests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import de.mxro.httpserver.HttpService;
import de.mxro.httpserver.Request;
import de.mxro.httpserver.Response;
import delight.async.callbacks.SimpleCallback;
import delight.functional.Closure;
import delight.functional.SuccessFail;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class JsonService implements HttpService {
  @Override
  public void start(final SimpleCallback callback) {
    callback.onSuccess();
  }
  
  @Override
  public void stop(final SimpleCallback callback) {
    callback.onSuccess();
  }
  
  @Override
  public void process(final Request request, final Response response, final Closure<SuccessFail> callback) {
    try {
      JsonParser _jsonParser = new JsonParser();
      byte[] _data = request.getData();
      String _string = new String(_data, "UTF-8");
      final JsonObject jsonObject = _jsonParser.parse(_string).getAsJsonObject();
      JsonPrimitive _jsonPrimitive = new JsonPrimitive("yes: hello and fällen");
      jsonObject.add("server", _jsonPrimitive);
      final String json = new Gson().toJson(jsonObject);
      response.setContent(json.getBytes("UTF-8"));
      callback.apply(SuccessFail.success());
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
