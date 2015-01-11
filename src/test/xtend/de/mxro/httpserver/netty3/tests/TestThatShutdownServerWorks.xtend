package de.mxro.httpserver.netty3.tests

import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services
import java.io.InputStream
import java.net.URL
import java.util.Scanner
import org.junit.Assert
import org.junit.Test
import de.mxro.async.jre.Async

class TestThatShutdownServerWorks {

	@Test
	def void test() {
		val server = Async.waitFor(
			[ cb |
				Netty3Server.start(Services.echo(), 12322, cb)
			])

		Async.waitFor(
			[ cb |
				Netty3Server.startShutdownServer(12321, "mysecret", server, cb)
			])

		val connection = new URL("http://localhost:12321/mysecret").openConnection

		connection.connect

		val is = connection.inputStream

		Assert.assertTrue(getString(is).contains('successful'))

	}

	def static getString(InputStream is) {
		val Scanner s = new Scanner(is).useDelimiter("\\A")
		return s.next
	}

}
