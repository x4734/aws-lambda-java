package demo.lambda.fuction.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import org.apache.commons.io.IOUtils;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

public class LambdaRequestStreamHandler implements RequestStreamHandler {
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
		
		
		  String input;
		try {
		//	input = IOUtils.toString(inputStream, "UTF-8");
			outputStream.write(("Hello World - " + inputStream).getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		 
		 }

}
