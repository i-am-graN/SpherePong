package spherepong;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;

import spherepong.components.BoundingBox;
import spherepong.components.PlayerAI;
import spherepong.components.Position;
import spherepong.components.Renderable;
import spherepong.components.Velocity;

public class EntityFactory {

    private static final int BALL_DIAMETER = 20;
    private static final int WALL_THICKNESS = 20;
    public static final int PLAYER_WIDTH = WALL_THICKNESS;
    public static final int PLAYER_LENGTH = PLAYER_WIDTH * 7;
    
    public static final String ENTITY_BALL = "BALL";
    
    private World world;

    public EntityFactory(World world) {
	this.world = world;
    }

    public Entity createBall(float x, float y, float z) {
	Entity entity = this.world.createEntity();
	entity.edit().add(new BoundingBox(BALL_DIAMETER, BALL_DIAMETER)).add(new Position(x, y, z)).add(new Renderable(BALL_DIAMETER, BALL_DIAMETER)).add(new Velocity(0.5f, 1, 0));
	this.world.getSystem(GroupManager.class).add(entity, ENTITY_BALL);
	return entity;
    }

    public Entity createTopWall() {
	Entity entity = this.world.createEntity();
	entity.edit().add(new BoundingBox(SpherePong.WINDOW_WIDTH, WALL_THICKNESS)).add(new Renderable(SpherePong.WINDOW_WIDTH, WALL_THICKNESS)).add(new Position(0, SpherePong.WINDOW_HEIGHT - WALL_THICKNESS, 0));
	return entity;
    }

    public Entity createBottomWall() {
	Entity entity = this.world.createEntity();
	entity.edit().add(new BoundingBox(SpherePong.WINDOW_WIDTH, WALL_THICKNESS)).add(new Renderable(SpherePong.WINDOW_WIDTH, WALL_THICKNESS)).add(new Position(0, 0, 0));
	return entity;
    }
    public Entity createPlayerA() {
	Entity entity = this.world.createEntity();
	float xPos = 0;
	float yPos = SpherePong.WINDOW_HEIGHT / 2 - PLAYER_LENGTH / 2;
	entity.edit().add(new BoundingBox(PLAYER_WIDTH, PLAYER_LENGTH)).add(new Renderable(PLAYER_WIDTH, PLAYER_LENGTH)).add(new Position(xPos, yPos, 0)).add(new Velocity(0, 0, 0)).add(new PlayerAI());
	return entity;
    }
    public Entity createPlayerB() {
	Entity entity = this.world.createEntity();
	float xPos = SpherePong.WINDOW_WIDTH - PLAYER_WIDTH;
	float yPos = SpherePong.WINDOW_HEIGHT / 2 - PLAYER_LENGTH / 2;
	entity.edit().add(new BoundingBox(PLAYER_WIDTH, PLAYER_LENGTH)).add(new Renderable(PLAYER_WIDTH, PLAYER_LENGTH)).add(new Position(xPos, yPos, 0)).add(new Velocity(0, 0, 0)).add(new PlayerAI());
	return entity;
    }
}
