package org.example;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class S3EventHandler implements RequestHandler<S3Event, Boolean> {

    private static final AmazonS3 s3Client = AmazonS3Client
            .builder()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .build();
    @Override
    public Boolean handleRequest(S3Event input, Context context) {
        //instantiate the lambda logger we can use Sysout but as it is an IO operation it will take time to
        //execute so the billing time will be increased.
        //so we will use the context to provide the logger just like log4j is implemetation class for slf4j in spring boot app
         final LambdaLogger logger = context.getLogger();
        //we will check if we are getting any record
        if(input.getRecords().isEmpty() ){
            logger.log("Hey there are no records form the file and its empty");
            return false;
        }
    //process the record if we have record
        for(S3EventNotification.S3EventNotificationRecord record:input.getRecords()){
           String bucketName = record.getS3().getBucket().getName();
            String ObjectKey = record.getS3().getObject().getKey();
            //1. creating the s3 client, try to add it outside the handler
            //2. invoke the get object
            //3. process the input streams from s3
            S3Object s3Object = s3Client.getObject(bucketName, ObjectKey);
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            //now we can do anything so we can process the data.
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
                    br.lines().skip(1)
                            .forEach(line->logger.log(line+"\n"));
            }catch (IOException e){
                logger.log("Error occured in lambda:"+e.getMessage());
                return false;
            }
        }

        return true;
    }
}
