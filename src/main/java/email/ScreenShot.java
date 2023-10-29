package email;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ScreenShot {

  public ScreenShot(String name) {
    try {
      Robot robot = new Robot();

      Rectangle screenRect = new Rectangle(
        (Toolkit.getDefaultToolkit()).getScreenSize()
      );

      BufferedImage screenImage = robot.createScreenCapture(screenRect);

      File outputFile = new File(name);
      ImageIO.write(screenImage, "png", outputFile);

      System.out.println("Screenshot successfully and save into " + name);
    } catch (Exception e) {
      System.out.println("Screenshot failed");
    }
  }
  // public static void main(String[] args) {
  //   new ScreenShot("name1.png");
  // }
}
