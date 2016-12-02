package de.mxro.httpserver.netty3.tests

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import de.mxro.httpserver.HttpService
import de.mxro.httpserver.Request
import de.mxro.httpserver.Response
import delight.async.callbacks.SimpleCallback
import delight.functional.Closure
import delight.functional.SuccessFail

class JsonService implements HttpService {
	
	override start(SimpleCallback callback) {
		callback.onSuccess
	}
	
	override stop(SimpleCallback callback) {
		callback.onSuccess
	}
	
	override process(Request request, Response response, Closure<SuccessFail> callback) {
		
		val JsonObject jsonObject = new JsonParser().parse(new String(request.data, "UTF-8")).getAsJsonObject();
		
		jsonObject.add("server", new JsonPrimitive("yes: hello and fällen"));
		
		val json = new Gson().toJson(jsonObject)
		
		
		response.setContent(json.getBytes("UTF-8"))
		
		callback.apply(SuccessFail.success)
		
	}
	
}