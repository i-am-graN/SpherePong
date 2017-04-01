package spherepong;

public class Launcher {
        
    public void run() {
	Thread game = new SpherePong();
	game.start();
    }

    public static void main(String[] args) {
	new Launcher().run();
    }

}
