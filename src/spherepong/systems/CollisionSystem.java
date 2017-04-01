package spherepong.systems;

import java.awt.Rectangle;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;

import spherepong.components.BoundingBox;
import spherepong.components.Position;
import spherepong.components.Velocity;

public class CollisionSystem extends EntitySystem {

    private enum Axis {
	X, Y, BOTH
    };

    public CollisionSystem() {
	super(Aspect.all(Position.class, BoundingBox.class));
    }

    @Override
    protected void processSystem() {

	Bag<Entity> entities = this.getEntities();
	Entity e1, e2;

	for (int i = 0; i < entities.size(); ++i) {
	    e1 = entities.get(i);
	    for (int j = i + 1; j < entities.size(); ++j) {
		e2 = entities.get(j);

		if (hasCollision(e1, e2)) {
		    Axis axis = getCollisionAxis(e1, e2);
		    Vector3f modifier = getVelocityModifier(axis);

		    Velocity e1Velocity = e1.getComponent(Velocity.class);
		    Velocity e2Velocity = e2.getComponent(Velocity.class);

		    if (e1Velocity != null) {
			e1Velocity.velocity.mul(modifier);
		    }
		    if (e2Velocity != null) {
			e2Velocity.velocity.mul(modifier);
		    }
		}
	    }
	}

	// Position position = e.getComponent(Position.class);
	// BoundingBox bounds = e.getComponent(BoundingBox.class);
	// Velocity velocity = e.getComponent(Velocity.class);

	// position.position.add(velocity.velocity);
    }

    private boolean hasCollision(Entity e1, Entity e2) {
	Vector3f e1Position = e1.getComponent(Position.class).position;
	Vector3f e2Position = e2.getComponent(Position.class).position;

	BoundingBox e1Box = e1.getComponent(BoundingBox.class);
	BoundingBox e2Box = e2.getComponent(BoundingBox.class);

	// TODO Losing precision with int's
	Rectangle rectangle1 = new Rectangle((int) e1Position.x, (int) e1Position.y, (int) e1Box.width,
		(int) e1Box.height);
	Rectangle rectangle2 = new Rectangle((int) e2Position.x, (int) e2Position.y, (int) e2Box.width,
		(int) e2Box.height);

	return rectangle1.intersects(rectangle2);
    }

    private Axis getCollisionAxis(Entity e1, Entity e2) {
	Vector3f e1Position = e1.getComponent(Position.class).position;
	Vector3f e2Position = e2.getComponent(Position.class).position;

	BoundingBox e1Box = e1.getComponent(BoundingBox.class);
	BoundingBox e2Box = e2.getComponent(BoundingBox.class);

	Vector3f e1Top = new Vector3f(0, e1Box.height + e1Position.y, 0);
	Vector3f e2Top = new Vector3f(0, e2Box.height + e2Position.y, 0);
	Vector3f e1Left = new Vector3f(e1Box.width + e1Position.x, 0, 0);
	Vector3f e2Left = new Vector3f(e2Box.width + e2Position.x, 0, 0);

	float xDiff = e1Left.distance(e2Left);
	float yDiff = e1Top.distance(e2Top);

	if (xDiff < yDiff) {
	    return Axis.X;
	} else if (xDiff > yDiff) {
	    return Axis.Y;
	} else {
	    return Axis.BOTH;
	}
    }

    private Vector3f getVelocityModifier(Axis axis) {
	if (axis == Axis.X) {
	    return new Vector3f(-1, 1, 1);
	} else if (axis == Axis.Y) {
	    return new Vector3f(1, -1, 1);
	} else {
	    return new Vector3f(-1, -1, 1);
	}
    }
}
