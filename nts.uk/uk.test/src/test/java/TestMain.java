import java.awt.EventQueue;
import common.CreateMainTest;

public class TestMain {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    CreateMainTest createMainTest = new CreateMainTest();
                    createMainTest.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}