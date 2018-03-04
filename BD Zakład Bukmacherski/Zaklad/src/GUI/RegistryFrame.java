package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by user on 2018-01-03.
 */

public class RegistryFrame {
    public JPanel mainPanel;
    private JTextField textFieldLogin;
    private JTextField textFieldName;
    private JTextField textFieldSurname;
    private JTextField textFieldEmail;
    private JTextField textFieldPhoneNumber;
    private JPasswordField passwordField;
    private JLabel LoginLabel;
    private JLabel passwordLabel;
    private JLabel NameLabel;
    private JLabel SurnameLabel;
    private JLabel EmailLabel;
    private JLabel PhonNumberLabel;
    private JButton buttonRegistry;
    Connection conn;
    ResultSet rs;
    PreparedStatement ps;

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public RegistryFrame() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","bukmacher","oracle");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        buttonRegistry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textFieldEmail.getText().equals("")||textFieldLogin.getText().equals("")||textFieldName.getText().equals("")||textFieldPhoneNumber.getText().equals("")||
                        textFieldSurname.getText().equals("")|| passwordField.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(null,
                            "Nie wszystkie pola zostały wypełnione",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if(!textFieldEmail.getText().contains("@"))
                {
                    JOptionPane.showMessageDialog(null,
                            "Podany format emailu jest nieprawidłowy",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if(isNumeric(textFieldPhoneNumber.getText())==false)
                {
                    JOptionPane.showMessageDialog(null,
                            "Podany numer telefonu nie jest liczbą",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if(textFieldPhoneNumber.getText().length()!=9)
                {
                    JOptionPane.showMessageDialog(null,
                            "Podany numer jest nierawidłowy. Numer telefonu powinien składać się z 9 cyfr",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else {
                    try {
                        ps =conn.prepareStatement("SELECT Login From Konto where Login=(?)");
                        ps.setString(1,textFieldLogin.getText());
                        ps.execute();
                        rs= ps.getResultSet();
                        if(rs.next())
                        {
                            JOptionPane.showMessageDialog(null,
                                    "Podany login jest zajęty. Proszę wybrać inny",
                                    "Inf",
                                    JOptionPane.WARNING_MESSAGE);

                        }
                        else
                        {
                            ps =conn.prepareStatement("INSERT INTO UZYTKOWNIK(UZYTKOWNIK_ID,IMIE,NAZWISKO,EMAIL,NR_TELEFONU)VALUES (uprawnienia_seq.nextval,(?),(?),(?),(?))");
                            ps.setString(1,textFieldName.getText());
                            ps.setString(2,textFieldSurname.getText());
                            ps.setString(3,textFieldEmail.getText());
                            ps.setInt(4,Integer.parseInt(textFieldPhoneNumber.getText()));
                            ps.execute();
                            rs= ps.getResultSet();

                            ps =conn.prepareStatement("INSERT INTO KONTO(LOGIN,UZYTKOWNIK_ID,UPRAWNIENIA_ID,HASLO)VALUES ((?),uprawnienia_seq.currval,(?),(?))");
                            ps.setString(1,textFieldLogin.getText());
                            ps.setInt(2,1);
                            ps.setString(3,passwordField.getText());
                            ps.execute();
                            conn.close();
                            ps.close();
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
