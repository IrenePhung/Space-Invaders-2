package invaders.engine;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import invaders.memento.Caretaker;
import invaders.memento.Memento;

class KeyboardInputHandler implements Caretaker{
    private final GameEngine model;
    private boolean left = false;
    private boolean right = false;
    private Set<KeyCode> pressedKeys = new HashSet<>();

    private Map<String, MediaPlayer> sounds = new HashMap<>();

    private Memento gameMemento;

    KeyboardInputHandler(GameEngine model) {
        this.model = model;

        // TODO (longGoneUser): Is there a better place for this code?
        URL mediaUrl = getClass().getResource("/shoot.wav");
        String jumpURL = mediaUrl.toExternalForm();

        Media sound = new Media(jumpURL);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        sounds.put("shoot", mediaPlayer);
    }

    void handlePressed(KeyEvent keyEvent) {
        if (pressedKeys.contains(keyEvent.getCode())) {
            return;
        }
        pressedKeys.add(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            Memento memento = model.save();
            if (model.shootPressed()) {
                MediaPlayer shoot = sounds.get("shoot");
                shoot.stop();
                shoot.play();
                save(memento);
            }
        }

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = true;
        }
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = true;
        }

        if (keyEvent.getCode().equals(KeyCode.R) && gameMemento != null) {
            model.restore(gameMemento);
        }

        if (keyEvent.getCode().equals(KeyCode.DIGIT1)){
            model.cheatRemove("Enemy", "Slow");
        }

        if (keyEvent.getCode().equals(KeyCode.DIGIT2)){
            model.cheatRemove("Enemy", "Fast");
        }

        if (keyEvent.getCode().equals(KeyCode.DIGIT3)){
            model.cheatRemove("EnemyProjectile", "Slow");
        }

        if (keyEvent.getCode().equals(KeyCode.DIGIT4)){
            model.cheatRemove("EnemyProjectile", "Fast");
        }

        if (left) {
            model.leftPressed();
        }

        if(right){
            model.rightPressed();
        }
    }

    void handleReleased(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = false;
            model.leftReleased();
        }
        if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            model.rightReleased();
            right = false;
        }
    }

    @Override
    public void save(Memento memento) {
        this.gameMemento = memento;
    }
}
