package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.sql.*;

/**
 * Created by user on 2018-01-04.
 */
public class BetFrame {
    public JPanel mainPanel;
    public JTable tableBet;
    public JScrollPane ScrollPaneBet;
    public JButton buttonAdd;
    public JButton buttonRemove;
    public JButton buttonChange;
    Connection conn;
    ResultSet rs;
    PreparedStatement ps;

    public BetFrame() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","bukmacher","oracle");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }



        buttonRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tableBet.getSelectionModel().isSelectionEmpty())
                {
                    JOptionPane.showMessageDialog(null,
                            "Wybierz zakład jaki chcesz usunąć",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
               else
                {
                    try {
                        ps =conn.prepareStatement("DELETE from WYDARZENIE WHERE WYDARZENIE_ID=(?)");
                        ps.setInt(1, (Integer) tableBet.getValueAt(tableBet.getSelectedRow(),0));
                        ps.execute();
                        DefaultTableModel model = (DefaultTableModel) tableBet.getModel();
                        model.removeRow(tableBet.getSelectedRow());
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }




            }
        });

        buttonChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tableBet.getSelectionModel().isSelectionEmpty())
                {
                    JOptionPane.showMessageDialog(null,
                            "Wybierz zakład jaki chcesz zmodyfikować",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    DefaultTableModel model = new DefaultTableModel(new Object[]{"Wydarzenie ID", "Dyscyplina", "Gospodarze","Goście", "Data Rozpoczęcia","Data Zakończenia","Wygrana Gospodarze","Wygrana Goście","Remis"}, 0);
                    JFrame newBetFrame=new JFrame("Modyfikacja");
                    NewBetFrame NewBetFrame=new NewBetFrame(tableBet.getValueAt(tableBet.getSelectedRow(),0).toString(),tableBet.getValueAt(tableBet.getSelectedRow(),1).toString(),tableBet.getValueAt(tableBet.getSelectedRow(),2).toString(),tableBet.getValueAt(tableBet.getSelectedRow(),3).toString(),tableBet.getValueAt(tableBet.getSelectedRow(),4).toString(),
                            tableBet.getValueAt(tableBet.getSelectedRow(),5).toString(),tableBet.getValueAt(tableBet.getSelectedRow(),6).toString(),tableBet.getValueAt(tableBet.getSelectedRow(),7).toString(),tableBet.getValueAt(tableBet.getSelectedRow(),8).toString());
                    newBetFrame.setContentPane(NewBetFrame.mainPanel);
                    newBetFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    newBetFrame.pack();
                    newBetFrame.setVisible(true);
                    newBetFrame.setLocationRelativeTo(null);
                    newBetFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                            try {
                                ps =conn.prepareStatement("SELECT WYDARZENIE_ID,DYSCYPLINANAZWA,DRUZYNA1,DRUZYNA2,DATA_ROZPOCZECIA,DATA_ROZPOCZECIA, trunc(KURS1,2),trunc(KURS2,2),trunc(KURS3,2) FROM WYDARZENIE where DATA_ZAKONCZENIA<(SELECT sysdate from dual)");
                                ps.execute();
                                rs= ps.getResultSet();
                                while (rs.next())
                                {
                                    int id=rs.getInt(1);
                                    String dysc=rs.getString(2);
                                    String d1=rs.getString(3);
                                    String d2=rs.getString(4);
                                    Date date=rs.getDate(5);
                                    Date date1=rs.getDate(6);
                                    float k1=rs.getFloat(7);
                                    float k2=rs.getFloat(8);
                                    float k3=rs.getFloat(9);
                                    model.addRow(new Object[]{id, dysc, d1,d2,date,date1,k1,k2,k3});
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            tableBet.setModel(model);

                        }
                    });
                }

            }
        });
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = new DefaultTableModel(new Object[]{"Wydarzenie ID", "Dyscyplina", "Gospodarze","Goście", "Data Rozpoczęcia","Data Zakończenia","Wygrana Gospodarze","Wygrana Goście","Remis"}, 0);
                JFrame addBetFrame=new JFrame("Dodanie");
                AddBetFrame AddBetFrame=new AddBetFrame();
                addBetFrame.setContentPane(AddBetFrame.mainPanel);
                addBetFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addBetFrame.pack();
                addBetFrame.setVisible(true);
                addBetFrame.setLocationRelativeTo(null);
                addBetFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            ps =conn.prepareStatement("SELECT WYDARZENIE_ID,DYSCYPLINANAZWA,DRUZYNA1,DRUZYNA2,DATA_ROZPOCZECIA,DATA_ROZPOCZECIA, trunc(KURS1,2),trunc(KURS2,2),trunc(KURS3,2) FROM WYDARZENIE where DATA_ZAKONCZENIA<(SELECT sysdate from dual)");
                            ps.execute();
                            rs= ps.getResultSet();
                            while (rs.next())
                            {
                                int id=rs.getInt(1);
                                String dysc=rs.getString(2);
                                String d1=rs.getString(3);
                                String d2=rs.getString(4);
                                Date date=rs.getDate(5);
                                Date date1=rs.getDate(6);
                                float k1=rs.getFloat(7);
                                float k2=rs.getFloat(8);
                                float k3=rs.getFloat(9);
                                model.addRow(new Object[]{id, dysc, d1,d2,date,date1,k1,k2,k3});
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        tableBet.setModel(model);

                    }
                });
            }
        });
    }
}

