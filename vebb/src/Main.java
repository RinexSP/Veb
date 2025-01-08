import com.fastcgi.FCGIInterface;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


public class Main {
    public static void main(String[] args) {
        var fcgiInterface = new FCGIInterface();


        while (fcgiInterface.FCGIaccept() >= 0) {
            if (fcgiInterface.request != null) {
                Properties requestProperties = fcgiInterface.request.params;
                String requestMethod = requestProperties.getProperty("REQUEST_METHOD");
                System.out.println("Полученный запрос: " + requestMethod);


                if ("POST".equalsIgnoreCase(requestMethod)) {
                    try {

                        String body = Utils.readRequestBody();
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(body);

                        if (jsonNode.get("x") == null || jsonNode.get("y") == null || jsonNode.get("r") == null) {
                            sendErrorResponse(fcgiInterface, "Недостаточно данных");
                            continue;
                        }

                        float x = (float) jsonNode.get("x").asDouble();
                        float y = (float) jsonNode.get("y").asDouble();
                        float r = (float) jsonNode.get("r").asDouble();
                        boolean values = Check.checkValues(x, y);
                        if (!values) {
                            break;
                        }
                        boolean result = Check.checkCoordinates(x, y, r);
                        String jsonResponse = objectMapper.writeValueAsString(new Response(result, x, y, r));
                        sendSuccessResponse(fcgiInterface, jsonResponse);

                    } catch (JsonProcessingException e) {

                        sendErrorResponse(fcgiInterface, "Невалидные данные");
                        e.printStackTrace();
                    } catch (IOException e) {

                        sendErrorResponse(fcgiInterface, "Не получилось прочитать тело запроса");
                        e.printStackTrace();
                    }
                } else {
                    sendErrorResponse(fcgiInterface, "Только POST запрос");
                }
            }
        }
    }


    private static void sendSuccessResponse(FCGIInterface fcgi, String jsonResponse) {
        try {
            String response = String.format(
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/json\r\n" +
                            "Content-Length: %d\r\n\r\n%s",
                    jsonResponse.length(), jsonResponse);
            fcgi.request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void sendErrorResponse(FCGIInterface fcgi, String errorMessage) {
        try {
            String response = String.format(
                    "HTTP/1.1 400 Bad Request\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: %d\r\n\r\n%s",
                    errorMessage.length(), errorMessage);
            fcgi.request.outStream.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}