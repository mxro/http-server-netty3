package de.mxro.httpserver.netty3.examples.staticfile

import de.mxro.async.Async
import de.mxro.async.jre.AsyncJre
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.resources.Resources
import de.mxro.httpserver.services.Services

class StartStaticFileServer {
	def static void main(String[] args) {
		
		val source = Resources.cache(Resources.forWeb(Resources.fromClasspath(StartStaticFileServer)));
		
		val service = Services.resources(source)

		AsyncJre.waitFor([cb | service.start(Async.wrap(cb))])


		val server = AsyncJre.waitFor([cb | Netty3Server.start(service, 8081, cb) ])
		 
		 println("Download file from at http://localhost:8081/bigfile.js")
		 println('Press key to stop server')
		 System.in.read
		
		AsyncJre.waitFor([cb | server.stop(Async.wrap(cb))]);
		
		AsyncJre.waitFor([cb | service.stop(Async.wrap(cb))])
	}
}