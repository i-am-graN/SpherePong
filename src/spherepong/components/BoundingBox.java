package spherepong.components;

import com.artemis.Component;

public class BoundingBox extends Component {

    public float width, height, offsetX, offsetY;

    public BoundingBox() {
	this(0, 0, 0, 0);
    }

    public BoundingBox(float width, float height) {
	this(width, height, 0, 0);
    }

    public BoundingBox(float width, float height, float offsetX, float offsetY) {
	this.width = width;
	this.height = height;
	this.offsetX = offsetX;
	this.offsetY = offsetY;
    }
}
