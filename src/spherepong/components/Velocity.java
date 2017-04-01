package spherepong.components;

import org.joml.Vector3f;

import com.artemis.Component;

public class Velocity extends Component {

    public Vector3f velocity;

    public Velocity() {
	this(0, 0, 0);
    }

    public Velocity(float x, float y, float z) {
	this.velocity = new Vector3f(x, y, z);
    }
}
