package de.mxro.httpserver.netty3.tests

import de.mxro.async.jre.AsyncJre
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services
import org.junit.Test

class TestThatShutdownServerWorks {
	
	@Test
	def void test() {
		
		AsyncJre.waitFor([cb |
			Netty3Server.start(Services.echoService(), 12322, );
		])
		
		
		Netty3Server.startShutdownServer(12321, "mysecret", )
		
		
	}
	
}