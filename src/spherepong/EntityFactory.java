package spherepong;

import com.artemis.Entity;
import com.artemis.World;

import spherepong.components.Position;
import spherepong.components.Renderable;
import spherepong.components.Velocity;

public class EntityFactory {

    private World world;

    public EntityFactory(World world) {
	this.world = world;
    }

    public Entity createBall(float x, float y, float z) {
	Entity entity = world.createEntity();
	entity.edit().add(new Position(x, y, z)).add(new Renderable(10, 10)).add(new Velocity(1, 1, 0));
	return entity;
    }
}
