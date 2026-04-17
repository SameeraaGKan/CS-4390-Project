public class MathProtocol {
    // Protocol constants used by both client and server for message parsing
    public static final String CONNECT = "CONNECT";
    public static final String ACK = "SUCCESS_ACK"; // Acknowledgement sent by server on successful connection
    public static final String MATH_REQ = "MATH_REQ";
    public static final String CLOSE = "CLOSE";
    public static final String RESULT = "RESULT";

    // Separator used to split message fields during parsing
    public static final String SEPARATOR = ":";

    // Message format: MATH_REQ:OPERATOR:NUM1:NUM2 (e.g. MATH_REQ:ADD:10:20)
    public static String buildMathRequest(String op, int a, int b) {
        return MATH_REQ + SEPARATOR + op + SEPARATOR + a + SEPARATOR + b;
    }
}