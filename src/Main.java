
import controller.CryptoController;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import view.CryptoView;

import java.security.Security;

public class Main {
    public static void main(String[] args) {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        CryptoView view = new CryptoView();
        new CryptoController(view);
    }
}
