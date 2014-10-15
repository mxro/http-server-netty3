package de.mxro.httpserver.netty3.tests

import de.mxro.async.jre.AsyncJre
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services
import java.io.InputStream
import java.net.URL
import java.util.ArrayList
import java.util.List
import java.util.Scanner
import org.junit.Test

class TestThatShutdownServerWorks {

	@Test
	def void test() {

		val server = AsyncJre.waitFor(
			[ cb |
				Netty3Server.start(Services.echoService(), 12322, cb);
			])

		AsyncJre.waitFor(
			[ cb |
				Netty3Server.startShutdownServer(12321, "mysecret", server, cb);
			])

		val connection = new URL("http://localhost:12321/mysecret").openConnection

		connection.connect

		val is = connection.inputStream

		

		println(getString(is));

	}

	def static getString(InputStream is) {
		val Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext();
	}

}
