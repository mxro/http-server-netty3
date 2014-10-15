package de.mxro.httpserver.netty3.examples.post

import de.mxro.async.callbacks.ValueCallback
import de.mxro.httpserver.netty3.Netty3Server
import de.mxro.httpserver.services.Services

class StartPostServer {

	def void main(String[] args) {
		
		
		Netty3Server.start(Services.echoService(), 8080, new ValueCallback() {
			
			override onSuccess(Object value) {
				
			}
			
			override onFailure(Throwable t) {
				
			}
			
		});
		
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
