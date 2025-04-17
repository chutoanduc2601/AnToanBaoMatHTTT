// === Main.java ===
import controller.CryptoController;
import view.CryptoView;

public class Main {
    public static void main(String[] args) {
        CryptoView view = new CryptoView();
        new CryptoController(view);
    }
}
