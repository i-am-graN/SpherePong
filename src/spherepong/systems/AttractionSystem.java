package spherepong.systems;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;

import spherepong.components.Acceleration;
import spherepong.components.Mass;
import spherepong.components.Position;
import spherepong.components.Velocity;

public class AttractionSystem extends EntitySystem {

    private static final float G = 1;// 6.67300E-11;
    private float radius;
    private float scalar;

    public AttractionSystem(float radius, float scalar) {
	super(Aspect.all(Position.class, Mass.class));
	this.radius = radius;
	this.scalar = scalar;
    }

    @Override
    protected void processSystem() {
	/**
	 * http://www.school-for-champions.com/science/gravitation_force_objects.htm#.WjlEVt_iZhE
	 * https://en.wikipedia.org/wiki/Euler%27s_laws_of_motion
	 * 
	 * F = G * M * m / R2 (Force of attraction)
	 * F = m * a (Newton's second law of motion)
	 * p = m * v (momentum, inertia)
	 */

	Bag<Entity> entities = this.getEntities();
	for (int i = 0; i < entities.size(); ++i) {
	    for (int j = 0; j < entities.size(); ++j) {
		Entity e1 = entities.get(i);
		Entity e2 = entities.get(j);

		if ((e1 == e2) || (e2.getComponent(Acceleration.class) == null)) {
		    continue;
		}

		Position e1Pos = e1.getComponent(Position.class);
		Mass e1Mass = e1.getComponent(Mass.class);

		Position e2Pos = e2.getComponent(Position.class);
		Mass e2Mass = e2.getComponent(Mass.class);

		Vector3f force = new Vector3f(e1Pos.position).sub(e2Pos.position);
		float distance = force.distance(0, 0, 0);

		if (distance < 10) {
		    continue; // Do nothing
		    // Random random = new Random();
		    // force = new Vector3f(random.nextInt(3) - 2, random.nextInt(3) - 2, 0);
		    // force.mul(0.001f);
		} else if (distance < this.radius) {
		    force.normalize();

		    float magnitude = (G * e1Mass.mass * e2Mass.mass) / (distance * distance);
		    force.mul(magnitude);

		    force.mul(e1Mass.mass).mul(this.scalar);
		    this.applyForce(e2, force);
		}

		// System.out.println(String.format("magnitude = %s", magnitude));
		// System.out.println(String.format("Force = %s", force));
	    }
	}
    }

    protected void applyForce(Entity entity, Vector3f force) {
	Acceleration acceleration = entity.getComponent(Acceleration.class);
	acceleration.acceleration.add(force);
    }
}
