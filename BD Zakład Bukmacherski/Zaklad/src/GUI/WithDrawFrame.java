package GUI;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by user on 2018-01-03.
 */

public class WithDrawFrame {
    public JPanel mainPanel;
    private JTextField textFieldWithDraw;
    private JButton buttonWithDraw;
    private JLabel labelWithDraw;
    int user_id;
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

    public WithDrawFrame(int user_id)
    {
        this.user_id=user_id;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","bukmacher","oracle");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        buttonWithDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldWithDraw.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(null,
                            "Podaj kwote jaką chcesz wypłacić",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if(isNumeric(textFieldWithDraw.getText())==false)
                {
                    JOptionPane.showMessageDialog(null,
                            "Podana kwota nie jest liczbą",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    try {
                        ps =conn.prepareStatement("SELECT SRODKI From KONTO where UZYTKOWNIK_ID=(?)");
                        ps.setInt(1,user_id);
                        ps.execute();
                        rs= ps.getResultSet();
                        rs.next();
                        if(rs.getFloat(1)<=Float.parseFloat(textFieldWithDraw.getText()))
                        {
                            JOptionPane.showMessageDialog(null,
                                    "Masz za mało środków twoje aktualne środki to: "+rs.getFloat(1),
                                    "Inf",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        else
                        {
                            ps =conn.prepareStatement("UPDATE KONTO SET SRODKI=(?) WHERE UZYTKOWNIK_ID=(?)");
                            ps.setFloat(1,rs.getFloat(1)-Float.parseFloat(textFieldWithDraw.getText()));
                            ps.setInt(2,user_id);
                            ps.execute();
                            JOptionPane.showMessageDialog(null,
                                    "Srodki zostały pomyślnie wypłacone",
                                    "Inf",
                                    JOptionPane.INFORMATION_MESSAGE);

                        }

                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
