package spherepong.systems;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.GroupManager;

import spherepong.EntityFactory;
import spherepong.components.PlayerAI;
import spherepong.components.Position;
import spherepong.components.Velocity;

public class PlayerAISystem extends EntitySystem {

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

	    Vector3f direction = ballPosition.position.sub(playerPosition.position, new Vector3f());
	    playerVelocity.velocity = direction.mul(0, 1, 0);
	}
    }
}
