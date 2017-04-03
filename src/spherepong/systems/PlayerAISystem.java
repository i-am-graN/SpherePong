package spherepong.systems;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.GroupManager;

import spherepong.EntityFactory;
import spherepong.SpherePong;
import spherepong.components.PlayerAI;
import spherepong.components.Position;
import spherepong.components.Velocity;

public class PlayerAISystem extends EntitySystem {

    private static final int MAX_SPEED = 5;

    public PlayerAISystem() {
	super(Aspect.all(PlayerAI.class, Position.class, Velocity.class));
    }

    @Override
    protected void processSystem() {
	Entity ball = this.world.getSystem(GroupManager.class).getEntities(EntityFactory.ENTITY_BALL).get(0);
	Position ballPosition = ball.getComponent(Position.class);

	for (Entity e : this.getEntities()) {
	    Position playerPosition = e.getComponent(Position.class);
	    Velocity playerVelocity = e.getComponent(Velocity.class);
	    Vector3f playerCenterPosition = playerPosition.position.add(EntityFactory.PLAYER_WIDTH / 2, EntityFactory.PLAYER_LENGTH / 2, 0, new Vector3f());
	    Vector3f distanceToBall = ballPosition.position.sub(playerPosition.position, new Vector3f());

	    float percentage = (SpherePong.WINDOW_WIDTH - Math.abs(distanceToBall.x)) / SpherePong.WINDOW_WIDTH;
	    percentage = (float) (Math.pow(percentage, Math.E));

	    float direction = playerCenterPosition.y > ballPosition.position.y ? -1 : 1;

	    playerVelocity.velocity.y = direction * percentage * MAX_SPEED;
	}
    }
}
