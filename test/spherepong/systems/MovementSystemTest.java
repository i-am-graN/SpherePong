package spherepong.systems;

import org.testng.annotations.Test;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;

import spherepong.UnitTest;
import spherepong.components.Position;
import spherepong.components.Velocity;

public class MovementSystemTest extends UnitTest {

    @Test
    public void testMovementSystemMoves() {
	World world = new World(new WorldConfigurationBuilder().with(new MovementSystem()).build());
	Entity entity = world.createEntity();
	entity.edit().add(new Position(1, 0, 0)).add(new Velocity(1, 2, 3));

	world.process();

	Position position = entity.getComponent(Position.class);
	assertEquals(position.position, new Position(2, 2, 3).position);
    }

}
