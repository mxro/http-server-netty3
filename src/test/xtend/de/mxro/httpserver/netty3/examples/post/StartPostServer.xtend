package de.mxro.httpserver.netty3.examples.post

import de.mxro.async.Async
import de.mxro.async.jre.AsyncJre
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services
import java.util.HashMap

class StartPostServer {

	def static void main(String[] args) {
		
		val services = new HashMap
		
		services.put("/service", Services.echo)
		services.put("*", Services.data(PAGE.bytes, "text/html"))

		
		val server = AsyncJre.waitFor([cb |
			Netty3Server.start(Services.dispatcher(services), 8081, cb)
			
			])
		 
		 println("Open page at http://localhost:8081")
		 println('Press key to stop server')
		 System.in.read
		
		AsyncJre.waitFor([cb | server.stop(Async.wrap(cb))]);
		
	}

	static val PAGE = '''
		
		<html>
		
		<body>
			<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
			
			
			<script>
				var text = "";
				for (var i=0;i<2000;i++) {
					text += ""+Math.random();
					
				}
				setInterval(function() {
				 $.ajax({
				 	        type: "POST",
		                    url: "/service",
		                    data:  "Some big chunk of data follows: "+text,
		                    processData: false,
		                    contentType: 'text',
		                    dataType: 'text',
		                    success: function(data, textStatus, rew) {
		                        $("body").append(data);
		                        
		
		                       
		                    }
		                    
		                });
		                }, 500);
			
			
			</script>
			
			
		</body>
		
		
		</html>
		
	'''

}
