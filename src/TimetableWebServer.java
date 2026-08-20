import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class TimetableWebServer {

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080")
        );

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 0
        );

        server.createContext("/", TimetableWebServer::handleHome);

        server.start();

        System.out.println("Timetable Web Server running on port " + port);
    }

    private static void handleHome(HttpExchange exchange) throws IOException {

        StringBuilder html = new StringBuilder();

        html.append("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Timetable Management System</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        background: #f4f6f8;
                    }

                    header {
                        background: #1f2937;
                        color: white;
                        padding: 25px;
                        text-align: center;
                    }

                    .container {
                        max-width: 1200px;
                        margin: 30px auto;
                        padding: 20px;
                    }

                    table {
                        width: 100%;
                        border-collapse: collapse;
                        background: white;
                        box-shadow: 0 2px 8px rgba(0,0,0,.1);
                    }

                    th, td {
                        padding: 12px;
                        border: 1px solid #ddd;
                        text-align: left;
                    }

                    th {
                        background: #374151;
                        color: white;
                    }

                    tr:nth-child(even) {
                        background: #f9fafb;
                    }

                    .error {
                        background: #fee2e2;
                        color: #991b1b;
                        padding: 15px;
                        border-radius: 6px;
                    }
                </style>
            </head>
            <body>

            <header>
                <h1>Timetable Management System</h1>
                <p>B.Tech CSE - CSE-33 - Semester 5</p>
            </header>

            <div class="container">
                <h2>Class Timetable</h2>
                <table>
                    <tr>
                        <th>Day</th>
                        <th>Start</th>
                        <th>End</th>
                        <th>Subject</th>
                        <th>Code</th>
                        <th>Faculty</th>
                        <th>Room</th>
                    </tr>
            """);

        String sql = """
            SELECT day, start_time, end_time,
                   subject_name, subject_code,
                   faculty_name, room_number
            FROM timetable_details
            ORDER BY
                FIELD(day, 'Monday','Tuesday','Wednesday',
                           'Thursday','Friday','Saturday'),
                start_time
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                html.append("<tr>");

                html.append("<td>")
                    .append(escape(rs.getString("day")))
                    .append("</td>");

                html.append("<td>")
                    .append(escape(rs.getString("start_time")))
                    .append("</td>");

                html.append("<td>")
                    .append(escape(rs.getString("end_time")))
                    .append("</td>");

                html.append("<td>")
                    .append(escape(rs.getString("subject_name")))
                    .append("</td>");

                html.append("<td>")
                    .append(escape(rs.getString("subject_code")))
                    .append("</td>");

                html.append("<td>")
                    .append(escape(rs.getString("faculty_name")))
                    .append("</td>");

                html.append("<td>")
                    .append(escape(rs.getString("room_number")))
                    .append("</td>");

                html.append("</tr>");
            }

        } catch (Exception e) {

            html.append("""
                </table>
                <div class="error">
                    Database error: 
                """);

            html.append(escape(e.getMessage()));

            html.append("</div>");
            send(exchange, html.toString());
            return;
        }

        html.append("""
                </table>
            </div>

            </body>
            </html>
            """);

        send(exchange, html.toString());
    }

    private static void send(HttpExchange exchange, String html)
            throws IOException {

        byte[] response = html.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");

        exchange.sendResponseHeaders(200, response.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    private static String escape(String value) {

        if (value == null)
            return "";

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
