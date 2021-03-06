import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Testing a Form");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Create User");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        gridPane.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("Email:");
        gridPane.add(userName, 0, 1);

        final TextField userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);

        Label firstName = new Label("First Name:");
        gridPane.add(firstName, 0, 2);

        final TextField firstNameTextField = new TextField();
        gridPane.add(firstNameTextField, 1, 2);

        Label lastName = new Label("Last Name:");
        gridPane.add(lastName, 0, 3);

        final TextField lastNameTextField = new TextField();
        gridPane.add(lastNameTextField, 1, 3);

        Button btn = new Button("Create");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        gridPane.add(hbBtn, 1, 4);

        final Text error = new Text();
        gridPane.add(error, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String email = userTextField.getText();

                if (firstName.isEmpty() ||
                        !validEmail(email) ||
                        lastName.isEmpty()) {
                    error.setFill(Color.FIREBRICK);
                    error.setText("Invalid Data");
                } else {
                    Connection connection = null;

                    try {
                        Class.forName("org.postgresql.Driver");
                        String url = "jdbc:postgresql://localhost/users";

                        connection = DriverManager.getConnection(url, "username", "password");

                        Statement stmt = connection.createStatement();

                        String insert = "insert into users (email, first_name, last_name)" +
                                " VALUES('" + email + "', '" + firstName + "', '" + lastName + "');";
                        stmt.executeUpdate(insert);
                    } catch (Exception e1) {

                    } finally {
                        try {
                            if (connection != null)
                                connection.close();
                        } catch (SQLException se) {

                        }
                    }
                }
            }
        });

        Scene scene = new Scene(gridPane, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static protected boolean validEmail(String email) {
        EmailValidator validator = new EmailValidator(email);
        return validator.isValid();
    }
}
