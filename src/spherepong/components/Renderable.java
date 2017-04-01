package spherepong.components;

import com.artemis.Component;

public class Renderable extends Component {

    public float height;
    public float width;

    public Renderable() {
	this(0, 0);
    }

    public Renderable(float width, float height) {
	this.width = width;
	this.height = height;
    }

}
