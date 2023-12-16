package email.ui;

import java.util.TimerTask;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javax.swing.*;
import java.awt.Image; 

import email.ServerThreading;

import java.util.Date;

public class Running extends javax.swing.JFrame {

    public String from;

    public Running(String mail) {
        this.from = mail;
        initComponents();
        startTimer();
    }

    private static Date parseDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean compareTime(Date dateToCompare) {
        Date currentTime = new Date();
        long diffInMillies = Math.abs(currentTime.getTime() - dateToCompare.getTime());
        long diffInSeconds = diffInMillies / 1000;
        return diffInSeconds <= 120;
    }

    private List<String> returnTask() {
        List<String> task = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("new_mail.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String[] temp = parts[0].split("=");
                    String mail = temp[0];
                    String action = temp[1];
                    String dateStr = parts[1];
                    Date date = parseDate(dateStr);
                    if (compareTime(date) && mail.equals(this.from)) {
                        task.add(action + '(' + dateStr + ')');
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return task;
    }

    private String formatTask(List<String> task) {
        if (task.isEmpty())
        {
            return "No task from client!!!";
        }
        String tempText;
        StringBuilder sb = new StringBuilder("<html>");
        for (String t : task) {
            tempText = t;
            if (tempText.length() > 35) {
                tempText = tempText.substring(0, 35) + "...";
            }
            sb.append('-'+tempText).append("<br>");
        }
        sb.append("</html>");
        return sb.toString();
    }

    private void getDemo(javax.swing.JLabel demoShow)
    {
        if (ServerThreading.fileDemo != "")
        {
            demoShow.setIcon(null);
            ServerThreading.screenshotDemo = null;
            demoShow.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); 
            demoShow.setForeground(new java.awt.Color(255, 255, 255));
            demoShow.setText(ServerThreading.fileDemo);
        }
        if (ServerThreading.screenshotDemo != null)
        {
            demoShow.setText("");
            int width = 352;
            int height = 198; 
            demoShow.setIcon(new ImageIcon(ServerThreading.screenshotDemo.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        }
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;
            String check = "check";
            String tempText;
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                        List<String> task = returnTask();
                        String formated_task = formatTask(task);
                        jLabel8.setText(formated_task);
                        tempText = "-Running: " + ServerThreading.statusRunning;
                        if (tempText.length() > 35) {
                            tempText = tempText.substring(0, 35) + "...";
                        }
                        jLabel3.setText(tempText);
                        if (count % 2 == 0) {
                            getDemo(demo);
                            jLabel2.setText("-Done: " + ServerThreading.status);
                        }
                        if (count % 12 == 0) {
                            if (check == ServerThreading.statusRunning && ServerThreading.statusRunning != "nothing is running"){
                                ServerThreading.statusRunning = "nothing is running";
                                jLabel3.setText("-Running: " + ServerThreading.statusRunning);
                                jLabel2.setText("-Done: ");
                                demo.setText("");
                                demo.setIcon(null);
                                ServerThreading.fileDemo = "";
                                ServerThreading.screenshotDemo = null;
                            }
                            else check = ServerThreading.statusRunning;
                        }
                        count++;
                });
            }
        }, 0, 1000);
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        demo = new javax.swing.JLabel();

        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RUNNING");
        setPreferredSize(new java.awt.Dimension(800, 500));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);
        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setIcon(new javax.swing.ImageIcon("src\\\\main\\\\java\\\\email\\\\ui\\\\image\\\\notification1.png")); 

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 0, 18)); 
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("-Done: ");

        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 0, 20)); 
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setBounds(200, 200, jLabel3.getPreferredSize().width, jLabel3.getPreferredSize().height);
        jPanel2.add(jLabel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10,10,10)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15,15,15)
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10,10,10)
                        .addComponent(demo))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10)
                        .addComponent(jLabel2)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10,10,10)
                .addComponent(jLabel1)
                .addGap(10,10,10)
                .addComponent(jLabel3)
                .addGap(5,5,5)
                .addComponent(demo))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(350)
                .addComponent(jLabel2))
                
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 400, 500);
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 50)); 
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setText("CONNECTED");
        jLabel5.setIcon(new javax.swing.ImageIcon("src\\\\main\\\\java\\\\email\\\\ui\\\\image\\\\controller.png")); 
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); 
        jLabel6.setText(this.from);
        jLabel7.setIcon(new javax.swing.ImageIcon("src\\\\main\\\\java\\\\email\\\\ui\\\\image\\\\action.png")); 
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); 
        jLabel8.setText("No task from client!!!");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(50,50,50)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createParallelGroup()
                        .addGap(20,20,20)
                        .addComponent(jLabel5)
                        .addComponent(jLabel7)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20,20,20)
                        .addComponent(jLabel8))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(20,20,20)
                        .addComponent(jLabel6))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20,20,20)
                .addComponent(jLabel4)
                .addGap(29, 29, 29)
                .addComponent(jLabel5)
                .addComponent(jLabel6)
                .addGap(50,50,50)
                .addComponent(jLabel7)
                .addComponent(jLabel8)
                .addContainerGap(45, Short.MAX_VALUE))
        );
        
        jPanel1.add(jPanel3);
        jPanel3.setBounds(400, 0, 400, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 113, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 126, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel demo;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
}
