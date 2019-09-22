//package ru.levelp;
//
//import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpServer;
//
//import java.io.*;
//import java.net.InetSocketAddress;
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Main {
//    private static final int PORT = 8080;
//
//    public static void main(String[] args) throws IOException {
//        ArrayList<String> messages = new ArrayList();
//
//        HttpServer httpServer = HttpServer.create();
//        httpServer.bind(new InetSocketAddress(PORT), 1000);
//        httpServer.createContext("/", httpExchange -> {
//            String path = httpExchange.getRequestURI().getPath();
//            switch (path) {
//                case "/":
//                    startPage(messages, httpExchange);
//                    break;
//                case "/add":
//                    if (addMessageForm(messages, httpExchange)) return;
//                default:
//                    sendResponse(httpExchange, 404, "<h1>404 Not found</h1>");
//                    break;
//            }
//        });
//        httpServer.start();
//    }
//
//    private static boolean addMessageForm(ArrayList<String> messages, HttpExchange httpExchange) throws IOException {
//        if (!httpExchange.getRequestMethod().equalsIgnoreCase("post")) {
//            sendResponse(httpExchange, 404, "<h1>Method POST is required</h1>");
//            return true;
//        }
//
//        HashMap<String, String> parameters = decodeForm(httpExchange);
//
//        String messageParameter = parameters.get("message");
//        synchronized (messages) {
//            messages.add(messageParameter);
//        }
//
//        sendResponse(httpExchange, 200, "<h3>Added</h3>" +
//                "<p><a href='/'>Показать список</a></p>");
//        return false;
//    }
//
//    private static void startPage(ArrayList<String> messages, HttpExchange httpExchange) throws IOException {
//        String responseText = "<h1>Welcome to the Chat Room</h1>" +
//                "<h2>Messages</h2>";
//
//        synchronized (messages) {
//            for (String message : messages) {
//                responseText += "<p>" + message + "</p>";
//            }
//        }
//
//        responseText += "<form method='post' action='/add'>";
//        responseText += "<input type='text' name='message'>";
//        responseText += "<input type='submit'>";
//        responseText += "</form>";
//
//        sendResponse(httpExchange, 200, responseText);
//    }
//
//    private static HashMap<String, String> decodeForm(HttpExchange httpExchange) throws IOException {
//        HashMap<String, String> parameters = new HashMap<>();
//        String formText = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody())).readLine();
//        for (String pair : formText.split("&")) {
//            int eqIndex = pair.indexOf('=');
//
//            parameters.put(URLDecoder.decode(pair.substring(0, eqIndex), "UTF-8"),
//                    URLDecoder.decode(pair.substring(eqIndex + 1), "UTF-8"));
//        }
//        return parameters;
//    }
//
//    private static void sendResponse(HttpExchange httpExchange, int status, String responseText) throws IOException {
//        httpExchange.getResponseHeaders().set(
//                "Content-Type", "text/html; charset=UTF-8");
//        httpExchange.sendResponseHeaders(status, 0);
//        Writer output = new OutputStreamWriter(httpExchange.getResponseBody(),
//                StandardCharsets.UTF_8);
//        output.write(responseText);
//        output.close();
//    }
//}
