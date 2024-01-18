package invaders.observer;

import invaders.engine.GameEngine;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class GameClock implements Observer{
    private Label label;

    public GameClock(){
        label = new Label("TIME: 0:00");

        label.setTextFill(Color.WHITE);
    }

    public Label getLabel() {
        return label;
    }

    @Override
    public void update(String message) {
        String[] tokens = message.split(" ");
        if(!tokens[0].equals("time")){
            return;
        }

        int sec = Integer.parseInt(tokens[1]);
        int min = sec / 60;
        sec = sec % 60;

        String secStr = String.format("%02d:%02d", min, sec);

        label.setText("TIME: " + secStr);
    }
}
