package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessPC {
  String os;
  Runtime runtime;
  ProcessBuilder processBuilder;

  public ProcessPC() {
    try {
      this.os = System.getProperty("os.name").toLowerCase();
      this.runtime = Runtime.getRuntime();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public boolean listService() {
    try {
      Process process = null;
      if (this.os.contains("win")) { 
        process = new ProcessBuilder("sc", "query", "state=", "all", "type=", "service").start();
      }
      else if (this.os.contains("mac") || this.os.contains("nix") || this.os.contains("nux")) {
        process = new ProcessBuilder("systemctl", "list-units", "--type=service", "--state=running", "--state=exited").start();
      }
      System.out.println("List services succeed");
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedWriter writer = new BufferedWriter(new FileWriter("service.txt"));
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

  public boolean listProcess() {
    try {
      Process process = null;
      if (this.os.contains("win")) { 
        process = runtime.exec("tasklist");
      }
      else if (this.os.contains("mac")||this.os.contains("nux") || this.os.contains("nix")) { 
        process = runtime.exec("ps aux");
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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

  public boolean startProcess(String path){
    try{
        if (this.os.contains("win")||this.os.contains("nux")||this.os.contains("nix")){
            this.processBuilder = new ProcessBuilder(path);
        }else if (this.os.contains("mac")) {
            this.processBuilder = new ProcessBuilder("open", "-n",path );
        }else{ System.out.println("Unsuported Operating System"); }
        Process process = processBuilder.start();
        if (process.isAlive()){
            System.out.println("succeed");
            return true;
        }else{
            System.out.println("failed");
            return false;
        }
    }catch(Exception e){
        System.out.println(e.toString());
        return false;
    }
  }

  public boolean stopProcess(String appname){
    try{
        if(this.os.contains("win")) {
            if(!appname.contains("exe")) {
                appname+=".exe";
            }
            this.runtime.exec("taskkill /F /IM " + appname + ".exe");
            System.out.println("Kill "+ appname+" successfully");
            return true;
        } else if (this.os.contains("mac")||this.os.contains("nix")||this.os.contains("nux")) {
            this.runtime.exec("pkill -f " + appname);
            System.out.println("Kill " + appname + " successfully");
            return true;
        } else { System.out.println("Unsupported operating system");
        return false;}
    }catch(Exception e){
        System.out.println(e.toString());
        return false;
    }
  }

    public boolean stopProcess(int processId) {
        try {
            Process process;
            if (this.os.contains("win")) {
                process = this.runtime.exec("taskkill /F /PID " + processId);
            } else if (this.os.contains("mac") || this.os.contains("nix") || this.os.contains("nux")) {
                process = this.runtime.exec("kill " + processId);
            } else {
                System.out.println("Unsupported operating system");
                return false;
            }
            int exitCode = process.waitFor();
            return (exitCode==0);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.toString());
            return false;
        }
    }

public boolean logout(String Password){
  try {
      Process process;
      int exitcode = -1; // Exit code for checking whether Shutdown is successful
      if(this.os.contains("win")) {
          process = this.runtime.exec("shutdown /l");
          exitcode = process.waitFor();
      }
      else if(this.os.contains("mac")){
          String command = "echo '" + Password + "' | sudo -S pkill loginwindow";
          process = Runtime.getRuntime().exec(new String[] { "/bin/bash","-c", command });
          exitcode = process.waitFor();
      }else if (this.os.contains("nux")||this.os.contains("nix")){
          String command = "echo '" + Password + "' | sudo pkill gnome-session";
          process = Runtime.getRuntime().exec(new String[] { "/bin/bash","-c", command });
          exitcode = process.waitFor();
      }

      if (exitcode == 0) {
        System.out.println("Logout successfully");
        return true;
      } else {
        System.out.println("Logout failed . Exit code: " + exitcode);
        return false;
      }
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
  }

  public boolean shutdown(String Password) {
    try {
      Process process;
      int exitCode = -1;
      if(this.os.contains("win")) {
        process = this.runtime.exec("shutdown -s -t 3600");
        exitCode = process.waitFor();
      }
      else if(this.os.contains("nux")||this.os.contains("nix")||this.os.contains("mac")){
          String command = "echo '" + Password + "' | sudo -S shutdown -h 3600";
          process = Runtime.getRuntime().exec(new String[] { "/bin/bash","-c", command });
          exitCode = process.waitFor();
      }


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
