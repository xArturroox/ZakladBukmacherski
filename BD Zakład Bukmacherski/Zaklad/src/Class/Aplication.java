package Class;

import GUI.LoginFrame;

import javax.swing.*;
/**
 * Created by user on 2017-12-30.
 */

public class Aplication {

    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }  catch(Exception e){
        }
        JFrame loginFrame =new JFrame ("Zaloguj");
        LoginFrame LoginFrame=new LoginFrame();
        loginFrame.setContentPane(LoginFrame.MainPanel);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        loginFrame.setVisible(true);
        loginFrame.setLocationRelativeTo(null);
    }
}
