package spherepong.systems;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;

import spherepong.components.Acceleration;
import spherepong.components.Mass;
import spherepong.components.Position;

public class GravitySystem extends EntitySystem {

    private Vector3f force;

    public GravitySystem(float force) {
	super(Aspect.all(Position.class, Acceleration.class, Mass.class));
	this.force = new Vector3f(0, force * -1, 0);
    }

    @Override
    protected void processSystem() {
	Bag<Entity> entities = this.getEntities();
	for (int i = 0; i < entities.size(); ++i) {
	    Entity entity = entities.get(i);

	    this.applyForce(entity, force);
	}
    }

    protected void applyForce(Entity entity, Vector3f force) {
	Acceleration acceleration = entity.getComponent(Acceleration.class);
	acceleration.acceleration.add(force);
    }
}
