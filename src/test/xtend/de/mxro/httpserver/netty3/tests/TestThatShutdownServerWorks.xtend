package de.mxro.httpserver.netty3.tests

import de.mxro.async.jre.AsyncJre
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services
import org.junit.Test
import java.net.URL

class TestThatShutdownServerWorks {
	
	@Test
	def void test() {
		
		val server = AsyncJre.waitFor([cb |
			Netty3Server.start(Services.echoService(), 12322, cb);
		])
		
		val shutdownServer = AsyncJre.waitFor([cb |
			Netty3Server.startShutdownServer(12321, "mysecret", server, cb);
		])
		
		
		new URL("http://localhost:12321/mysecret");
		
		
	}
	
}