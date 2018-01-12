package spherepong.systems;

import org.joml.Vector3f;

import com.artemis.Entity;

import spherepong.components.Acceleration;

public class RepulsionSystem extends AttractionSystem {

    public RepulsionSystem(float radius, float scalar) {
	super(radius, scalar);
    }

    @Override
    protected void applyForce(Entity entity, Vector3f force) {
	Acceleration acceleration = entity.getComponent(Acceleration.class);
	acceleration.acceleration.add(force.mul(-1));
    }
}
