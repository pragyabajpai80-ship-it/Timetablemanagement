import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TimetableWebServer {

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(
                System.getenv().getOrDefault("PORT", "8080")
        );

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", port), 0
        );

        server.createContext("/", TimetableWebServer::home);
        server.createContext("/add", TimetableWebServer::add);
        server.createContext("/edit", TimetableWebServer::edit);
        server.createContext("/delete", TimetableWebServer::delete);

        server.start();

        System.out.println("Timetable Web Server running on port " + port);
    }

    // ================= HOME / VIEW =================

    private static void home(HttpExchange exchange) throws IOException {

        StringBuilder html = new StringBuilder();

        html.append(header("Timetable Management System"));

        html.append("""
                <div class="container">
                <div class="top">
                    <div>
                        <h2>Class Timetable</h2>
                        <p>B.Tech CSE | CSE-33 | Semester 5</p>
                    </div>
                    <a class="button" href="/add">+ Add Timetable</a>
                </div>

                <div class="table-box">
                <table>
                <tr>
                    <th>ID</th>
                    <th>Day</th>
                    <th>Start</th>
                    <th>End</th>
                    <th>Subject</th>
                    <th>Code</th>
                    <th>Faculty</th>
                    <th>Room</th>
                    <th>Actions</th>
                </tr>
                """);

        String sql = """
                SELECT timetable_id, day, start_time, end_time,
                       subject_name, subject_code,
                       faculty_name, room_number
                FROM timetable_details
                ORDER BY
                    FIELD(day,
                    'Monday','Tuesday','Wednesday',
                    'Thursday','Friday','Saturday'),
                    start_time
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("timetable_id");

                html.append("<tr>");

                html.append("<td>")
                        .append(id)
                        .append("</td>");

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

                html.append("<td class='actions'>")
                        .append("<a class='edit' href='/edit?id=")
                        .append(id)
                        .append("'>Edit</a>")
                        .append("<a class='delete' href='/delete?id=")
                        .append(id)
                        .append("' onclick=\"return confirm('Delete this timetable record?')\">Delete</a>")
                        .append("</td>");

                html.append("</tr>");
            }

        } catch (Exception e) {

            html.append("<tr><td colspan='9'>")
                    .append("Database Error: ")
                    .append(escape(e.getMessage()))
                    .append("</td></tr>");
        }

        html.append("""
                </table>
                </div>
                </div>
                """);

        html.append(footer());

        send(exchange, html.toString());
    }

    // ================= ADD =================

    private static void add(HttpExchange exchange) throws IOException {

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            showForm(exchange, 0);
            return;
        }

        Map<String, String> data = parseForm(exchange);

        String sql = """
                INSERT INTO timetable
                (class_id, subject_id, faculty_id, room_id,
                 day, start_time, end_time)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(data.get("class_id")));
            ps.setInt(2, Integer.parseInt(data.get("subject_id")));

            setNullableInt(ps, 3, data.get("faculty_id"));
            setNullableInt(ps, 4, data.get("room_id"));

            ps.setString(5, data.get("day"));
            ps.setTime(6, Time.valueOf(data.get("start_time") + ":00"));
            ps.setTime(7, Time.valueOf(data.get("end_time") + ":00"));

            ps.executeUpdate();

            redirect(exchange, "/");

        } catch (Exception e) {
            error(exchange, e);
        }
    }

    // ================= EDIT =================

    private static void edit(HttpExchange exchange) throws IOException {

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {

            Map<String, String> query =
                    parseParameters(exchange.getRequestURI().getRawQuery());

            String id = query.get("id");

            if (id == null) {
                redirect(exchange, "/");
                return;
            }

            showForm(exchange, Integer.parseInt(id));
            return;
        }

        Map<String, String> data = parseForm(exchange);

        String sql = """
                UPDATE timetable
                SET class_id = ?,
                    subject_id = ?,
                    faculty_id = ?,
                    room_id = ?,
                    day = ?,
                    start_time = ?,
                    end_time = ?
                WHERE timetable_id = ?
                """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(data.get("class_id")));
            ps.setInt(2, Integer.parseInt(data.get("subject_id")));

            setNullableInt(ps, 3, data.get("faculty_id"));
            setNullableInt(ps, 4, data.get("room_id"));

            ps.setString(5, data.get("day"));
            ps.setTime(6, Time.valueOf(data.get("start_time") + ":00"));
            ps.setTime(7, Time.valueOf(data.get("end_time") + ":00"));
            ps.setInt(8, Integer.parseInt(data.get("timetable_id")));

            ps.executeUpdate();

            redirect(exchange, "/");

        } catch (Exception e) {
            error(exchange, e);
        }
    }

    // ================= DELETE =================

    private static void delete(HttpExchange exchange) throws IOException {

        Map<String, String> query =
                parseParameters(exchange.getRequestURI().getRawQuery());

        String idText = query.get("id");

        if (idText == null) {
            redirect(exchange, "/");
            return;
        }

        int id = Integer.parseInt(idText);

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                /*
                 * timetable_faculty references timetable.
                 * Therefore delete child records first.
                 */

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM timetable_faculty WHERE timetable_id = ?")) {

                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM timetable WHERE timetable_id = ?")) {

                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                con.commit();

            } catch (Exception e) {

                con.rollback();
                throw e;
            }

            redirect(exchange, "/");

        } catch (Exception e) {
            error(exchange, e);
        }
    }

    // ================= FORM =================

    private static void showForm(
            HttpExchange exchange, int id) throws IOException {

        String classId = "";
        String subjectId = "";
        String facultyId = "";
        String roomId = "";
        String day = "Monday";
        String start = "09:00";
        String end = "10:00";

        if (id != 0) {

            String sql = """
                    SELECT class_id, subject_id, faculty_id,
                           room_id, day, start_time, end_time
                    FROM timetable
                    WHERE timetable_id = ?
                    """;

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        classId = String.valueOf(
                                rs.getInt("class_id"));

                        subjectId = String.valueOf(
                                rs.getInt("subject_id"));

                        if (rs.getObject("faculty_id") != null) {
                            facultyId = String.valueOf(
                                    rs.getInt("faculty_id"));
                        }

                        if (rs.getObject("room_id") != null) {
                            roomId = String.valueOf(
                                    rs.getInt("room_id"));
                        }

                        day = rs.getString("day");

                        start = rs.getTime("start_time")
                                .toLocalTime()
                                .toString()
                                .substring(0, 5);

                        end = rs.getTime("end_time")
                                .toLocalTime()
                                .toString()
                                .substring(0, 5);
                    }
                }

            } catch (Exception e) {
                error(exchange, e);
                return;
            }
        }

        String action = id == 0 ? "/add" : "/edit";
        String title = id == 0 ? "Add Timetable" : "Edit Timetable";

        StringBuilder html = new StringBuilder();

        html.append(header(title));

        html.append("<div class='form-box'>");

        html.append("<h2>")
                .append(title)
                .append("</h2>");

        html.append("<form method='POST' action='")
                .append(action)
                .append("'>");

        if (id != 0) {

            html.append("<input type='hidden' name='timetable_id' value='")
                    .append(id)
                    .append("'>");
        }

        // CLASS

        html.append("<label>Class</label>");
        html.append("<select name='class_id' required>");

        appendClasses(html, classId);

        html.append("</select>");

        // SUBJECT

        html.append("<label>Subject</label>");
        html.append("<select name='subject_id' required>");

        appendSubjects(html, subjectId);

        html.append("</select>");

        // FACULTY

        html.append("<label>Faculty</label>");
        html.append("<select name='faculty_id'>");

        html.append("<option value=''>Not Assigned</option>");

        appendFaculty(html, facultyId);

        html.append("</select>");

        // ROOM

        html.append("<label>Room</label>");
        html.append("<select name='room_id'>");

        html.append("<option value=''>Not Assigned</option>");

        appendRooms(html, roomId);

        html.append("</select>");

        // DAY

        html.append("<label>Day</label>");
        html.append("<select name='day' required>");

        String[] days = {
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        };

        for (String d : days) {

            html.append("<option value='")
                    .append(d)
                    .append("'");

            if (d.equals(day)) {
                html.append(" selected");
            }

            html.append(">")
                    .append(d)
                    .append("</option>");
        }

        html.append("</select>");

        // TIME

        html.append("<label>Start Time</label>");
        html.append("<input type='time' name='start_time' value='")
                .append(start)
                .append("' required>");

        html.append("<label>End Time</label>");
        html.append("<input type='time' name='end_time' value='")
                .append(end)
                .append("' required>");

        html.append("<button type='submit'>Save</button>");

        html.append("<a class='cancel' href='/'>Cancel</a>");

        html.append("</form>");
        html.append("</div>");

        html.append(footer());

        send(exchange, html.toString());
    }

    // ================= DROPDOWNS =================

    private static void appendClasses(
            StringBuilder html, String selected) {

        String sql =
                "SELECT class_id, class_name, section, semester " +
                "FROM class_section ORDER BY class_id";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String id = String.valueOf(
                        rs.getInt("class_id"));

                html.append("<option value='")
                        .append(id)
                        .append("'");

                if (id.equals(selected)) {
                    html.append(" selected");
                }

                html.append(">");

                html.append(escape(rs.getString("class_name")))
                        .append(" - ")
                        .append(escape(rs.getString("section")))
                        .append(" (Semester ")
                        .append(rs.getInt("semester"))
                        .append(")");

                html.append("</option>");
            }

        } catch (Exception e) {
            html.append("<option>Error loading classes</option>");
        }
    }

    private static void appendSubjects(
            StringBuilder html, String selected) {

        String sql =
                "SELECT subject_id, subject_name, subject_code " +
                "FROM subject ORDER BY subject_name";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String id = String.valueOf(
                        rs.getInt("subject_id"));

                html.append("<option value='")
                        .append(id)
                        .append("'");

                if (id.equals(selected)) {
                    html.append(" selected");
                }

                html.append(">");

                html.append(escape(rs.getString("subject_name")))
                        .append(" (")
                        .append(escape(rs.getString("subject_code")))
                        .append(")");

                html.append("</option>");
            }

        } catch (Exception e) {
            html.append("<option>Error loading subjects</option>");
        }
    }

    private static void appendFaculty(
            StringBuilder html, String selected) {

        String sql =
                "SELECT faculty_id, faculty_name " +
                "FROM faculty ORDER BY faculty_name";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String id = String.valueOf(
                        rs.getInt("faculty_id"));

                html.append("<option value='")
                        .append(id)
                        .append("'");

                if (id.equals(selected)) {
                    html.append(" selected");
                }

                html.append(">");

                html.append(escape(
                        rs.getString("faculty_name")));

                html.append("</option>");
            }

        } catch (Exception e) {
            html.append("<option>Error loading faculty</option>");
        }
    }

    private static void appendRooms(
            StringBuilder html, String selected) {

        String sql =
                "SELECT room_id, room_number " +
                "FROM room ORDER BY room_number";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String id = String.valueOf(
                        rs.getInt("room_id"));

                html.append("<option value='")
                        .append(id)
                        .append("'");

                if (id.equals(selected)) {
                    html.append(" selected");
                }

                html.append(">");

                html.append(escape(
                        rs.getString("room_number")));

                html.append("</option>");
            }

        } catch (Exception e) {
            html.append("<option>Error loading rooms</option>");
        }
    }

    // ================= HELPERS =================

    private static void setNullableInt(
            PreparedStatement ps,
            int index,
            String value) throws SQLException {

        if (value == null || value.trim().isEmpty()) {

            ps.setNull(index, Types.INTEGER);

        } else {

            ps.setInt(index, Integer.parseInt(value));
        }
    }

        private static Map<String, String> parseForm(
            HttpExchange exchange) throws IOException {

        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        return parseParameters(body);
    }

    private static Map<String, String> parseParameters(
            String data) {

        Map<String, String> map = new HashMap<>();

        if (data == null || data.isEmpty()) {
            return map;
        }

        for (String pair : data.split("&")) {

            String[] parts = pair.split("=", 2);

            if (parts.length == 2) {

                String key = URLDecoder.decode(
                        parts[0],
                        StandardCharsets.UTF_8
                );

                String value = URLDecoder.decode(
                        parts[1],
                        StandardCharsets.UTF_8
                );

                map.put(key, value);
            }
        }

        return map;
    }

    private static void redirect(
            HttpExchange exchange,
            String location) throws IOException {

        exchange.getResponseHeaders()
                .set("Location", location);

        exchange.sendResponseHeaders(303, -1);
        exchange.close();
    }

    private static void error(
            HttpExchange exchange,
            Exception e) throws IOException {

        String html =
                header("Error") +
                "<div class='form-box'>" +
                "<h2>Operation Failed</h2>" +
                "<p class='error'>" +
                escape(e.getMessage()) +
                "</p>" +
                "<a href='/'>Back to Timetable</a>" +
                "</div>" +
                footer();

        send(exchange, html);
    }

    private static String header(String title) {

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1">

                    <title>
                """ + escape(title) + """
                    </title>

                    <style>
                        * {
                            box-sizing: border-box;
                        }

                        body {
                            margin: 0;
                            font-family: Arial, sans-serif;
                            background: #f3f4f6;
                            color: #222;
                        }

                        header {
                            background: #1f2937;
                            color: white;
                            text-align: center;
                            padding: 25px;
                        }

                        .container {
                            max-width: 1400px;
                            margin: 30px auto;
                            padding: 20px;
                        }

                        .top {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            margin-bottom: 20px;
                        }

                        .button,
                        button {
                            background: #2563eb;
                            color: white;
                            padding: 11px 18px;
                            border: none;
                            border-radius: 5px;
                            text-decoration: none;
                            cursor: pointer;
                        }

                        .table-box {
                            overflow-x: auto;
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                            background: white;
                            box-shadow: 0 2px 8px rgba(0,0,0,.1);
                        }

                        th,
                        td {
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

                        .edit {
                            color: #2563eb;
                            text-decoration: none;
                            margin-right: 10px;
                        }

                        .delete {
                            color: #dc2626;
                            text-decoration: none;
                        }

                        .form-box {
                            max-width: 650px;
                            margin: 40px auto;
                            background: white;
                            padding: 30px;
                            border-radius: 8px;
                            box-shadow: 0 2px 10px rgba(0,0,0,.1);
                        }

                        label {
                            display: block;
                            margin-top: 15px;
                            margin-bottom: 6px;
                            font-weight: bold;
                        }

                        input,
                        select {
                            width: 100%;
                            padding: 10px;
                            border: 1px solid #ccc;
                            border-radius: 5px;
                        }

                        form button {
                            margin-top: 20px;
                        }

                        .cancel {
                            margin-left: 15px;
                            color: #555;
                        }

                        .error {
                            background: #fee2e2;
                            color: #991b1b;
                            padding: 15px;
                            border-radius: 5px;
                        }

                        @media(max-width: 700px) {
                            .top {
                                flex-direction: column;
                                align-items: flex-start;
                                gap: 15px;
                            }

                            table {
                                min-width: 1000px;
                            }
                        }
                    </style>
                </head>

                <body>

                <header>
                    <h1>Timetable Management System</h1>
                    <p>B.Tech CSE | CSE-33 | Semester 5</p>
                </header>
                """;
    }

    private static String footer() {
        return """
                </body>
                </html>
                """;
    }

    private static String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static void send(
            HttpExchange exchange,
            String html) throws IOException {

        byte[] data =
                html.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set("Content-Type",
                        "text/html; charset=UTF-8");

        exchange.sendResponseHeaders(
                200,
                data.length
        );

        try (OutputStream out =
                     exchange.getResponseBody()) {

            out.write(data);
        }
    }
}
