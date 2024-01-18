package invaders.engine;

import java.util.ArrayList;
import java.util.List;

import invaders.ConfigReader;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.factory.EnemyProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.memento.GEngineMemento;
import invaders.memento.Memento;
import invaders.memento.Originator;
import invaders.observer.Observer;
import invaders.observer.Subject;
import invaders.entities.Player;
import invaders.rendering.Renderable;
import invaders.strategy.ProjectileStrategy;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import org.json.simple.JSONObject;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine implements Subject, Originator {
	private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
	private List<GameObject> pendingToAddGameObject = new ArrayList<>();
	private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();

	private List<Renderable> pendingToAddRenderable = new ArrayList<>();
	private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();

	private List<Renderable> renderables = new ArrayList<>();

	private List<Observer> observers = new ArrayList<>();

	private Player player;
	private List<Enemy> enemies = new ArrayList<>();

	private Timeline timeline;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int timer = 45;
	private int secCounter = 0;
	private int score = 0;

	public GameEngine(String config) {
		// Read the config here
		ConfigReader.parse(config);

		// Get game width and height
		gameWidth = ((Long) ((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
		gameHeight = ((Long) ((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

		// Get player info
		this.player = new Player(ConfigReader.getPlayerInfo());
		renderables.add(player);

		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		// Get Bunkers info
		for (Object eachBunkerInfo : ConfigReader.getBunkersInfo()) {
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}

		EnemyBuilder enemyBuilder = new EnemyBuilder();
		// Get Enemy info
		for (Object eachEnemyInfo : ConfigReader.getEnemiesInfo()) {
			Enemy enemy = director.constructEnemy(this, enemyBuilder, (JSONObject) eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
			enemies.add(enemy);
		}

		timeline = new Timeline(new KeyFrame(Duration.seconds(1), t -> {
			secCounter++;
			notifyObservers("time " + secCounter);
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	/**
	 * Updates the game/simulation
	 */
	public void update() {
		timer += 1;

		movePlayer();

		for (GameObject go : gameObjects) {
			go.update(this);
		}

		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderableA = renderables.get(i);
			for (int j = i + 1; j < renderables.size(); j++) {
				Renderable renderableB = renderables.get(j);

				if ((renderableA.getRenderableObjectName().equals("Enemy")
						&& renderableB.getRenderableObjectName().equals("EnemyProjectile"))
						|| (renderableA.getRenderableObjectName().equals("EnemyProjectile")
								&& renderableB.getRenderableObjectName().equals("Enemy"))
						||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile")
								&& renderableB.getRenderableObjectName().equals("EnemyProjectile"))) {
				} else {
					if (renderableA.isColliding(renderableB)
							&& (renderableA.getHealth() > 0 && renderableB.getHealth() > 0)) {
						renderableA.takeDamage(1);
						renderableB.takeDamage(1);

						if (renderableA.getRenderableObjectName().equals("PlayerProjectile")) {
							addScore(renderableB);
						} else if (renderableB.getRenderableObjectName().equals("PlayerProjectile")) {
							addScore(renderableA);
						}
					}
				}
			}
		}

		if (isGameOver()) {
			stopTimer();
			notifyObservers("gameOver ");
		}

		// ensure that renderable foreground objects don't go off-screen
		int offset = 1;
		for (Renderable ro : renderables) {
			if (!ro.getLayer().equals(Renderable.Layer.FOREGROUND)) {
				continue;
			}
			if (ro.getPosition().getX() + ro.getWidth() >= gameWidth) {
				ro.getPosition().setX((gameWidth - offset) - ro.getWidth());
			}

			if (ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(offset);
			}

			if (ro.getPosition().getY() + ro.getHeight() >= gameHeight) {
				ro.getPosition().setY((gameHeight - offset) - ro.getHeight());
			}

			if (ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(offset);
			}
		}

	}

	private void addScore(Renderable hit) {
		String hitType = hit.getRenderableObjectName();
		if (hitType.equals("Enemy")) {
			Enemy enemy = (Enemy) hit;
			ProjectileStrategy strategy = enemy.getProjectileStrategy();
			if (strategy.getProjectileStrategyName().equals("Fast")) {
				score += 4;
			} else {
				score += 3;
			}
		} else if (hitType.equals("EnemyProjectile")) {
			EnemyProjectile projectile = (EnemyProjectile) hit;
			ProjectileStrategy strategy = projectile.getStrategy();
			if (strategy.getProjectileStrategyName().equals("Fast")) {
				score += 2;
			} else {
				score += 1;
			}
		}

		notifyObservers("score " + score);
	}

	public List<Renderable> getRenderables() {
		return renderables;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public List<GameObject> getPendingToAddGameObject() {
		return pendingToAddGameObject;
	}

	public List<GameObject> getPendingToRemoveGameObject() {
		return pendingToRemoveGameObject;
	}

	public List<Renderable> getPendingToAddRenderable() {
		return pendingToAddRenderable;
	}

	public List<Renderable> getPendingToRemoveRenderable() {
		return pendingToRemoveRenderable;
	}

	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased() {
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}

	public void rightPressed() {
		this.right = true;
	}

	public boolean shootPressed() {
		if (timer > 45 && player.isAlive()) {
			Projectile projectile = player.shoot();
			gameObjects.add(projectile);
			renderables.add(projectile);
			timer = 0;
			return true;
		}
		return false;
	}

	private void movePlayer() {
		if (left) {
			player.left();
		}

		if (right) {
			player.right();
		}
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.removeIf(o -> o.equals(observer));
	}

	public void stopTimer() {
		timeline.stop();
	}

	public int getSecCounter() {
		return secCounter;
	}

	public boolean playerWon() {
		boolean allEnemiesDead = true;
		for (Enemy enemy : enemies) {
			if (enemy.isAlive()) {
				allEnemiesDead = false;
				break;
			}
		}
		return allEnemiesDead;
	}

	public boolean playerLost() {
		return !player.isAlive();
	}

	public boolean isGameOver() {
		return playerWon() || playerLost();
	}

	@Override
	public void notifyObservers(String message) {
		for (Observer observer : observers) {
			observer.update(message);
		}
	}

	@Override
	public Memento save() {
		return new GEngineMemento(renderables, player, enemies, secCounter, score);
	}

	@Override
	public void restore(Memento memento) {
		GEngineMemento gEngineMemento = (GEngineMemento) memento;

		pendingToRemoveRenderable.add(this.player);
		this.player = gEngineMemento.getPlayer().cloneObject();
		pendingToAddRenderable.add(this.player);

		this.enemies.clear();
		this.secCounter = gEngineMemento.getSecCounter();
		this.score = gEngineMemento.getScore();

		pendingToRemoveGameObject.addAll(gameObjects);
		pendingToRemoveRenderable.addAll(renderables);

		for (Enemy enemy : enemies) {
			enemy = enemy.cloneObject();
			pendingToAddGameObject.add(enemy);
			pendingToAddRenderable.add(enemy);
			this.enemies.add(enemy);
		}

		for (Bunker bunker : gEngineMemento.getBunkers()) {
			bunker = bunker.cloneObject();
			pendingToAddGameObject.add(bunker);
			pendingToAddRenderable.add(bunker);
		}

		for (Projectile projectile : gEngineMemento.getProjectiles()) {
			projectile = projectile.cloneObject();
			pendingToAddRenderable.add(projectile);
			pendingToAddGameObject.add(projectile);
		}

		this.notifyObservers("time " + this.secCounter);
		this.notifyObservers("score " + this.score);

		if (!isGameOver() && timeline.getStatus().equals(Timeline.Status.STOPPED)) {
			timeline.play();
		}
	}

	private boolean checkRenderable(Renderable re, String type, String strategy) {
		if (re.getRenderableObjectName().equals(type)) {
			if (type.equals("Enemy")) {
				Enemy enemy = (Enemy) re;
				if (enemy.getProjectileStrategy().getProjectileStrategyName().equals(strategy)) {
					return true;
				}
			} else if (type.equals("EnemyProjectile")) {
				EnemyProjectile projectile = (EnemyProjectile) re;
				if (projectile.getStrategy().getProjectileStrategyName().equals(strategy)) {
					return true;
				}
			}
		}
		return false;
	}

	public void cheatRemove(String type, String strategy) {
		int multiplier = 1;
		if (type.equals("Enemy"))
			multiplier = 3;
		if (strategy.equals("Fast"))
			multiplier += 1;

		for (Renderable re : renderables){
			if (checkRenderable(re, type, strategy)){
				pendingToRemoveGameObject.add((GameObject)re);
				pendingToRemoveRenderable.add(re);
				score += multiplier;
				re.takeDamage(Integer.MAX_VALUE);
			}
		}

		notifyObservers("score " + score);
	}
}
