package invaders.factory;

import invaders.engine.GameEngine;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

public class EnemyProjectile extends Projectile{
    private ProjectileStrategy strategy;

    public EnemyProjectile(Vector2D position, ProjectileStrategy strategy, Image image) {
        super(position,image);
        this.strategy = strategy;
    }

    @Override
    public void update(GameEngine model) {
        strategy.update(this);

        if(this.getPosition().getY()>= model.getGameHeight() - this.getImage().getHeight()){
            this.takeDamage(1);
        }

        if (!this.isAlive()){
            model.getPendingToRemoveGameObject().add(this);
            model.getPendingToRemoveRenderable().add(this);
        }

    }
    @Override
    public String getRenderableObjectName() {
        return "EnemyProjectile";
    }

    public ProjectileStrategy getStrategy() {
        return strategy;
    }

    @Override
    public Projectile cloneObject() {
        double x = this.getPosition().getX();
        double y = this.getPosition().getY();
        return new EnemyProjectile(new Vector2D(x,y), this.strategy, this.getImage());
    }
}
