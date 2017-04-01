package spherepong;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;

import spherepong.exceptions.SystemExitException;
import spherepong.systems.RenderingSystem;

public class SpherePong extends Thread {

    private static final float GAME_DELTA = 10.0f;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private World world;

    public SpherePong() {
    }

    public void initialize() {

	WorldConfiguration config = new WorldConfigurationBuilder()
		.with(new RenderingSystem(WINDOW_WIDTH, WINDOW_HEIGHT, "SpherePong")).build();

	world = new World(config);

	EntityFactory factory = new EntityFactory(world);
	factory.createBall(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 0);
    }

    @Override
    public void run() {
	this.initialize();

	try {
	    while (true) {
		world.setDelta(GAME_DELTA);
		world.process();
	    }
	} catch (SystemExitException e) {
	}
    }
}