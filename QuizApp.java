import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

// UI Style Utility Class
class UIStyle {
    static Color primaryColor = new Color(70, 130, 180);    
    static Color secondaryColor = new Color(240, 248, 255); 
    static Color accentColor = new Color(255, 165, 0);      
    static Color textColor = new Color(25, 25, 25);         
    static Color errorColor = new Color(220, 20, 60);       
    static Color successColor = new Color(46, 139, 87);     
    
    static Font titleFont = new Font("Arial", Font.BOLD, 16);
    static Font normalFont = new Font("Arial", Font.PLAIN, 14);
    static Font buttonFont = new Font("Arial", Font.BOLD, 14);
    
    static void styleButton(Button btn) {
        btn.setBackground(primaryColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(buttonFont);
    }
    
    static void styleTextField(TextField tf) {
        tf.setBackground(secondaryColor);
        tf.setForeground(textColor);
        tf.setFont(normalFont);
    }
    
    static void styleLabel(Label lbl) {
        lbl.setForeground(textColor);
        lbl.setFont(normalFont);
    }
    
    static void styleTitleLabel(Label lbl) {
        lbl.setForeground(primaryColor);
        lbl.setFont(titleFont);
    }

    static void styleFormPanel(Panel panel) {
        panel.setBackground(Color.WHITE);
    }
}

// SQLite Database Utility
class DB {
    static Connection conn;
    static Statement stmt;

    static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:quiz.db");
            stmt = conn.createStatement();

            // Create tables if not exist
            stmt.execute("CREATE TABLE IF NOT EXISTS users(uid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, role TEXT, pwd TEXT);");
            stmt.execute("CREATE TABLE IF NOT EXISTS quizzes(qid INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, total_questions INTEGER, duration INTEGER);");
            stmt.execute("CREATE TABLE IF NOT EXISTS questions(id INTEGER PRIMARY KEY AUTOINCREMENT, qid INTEGER, question TEXT, opt1 TEXT, opt2 TEXT, opt3 TEXT, opt4 TEXT, answer INTEGER);");
            stmt.execute("CREATE TABLE IF NOT EXISTS scores(uid INTEGER, qid INTEGER, score INTEGER, total INTEGER, date TEXT);");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static ResultSet query(String sql) throws SQLException {
        return stmt.executeQuery(sql);
    }

    static void update(String sql) throws SQLException {
        stmt.executeUpdate(sql);
    }
}

// Signup Form
class SignupForm extends Frame implements ActionListener {
    TextField tname = new TextField();
    TextField temail = new TextField();
    Choice crole = new Choice();
    TextField tpass = new TextField();
    Button bsignup = new Button("Signup");
    Button bback = new Button("Back to Login");

