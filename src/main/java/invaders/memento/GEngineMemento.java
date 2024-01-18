package invaders.memento;

import java.util.ArrayList;
import java.util.List;

import invaders.entities.Player;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.rendering.Renderable;

public class GEngineMemento implements Memento {
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Bunker> bunkers = new ArrayList<>();
    private List<Projectile> projectiles = new ArrayList<>();

    private int secCounter = 0;
    private int score = 0;

    public GEngineMemento(List<Renderable> renderables, Player player, List<Enemy> enemies, int secCounter, int score) {
        this.player = player.cloneObject();
        this.secCounter = secCounter;
        this.score = score;

        for (Enemy enemy : enemies) {
            this.enemies.add(enemy.cloneObject());
        }

        for (Renderable renderable : renderables) {
            if (renderable.getRenderableObjectName().equals("Bunker")) {
                this.bunkers.add(((Bunker) renderable).cloneObject());
            } else if (renderable.getRenderableObjectName().equals("EnemyProjectile")
                    || renderable.getRenderableObjectName().equals("PlayerProjectile")) {
                this.projectiles.add(((Projectile) renderable).cloneObject());
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Bunker> getBunkers() {
        return bunkers;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public int getSecCounter() {
        return secCounter;
    }

    public int getScore(){
        return score;
    }
}

