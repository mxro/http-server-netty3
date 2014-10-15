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
		
		val server = AsyncJre.waitFor([cb |
			Netty3Server.start(Services.echoService(), 12322, cb);
		])
		
		AsyncJre.waitFor([cb |
			Netty3Server.startShutdownServer(12321, "mysecret", server, cb);
		])
		
		
		val connection = new URL("http://localhost:12321/mysecret").openConnection
		
		connection.connect
		
		val is = connection.inputStream
		
		var byte b;
		var List<Byte> res = new ArrayList
		b = is.read() as byte
		while (b > -1) {
			res.add(Byte.valueOf( b));
		}
		
		val Byte[] data = res.toArray(#[]);
		
		println(new String(data));
		
	}
	
	def static  getString(InputStream is) {
    val Scanner s = new Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next() : "";
    }

	
}