    SignupForm() {
        setTitle("Quiz App - Signup");
        setSize(400, 320);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        Panel titlePanel = new Panel();
        titlePanel.setBackground(UIStyle.primaryColor);
        Label titleLabel = new Label("Create New Account", Label.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);

        Panel formPanel = new Panel(new GridLayout(5, 2, 10, 12));
        formPanel.setBackground(Color.WHITE);
        
        Label nameLabel = new Label("Name:", Label.RIGHT);
        Label emailLabel = new Label("Email:", Label.RIGHT);
        Label roleLabel = new Label("Role:", Label.RIGHT);
        Label passLabel = new Label("Password:", Label.RIGHT);
        UIStyle.styleLabel(nameLabel);
        UIStyle.styleLabel(emailLabel);
        UIStyle.styleLabel(roleLabel);
        UIStyle.styleLabel(passLabel);
        
        crole.add("Student");
        crole.add("Teacher");
        crole.setBackground(UIStyle.secondaryColor);
        crole.setForeground(UIStyle.textColor);
        crole.setFont(UIStyle.normalFont);
        
        tpass.setEchoChar('*'); 
        
        UIStyle.styleTextField(tname);
        UIStyle.styleTextField(temail);
        UIStyle.styleTextField(tpass);
        
        formPanel.add(nameLabel); formPanel.add(tname);
        formPanel.add(emailLabel); formPanel.add(temail);
        formPanel.add(roleLabel); formPanel.add(crole);
        formPanel.add(passLabel); formPanel.add(tpass);
        
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(bsignup);
        buttonPanel.add(bback);
        UIStyle.styleButton(bsignup);
        UIStyle.styleButton(bback);
        
        Panel mainPanel = new Panel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        Panel westPadding = new Panel();
        Panel eastPadding = new Panel();
        westPadding.setBackground(Color.WHITE);
        eastPadding.setBackground(Color.WHITE);
        add(westPadding, BorderLayout.WEST);
        add(eastPadding, BorderLayout.EAST);
        westPadding.setPreferredSize(new Dimension(30, 0));
        eastPadding.setPreferredSize(new Dimension(30, 0));

        bsignup.addActionListener(this);
        bback.addActionListener(this);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); }});
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 400) / 2, (screenSize.height - 320) / 2);
        
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bback) {
            dispose();
            new LoginForm();
            return;
        }
        
        try {
            String sql = String.format("INSERT INTO users(name, email, role, pwd) VALUES('%s','%s','%s','%s');",
                tname.getText(), temail.getText(), crole.getSelectedItem(), tpass.getText());
            DB.update(sql);
            dispose();
            new LoginForm();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Signup failed. Email may already be in use.");
        }
    }
    
    void showError(String msg) {
        Dialog d = new Dialog(this, "Error", true);
        d.setLayout(new BorderLayout(10, 10));
        d.setBackground(Color.WHITE);
        
        Label msgLabel = new Label(msg, Label.CENTER);
        msgLabel.setForeground(UIStyle.errorColor);
        
        Button okButton = new Button("OK");
        UIStyle.styleButton(okButton);
        
        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        
        d.add(msgLabel, BorderLayout.CENTER);
        d.add(buttonPanel, BorderLayout.SOUTH);
        d.setSize(300, 120);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        d.setLocation((screenSize.width - 300) / 2, (screenSize.height - 120) / 2);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
        
        d.setVisible(true);
    }
}

// Login Form
class LoginForm extends Frame implements ActionListener {
    TextField temail = new TextField();
    TextField tpass = new TextField();
    Button blogin = new Button("Login"), bsignup = new Button("Signup");

