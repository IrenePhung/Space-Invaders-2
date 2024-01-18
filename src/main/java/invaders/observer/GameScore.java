package invaders.observer;

import invaders.factory.EnemyProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Enemy;
import invaders.rendering.Renderable;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class GameScore implements Observer {
    private Label label;

    public GameScore() {
        label = new Label("SCORE: 0");

        label.setTextFill(Color.WHITE);
    }

    @Override
    public void update(String message) {
        String[] tokens = message.split(" ");
        if (!tokens[0].equals("score")) {
            return;
        }

        int score = Integer.parseInt(tokens[1]);

        label.setText("SCORE: " + score);
    }

    public Label getLabel() {
        return label;
    }
}
