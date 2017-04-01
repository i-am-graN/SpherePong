package spherepong.components;

import org.joml.Vector3f;

import com.artemis.Component;

public class Position extends Component {

    public Vector3f position;

    public Position() {
	this(0, 0, 0);
    }

    public Position(float x, float y, float z) {
	this.position = new Vector3f(x, y, z);
    }
}