    LoginForm() {
        setTitle("Quiz App - Login");
        setSize(400, 250);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        Panel titlePanel = new Panel();
        titlePanel.setBackground(UIStyle.primaryColor);
        Label titleLabel = new Label("Quiz Application", Label.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        Panel formPanel = new Panel(new GridLayout(3, 2, 10, 15));
        formPanel.setBackground(Color.WHITE);
        
        Label emailLabel = new Label("Email:", Label.RIGHT);
        Label passLabel = new Label("Password:", Label.RIGHT);
        UIStyle.styleLabel(emailLabel);
        UIStyle.styleLabel(passLabel);
        
        formPanel.add(emailLabel); formPanel.add(temail);
        formPanel.add(passLabel); formPanel.add(tpass);
        tpass.setEchoChar('*'); 
        UIStyle.styleTextField(temail);
        UIStyle.styleTextField(tpass);
        
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(blogin); buttonPanel.add(bsignup);
        UIStyle.styleButton(blogin);
        UIStyle.styleButton(bsignup);
        
        Panel mainPanel = new Panel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        Panel westPadding = new Panel();
        Panel eastPadding = new Panel();
        westPadding.setBackground(Color.WHITE);
        eastPadding.setBackground(Color.WHITE);
        add(westPadding, BorderLayout.WEST);
        add(eastPadding, BorderLayout.EAST);
        westPadding.setPreferredSize(new Dimension(30, 0));
        eastPadding.setPreferredSize(new Dimension(30, 0));

        blogin.addActionListener(this);
        bsignup.addActionListener(this);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); }});
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 400) / 2, (screenSize.height - 250) / 2);
        
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bsignup) {
            dispose();
            new SignupForm();
            return;
        }

        String email = temail.getText();
        String pwd = tpass.getText();
        try {
            ResultSet rs = DB.query("SELECT * FROM users WHERE email='" + email + "' AND pwd='" + pwd + "'");
            if (rs.next()) {
                String role = rs.getString("role");
                int uid = rs.getInt("uid");
                String name = rs.getString("name");
                dispose();
                if (role.equals("Student")) {
                    new QuizWindow(uid, name);
                } else {
                    new QuizCreation(uid, name);
                }
            } else {
                showMsg("Invalid login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showMsg(String msg) {
        Dialog d = new Dialog(this, "Message", true);
        d.setLayout(new BorderLayout(10, 10));
        d.setBackground(Color.WHITE);
        
        Label msgLabel = new Label(msg, Label.CENTER);
        UIStyle.styleLabel(msgLabel);
        
        Button okButton = new Button("OK");
        UIStyle.styleButton(okButton);
        
        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        
        d.add(msgLabel, BorderLayout.CENTER);
        d.add(buttonPanel, BorderLayout.SOUTH);
        d.setSize(250, 120);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        d.setLocation((screenSize.width - 250) / 2, (screenSize.height - 120) / 2);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
        
        d.setVisible(true);
    }
}

// Quiz Creation
class QuizCreation extends Frame implements ActionListener {
    int uid;
    String name;
    TextField ttitle = new TextField(), ttotal = new TextField(), tdur = new TextField();
    Button bnext = new Button("Next"), blogout = new Button("Logout"), bdelete = new Button("Delete Selected Quiz");
    java.awt.List quizList = new java.awt.List(); 
    java.awt.List scoreList = new java.awt.List(); 

    QuizCreation(int uid, String name) {
        this.uid = uid;
        this.name = name;
        setTitle("Teacher Panel - " + name);
        setSize(700, 550);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        Panel top = new Panel(new BorderLayout(10, 0));
        top.setBackground(UIStyle.primaryColor);
        
        Label welcomeLabel = new Label("Teacher: " + name, Label.LEFT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(UIStyle.titleFont);
        
        blogout.setBackground(UIStyle.accentColor);
        blogout.setForeground(Color.WHITE);
        blogout.setFont(UIStyle.buttonFont);
        
        top.add(welcomeLabel, BorderLayout.WEST);
        top.add(blogout, BorderLayout.EAST);
        
        Panel formPanel = new Panel(new GridLayout(3, 2, 10, 15));
        formPanel.setBackground(Color.WHITE);
        
        Label titleLabel = new Label("Quiz Title:", Label.RIGHT);
        Label totalLabel = new Label("No. of Questions:", Label.RIGHT);
        Label durationLabel = new Label("Duration (minutes):", Label.RIGHT);
        
        UIStyle.styleLabel(titleLabel);
        UIStyle.styleLabel(totalLabel);
        UIStyle.styleLabel(durationLabel);
        UIStyle.styleTextField(ttitle);
        UIStyle.styleTextField(ttotal);
        UIStyle.styleTextField(tdur);
        
        formPanel.add(titleLabel); formPanel.add(ttitle);
        formPanel.add(totalLabel); formPanel.add(ttotal);
        formPanel.add(durationLabel); formPanel.add(tdur);
        
        // Title for new quiz creation
        Panel formTitlePanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        formTitlePanel.setBackground(Color.WHITE);
        Label formTitle = new Label("Create New Quiz");
        formTitle.setFont(new Font("Arial", Font.BOLD, 16));
        formTitle.setForeground(UIStyle.primaryColor);
        formTitlePanel.add(formTitle);
        
        Panel listPanel = new Panel(new GridLayout(1, 2, 20, 0));
        listPanel.setBackground(Color.WHITE);
        
        Panel quizListPanel = new Panel(new BorderLayout());
        quizListPanel.setBackground(Color.WHITE);
        
        Label quizListLabel = new Label("Created Quizzes");
        quizListLabel.setFont(new Font("Arial", Font.BOLD, 14));
        quizListLabel.setForeground(UIStyle.primaryColor);
        
        quizList.setBackground(UIStyle.secondaryColor);
        quizList.setForeground(UIStyle.textColor);
        quizList.setFont(UIStyle.normalFont);
        
        quizListPanel.add(quizListLabel, BorderLayout.NORTH);
        quizListPanel.add(quizList, BorderLayout.CENTER);
        
        Panel scoreListPanel = new Panel(new BorderLayout());
        scoreListPanel.setBackground(Color.WHITE);
        
        Label scoreListLabel = new Label("Student Scores");
        scoreListLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreListLabel.setForeground(UIStyle.primaryColor);
        
        scoreList.setBackground(UIStyle.secondaryColor);
        scoreList.setForeground(UIStyle.textColor);
        scoreList.setFont(UIStyle.normalFont);
        
        scoreListPanel.add(scoreListLabel, BorderLayout.NORTH);
        scoreListPanel.add(scoreList, BorderLayout.CENTER);
        
        listPanel.add(quizListPanel);
        listPanel.add(scoreListPanel);
        
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        UIStyle.styleButton(bnext);
        UIStyle.styleButton(bdelete);
        bnext.setBackground(UIStyle.successColor);
        bdelete.setBackground(UIStyle.errorColor);
        
        buttonPanel.add(bnext);
        buttonPanel.add(bdelete);
        
        Panel formContainer = new Panel(new BorderLayout(0, 15));
        formContainer.setBackground(Color.WHITE);
        formContainer.add(formTitlePanel, BorderLayout.NORTH);
        formContainer.add(formPanel, BorderLayout.CENTER);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        Panel centerPanel = new Panel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(formContainer, BorderLayout.NORTH);
        centerPanel.add(listPanel, BorderLayout.CENTER);
        
        Panel mainContainer = new Panel(new BorderLayout(20, 20));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.add(centerPanel, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);

        bnext.addActionListener(this);
        blogout.addActionListener(this);
        bdelete.addActionListener(this);
        quizList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Quiz selected: " + quizList.getSelectedItem());
                    loadStudentScores();
                }
            }
        });
        
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); }});

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 700) / 2, (screenSize.height - 550) / 2);
        
        loadQuizHistory();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == blogout) {
            dispose();
            new LoginForm();
            return;
        }

        if (ae.getSource() == bdelete) {
            deleteSelectedQuiz();
            return;
        }

        if (ae.getSource() == bnext) {
            try {
                String title = ttitle.getText();
                int total = Integer.parseInt(ttotal.getText());
                int dur = Integer.parseInt(tdur.getText());
                DB.update("INSERT INTO quizzes(title, total_questions, duration) VALUES('" + title + "'," + total + "," + dur + ")");
                ResultSet rs = DB.query("SELECT last_insert_rowid() AS qid"); 
                if (rs.next()) {
                    int qid = rs.getInt("qid");
                    dispose(); 
                    new QuestionForm(uid, name, qid, total);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void loadQuizHistory() {
        quizList.removeAll();
        try {
            ResultSet rs = DB.query("SELECT qid, title FROM quizzes");
            while (rs.next()) {
                quizList.add(rs.getInt("qid") + " - " + rs.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadStudentScores() {
        scoreList.removeAll();
        String selected = quizList.getSelectedItem();
        System.out.println("Selected item: " + selected);
        
        if (selected == null) {
            scoreList.add("Select a quiz to view scores");
            return;
        }

        try {
            int qid = Integer.parseInt(selected.split(" - ")[0]);
            System.out.println("Looking for scores for quiz ID: " + qid);
            ResultSet checkScores = DB.query("SELECT COUNT(*) as count FROM scores WHERE qid = " + qid);
            if (checkScores.next()) {
                System.out.println("Number of score records found: " + checkScores.getInt("count"));
            }
            
            String sql = "SELECT u.name AS student_name, s.score, s.total " +
                         "FROM scores s " +
                         "JOIN users u ON s.uid = u.uid " +
                         "WHERE s.qid = " + qid;
            
            ResultSet rs = DB.query(sql);
            boolean hasScores = false;
            
            while (rs.next()) {
                hasScores = true;
                String studentName = rs.getString("student_name");
                int score = rs.getInt("score");
                int total = rs.getInt("total");
                
                String scoreText = studentName + " - " + score + "/" + total;
                System.out.println("Adding score: " + scoreText);
                scoreList.add(scoreText);
            }
            
            if (!hasScores) {
                System.out.println("No scores available for this quiz");
                scoreList.add("No scores available for this quiz");
            }
            System.out.println("Score list item count: " + scoreList.getItemCount());
            scoreList.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            scoreList.add("Error: " + e.getMessage());
        }
    }

    void deleteSelectedQuiz() {
        String selected = quizList.getSelectedItem();
        if (selected == null) {
            showMsg("Please select a quiz to delete.");
            return;
        }
        try {
            int qid = Integer.parseInt(selected.split(" - ")[0]);
            DB.update("DELETE FROM quizzes WHERE qid=" + qid);
            DB.update("DELETE FROM questions WHERE qid=" + qid); 
            DB.update("DELETE FROM scores WHERE qid=" + qid); 
            loadQuizHistory(); 
            scoreList.removeAll(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showMsg(String msg) {
        Dialog d = new Dialog(this, "Message", true);
        d.setLayout(new BorderLayout(10, 10));
        d.setBackground(Color.WHITE);
        
        Label msgLabel = new Label(msg, Label.CENTER);
        UIStyle.styleLabel(msgLabel);
        
        Button okButton = new Button("OK");
        UIStyle.styleButton(okButton);
        
        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        
        d.add(msgLabel, BorderLayout.CENTER);
        d.add(buttonPanel, BorderLayout.SOUTH);
        d.setSize(250, 120);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        d.setLocation((screenSize.width - 250) / 2, (screenSize.height - 120) / 2);
        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
        
        d.setVisible(true);
    }
}

// Question Form
class QuestionForm extends Frame implements ActionListener {
    int uid, qid, total, current = 0;
    TextField tq = new TextField(), o1 = new TextField(), o2 = new TextField(), o3 = new TextField(), o4 = new TextField();
    Choice answer = new Choice();
    Button bnext = new Button("Save & Next");

    QuestionForm(int uid, String name, int qid, int total) {
        this.uid = uid;
        this.qid = qid;
        this.total = total;

        setTitle("Add Questions - " + name);
        setSize(500, 450);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        Panel headerPanel = new Panel();
        headerPanel.setBackground(UIStyle.primaryColor);
        Label headerLabel = new Label("Question " + (current + 1) + " to " + total, Label.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        
        Panel formPanel = new Panel(new GridLayout(7, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        Label questionLabel = new Label("Question:", Label.RIGHT);
        Label opt1Label = new Label("Option 1:", Label.RIGHT);
        Label opt2Label = new Label("Option 2:", Label.RIGHT);
        Label opt3Label = new Label("Option 3:", Label.RIGHT);
        Label opt4Label = new Label("Option 4:", Label.RIGHT);
        Label answerLabel = new Label("Correct Option (1-4):", Label.RIGHT);
        
        UIStyle.styleLabel(questionLabel);
        UIStyle.styleLabel(opt1Label);
        UIStyle.styleLabel(opt2Label);
        UIStyle.styleLabel(opt3Label);
        UIStyle.styleLabel(opt4Label);
        UIStyle.styleLabel(answerLabel);
        
        UIStyle.styleTextField(tq);
        UIStyle.styleTextField(o1);
        UIStyle.styleTextField(o2);
        UIStyle.styleTextField(o3);
        UIStyle.styleTextField(o4);
        
        answer.setBackground(UIStyle.secondaryColor);
        answer.setForeground(UIStyle.textColor);
        answer.setFont(UIStyle.normalFont);
        for (int i = 1; i <= 4; i++) answer.add(String.valueOf(i));
        
        formPanel.add(questionLabel); formPanel.add(tq);
        formPanel.add(opt1Label); formPanel.add(o1);
        formPanel.add(opt2Label); formPanel.add(o2);
        formPanel.add(opt3Label); formPanel.add(o3);
        formPanel.add(opt4Label); formPanel.add(o4);
        formPanel.add(answerLabel); formPanel.add(answer);

        Panel buttonPanel = new Panel();
        buttonPanel.setBackground(Color.WHITE);
        UIStyle.styleButton(bnext);
        bnext.setBackground(UIStyle.successColor);
        buttonPanel.add(bnext);
        
        Panel mainContainer = new Panel(new BorderLayout(20, 20));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.add(formPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainContainer, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        Panel westPadding = new Panel();
        Panel eastPadding = new Panel();
        westPadding.setBackground(Color.WHITE);
        eastPadding.setBackground(Color.WHITE);
        add(westPadding, BorderLayout.WEST);
        add(eastPadding, BorderLayout.EAST);
        westPadding.setPreferredSize(new Dimension(20, 0));
        eastPadding.setPreferredSize(new Dimension(20, 0));

        bnext.addActionListener(this);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); }});
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 500) / 2, (screenSize.height - 450) / 2);
        
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            DB.update(String.format("INSERT INTO questions(qid, question, opt1, opt2, opt3, opt4, answer) VALUES(%d, '%s', '%s', '%s', '%s', '%s', %s);",
                qid, tq.getText(), o1.getText(), o2.getText(), o3.getText(), o4.getText(), answer.getSelectedItem()));
            current++;
            
            if (current >= total) {
                dispose();
                new LoginForm();
            } else {
                setTitle("Add Questions - " + (current + 1) + " of " + total);
                tq.setText(""); 
                o1.setText(""); 
                o2.setText(""); 
                o3.setText(""); 
                o4.setText("");
                answer.select(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Quiz Window 
class QuizWindow extends Frame implements ActionListener {
    int uid, qid, duration;
    String name, quizTitle;
    ArrayList<String[]> questions = new ArrayList<>();
    int current = 0, score = 0;
    Label lq = new Label();
    CheckboxGroup group = new CheckboxGroup();
    Checkbox[] opts = new Checkbox[4];
    Button bnext = new Button("Next"), blogout = new Button("Logout");
    Label timerLabel = new Label("Time Left: 00:00");
    javax.swing.Timer timer; 
    int timeLeft; 

    QuizWindow(int uid, String name) {
        this.uid = uid;
        this.name = name;
        setTitle("Student Quiz - " + name);
        setSize(600, 500);
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        Panel top = new Panel(new BorderLayout(10, 0));
        top.setBackground(UIStyle.primaryColor);

        Label welcomeLabel = new Label("Welcome: " + name, Label.LEFT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(UIStyle.titleFont);

        timerLabel = new Label("Time Left: 00:00", Label.CENTER);
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        blogout.setBackground(UIStyle.accentColor);
        blogout.setForeground(Color.WHITE);
        blogout.setFont(UIStyle.buttonFont);

        top.add(welcomeLabel, BorderLayout.WEST);
        top.add(timerLabel, BorderLayout.CENTER);
        top.add(blogout, BorderLayout.EAST);
        
        Panel titlePanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        
        Panel center = new Panel(new GridLayout(6, 1, 0, 10));
        center.setBackground(Color.WHITE);
        
        lq.setFont(new Font("Arial", Font.BOLD, 16));
        lq.setForeground(UIStyle.primaryColor);
        center.add(lq);
        
        for (int i = 0; i < 4; i++) {
            opts[i] = new Checkbox("", group, false);
            opts[i].setFont(UIStyle.normalFont);
            opts[i].setForeground(UIStyle.textColor);
            center.add(opts[i]);
        }

        Panel centerContainer = new Panel(new BorderLayout(20, 20));
        centerContainer.setBackground(Color.WHITE);
        centerContainer.add(center, BorderLayout.CENTER);
        
        bnext.setBackground(UIStyle.successColor);
        bnext.setForeground(Color.WHITE);
        bnext.setFont(UIStyle.buttonFont);
        
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(bnext);

        add(top, BorderLayout.NORTH);
        add(centerContainer, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        Panel westPadding = new Panel();
        Panel eastPadding = new Panel();
        westPadding.setBackground(Color.WHITE);
        eastPadding.setBackground(Color.WHITE);
        add(westPadding, BorderLayout.WEST);
        add(eastPadding, BorderLayout.EAST);
        westPadding.setPreferredSize(new Dimension(30, 0));
        eastPadding.setPreferredSize(new Dimension(30, 0));

        blogout.addActionListener(this);
        bnext.addActionListener(this);
        addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent e) { System.exit(0); }});

        try {
            ResultSet rsq = DB.query("SELECT * FROM quizzes WHERE qid = (SELECT MAX(qid) FROM quizzes)");
            if (rsq.next()) {
                qid = rsq.getInt("qid");
                quizTitle = rsq.getString("title");
                duration = rsq.getInt("duration");
                timeLeft = duration * 60; 
                
                Label quizTitleLabel = new Label("Quiz: " + quizTitle, Label.CENTER);
                quizTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                quizTitleLabel.setForeground(UIStyle.primaryColor);
                titlePanel.add(quizTitleLabel);
                centerContainer.add(titlePanel, BorderLayout.NORTH);
            }

            ResultSet rs = DB.query("SELECT * FROM questions WHERE qid=" + qid);
            while (rs.next()) {
                questions.add(new String[] {
                    rs.getString("question"),
                    rs.getString("opt1"),
                    rs.getString("opt2"),
                    rs.getString("opt3"),
                    rs.getString("opt4"),
                    rs.getString("answer")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 600) / 2, (screenSize.height - 500) / 2);

        startTimer();
        loadQuestion();
        setVisible(true);
    }

    void startTimer() {
        timer = new javax.swing.Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                timerLabel.setText(String.format("Time Left: %02d:%02d", minutes, seconds));

                if (timeLeft <= 0) {
                    timer.stop();
                    showResult();
                }
            }
        });
        timer.start();
    }

    void loadQuestion() {
        if (current >= questions.size()) {
            timer.stop();
            showResult();
            return;
        }
        String[] q = questions.get(current);
        lq.setText("Q" + (current + 1) + ": " + q[0]);
        for (int i = 0; i < 4; i++) opts[i].setLabel(q[i + 1]);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == blogout) {
            timer.stop();
            dispose();
            new LoginForm();
            return;
        }
        int selected = -1;
        for (int i = 0; i < 4; i++) {
            if (opts[i].getState()) selected = i + 1;
        }
        if (selected == Integer.parseInt(questions.get(current)[5])) score++;
        current++;
        loadQuestion();
    }

    void showResult() {
        try {
            DB.update("INSERT INTO scores(uid, qid, score, total, date) VALUES(" + uid + "," + qid + "," + score + "," + questions.size() + ",date('now'))");
        } catch (Exception e) { e.printStackTrace(); }

        removeAll();
        setLayout(new BorderLayout());
        
        Panel resultPanel = new Panel(new BorderLayout(20, 20));
        resultPanel.setBackground(Color.WHITE);
        
        Panel scorePanel = new Panel(new GridLayout(3, 1, 0, 10));
        scorePanel.setBackground(UIStyle.secondaryColor);
        
        Label congratsLabel = new Label("Quiz Complete!", Label.CENTER);
        congratsLabel.setFont(new Font("Arial", Font.BOLD, 22));
        congratsLabel.setForeground(UIStyle.primaryColor);
        
        Label scoreLabel = new Label("Your Score: " + score + "/" + questions.size(), Label.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        if ((double)score/questions.size() >= 0.7) {
            scoreLabel.setForeground(UIStyle.successColor);
        } else if ((double)score/questions.size() >= 0.4) {
            scoreLabel.setForeground(UIStyle.accentColor);
        } else {
            scoreLabel.setForeground(UIStyle.errorColor);
        }
        
        Label percentLabel = new Label("(" + (questions.size() > 0 ? score * 100 / questions.size() : 0) + "%)", Label.CENTER);
        percentLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        percentLabel.setForeground(UIStyle.textColor);
        
        scorePanel.add(congratsLabel);
        scorePanel.add(scoreLabel);
        scorePanel.add(percentLabel);
        
        Button blogout = new Button("Back to Login");
        blogout.setBackground(UIStyle.primaryColor);
        blogout.setForeground(Color.WHITE);
        blogout.setFont(UIStyle.buttonFont);
        
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(blogout);
        
        resultPanel.add(scorePanel, BorderLayout.CENTER);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        Panel northPadding = new Panel();
        Panel westPadding = new Panel();
        Panel eastPadding = new Panel();
        northPadding.setBackground(Color.WHITE);
        westPadding.setBackground(Color.WHITE);
        eastPadding.setBackground(Color.WHITE);
        northPadding.setPreferredSize(new Dimension(0, 50));
        westPadding.setPreferredSize(new Dimension(100, 0));
        eastPadding.setPreferredSize(new Dimension(100, 0));
        
        add(northPadding, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        add(westPadding, BorderLayout.WEST);
        add(eastPadding, BorderLayout.EAST);
        
        blogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginForm();
            }
        });
        
        validate();
    }
}

// Main class
public class QuizApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        DB.connect();
        new LoginForm();
    }
}