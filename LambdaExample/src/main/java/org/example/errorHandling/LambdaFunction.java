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

public class LambdaFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
   public  User user;
     final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
       final LambdaLogger logger = context.getLogger();
       //log the entire event
        logger.log("Log the complete API gateway request"+requestEvent.toString());
        // get the user details from the post request and save that to DB

        //fetch the request body
        String body = requestEvent.getBody();
        try {
            User user = objectMapper.readValue(requestEvent.getBody(), User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //client check - if username and id are not null
       if(StringUtils.isNullOrEmpty(user.getName())||user.getId()==null){
           throw new RuntimeJsonMappingException("Details are not vaild");
       }
        return null;
    }
}
