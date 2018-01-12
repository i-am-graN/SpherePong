package spherepong.components;

import com.artemis.Component;

public class Spring extends Component {

    public int aID;
    public int bID;

    public Spring() {
	this(0, 0);
    }

    public Spring(int aID, int bID) {
	this.aID = aID;
	this.bID = bID;
    }
}
