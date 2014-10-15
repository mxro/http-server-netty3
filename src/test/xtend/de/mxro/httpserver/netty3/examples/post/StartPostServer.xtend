package de.mxro.httpserver.netty3.examples.post

import de.mxro.async.jre.AsyncJre
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.resources.Resources
import de.mxro.httpserver.services.Services
import java.util.HashMap

class StartPostServer {

	def void main(String[] args) {
		
		val services = new HashMap
		
		services.put("/service", Services.echo)
		services.put("*", Services.data(PAGE.bytes, "text/html"))
		
		
		
		val server = AsyncJre.waitFor([cb |
			Netty3Server.start(Services.dispatcher(services), 8080, cb)
			
			])
		 
		 println 'Press key to stop server'
		 System.in.read
		
		server.shutdown().get
		
	}

	static val PAGE = '''
		
		<html>
		
		<body>
			<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
			
			
			<script>
			
				 $.ajax({
				 	        type: "POST",
		                    url: "/service",
		                    data:  "Hello!",
		                    processData: false,
		                    contentType: 'text',
		                    dataType: 'text',
		                    success: function(data, textStatus, rew) {
		                        $("body").append(data);
		                        
		
		                       
		                    }
		                    
		                });
			
			
			</script>
			
			
		</body>
		
		
		</html>
		
	'''

}
