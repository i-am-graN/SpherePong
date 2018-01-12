package spherepong.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;

import spherepong.components.Acceleration;
import spherepong.components.Position;
import spherepong.components.Velocity;

public class MovementSystem extends EntitySystem {

    public MovementSystem() {
	super(Aspect.all(Position.class, Velocity.class));
    }

    @Override
    protected void processSystem() {
	for (Entity e : this.getEntities()) {
	    Position position = e.getComponent(Position.class);
	    Velocity velocity = e.getComponent(Velocity.class);
	    Acceleration acceleration = e.getComponent(Acceleration.class);

	    if (acceleration != null) {
		velocity.velocity.add(acceleration.acceleration);
		acceleration.acceleration.mul(0);
	    }

	    position.position.add(velocity.velocity);
	}
    }
}
