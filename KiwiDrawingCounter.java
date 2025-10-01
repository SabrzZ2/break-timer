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



public class KiwiDrawingCounter implements ActionListener   {

    static JFrame frame = new JFrame();

    JTextField field = new JTextField(5);
    JTextField filepathField = new JTextField(5);

    JButton button = new JButton("Click to start timer.");
    JButton stop = new JButton("Stop");

    JLabel label = new JLabel();

     static int timeSaved;

     static int minutestoMilliseconds = 60000;

     static boolean stopPlease;

     static String filepath = "wake up.wav";

     public static void PlaySound(String location) {

         // Music file addition. This method is only to play the sound.

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
        // Action listener for the stop button, so that if it is clicked, then make the variable true.
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
                    // isNumeric is a method to make sure the textbox actually has a number.
                    if (isNumeric(field.getText())) {
                        // Parse it as an integer, then save it.
                        timeSaved = Integer.parseInt(field.getText());
                        System.out.println("Time saved in variable: " + timeSaved);

                    }
                    else {
                        // If not, log it in console and display a dialog box saying it's wrong.
                        System.out.println("NOT A VALID ENTRY.");
                        JOptionPane.showMessageDialog(frame, "This is an invalid entry honey. Please put in an actual number.", "Integer Error", JOptionPane.ERROR_MESSAGE );


                    }


                }
            }

                                 @Override
                                 public void keyReleased(KeyEvent e) {

                                 } // Unused.
                             });
        filepathField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // Unused.

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // If the Enter key is pressed on the text field, then grab the text and put it in the string.
                    filepath = filepathField.getText();
                    System.out.println("File path: " + filepath);

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Unused.

            }
        });


       // JLabel label = new JLabel("Timer: ");

        // JPanel and its dimensions set. Add the button, field, and stop to window.

        JPanel panel = new JPanel();
        // Sets panel size.
        panel.setPreferredSize(new Dimension(500, 500));
        // Sets the panel border.
        panel.setBorder(BorderFactory.createBevelBorder(1));
        // Panel layout (the buttons, fields, etc.)
        panel.setLayout(new GridLayout(5, 3));
        // Adds all the additional stuff like buttons.
        panel.add(field);
        panel.add(button);
        panel.add(stop);
        panel.add(filepathField);

        // The frame around the window.

        frame.add(panel, BorderLayout.CENTER);
        // Sets the default close operation.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // The title of the window.
        frame.setTitle("Break Timer");
        // Packs them all together nice and tight.
        frame.pack();
        // Sets the location to the center.
        frame.setLocationRelativeTo(null);
        // Make sure its visible...
        frame.setVisible(true);




    }

public static void main (String[] args) {

    new KiwiDrawingCounter();
// Unused for now.





    }



    // Method for when the button is clicked.

    @Override
    public void actionPerformed(ActionEvent e) {
         // If the start button is clicked, make a thread worker GET TO FUCKING WORK!!!!!
        new myWorker().execute();



    }
// This class is only to make sure the actual operation of the timer is on a separate thread. Just so it doesn't freeze the entire damn thing.
    public static class myWorker extends SwingWorker {

        @Override
        protected Object doInBackground() throws Exception {
            long systemTime = System.currentTimeMillis();
            long maxSeconds = systemTime + ((long) minutestoMilliseconds * timeSaved);

            do {
                // systemTime is just to measure the current time on the computer.
                systemTime = System.currentTimeMillis();
                // Console log to debug if the loop is actually working.
                System.out.println("Pass.");
                // If the stopPlease variable is true from the stop button action, then break the loop and make the variable false.
                if (stopPlease) {
                    stopPlease = false;
                    break;

                }
                // The timer itself. Make sure its synchronized and that there is a "catch" for any errors. To be honest,
                // I really do not know how this actually works. But it works, so I don't really care.
                synchronized (this) {
                    try {
                        // Wait function so that it doesn't just loop every planck second and completely crash the application, or even the computer.
                        wait(1000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                // End the loop if the systemTime on the computer is equal to or bigger than the seconds allocated.
            } while (systemTime < maxSeconds);
            // Print done in the console. This was purely for debugging.
            System.out.println("Done!");
            // PLay the sound method. Planning to make this changable.
            PlaySound(filepath);
            // Show a window saying the timer is done.
            JOptionPane.showMessageDialog(frame, "You're done!");


            return null;
        }
    }


    // Error check, just in case she puts a fucking sentence for some reason.
    public static boolean isNumeric(String str) {

        try {
            // Make sure the string is able to be parsed as a double datatype.
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            // If not, it will throw an exception. Return false.
            return false;
        }



    }
}
