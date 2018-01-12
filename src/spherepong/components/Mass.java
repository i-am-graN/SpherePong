package spherepong.components;

import com.artemis.Component;

public class Mass extends Component {

    public float mass;

    public Mass() {
	this(0);
    }

    public Mass(float mass) {
	this.mass = mass;
    }
}
