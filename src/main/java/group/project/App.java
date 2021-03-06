/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package group.project;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 * The main class used to run the program
 *
 * @author petra
 */
public class App extends Application {

    /**
     * The main entry point of the program
     *
     * @param args The arguments passed by the command line
     */
    public static void main(String[] args) {
        //DAO.deleteAllStudents();
        //DAO.deleteAllVisits();

        // Check current students as well as running preparation codes for the database
        System.out.println("Currently there are " + DAO.getAllStudents().size() + " student(s) in the database");

        // display every student information
        for (Student s : DAO.getAllStudents()) {
            System.out.println(s);
        }

        // launch the JavaFX application
        Application.launch(args);
    }

    /**
     * The start method used by JavaFX when starting the JavaFX application
     *
     * @param stage The stage/window used to display the UI
     * @throws IOException Error when the Start.fxml cannot be found/loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        // load and display the home view
        Pane home = FXMLLoader.load(App.class.getClassLoader().getResource("Start.fxml"));
        Scene scene = new Scene(home);
        stage.setScene(scene);
        stage.show();
    }

}
