import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class TimetableDashboard extends JFrame {

    // =========================================================
    // DATABASE
    // =========================================================

    private static final String URL =
            "jdbc:mariadb://localhost:3306/timetable_management";

    private static final String USER =
            "timetable_user";

    private static final String PASSWORD =
            "timetable123";

    // =========================================================
    // COLORS
    // =========================================================

    private final Color PURPLE =
            new Color(139, 92, 246);

    private final Color DARK_PURPLE =
            new Color(105, 65, 205);

    private final Color LIGHT_PURPLE =
            new Color(245, 240, 255);

    private final Color BACKGROUND =
            new Color(247, 248, 252);

    private final Color WHITE =
            Color.WHITE;

    private final Color TEXT =
            new Color(38, 38, 50);

    private final Color MUTED =
            new Color(125, 125, 140);

    private final Color BORDER =
            new Color(232, 232, 238);

    private final Color GREEN =
            new Color(34, 197, 94);

    private final Color RED =
            new Color(239, 68, 68);

    private final Color BLUE =
            new Color(59, 130, 246);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JPanel pagePanel;

    private JTable timetableTable;

    private DefaultTableModel timetableModel;

    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField searchField;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public TimetableDashboard() {

        setTitle("Timetable Management System");

        setSize(1280, 780);

        setMinimumSize(
                new Dimension(1050, 650)
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        buildInterface();
    }

    // =========================================================
    // DATABASE CONNECTION
    // =========================================================

    private Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    // =========================================================
    // MAIN INTERFACE
    // =========================================================

    private void buildInterface() {

        JPanel main =
                new JPanel(
                        new BorderLayout()
                );

        main.setBackground(
                BACKGROUND
        );

        main.add(
                createSidebar(),
                BorderLayout.WEST
        );

        JPanel right =
                new JPanel(
                        new BorderLayout()
                );

        right.setBackground(
                BACKGROUND
        );

        right.add(
                createTopBar(),
                BorderLayout.NORTH
        );

        pagePanel =
                new JPanel(
                        new BorderLayout()
                );

        pagePanel.setBackground(
                BACKGROUND
        );

        right.add(
                pagePanel,
                BorderLayout.CENTER
        );

        main.add(
                right,
                BorderLayout.CENTER
        );

        add(main);

        showDashboard();
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(220, 780)
        );

        sidebar.setBackground(
                PURPLE
        );

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        sidebar.add(
                Box.createVerticalStrut(25)
        );

        JLabel logo =
                new JLabel("TM");

        logo.setForeground(
                Color.WHITE
        );

        logo.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        32
                )
        );

        logo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        sidebar.add(logo);

        JLabel title =
                new JLabel("Timetable");

        title.setForeground(
                Color.WHITE
        );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        sidebar.add(title);

        JLabel small =
                new JLabel(
                        "MANAGEMENT SYSTEM"
                );

        small.setForeground(
                new Color(
                        235,
                        225,
                        255
                )
        );

        small.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        9
                )
        );

        small.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        sidebar.add(small);

        sidebar.add(
                Box.createVerticalStrut(30)
        );

        sidebar.add(
                menuButton(
                        "Dashboard",
                        "D"
                )
        );

        sidebar.add(
                menuButton(
                        "Timetable",
                        "T"
                )
        );

        sidebar.add(
                menuButton(
                        "Add Class",
                        "+"
                )
        );

        sidebar.add(
                menuButton(
                        "Subjects",
                        "S"
                )
        );

        sidebar.add(
                menuButton(
                        "Faculty",
                        "F"
                )
        );

        sidebar.add(
                menuButton(
                        "Rooms",
                        "R"
                )
        );

        sidebar.add(
                Box.createVerticalGlue()
        );

        sidebar.add(
                menuButton(
                        "Refresh",
                        "R"
                )
        );

        sidebar.add(
                menuButton(
                        "Settings",
                        "S"
                )
        );

        sidebar.add(
                menuButton(
                        "Exit",
                        "X"
                )
        );

        sidebar.add(
                Box.createVerticalStrut(20)
        );

        return sidebar;
    }

    // =========================================================
    // SIDEBAR BUTTON
    // =========================================================

    private JButton menuButton(
            String text,
            String icon) {

        JButton button =
                new JButton(
                        icon + "    " + text
                );

        button.setMaximumSize(
                new Dimension(
                        205,
                        46
                )
        );

        button.setPreferredSize(
                new Dimension(
                        205,
                        46
                )
        );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                PURPLE
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        25,
                        0,
                        10
                )
        );

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        button.setBackground(
                                DARK_PURPLE
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        button.setBackground(
                                PURPLE
                        );
                    }
                }
        );

        if (text.equals("Dashboard")) {

            button.addActionListener(
                    e -> showDashboard()
            );

        } else if (text.equals("Timetable")) {

            button.addActionListener(
                    e -> showTimetable()
            );

        } else if (text.equals("Add Class")) {

            button.addActionListener(
                    e -> showAddClass()
            );

        } else if (text.equals("Subjects")) {

            button.addActionListener(
                    e -> showSubjects()
            );

        } else if (text.equals("Faculty")) {

            button.addActionListener(
                    e -> showFaculty()
            );

        } else if (text.equals("Rooms")) {

            button.addActionListener(
                    e -> showRooms()
            );

        } else if (text.equals("Refresh")) {

            button.addActionListener(
                    e -> showDashboard()
            );

        } else if (text.equals("Settings")) {

            button.addActionListener(
                    e -> JOptionPane.showMessageDialog(
                            this,
                            "Timetable Management System\n"
                                    + "Java Swing + MariaDB\n\n"
                                    + "Database: timetable_management",
                            "Settings",
                            JOptionPane.INFORMATION_MESSAGE
                    )
            );

        } else if (text.equals("Exit")) {

            button.addActionListener(
                    e -> System.exit(0)
            );
        }

        return button;
    }

    // =========================================================
    // TOP BAR
    // =========================================================

    private JPanel createTopBar() {

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(
                WHITE
        );

        top.setPreferredSize(
                new Dimension(
                        0,
                        70
                )
        );

        top.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        1,
                        0,
                        BORDER
                )
        );

        searchField =
                new JTextField();

        searchField.setToolTipText(
                "Search timetable"
        );

        searchField.setPreferredSize(
                new Dimension(
                        330,
                        38
                )
        );

        searchField.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                0,
                                12,
                                0,
                                12
                        )
                )
        );

        searchField.addActionListener(
                e -> filterTable()
        );

        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                25,
                                15
                        )
                );

        left.setBackground(
                WHITE
        );

        left.add(searchField);

        JButton searchButton =
                new JButton("Search");

        searchButton.setBackground(
                PURPLE
        );

        searchButton.setForeground(
                Color.WHITE
        );

        searchButton.setFocusPainted(
                false
        );

        searchButton.addActionListener(
                e -> filterTable()
        );

        left.add(searchButton);

        top.add(
                left,
                BorderLayout.WEST
        );

        JPanel right =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                20,
                                13
                        )
                );

        right.setBackground(
                WHITE
        );

        JLabel profile =
                new JLabel("P");

        profile.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        profile.setPreferredSize(
                new Dimension(
                        36,
                        36
                )
        );

        profile.setOpaque(true);

        profile.setBackground(
                LIGHT_PURPLE
        );

        profile.setForeground(
                PURPLE
        );

        profile.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        JLabel user =
                new JLabel(
                        "<html><b>Pragya</b><br>"
                                + "<font color='#888888'>Student</font>"
                                + "</html>"
                );

        right.add(profile);

        right.add(user);

        top.add(
                right,
                BorderLayout.EAST
        );

        return top;
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private void showDashboard() {

        JPanel page =
                createPagePanel(
                        "Good Morning, Pragya!",
                        "Here is what's happening with your timetable."
                );

        JPanel main =
                new JPanel(
                        new BorderLayout(
                                0,
                                18
                        )
                );

        main.setOpaque(false);

        main.add(
                createStatistics(),
                BorderLayout.NORTH
        );

        main.add(
                createDashboardTimetable(),
                BorderLayout.CENTER
        );

        page.add(
                main,
                BorderLayout.CENTER
        );

        setPage(page);
    }

    // =========================================================
    // STATISTICS
    // =========================================================

    private JPanel createStatistics() {

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                0
                        )
                );

        cards.setOpaque(false);

        cards.add(
                createStatCard(
                        "Classes",
                        getCount("timetable"),
                        PURPLE
                )
        );

        cards.add(
                createStatCard(
                        "Subjects",
                        getCount("subject"),
                        BLUE
                )
        );

        cards.add(
                createStatCard(
                        "Faculty",
                        getCount("faculty"),
                        GREEN
                )
        );

        cards.add(
                createStatCard(
                        "Rooms",
                        getCount("room"),
                        new Color(
                                245,
                                166,
                                65
                        )
                )
        );

        return cards;
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String title,
            int number,
            Color accent) {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        WHITE
                );

        card.setLayout(
                new BorderLayout()
        );

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        20,
                        18,
                        20
                )
        );

        JLabel icon =
                new JLabel(
                        title.substring(0, 1),
                        SwingConstants.CENTER
                );

        icon.setPreferredSize(
                new Dimension(
                        45,
                        45
                )
        );

        icon.setOpaque(true);

        icon.setBackground(
                new Color(
                        accent.getRed(),
                        accent.getGreen(),
                        accent.getBlue(),
                        35
                )
        );

        icon.setForeground(
                accent
        );

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setForeground(
                MUTED
        );

        JLabel numberLabel =
                new JLabel(
                        String.valueOf(number)
                );

        numberLabel.setForeground(
                TEXT
        );

        numberLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        23
                )
        );

        text.add(titleLabel);

        text.add(
                Box.createVerticalStrut(3)
        );

        text.add(numberLabel);

        card.add(
                icon,
                BorderLayout.WEST
        );

        card.add(
                Box.createHorizontalStrut(15),
                BorderLayout.CENTER
        );

        card.add(
                text,
                BorderLayout.EAST
        );

        return card;
    }

    // =========================================================
    // TIMETABLE CARD
    // =========================================================

    private JPanel createDashboardTimetable() {

        RoundedPanel card =
                new RoundedPanel(
                        18,
                        WHITE
                );

        card.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        card.setBorder(
                BorderFactory.createEmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        JLabel title =
                new JLabel(
                        "Timetable Records"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        19
                )
        );

        title.setForeground(
                TEXT
        );

        card.add(
                title,
                BorderLayout.NORTH
        );

        timetableModel =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "CLASS",
                                "SECTION",
                                "DAY",
                                "TIME",
                                "SUBJECT",
                                "FACULTY",
                                "ROOM"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        timetableTable =
                new JTable(
                        timetableModel
                );

        styleTable(
                timetableTable
        );

        sorter =
                new TableRowSorter<>(
                        timetableModel
                );

        timetableTable.setRowSorter(
                sorter
        );

        timetableTable.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e) {

                        if (e.getClickCount() == 2) {

                            editSelectedClass();
                        }
                    }
                }
        );

        JScrollPane scroll =
                new JScrollPane(
                        timetableTable
                );

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        card.add(
                scroll,
                BorderLayout.CENTER
        );

        loadTimetableData();

        return card;
    }

    // =========================================================
    // LOAD TIMETABLE
    // =========================================================

    private void loadTimetableData() {

        if (timetableModel == null) {
            return;
        }

        timetableModel.setRowCount(0);

        String sql =
                "SELECT "
                        + "t.timetable_id, "
                        + "c.class_name, "
                        + "c.section, "
                        + "t.day, "
                        + "t.start_time, "
                        + "t.end_time, "
                        + "s.subject_name, "
                        + "COALESCE(f.faculty_name,'-') AS faculty_name, "
                        + "COALESCE(r.room_number,'-') AS room_number "
                        + "FROM timetable t "
                        + "JOIN class_section c "
                        + "ON t.class_id = c.class_id "
                        + "JOIN subject s "
                        + "ON t.subject_id = s.subject_id "
                        + "LEFT JOIN faculty f "
                        + "ON t.faculty_id = f.faculty_id "
                        + "LEFT JOIN room r "
                        + "ON t.room_id = r.room_id "
                        + "ORDER BY FIELD(t.day,"
                        + "'Monday','Tuesday','Wednesday',"
                        + "'Thursday','Friday','Saturday','Sunday'), "
                        + "t.start_time";

        try (
                Connection con =
                        getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                timetableModel.addRow(
                        new Object[]{
                                rs.getInt(
                                        "timetable_id"
                                ),
                                rs.getString(
                                        "class_name"
                                ),
                                rs.getString(
                                        "section"
                                ),
                                rs.getString(
                                        "day"
                                ),
                                rs.getString(
                                        "start_time"
                                )
                                        + " - "
                                        + rs.getString(
                                                "end_time"
                                        ),
                                rs.getString(
                                        "subject_name"
                                ),
                                rs.getString(
                                        "faculty_name"
                                ),
                                rs.getString(
                                        "room_number"
                                )
                        }
                );
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =========================================================
    // TIMETABLE PAGE
    // =========================================================

    private void showTimetable() {

        JPanel page =
                createPagePanel(
                        "Class Timetable",
                        "View, edit and delete timetable records"
                );

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        panel.setOpaque(false);

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        buttons.setOpaque(false);

        JButton add =
                createButton(
                        "Add Class",
                        PURPLE
                );

        JButton edit =
                createButton(
                        "Edit Selected",
                        BLUE
                );

        JButton delete =
                createButton(
                        "Delete Selected",
                        RED
                );

        JButton refresh =
                createButton(
                        "Refresh",
                        GREEN
                );

        add.addActionListener(
                e -> showAddClass()
        );

        edit.addActionListener(
                e -> editSelectedClass()
        );

        delete.addActionListener(
                e -> deleteSelectedClass()
        );

        refresh.addActionListener(
                e -> {
                    loadTimetableData();
                    showTimetable();
                }
        );

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(refresh);

        panel.add(
                buttons,
                BorderLayout.NORTH
        );

        panel.add(
                createDashboardTimetable(),
                BorderLayout.CENTER
        );

        page.add(
                panel,
                BorderLayout.CENTER
        );

        setPage(page);
    }

    // =========================================================
    // ADD CLASS
    // =========================================================

    private void showAddClass() {

        JPanel page =
                createPagePanel(
                        "Add Class",
                        "Create a new timetable entry"
                );

        JPanel form =
                createFormPanel();

        JTextField classId =
                new JTextField();

        JTextField subjectId =
                new JTextField();

        JTextField facultyId =
                new JTextField();

        JTextField roomId =
                new JTextField();

        JComboBox<String> day =
                new JComboBox<>(
                        new String[]{
                                "Monday",
                                "Tuesday",
                                "Wednesday",
                                "Thursday",
                                "Friday",
                                "Saturday"
                        }
                );

        JTextField start =
                new JTextField(
                        "09:00:00"
                );

        JTextField end =
                new JTextField(
                        "10:00:00"
                );

        addField(
                form,
                "Class ID *",
                classId
        );

        addField(
                form,
                "Subject ID *",
                subjectId
        );

        addField(
                form,
                "Faculty ID",
                facultyId
        );

        addField(
                form,
                "Room ID",
                roomId
        );

        addField(
                form,
                "Day",
                day
        );

        addField(
                form,
                "Start Time (HH:MM:SS)",
                start
        );

        addField(
                form,
                "End Time (HH:MM:SS)",
                end
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        buttons.setOpaque(false);

        JButton save =
                createButton(
                        "Save Class",
                        PURPLE
                );

        JButton clear =
                createButton(
                        "Clear",
                        MUTED
                );

        JButton back =
                createButton(
                        "Back",
                        BLUE
                );

        save.addActionListener(
                e -> {

                    boolean success =
                            insertTimetable(
                                    classId.getText(),
                                    subjectId.getText(),
                                    facultyId.getText(),
                                    roomId.getText(),
                                    day.getSelectedItem().toString(),
                                    start.getText(),
                                    end.getText()
                            );

                    if (success) {

                        classId.setText("");
                        subjectId.setText("");
                        facultyId.setText("");
                        roomId.setText("");
                        start.setText("09:00:00");
                        end.setText("10:00:00");

                        showTimetable();
                    }
                }
        );

        clear.addActionListener(
                e -> {

                    classId.setText("");
                    subjectId.setText("");
                    facultyId.setText("");
                    roomId.setText("");
                    start.setText("");
                    end.setText("");
                }
        );

        back.addActionListener(
                e -> showTimetable()
        );

        buttons.add(save);
        buttons.add(clear);
        buttons.add(back);

        JPanel wrapper =
                new JPanel(
                        new BorderLayout()
                );

        wrapper.setOpaque(false);

        wrapper.add(
                form,
                BorderLayout.NORTH
        );

        wrapper.add(
                buttons,
                BorderLayout.SOUTH
        );

        page.add(
                wrapper,
                BorderLayout.NORTH
        );

        setPage(page);
    }

    // =========================================================
    // INSERT
    // =========================================================

    private boolean insertTimetable(
            String classIdText,
            String subjectIdText,
            String facultyIdText,
            String roomIdText,
            String day,
            String start,
            String end) {

        try {
            int classId = Integer.parseInt(classIdText.trim());
            int subjectId = Integer.parseInt(subjectIdText.trim());

            Integer facultyId = parseNullableInteger(facultyIdText);
            Integer roomId = parseNullableInteger(roomIdText);

            if (start.trim().isEmpty() || end.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Start and end time are required."
                );
                return false;
            }

            if (start.compareTo(end) >= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "End time must be after start time."
                );
                return false;
            }

            String sql =
                    "INSERT INTO timetable " +
                    "(class_id, subject_id, faculty_id, room_id, day, start_time, end_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, classId);
                ps.setInt(2, subjectId);

                if (facultyId == null)
                    ps.setNull(3, java.sql.Types.INTEGER);
                else
                    ps.setInt(3, facultyId);

                if (roomId == null)
                    ps.setNull(4, java.sql.Types.INTEGER);
                else
                    ps.setInt(4, roomId);

                ps.setString(5, day);
                ps.setString(6, start.trim());
                ps.setString(7, end.trim());

                int affected = ps.executeUpdate();

                if (affected == 1) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Timetable added successfully!"
                    );
                    showTimetable();
                    return true;
                }
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Class ID and Subject ID must be numbers."
            );

        } catch (SQLException e) {

            showError(e);
        }

        return false;
    }


    // =========================================================
    // EDIT SELECTED TIMETABLE
    // =========================================================

    private void editSelectedClass() {

        int row = timetableTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a timetable record first."
            );
            return;
        }

        int modelRow = timetableTable.convertRowIndexToModel(row);

        int timetableId = Integer.parseInt(
                timetableTable.getModel()
                        .getValueAt(modelRow, 0)
                        .toString()
        );

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT class_id, subject_id, faculty_id, room_id, " +
                     "day, start_time, end_time " +
                     "FROM timetable WHERE timetable_id=?")) {

            ps.setInt(1, timetableId);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Timetable record not found."
                    );
                    return;
                }

                JTextField classId =
                        new JTextField(String.valueOf(rs.getInt("class_id")));

                JTextField subjectId =
                        new JTextField(String.valueOf(rs.getInt("subject_id")));

                int facultyValue = rs.getInt("faculty_id");
                JTextField facultyId =
                        new JTextField(
                                rs.wasNull() ? "" : String.valueOf(facultyValue)
                        );

                int roomValue = rs.getInt("room_id");
                JTextField roomId =
                        new JTextField(
                                rs.wasNull() ? "" : String.valueOf(roomValue)
                        );

                JComboBox<String> day =
                        new JComboBox<>(
                                new String[]{
                                        "Monday",
                                        "Tuesday",
                                        "Wednesday",
                                        "Thursday",
                                        "Friday",
                                        "Saturday"
                                }
                        );

                day.setSelectedItem(rs.getString("day"));

                JTextField start =
                        new JTextField(rs.getString("start_time"));

                JTextField end =
                        new JTextField(rs.getString("end_time"));

                JPanel panel = new JPanel(
                        new GridLayout(0, 2, 8, 8)
                );

                panel.add(new JLabel("Class ID:"));
                panel.add(classId);

                panel.add(new JLabel("Subject ID:"));
                panel.add(subjectId);

                panel.add(new JLabel("Faculty ID:"));
                panel.add(facultyId);

                panel.add(new JLabel("Room ID:"));
                panel.add(roomId);

                panel.add(new JLabel("Day:"));
                panel.add(day);

                panel.add(new JLabel("Start Time:"));
                panel.add(start);

                panel.add(new JLabel("End Time:"));
                panel.add(end);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Edit Timetable #" + timetableId,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                updateTimetable(
                        timetableId,
                        classId.getText(),
                        subjectId.getText(),
                        facultyId.getText(),
                        roomId.getText(),
                        day.getSelectedItem().toString(),
                        start.getText(),
                        end.getText()
                );
            }

        } catch (SQLException e) {
            showError(e);
        }
    }

    // =========================================================
    // UPDATE DATABASE
    // =========================================================

    private void updateTimetable(
            int timetableId,
            String classIdText,
            String subjectIdText,
            String facultyIdText,
            String roomIdText,
            String day,
            String start,
            String end) {

        try {
            int classId = Integer.parseInt(classIdText.trim());
            int subjectId = Integer.parseInt(subjectIdText.trim());

            Integer facultyId = parseNullableInteger(facultyIdText);
            Integer roomId = parseNullableInteger(roomIdText);

            if (start.trim().isEmpty() || end.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Start and end time are required."
                );
                return;
            }

            if (start.compareTo(end) >= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "End time must be after start time."
                );
                return;
            }

            String sql =
                    "UPDATE timetable SET " +
                    "class_id=?, subject_id=?, faculty_id=?, room_id=?, " +
                    "day=?, start_time=?, end_time=? " +
                    "WHERE timetable_id=?";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, classId);
                ps.setInt(2, subjectId);

                if (facultyId == null)
                    ps.setNull(3, java.sql.Types.INTEGER);
                else
                    ps.setInt(3, facultyId);

                if (roomId == null)
                    ps.setNull(4, java.sql.Types.INTEGER);
                else
                    ps.setInt(4, roomId);

                ps.setString(5, day);
                ps.setString(6, start.trim());
                ps.setString(7, end.trim());
                ps.setInt(8, timetableId);

                int affected = ps.executeUpdate();

                if (affected == 1) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Timetable updated successfully!"
                    );
                    showTimetable();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No timetable record was updated."
                    );
                }
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Class ID and Subject ID must be numbers."
            );

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void deleteSelectedClass() {

        int row = timetableTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a timetable record first."
            );
            return;
        }

        int modelRow =
                timetableTable.convertRowIndexToModel(row);

        int timetableId =
                Integer.parseInt(
                        timetableTable.getModel()
                                .getValueAt(modelRow, 0)
                                .toString()
                );

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete timetable record #" +
                        timetableId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                // Delete dependent records first.
                try (PreparedStatement ps =
                             con.prepareStatement(
                                     "DELETE FROM timetable_faculty " +
                                     "WHERE timetable_id=?")) {

                    ps.setInt(1, timetableId);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps =
                             con.prepareStatement(
                                     "DELETE FROM timetable_details " +
                                     "WHERE timetable_id=?")) {

                    ps.setInt(1, timetableId);
                    ps.executeUpdate();
                }

                // Now delete the timetable parent record.
                int affected;

                try (PreparedStatement ps =
                             con.prepareStatement(
                                     "DELETE FROM timetable " +
                                     "WHERE timetable_id=?")) {

                    ps.setInt(1, timetableId);
                    affected = ps.executeUpdate();
                }

                if (affected != 1) {
                    con.rollback();

                    JOptionPane.showMessageDialog(
                            this,
                            "Timetable record was not found."
                    );
                    return;
                }

                con.commit();

                JOptionPane.showMessageDialog(
                        this,
                        "Timetable record deleted successfully."
                );

                showTimetable();

            } catch (SQLException e) {

                con.rollback();
                throw e;
            }

        } catch (SQLException e) {

            showError(e);
        }
    }

    // =========================================================
    // SUBJECTS
    // =========================================================

    private void showSubjects() {
    showSimpleDatabaseTable(
                "Subjects",
                "SELECT subject_id, subject_name, subject_code, faculty_id "
                        + "FROM subject",
                new String[]{
                        "ID",
                        "Subject",
                        "Code",
                        "Faculty ID"
                }
        );
    }

    // =========================================================
    // FACULTY
    // =========================================================

    private void showFaculty() {

        showSimpleDatabaseTable(
                "Faculty",
                "SELECT faculty_id, faculty_name, department "
                        + "FROM faculty",
                new String[]{
                        "ID",
                        "Faculty Name",
                        "Department"
                }
        );
    }

    // =========================================================
    // ROOMS
    // =========================================================

    private void showRooms() {

        showSimpleDatabaseTable(
                "Rooms",
                "SELECT room_id, room_number, capacity "
                        + "FROM room",
                new String[]{
                        "ID",
                        "Room Number",
                        "Capacity"
                }
        );
    }

    // =========================================================
    // SIMPLE DATABASE VIEW
    // =========================================================

    private void showSimpleDatabaseTable(
            String title,
            String sql,
            String[] columns) {

        JPanel page =
                createPagePanel(
                        title,
                        "Database information"
                );

        DefaultTableModel model =
                new DefaultTableModel(
                        columns,
                        0
                );

        JTable table =
                new JTable(model);

        styleTable(table);

        try (
                Connection con =
                        getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            while (rs.next()) {

                Object[] row =
                        new Object[
                                columns.length
                        ];

                for (
                        int i = 0;
                        i < columns.length;
                        i++
                ) {

                    row[i] =
                            rs.getObject(
                                    i + 1
                            );
                }

                model.addRow(row);
            }

        } catch (SQLException e) {

            showError(e);
        }

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        page.add(
                scroll,
                BorderLayout.CENTER
        );

        setPage(page);
    }

    // =========================================================
    // SEARCH
    // =========================================================

    private void filterTable() {

        if (sorter == null) {
            return;
        }

        String text =
                searchField.getText().trim();

        if (text.isEmpty()) {

            sorter.setRowFilter(null);

        } else {

            sorter.setRowFilter(
                    RowFilter.regexFilter(
                            "(?i)" +
                                    java.util.regex.Pattern.quote(
                                            text
                                    )
                    )
            );
        }
    }

    // =========================================================
    // PAGE PANEL
    // =========================================================

    private JPanel createPagePanel(
            String title,
            String subtitle) {

        JPanel page =
                new JPanel(
                        new BorderLayout(
                                0,
                                18
                        )
                );

        page.setBackground(
                BACKGROUND
        );

        page.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        JPanel header =
                new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        27
                )
        );

        titleLabel.setForeground(
                TEXT
        );

        JLabel subtitleLabel =
                new JLabel(subtitle);

        subtitleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitleLabel.setForeground(
                MUTED
        );

        header.add(titleLabel);

        header.add(
                Box.createVerticalStrut(5)
        );

        header.add(subtitleLabel);

        page.add(
                header,
                BorderLayout.NORTH
        );

        return page;
    }

    // =========================================================
    // FORM PANEL
    // =========================================================

    private JPanel createFormPanel() {

        JPanel form =
                new JPanel();

        form.setOpaque(true);

        form.setBackground(
                WHITE
        );

        form.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        BorderFactory.createEmptyBorder(
                                25,
                                25,
                                25,
                                25
                        )
                )
        );

        form.setLayout(
                new GridLayout(
                        0,
                        2,
                        15,
                        15
                )
        );

        return form;
    }

    // =========================================================
    // FORM FIELD
    // =========================================================

    private void addField(
            JPanel form,
            String label,
            Component component) {

        JLabel l =
                new JLabel(label);

        l.setForeground(
                TEXT
        );

        l.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        form.add(l);

        if (component instanceof JTextField) {

            JTextField field =
                    (JTextField) component;

            field.setPreferredSize(
                    new Dimension(
                            250,
                            38
                    )
            );

            field.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            13
                    )
            );

            field.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    BORDER
                            ),
                            BorderFactory.createEmptyBorder(
                                    5,
                                    10,
                                    5,
                                    10
                            )
                    )
            );
        }

        form.add(component);
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
            String text,
            Color color) {

        JButton button =
                new JButton(text);

        button.setBackground(
                color
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        button.setMargin(
                new Insets(
                        8,
                        15,
                        8,
                        15
                )
        );

        return button;
    }

    // =========================================================
    // TABLE STYLE
    // =========================================================

    private void styleTable(
            JTable table) {

        table.setRowHeight(34);

        table.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        table.setForeground(
                TEXT
        );

        table.setGridColor(
                BORDER
        );

        table.setSelectionBackground(
                LIGHT_PURPLE
        );

        table.setSelectionForeground(
                TEXT
        );

        table.setAutoCreateRowSorter(
                false
        );

        JTableHeader header =
                table.getTableHeader();

        header.setBackground(
                PURPLE
        );

        header.setForeground(
                Color.WHITE
        );

        header.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        header.setPreferredSize(
                new Dimension(
                        0,
                        38
                )
        );
    }

    // =========================================================
    // COUNT
    // =========================================================

    private int getCount(
            String table) {

        String[] allowed = {
                "timetable",
                "subject",
                "faculty",
                "room"
        };

        boolean valid = false;

        for (String s : allowed) {

            if (s.equals(table)) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            return 0;
        }

        String sql =
                "SELECT COUNT(*) FROM " +
                        table;

        try (
                Connection con =
                        getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql);

                ResultSet rs =
                        ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {

            System.err.println(
                    "Count error: " +
                            e.getMessage()
            );
        }

        return 0;
    }

    // =========================================================
    // NULLABLE INTEGER
    // =========================================================

    private Integer parseNullableInteger(
            String text) {

        if (
                text == null ||
                        text.trim().isEmpty()
        ) {

            return null;
        }

        return Integer.parseInt(
                text.trim()
        );
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            SQLException e) {

        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Database error:\n" +
                        e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // SET PAGE
    // =========================================================

    private void setPage(
            JPanel page) {

        pagePanel.removeAll();

        pagePanel.add(
                page,
                BorderLayout.CENTER
        );

        pagePanel.revalidate();

        pagePanel.repaint();
    }

    // =========================================================
    // ROUNDED PANEL
    // =========================================================

    private static class RoundedPanel
            extends JPanel {

        private final int radius;

        private final Color background;

        public RoundedPanel(
                int radius,
                Color background) {

            this.radius = radius;

            this.background =
                    background;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
                java.awt.Graphics g) {

            java.awt.Graphics2D g2 =
                    (java.awt.Graphics2D) g.create();

            g2.setRenderingHint(
                    java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    background
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    TimetableDashboard app =
                            new TimetableDashboard();

                    app.setVisible(true);
                }
        );
    }
}
