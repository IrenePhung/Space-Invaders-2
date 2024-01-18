package invaders;

import javafx.application.Application;
import javafx.stage.Stage;
import invaders.diffMenu.DifficultMenu;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

import java.util.Map;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DifficultMenu startMenu = new DifficultMenu(primaryStage);

        primaryStage.setTitle("Space Invaders");
        primaryStage.setScene(startMenu.getScene());
        primaryStage.show();
    }
}
