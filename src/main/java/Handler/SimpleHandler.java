package Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

public class SimpleHandler implements RequestHandler<S3Event, Object> {


    @Override
    public String handleRequest(S3Event input, Context context) {
        //loop through s3event records

        for(S3EventNotification.S3EventNotificationRecord record : input.getRecords()){
            String fileName = record.getS3().getObject().getKey();
            String bucketName = record.getS3().getBucket().getName();

            System.out.println(fileName + "Bucket: " + bucketName);


        }

        return "File read: ";
    }
}
