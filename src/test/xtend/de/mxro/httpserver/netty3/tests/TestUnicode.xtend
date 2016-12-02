package de.mxro.httpserver.netty3.tests

import com.mashape.unirest.http.Unirest
import de.mxro.httpserver.HttpService
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services
import delight.async.AsyncCommon
import delight.async.jre.Async
import java.util.HashMap
import org.junit.Assert
import org.junit.Test

class TestUnicode {
	
	
	@Test
	def public void test() {
		val serviceMap = new HashMap<String, HttpService>()

		serviceMap.put("/one", Services.delayedEcho(1))
		serviceMap.put("/two", new JsonService)

		val service = Services.withParallelWorkerThreads("test", 10, 230000, Services.dispatcher(serviceMap))
		
		Async.waitFor [ cb |
			service.start(AsyncCommon.asSimpleCallback(cb))
		]

		val server = Async.waitFor(
			[ cb |
			Netty3Server.start(
				service,
				12442,
				cb
			)
		])
		
		Assert.assertEquals("this and that and … and fällen", Unirest.post("http://localhost:12442/one").body("this and that and … and fällen").asString.body)
		
		val json = Unirest.post("http://localhost:12442/two").body('{"client":"hello …"}').asString.body
		
		Assert.assertTrue(json.contains("…"))
		Assert.assertTrue(json.contains("ä"))
		
		
		Async.waitFor [ cb |
			server.stop(
				AsyncCommon.asSimpleCallback(cb)
			)
		]
	}
}