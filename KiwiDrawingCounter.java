package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.management.Notification;



public class KiwiDrawingCounter implements ActionListener   {

    static JFrame frame = new JFrame();

    JTextField field = new JTextField(5);

    JButton button = new JButton("Click to start timer.");
    JButton stop = new JButton("Stop");

    JLabel label = new JLabel();

     static int timeSaved;

     static int minutestoMilliseconds = 60000;

     static boolean stopPlease;

     static String filepath = "wake up.wav";

     public static void PlaySound(String location) {

         try {

             File musicPath = new File(location);

             if (musicPath.exists()) {

                 AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(location));
                 Clip clippy = AudioSystem.getClip();
                 clippy.open(audioInput);
                 clippy.start();

             } else {
                 System.out.println("Error: File doesn't exist");
             }
         }
         catch(Exception e) {
             System.out.println(e);
         }

     }



    public KiwiDrawingCounter() {


        button.addActionListener(this);
        stop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopPlease = true;
            }
        });

        field.addKeyListener(new KeyListener() {

                                 @Override
                                 public void keyTyped(KeyEvent e) {

                                 }

                                 @Override

            public void keyPressed(KeyEvent e) {

                                     // If "Enter" is pressed, do the following if statement.
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    // If the text field has a number at all, then it will be stored. If not, then it will show a console log entry.

                    if (isNumeric(field.getText())) {

                        timeSaved = Integer.parseInt(field.getText());
                        System.out.println("Time saved in variable: " + timeSaved);

                    }
                    else {
                        System.out.println("NOT A VALID ENTRY.");
                        JOptionPane.showMessageDialog(frame, "This is an invalid entry honey. Please put in an actual number.", "Integer Error", JOptionPane.ERROR_MESSAGE );


                    }


                }
            }

                                 @Override
                                 public void keyReleased(KeyEvent e) {

                                 }
                             });


        JLabel label = new JLabel("Timer: ");

        // JPanel and its dimensions set. Add the button to the window.

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createBevelBorder(1));
        panel.setLayout(new GridLayout(5, 3));
        panel.add(field);
        panel.add(button);
        panel.add(stop);

        // The frame around the window.

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Break Timer");
        frame.pack();
        frame.setVisible(true);




    }

public static void main (String[] args) {

    new KiwiDrawingCounter();






    }



    // Method for when the button is clicked.

    @Override
    public void actionPerformed(ActionEvent e) {
        new myWorker().execute();



    }

    public static class myWorker extends SwingWorker {

        @Override
        protected Object doInBackground() throws Exception {
            long systemTime = System.currentTimeMillis();
            long maxSeconds = systemTime + ((long) minutestoMilliseconds * timeSaved);

            do {
                systemTime = System.currentTimeMillis();
                System.out.println("Pass.");

                if (stopPlease) {
                    stopPlease = false;
                    break;

                }

                synchronized (this) {
                    try {
                        wait(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }


            } while (systemTime < maxSeconds);
            System.out.println("Done!");
            PlaySound(filepath);
            JOptionPane.showMessageDialog(frame, "You're done!");


            return null;
        }
    }


    // Error check, just in case she puts a fucking sentence for some reason.
    public static boolean isNumeric(String str) {

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }



    }
}
