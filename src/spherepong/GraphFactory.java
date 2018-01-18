package spherepong;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphFactory {

    public List<String> createFileGraph() {
	File root = new File("C:/");
	return showFiles(root);
    }

    public static void main(String... args) {
	File root = new File("C:/Users/htgra/Documents/My Games/Fallout4");
	System.out.println(showFiles(root));
    }

    public static List<String> showFiles(File path) {
	List<String> list = new ArrayList<String>();

	if (path.isDirectory()) {
	    System.out.println("Directory: " + path.getName());
	    for (File file : path.listFiles()) {
		list.addAll(showFiles(file));
	    }
	} else {
	    System.out.println("File: " + path.getName());
	    list.add(path.getAbsolutePath());
	}
	return list;
    }
}
