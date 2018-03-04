package GUI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import Class.User;

/**
 * Created by user on 2017-12-30.
 */
public class LoginFrame {
    public JPanel MainPanel;
    private JButton buttonRegistry;
    private JButton buttonLogin;
    private JPasswordField passwordField;
    private JTextField textFieldLogin;
    private JLabel passwordLabel;
    private JLabel loginLabel;
    Connection conn;
    ResultSet rs;
    PreparedStatement ps;
    int count=0;
    User user;

    public void Block()
    {
        textFieldLogin.enable(false);
        passwordField.enable(false);
        buttonLogin.enable(false);
        buttonLogin.setVisible(false);
        JOptionPane.showMessageDialog(null,
                "Dostęp do logowania został zablokowany",
                "Inf",
                JOptionPane.WARNING_MESSAGE);
    }

    public LoginFrame() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","bukmacher","oracle");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(textFieldLogin.getText().equals("")||passwordField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null,
                            "Wypełnij pola Login oraz hasło",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                    count++;
                    if(count>=5)
                    {
                        Block();
                    }
                }
                else
                {
                    try {
                        ps =conn.prepareStatement("SELECT UPRAWNIENIA_ID,UZYTKOWNIK_ID,SRODKI From Konto where Login=(?) and HASLO=(?)");
                        ps.setString(1,textFieldLogin.getText());
                        ps.setString(2,passwordField.getText());
                        ps.execute();
                        rs= ps.getResultSet();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        if(!rs.next())
                        {
                            JOptionPane.showMessageDialog(null,
                                    "Podany login lub hasło jest nie prawidłowe",
                                    "Inf",
                                    JOptionPane.WARNING_MESSAGE);
                            count++;
                            System.out.println(count);
                            if(count>=5)
                            {
                                Block();
                            }
                        }
                        else
                        {
                            user = new User();
                            user.setPermissions(Integer.parseInt(rs.getString(1)));
                            user.setMoney(rs.getFloat(3));
                            user.setUser_id(Integer.parseInt(rs.getString(2)));

                            ps =conn.prepareStatement("SELECT Imie,nazwisko,email,nr_telefonu From UZYTKOWNIK where UZYTKOWNIK_ID=(?)");
                            ps.setString(1,rs.getString(2));
                            ps.execute();
                            rs= ps.getResultSet();
                            rs.next();
                            user.setName(rs.getString(1));
                            user.setSurname(rs.getString(2));
                            user.setPhoneNumber(rs.getInt(4));
                            user.setEmail(rs.getString(3));


                            JFrame menuFrame =new JFrame ("Menu");
                            MenuFrame menu=new MenuFrame(user);
                            menuFrame.setContentPane(menu.mainPanel);
                            menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            menuFrame.pack();
                            menuFrame.setVisible(true);
                            menuFrame.setLocationRelativeTo(null);
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        buttonRegistry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame registryFrame =new JFrame ("Zarejestruj");
                RegistryFrame RegistryFrame=new RegistryFrame();
                registryFrame.setContentPane(RegistryFrame.mainPanel);
                registryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                registryFrame.pack();
                registryFrame.setVisible(true);
            }
        });
    }
}
