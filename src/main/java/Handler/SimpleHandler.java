package Handler;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class SimpleHandler implements RequestHandler<S3Event, Object> {
    public static final String accessKey = System.getenv("accessKey");
    public static final String privateKey =System.getenv("privateKey");
    private static final String tempDir = System.getProperty("java.io.tmpdir");
    @Override
    public String handleRequest(S3Event input, Context context) {
        //loop through s3event records

        for(S3EventNotification.S3EventNotificationRecord record : input.getRecords()){
            String fileName = record.getS3().getObject().getKey();
            String bucketName = record.getS3().getBucket().getName();
            System.out.println(fileName + "Bucket: " + bucketName);

            // now to download  file
            Regions clientRegion = Regions.EU_WEST_2;

            S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;
            try {

                AWSCredentials credentials = new BasicAWSCredentials(accessKey,privateKey);
                AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                        .withRegion(clientRegion)
                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
                        .build();

                // Get an object and print its contents.
                System.out.println("Downloading an object");
                InputStream inputa = s3Client.getObject(new GetObjectRequest(bucketName,fileName)).getObjectContent();
                System.out.println("Stream available: " + inputa.available());
                fullObject = s3Client.getObject(new GetObjectRequest(bucketName,fileName));

                File file = new File(tempDir + "/"+fileName);

                java.nio.file.Files.copy(
                        inputa,
                        file.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                // read file

                try (BufferedReader br = new BufferedReader(new FileReader(tempDir + "/"+fileName))) {
                    String line;
                    long start = System.currentTimeMillis();
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);

                    }
                    long finish = System.currentTimeMillis();
                    long timeElapsed = finish - start;
                    System.out.println("Time taken by buffered reader: " + timeElapsed);
                } catch (IOException e) {
                    e.printStackTrace();
                }
/*
                System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
                System.out.println("Content: ");
                System.out.println(Arrays.toString(fullObject.getObjectContent().readAllBytes()));*/
            } catch(Exception e){

            }

        }

        return "File read: ";
    }
}
