package Handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SimpleHandler implements RequestHandler {
    @Override
    public Object handleRequest(Object input, Context context) {
        context.getLogger().log("The input was the following: " + input);
        return "Lambda executed successfully with the following input - " +input;
    }
}
