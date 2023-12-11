import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.clavardage.ChatSystem.*;

public class MessagesBDDTests {

    private static MessagesBDD bdd;

    @BeforeAll
    public static void setUp() {
        MessagesBDDTests.bdd = MessagesBDD.getInstance()
    }
}
