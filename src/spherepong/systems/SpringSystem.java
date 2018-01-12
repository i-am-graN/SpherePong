package spherepong.systems;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;

import spherepong.components.Acceleration;
import spherepong.components.Mass;
import spherepong.components.Position;
import spherepong.components.Spring;
import spherepong.components.Velocity;

public class SpringSystem extends EntitySystem {

    private static final float RESTLENGTH = 10;
    private static final float DAMPING_COEFFICENT = 0.2f;
    private static final float STIFFNESS = 0.1f;

    public SpringSystem() {
	super(Aspect.all(Spring.class));
    }

    @Override
    protected void processSystem() {
	/**
	 * Hooke's Law with a damping force
	 * F = -kx-cv
	 * 
	 * k = Spring force
	 * x = Distance from rest length
	 * c = Damping coefficient
	 * v = Relative velocity
	 */
	Bag<Entity> entities = this.getEntities();
	for (int i = 0; i < entities.size(); ++i) {
	    Entity entity = entities.get(i);
	    Spring spring = entity.getComponent(Spring.class);

	    Entity entityA = this.getWorld().getEntity(spring.aID);
	    Entity entityB = this.getWorld().getEntity(spring.bID);
	    Position pointA = entityA.getComponent(Position.class);
	    Position pointB = entityB.getComponent(Position.class);

	    Velocity velocityA = entityA.getComponent(Velocity.class);
	    Velocity velocityB = entityB.getComponent(Velocity.class);

	    Vector3f force = new Vector3f(pointB.position).sub(pointA.position);
	    float length = force.distance(0, 0, 0);
	    float error = length - RESTLENGTH;

	    force.normalize();
	    force.mul(-1 * STIFFNESS * error);

	    if (velocityA.velocity.distance(0, 0, 0) != 0) {
		Vector3f dampingForce = new Vector3f(velocityB.velocity).sub(velocityA.velocity);
		dampingForce.mul(DAMPING_COEFFICENT);
		force.sub(dampingForce);
	    }
	    
	    this.applyForce(entityB, force);
	    this.applyForce(entityA, force.mul(-1));
	}
    }

    protected void applyForce(Entity entity, Vector3f force) {
	Acceleration acceleration = entity.getComponent(Acceleration.class);
	if (acceleration != null) {
	    acceleration.acceleration.add(force);
	}
    }
}
