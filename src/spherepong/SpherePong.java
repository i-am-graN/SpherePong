package spherepong;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;

import spherepong.components.Spring;
import spherepong.exceptions.SystemExitException;
import spherepong.systems.RepulsionSystem;
import spherepong.systems.AttractionSystem;
import spherepong.systems.CollisionSystem;
import spherepong.systems.GravitySystem;
import spherepong.systems.MovementSystem;
import spherepong.systems.RenderingSystem;
import spherepong.systems.SpringSystem;

public class SpherePong extends Thread {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    private static final float GAME_DELTA = 10.0f;

    private World world;

    public void initialize() {

	WorldConfiguration config = new WorldConfigurationBuilder()
		// .with(new GroupManager())
//		 .with(new CollisionSystem())
		// .with(new PlayerAISystem())
		.with(new MovementSystem())
//		.with(new AttractionSystem(5000, 0.5f))
		.with(new RepulsionSystem(5000, 0.5f))
		.with(new GravitySystem(0.3f))
		.with(new SpringSystem())
		.with(new RenderingSystem(WINDOW_WIDTH, WINDOW_HEIGHT, "SpherePong")).build();

	this.world = new World(config);

	EntityFactory factory = new EntityFactory(this.world);

	Entity previousEntity = factory.createStationaryNode(20);
	
	for (int i = 0; i < 20; ++i) {
	    Entity currentEntity = factory.createAccelerationNode(4);
	    this.world.createEntity().edit().add(new Spring(currentEntity.getId(), previousEntity.getId()));
	    previousEntity = currentEntity;
	}
    }

    @Override
    public void run() {
	this.initialize();

	try {
	    while (true) {
		this.world.setDelta(GAME_DELTA);
		this.world.process();
	    }
	} catch (SystemExitException e) {
	}
    }
}