import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            Statement statement = connection.createStatement();
            Scanner scanner = new Scanner(System.in);
            int x = 0;
            String s = "";
            while (!"6".equals(s)) {
                System.out.println("1. Для создания таблицы введите 1");
                System.out.println("2. Для удаления таблицы введите 2");
                System.out.println("3. Для добавления новых данных введите 3");
                System.out.println("4. Для вывода всех данных введите 4");
                System.out.println("5. Для поиска данных по тексту введите 5");
                System.out.println("6. Для выхода из приложения введите 6");
                s = scanner.next();
                try {
                    x = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    System.out.println("Неверный ввод");
                }
                switch (x) {
                    case 1:
                        statement.executeUpdate("CREATE TABLE 'student' ('id' INTEGER, 'name' VARCHAR(100), 'secondName' VARCHAR(100), 'audience' VARCHAR(100))");
                        break;

                    case 2:
                        statement.executeUpdate("DROP TABLE 'student' ");
                        break;

                    case 3:
                        System.out.println("Введите номер");
                        int id = scanner.nextInt();

                        System.out.println("Введите имя");
                        String name = scanner.next();

                        System.out.println("Введите фамилию");
                        String secondName = scanner.next();

                        System.out.println("Введите аудиторию");
                        String audience = scanner.next();

                        insertStudent(connection, id, name, secondName, audience);
                        System.out.println("Студент создан");

                        break;

                    case 4:
                        showStudents(connection);
                        break;

                    case 5:
                        System.out.println("Введите нужный текст");
                        String searchString = scanner.next();
                        searchStudent(connection, searchString);
                        break;

                    case 6:
                        System.out.println("Выход");
                        break;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error" + e.getLocalizedMessage());
        }

    }

    private void searchStudent(Connection connection, String searchString) throws SQLException {
        PreparedStatement stmt2 = connection.prepareStatement("SELECT * FROM student WHERE ? IN (id,name,secondName,audience) ORDER BY name");
        // PreparedStatement stmt2 = connection.prepareStatement("SELECT id,name,secondName,audience FROM 'student' WHERE id OR name OR secondName OR audience = ? ORDER BY name");
        stmt2.setString(1, searchString);
        ResultSet resultSet = stmt2.executeQuery();
        while (resultSet.next()) {
            int resultId = resultSet.getInt("id");
            String resultName = resultSet.getString("name");
            String resultSecondName = resultSet.getString("secondName");
            String resultAudience = resultSet.getString("audience");
            System.out.printf("%d. %s %s %s \n", resultId, resultName, resultSecondName, resultAudience);
        }
    }

    private void insertStudent(Connection connection, int id, String name, String secondName, String audience) {
        final String insertTemplate =
                "INSERT INTO 'student'('id','name','secondName','audience') VALUES(?,?,?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertTemplate)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, secondName);
            preparedStatement.setString(4, audience);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert student", e);
        }
    }


}
