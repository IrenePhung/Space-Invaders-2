package invaders.diffMenu;

import invaders.engine.GameEngine;
import invaders.engine.GameWindow;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DifficultMenu {
    private Stage currentStage;

    private VBox root;
    private Scene scene;

    public DifficultMenu(Stage currentStage) {
        this.currentStage = currentStage;

        root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        // Fill the background with black
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        addInterface();

        scene = new Scene(root, 600, 800, Color.BLACK);
    }

    private void addInterface() {
        Label label = new Label("Pick a difficulty");
        label.setTextFill(Color.WHITE);

        // Create a ComboBox (Dropdown) with difficulty options
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Easy", "Medium", "Hard");
        comboBox.setValue("Easy");

        Button button = new Button("OK");
        button.setOnAction(e -> {
            start(comboBox.getValue());
        });

        root.getChildren().addAll(label, comboBox, button);
    }

    public Scene getScene() {
        return scene;
    }

    private void start(String difficulty) {
        String path = "src/main/resources/config_" + difficulty.toLowerCase() + ".json";

        GameEngine engine = new GameEngine(path);
        GameWindow window = new GameWindow(engine);
        window.run();

        currentStage.setScene(window.getScene());
        window.run();
    }
}
