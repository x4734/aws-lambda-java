package demo.lambda.fuction.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;

public class HelloWorld implements RequestHandler< S3Event, String> {
   
    public String handleRequest(S3Event event, Context context) {
    	 context.getLogger().log("started ............ ");
     S3EventNotification.S3EventNotificationRecord record=event.getRecords().get(0);
     System.out.println("Bucket Name is "+record.getS3().getBucket().getName());
     System.out.println("File Path is "+record.getS3().getObject().getKey());
     context.getLogger().log("Bucket Name is "+record.getS3().getBucket().getName());
     context.getLogger().log("File Path is "+record.getS3().getObject().getKey());
     return null;
    }
}