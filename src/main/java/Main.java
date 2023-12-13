import name.panitz.game.app.SimpleGame;
import name.panitz.game.framework.swing.SwingGame;

public class Main {
    public static void main(String[] args) {
        SwingGame.startGame(new SimpleGame<>(() -> System.exit(0)));
    }
}
