package org.example.errorHandling;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;

public class LambdaFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>{

  // private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
     final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
       final LambdaLogger logger = context.getLogger();
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
       //log the entire event
        logger.log("Capturing the entire request "+requestEvent.toString());
        // get the user details from the post request and save that to DB
        //fetch the request body
        String body = requestEvent.getBody();
        if (body == null) {
            throw new IllegalArgumentException("Request body is missing");
        }
        final User user;
       try {
          user = objectMapper.readValue(requestEvent.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        responseEvent.setStatusCode(200);
        responseEvent.setHeaders(Collections.singletonMap("Content-Type", "application/json"));
        responseEvent.setBody(user.toString());
       return responseEvent;
    }
}
