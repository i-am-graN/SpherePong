package spherepong.components;

import org.joml.Vector3f;

import com.artemis.Component;

public class Acceleration extends Component {

    public Vector3f acceleration;

    public Acceleration() {
	this(0, 0, 0);
    }

    public Acceleration(float x, float y, float z) {
	this.acceleration = new Vector3f(x, y, z);
    }
}
