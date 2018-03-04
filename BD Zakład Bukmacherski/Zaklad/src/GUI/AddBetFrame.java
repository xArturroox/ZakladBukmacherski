package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by user on 2018-01-05.
 */
public class AddBetFrame {
    public JPanel mainPanel;
    private JComboBox comboBoxDiscipline;
    private JLabel labelDiscipline;
    private JLabel labelTeam1;
    private JTextField textFieldTeam1;
    private JLabel labelTeam2;
    private JTextField textFieldTeam2;
    private JLabel labelStartDate;
    private JTextField textFieldStartDate;
    private JLabel labelFinishDate;
    private JTextField textFieldFinishDate;
    private JLabel labelK1;
    private JTextField textFieldK1;
    private JLabel labelK2;
    private JTextField textFieldK2;
    private JLabel labelK3;
    private JTextField textFieldK3;
    private JLabel labelWon;
    private JTextField textFieldWon;
    private JButton buttonAdd;
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

    public AddBetFrame()
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

        buttonAdd.addActionListener(new ActionListener() {
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
                else if(textFieldFinishDate.getText().contains("-")==false || textFieldStartDate.getText().contains("-")==false)
                {
                    JOptionPane.showMessageDialog(null,
                            "Nieprawidłowy format daty. Przykład poprawnego formatu: 2017-01-01",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    try {
                        ps =conn.prepareStatement("INSERT INTO WYDARZENIE (WYDARZENIE_ID,DYSCYPLINANAZWA,DRUZYNA1,DRUZYNA2,DATA_ROZPOCZECIA,DATA_ZAKONCZENIA,KURS1,KURS2,KURS3,KTO_WYGRAL) VALUES (dodajWydarzenie_seq.nextval,(?),(?),(?),(?),(?),(?),(?),(?),(?))");
                        ps.setString(1,comboBoxDiscipline.getSelectedItem().toString());
                        ps.setString(2,textFieldTeam1.getText());
                        ps.setString(3,textFieldTeam2.getText());
                        ps.setDate(4,Date.valueOf(textFieldStartDate.getText()));
                        ps.setDate(5,Date.valueOf(textFieldFinishDate.getText()));
                        ps.setFloat(6,Float.parseFloat(textFieldK1.getText()));
                        ps.setFloat(7,Float.parseFloat(textFieldK2.getText()));
                        ps.setFloat(8,Float.parseFloat(textFieldK3.getText()));
                        ps.setInt(9,Integer.parseInt(textFieldWon.getText()));
                        ps.execute();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
    }
}
