package spherepong;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.GroupManager;

import spherepong.exceptions.SystemExitException;
import spherepong.systems.CollisionSystem;
import spherepong.systems.MovementSystem;
import spherepong.systems.PlayerAISystem;
import spherepong.systems.RenderingSystem;

public class SpherePong extends Thread {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    private static final float GAME_DELTA = 10.0f;

    private World world;

    public void initialize() {

	WorldConfiguration config = new WorldConfigurationBuilder()
		.with(new GroupManager())
		.with(new CollisionSystem())
		.with(new PlayerAISystem())
		.with(new MovementSystem())
		.with(new RenderingSystem(WINDOW_WIDTH, WINDOW_HEIGHT, "SpherePong"))
		.build();

	this.world = new World(config);

	EntityFactory factory = new EntityFactory(this.world);

	// Ball
	factory.createBall(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 0);

	// Walls
	factory.createTopWall();
	factory.createBottomWall();
	
	// Players
	factory.createPlayerA();
	factory.createPlayerB();
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