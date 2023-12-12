package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ProcessPC {

  Runtime runtime;

  public ProcessPC() {
    try {
      this.runtime = Runtime.getRuntime();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public boolean listProcess() {
    try {
      Process process = runtime.exec("tasklist");
      BufferedReader reader = new BufferedReader(
        new InputStreamReader(process.getInputStream())
      );
      BufferedWriter writer = new BufferedWriter(new FileWriter("process.txt"));
      String line;
      while ((line = reader.readLine()) != null) {
        writer.write(line);
        writer.newLine();
      }
      writer.close();
      reader.close();
      return true;
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }

  public boolean startProcess(String cmd) {
    try {
      ProcessBuilder builder = new ProcessBuilder(cmd);
      builder.start();
      System.out.println("Start procss: " + cmd + " successfull");
      return true;
    } catch (Exception e) {
      System.out.println("Start procss: " + cmd + " failed");
      return false;
    }
  }

  public boolean stopProcess(String pid) {
    try {
      this.runtime.exec("taskkill /F /PID " + pid);
      System.out.println("Stop procss: " + pid + " successfull");
      return true;
    } catch (Exception e) {
      System.out.println("Stop procss: " + pid + " failed");
      return false;
    }
  }

  public boolean logout() {
    try {
      Process process = this.runtime.exec("shutdown -s -t 3600");
      int exitCode = process.waitFor();

      if (exitCode == 0) {
        System.out.println("Logout successfull");
        return true;
      } else {
        System.out.println("Logout faild . Exit code: " + exitCode);
        return false;
      }
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }

  public boolean shutdown() {
    try {
      Process process = this.runtime.exec("shutdown -s -t 3600");
      int exitCode = process.waitFor();

      if (exitCode == 0) {
        System.out.println("Shutdown successfull");
        return true;
      } else {
        System.out.println("Shutdown faild. Exit code: " + exitCode);
        return false;
      }
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }
}
