package de.mxro.httpserver.netty3.tests

import de.mxro.async.jre.AsyncJre
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services
import java.io.ByteArrayInputStream
import java.net.URL
import org.junit.Test
import java.util.List

class TestThatShutdownServerWorks {
	
	@Test
	def void test() {
		
		val server = AsyncJre.waitFor([cb |
			Netty3Server.start(Services.echoService(), 12322, cb);
		])
		
		val shutdownServer = AsyncJre.waitFor([cb |
			Netty3Server.startShutdownServer(12321, "mysecret", server, cb);
		])
		
		
		val connection = new URL("http://localhost:12321/mysecret").openConnection
		
		connection.connect
		
		val is = connection.inputStream
		
		var int b;
		var List<Byte> res = new ArrayList
		b = is.read()
		while (b > -1) {
			res.add
		}
		
		
	}
	
}