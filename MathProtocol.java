public class MathProtocol {
    // Message Types
    public static final String CONNECT = "CONNECT";
    public static final String ACK = "SUCCESS_ACK";
    public static final String MATH_REQ = "MATH_REQ";
    public static final String CLOSE = "CLOSE";
    
    // Delimiter for parsing strings
    public static final String SEPARATOR = ":";

    // Example format: MATH_REQ:ADD:10:20
    public static String buildMathRequest(String op, int a, int b) {
        return MATH_REQ + SEPARATOR + op + SEPARATOR + a + SEPARATOR + b;
    }
}