package spherepong.systems;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;

import spherepong.UnitTest;
import spherepong.components.BoundingBox;
import spherepong.components.Position;
import spherepong.components.Velocity;

public class CollisionSystemTest extends UnitTest {

    @Test
    public void testMovingBoxBouncesOffStationaryBox() {
	World world = new World(new WorldConfigurationBuilder().with(new CollisionSystem()).build());
	Entity box1 = world.createEntity();
	Entity box2 = world.createEntity();
	box1.edit().add(new Position(19.9f, 5, 0)).add(new BoundingBox(20, 10));
	box2.edit().add(new Position(0, 0, 0)).add(new BoundingBox(20, 10)).add(new Velocity(1, 1, 0));

	world.process();

	assertEqualComponents(box2.getComponent(Velocity.class), new Velocity(1, -1, 0));
    }

    @Test
    public void testMovingBoxesDoesNotCollide() {
	World world = new World(new WorldConfigurationBuilder().with(new CollisionSystem()).build());
	Entity box1 = world.createEntity();
	Entity box2 = world.createEntity();
	box1.edit().add(new Position(0, 0, 0)).add(new BoundingBox(10, 10)).add(new Velocity(1, 1, 0));
	box2.edit().add(new Position(20, 20, 20)).add(new BoundingBox(10, 10)).add(new Velocity(1, 1, 0));

	world.process();

	assertEqualComponents(box1.getComponent(Velocity.class), new Velocity(1, 1, 0));
	assertEqualComponents(box2.getComponent(Velocity.class), new Velocity(1, 1, 0));
    }

}
