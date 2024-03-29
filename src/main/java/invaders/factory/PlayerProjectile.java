package invaders.factory;

import invaders.engine.GameEngine;
import invaders.physics.Vector2D;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

import java.io.File;

public class PlayerProjectile extends Projectile {
    private ProjectileStrategy strategy;

    public PlayerProjectile(Vector2D position, ProjectileStrategy strategy) {
        super(position,
                new Image(new File("src/main/resources/player_shot.png").toURI().toString(), 10, 10, true, true));
        this.strategy = strategy;
    }

    @Override
    public void update(GameEngine model) {
        strategy.update(this);

        if (this.getPosition().getY() <= this.getImage().getHeight()) {
            this.takeDamage(1);
        }

        if (!this.isAlive()) {
            model.getPendingToRemoveGameObject().add(this);
            model.getPendingToRemoveRenderable().add(this);
        }
    }

    @Override
    public String getRenderableObjectName() {
        return "PlayerProjectile";
    }

    @Override
    public Projectile cloneObject() {
        double x = this.getPosition().getX();
        double y = this.getPosition().getY();
        return new PlayerProjectile(new Vector2D(x, y), this.strategy);
    }
}
