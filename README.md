# Quiz Application

A simple Java-based desktop application for creating, managing, and taking quizzes.

---

## Features

### For Teachers
- Create quizzes with a title, number of questions, and duration.
- Add multiple-choice questions with correct answers.
- View and delete created quizzes.
- View student scores for each quiz.

### For Students
- Take quizzes created by teachers.
- Answer multiple-choice questions within a time limit.
- View quiz results with scores and percentages.

---

## Technology Stack
- **Programming Language:** Java
- **UI Framework:** Java Swing
- **Database:** SQLite

---

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- SQLite JDBC Driver (included in the project)

### Installation

## 1. Clone the repository:
```bash
git clone https://github.com/your-username/quiz-application.git
```

## 2. Compile the Java files:
```bash
javac QuizApp.java
```

## 3. Run the application:
```bash
java -cp "sqlite-jdbc-3.49.1.0.jar;." QuizApp
```

---

## Database
The application uses an SQLite database (quiz.db) with the following tables:
- users: Stores user information (teachers and students).
- quizzes: Stores quiz metadata.
- questions: Stores quiz questions.
- scores: Stores student scores.

---

## Usage

### 1. Login
- Enter your email and password to log in.
- If you don't have an account, click "Signup" to create one.

### 2. Signup
- Enter your name, email, password, and role (Student or Teacher).
- Click "Signup" to create an account.

### 3. Teacher Panel
- Create quizzes by entering a title, number of questions, and duration.
- Add questions to quizzes with four options and a correct answer.
- View student scores for each quiz.
- Delete quizzes if needed.

### 4. Student Panel
- Take quizzes created by teachers.
- Answer multiple-choice questions within the time limit.
- View your score and percentage after completing the quiz.

---

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

---

## Acknowledgements
- Java Swing and AWT for UI components.
- SQLite for database management.
- Java community for support and resources.

---

## Contact
For questions or support, please contact sathvikinguva@gmail.com.
