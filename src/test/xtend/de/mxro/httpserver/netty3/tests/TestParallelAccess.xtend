package de.mxro.httpserver.netty3.tests

import com.codahale.metrics.Histogram
import com.mashape.unirest.http.Unirest
import de.mxro.httpserver.HttpService
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services
import de.mxro.metrics.jre.Metrics
import delight.async.AsyncCommon
import delight.async.jre.Async
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import org.junit.Assert
import org.junit.Test
import delight.functional.Success

class TestParallelAccess {
	
	@Test
	def void test_parallel_possible() {

		val serviceMap = new HashMap<String, HttpService>()

		serviceMap.put("/one", Services.delayedEcho(100))
		serviceMap.put("/two", Services.delayedEcho(1))

		val service = Services.withParallelWorkerThreads("test", 10, 230000, Services.dispatcher(serviceMap))

		Async.waitFor [ cb |
			service.start(AsyncCommon.asSimpleCallback(cb))
		]

		val server = Async.waitFor(
			[ cb |
			Netty3Server.start(
				service,
				12422,
				cb
			)
		])

		val list = Collections.synchronizedList(new ArrayList())
		val t1 = new Thread [
			for (i : 1 .. 3) {
				list.add("1")
				Unirest.post("http://localhost:12422/one").body("Hello").asString.body
				list.add("7")

			}
		]

		t1.start

		val t2 = new Thread [
			for (i : 1 .. 300) {
				list.add("6")
				Unirest.post("http://localhost:12422/two").body("Hello").asString.body
				list.add("8")
			}
		]
		t2.start

		t1.join

		t2.join

		val order = list.join("")
		// println(order)
		val startLong = order.indexOf("1")
		val endLong = order.indexOf("7")

		Assert.assertTrue(endLong - startLong > 5)

		Async.waitFor [ cb |
			server.stop(
				AsyncCommon.asSimpleCallback(cb)
			)
		]

	}

	@Test
	def void test_queue_overflow() {
		val serviceMap = new HashMap<String, HttpService>()

		serviceMap.put("/one", Services.delayedEcho(500))
		serviceMap.put("/two", Services.delayedEcho(1))

		val service = Services.withParallelWorkerThreads("test", 2, 230000, Services.dispatcher(serviceMap))

		Async.waitFor [ cb |
			service.start(AsyncCommon.asSimpleCallback(cb))
		]

		val server = Async.waitFor(
			[ cb |
			Netty3Server.start(
				service,
				12428,
				cb
			)
		])

		val list = Collections.synchronizedList(new ArrayList())
		val t1 = new Thread [
			for (i : 1 .. 5) {
				list.add("1")
				Unirest.post("http://localhost:12428/one").body("Hello").asString.body
				list.add("7")

			}
		]

		t1.start

		val t2 = new Thread [
			for (i : 1 .. 300) {
				list.add("6")
				Unirest.post("http://localhost:12428/two").body("Hello").asString.body
				list.add("8")
			}
		]
		t2.start

		t1.join

		t2.join

		val order = list.join("")

		 println(order)
		val startLong = order.indexOf("1")
		val endLong = order.indexOf("7")

		Assert.assertTrue(endLong - startLong > 5)

		Async.waitFor [ cb |
			server.stop(
				AsyncCommon.asSimpleCallback(cb)
			)
		]
	}

	@Test
	def void test_task_delayed() {
		val serviceMap = new HashMap<String, HttpService>()

		serviceMap.put("/slow", Services.withParallelWorkerThreads("slow", 2, 5000, Services.delayedEcho(400)))
		serviceMap.put("/fast", Services.withParallelWorkerThreads("fast", 2, 5000, Services.delayedEcho(1)))

		val service = Services.withParallelWorkerThreads("dispatcher", 2, 230000, Services.dispatcher(serviceMap))

		Async.waitFor [ cb |
			service.start(AsyncCommon.asSimpleCallback(cb))
		]

		val server = Async.waitFor(
			[ cb |
			Netty3Server.start(
				service,
				12628,
				cb
			)
		])

		val list = Collections.synchronizedList(new ArrayList())
		val metrics = Metrics.createUnsafe

		val t1 = new Thread [
			for (i : 1 .. 5) {
				new Thread [
					list.add("1")
					Unirest.post("http://localhost:12628/slow").body("Hello").asString.body
					list.add("7")
				].start

			}
		]

		t1.start

		val t2 = new Thread [
			for (i : 1 .. 50) {
				new Thread [
					val time = System.currentTimeMillis
					list.add("6")

					Unirest.post("http://localhost:12628/fast").body("Hello").asString.body
					list.add("8")
					metrics.record(Metrics.value("time", System.currentTimeMillis - time))
					//println('Done in ' + (System.currentTimeMillis - time))
				].start

			}
		]
		t2.start

		t1.join

		t2.join

		while (list.size < 110) {
			Thread.sleep(10)
		}

		val order = list.join("")
		
		// Verify that short operations are completed before long ones		
		val startLong = order.indexOf("1")
		val endLong = order.indexOf("7")

		Assert.assertTrue(endLong - startLong > 20)
		
		// Verify that operations are not delayed
		Async.waitFor [ cb |
			metrics.retrieve("time").get [ time |
				val hist = time as Histogram

				val snap = hist.snapshot

				if (snap.stdDev > 300) {
					cb.onFailure(new Exception("Std Dev of operations too big."))
					return
				}
				cb.onSuccess(Success.INSTANCE)
			]
		]

		

		Async.waitFor [ cb |
			server.stop(
				AsyncCommon.asSimpleCallback(cb)
			)
		]
	}
	
	@Test
	def void test_task_timeout() {
		val serviceMap = new HashMap<String, HttpService>()

		serviceMap.put("/slow", Services.withParallelWorkerThreads("slow", 2, 100, Services.delayedEcho(1000)))

		val service = Services.withParallelWorkerThreads("dispatcher", 2, 100, Services.dispatcher(serviceMap))

		Async.waitFor [ cb |
			service.start(AsyncCommon.asSimpleCallback(cb))
		]
	
		val server = Async.waitFor(
			[ cb |
			Netty3Server.start(
				service,
				12728,
				cb
			)
		])
		
		Async.waitFor [cb |
			println(Unirest.post("http://localhost:12728/slow").body("Hello").asString.body)
			
			cb.onSuccess(Success.INSTANCE)
		]
		
		
			
		Async.waitFor [ cb |
			server.stop(
				AsyncCommon.asSimpleCallback(cb)
			)
		]
	}
}
