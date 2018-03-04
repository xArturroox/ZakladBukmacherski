package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;

/**
 * Created by user on 2018-01-05.
 */
public class NewBetFrame {
    public JPanel mainPanel;
    private JLabel labelDiscipline;
    private JLabel labelTeam1;
    private JLabel labelTeam2;
    private JLabel labelStartDate;
    private JLabel labelFinishDate;
    private JLabel labelK1;
    private JLabel labelK2;
    private JLabel labelK3;
    private JLabel labelWon;
    private JTextField textFieldDiscipline;
    private JTextField textFieldTeam1;
    private JTextField textFieldTeam2;
    private JTextField textFieldStartDate;
    private JTextField textFieldFinishDate;
    private JTextField textFieldK1;
    private JTextField textFieldK2;
    private JTextField textFieldK3;
    private JTextField textFieldWon;
    private JButton buttonModify;
    private JComboBox comboBoxDiscipline;
    Connection conn;
    ResultSet rs;
    PreparedStatement ps;
    String won;
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

    public NewBetFrame(String id,String discipline,String team1,String team2,String startDate,String finishDate, String k1,String k2,String k3)
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","bukmacher","oracle");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            ps =conn.prepareStatement("SELECT KTO_WYGRAL From WYDARZENIE where WYDARZENIE_ID=(?)");
            ps.setInt(1,Integer.parseInt(id));
            ps.execute();
            rs= ps.getResultSet();
            rs.next();
            won=rs.getString(1);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        textFieldTeam1.setText(team1);
        textFieldTeam2.setText(team2);
        textFieldStartDate.setText(startDate);
        textFieldFinishDate.setText(finishDate);
        textFieldK1.setText(k1);
        textFieldK2.setText(k2);
        textFieldK3.setText(k3);
        textFieldWon.setText(won);
        try {
            ps =conn.prepareStatement("SELECT * From DYSCYPLINA");
            ps.execute();
            rs= ps.getResultSet();
            while (rs.next())
            {
                comboBoxDiscipline.addItem(rs.getString(1));
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
        }



        buttonModify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textFieldTeam1.getText().equals("")||textFieldTeam2.getText().equals("")||textFieldStartDate.getText().equals("")||
                        textFieldFinishDate.getText().equals("")|| textFieldK1.getText().equals("")||textFieldK2.getText().equals("")||textFieldK3.getText().equals("")||textFieldWon.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(null,
                            "Wypełnij wszystkie pola",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if(isNumeric(textFieldK1.getText())==false ||isNumeric(textFieldK2.getText())==false ||isNumeric(textFieldK3.getText())==false )
                {
                    JOptionPane.showMessageDialog(null,
                            "Kursy powinny być liczbami",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    try {
                        ps =conn.prepareStatement("UPDATE WYDARZENIE SET DYSCYPLINANAZWA=(?),DRUZYNA1=(?),DRUZYNA2=(?),DATA_ROZPOCZECIA=(?),DATA_ZAKONCZENIA=(?),KURS1=(?)," +
                                "KURS2=(?),KURS3=(?),KTO_WYGRAL=(?) WHERE WYDARZENIE_ID=(?)");
                        ps.setString(1,comboBoxDiscipline.getSelectedItem().toString());
                        ps.setString(2,textFieldTeam1.getText());
                        ps.setString(3,textFieldTeam2.getText());
                        ps.setDate(4,Date.valueOf(textFieldStartDate.getText()));
                        ps.setDate(5,Date.valueOf(textFieldFinishDate.getText()));
                        ps.setFloat(6,Float.parseFloat(textFieldK1.getText()));
                        ps.setFloat(7,Float.parseFloat(textFieldK2.getText()));
                        ps.setFloat(8,Float.parseFloat(textFieldK3.getText()));
                        ps.setInt(9,Integer.parseInt(textFieldWon.getText()));
                        ps.setInt(10,Integer.parseInt(id));
                        ps.execute();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
