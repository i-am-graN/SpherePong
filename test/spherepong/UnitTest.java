package spherepong;

import java.lang.reflect.Field;

import org.testng.Assert;

import com.artemis.Component;

public class UnitTest extends Assert {

    public static <T extends Component> void assertEqualComponents(T component1, T component2) {
	Field[] fields = component1.getClass().getFields();

	for (Field field : fields) {
	    try {
		assertEquals(field.get(component1), field.get(component2));
	    } catch (IllegalArgumentException | IllegalAccessException e) {
		fail("Components does not match", e);
	    }
	}
    }
}
