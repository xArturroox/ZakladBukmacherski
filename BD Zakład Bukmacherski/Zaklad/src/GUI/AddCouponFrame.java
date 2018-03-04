package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import Class.Bet;
import Class.Coupon;
public class AddCouponFrame {
    public JPanel mainPanel;
    private JTable tableEvent;
    private JButton buttonAddBet;
    private JButton buttonAddCoupon;
    private JTable tableCoupon;
    private JCheckBox CheckBoxHomeWin;
    private JCheckBox CheckBoxDraw;
    private JCheckBox CheckBoxAwayWon;
    private JScrollPane ScrollEvent;
    private JScrollPane ScrollCoupon;
    private JTextField textFieldMoney;
    private JLabel labelMoney;
    private JTextField textFieldOdd;
    private JTextField textFieldWon;
    private JLabel labelOdd;
    private JLabel labelWon;
    Connection conn;
    ResultSet rs;
    PreparedStatement ps;


    public AddCouponFrame(Coupon coupon,int user_id)
    {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Wydarzenie ID", "Data założenia zakładu", "Data zakończenia zakładu","Kurs zakładu", "Obstawienie kto wygra"}, 0);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","bukmacher","oracle");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        DefaultTableModel model1 = new DefaultTableModel(new Object[]{"Wydarzenie ID", "Dyscyplina", "Gospodarze","Goście", "Data Rozpoczęcia","Data Zakończenia","Wygrana Gospodarze","Wygrana Goście","Remis"}, 0);
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
                model1.addRow(new Object[]{id, dysc, d1,d2,date,date1,k1,k2,k3});
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        tableEvent.setModel(model1);
        buttonAddBet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tableEvent.getSelectionModel().isSelectionEmpty())
                {
                    JOptionPane.showMessageDialog(null,
                            "Wybierz wydarzenie jakie chcesz dodać do kuponu",
                            "Inf",
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {

                    Bet bet = new Bet();
                    bet.setEventID(Integer.parseInt(tableEvent.getValueAt(tableEvent.getSelectedRow(), 0).toString()));

                    if(CheckBoxHomeWin.isSelected())
                    {
                        bet.setBetOdd(Float.parseFloat(tableEvent.getValueAt(tableEvent.getSelectedRow(),6).toString()));
                        bet.setWhoWon("Gospodarze");
                    }
                    else if(CheckBoxDraw.isSelected())
                    {
                        bet.setBetOdd(Float.parseFloat(tableEvent.getValueAt(tableEvent.getSelectedRow(),8).toString()));
                        bet.setWhoWon("Remis");
                    }
                    else if(CheckBoxAwayWon.isSelected())
                    {
                        bet.setBetOdd(Float.parseFloat(tableEvent.getValueAt(tableEvent.getSelectedRow(),7).toString()));
                        bet.setWhoWon("Goście");
                    }
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.now();
                    bet.setDateStart(dtf.format(localDate));
                    bet.setDateEnd(tableEvent.getValueAt(tableEvent.getSelectedRow(),5).toString());
                    model.addRow(new Object[]{bet.getEventID(),bet.getDateStart(),bet.getDateEnd(),bet.getBetOdd(),bet.getWhoWon()});
                    tableCoupon.setModel(model);
                    coupon.betList.add(bet);


                }
            }
        });
        CheckBoxHomeWin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckBoxDraw.setSelected(false);
                CheckBoxAwayWon.setSelected(false);
            }
        });
        CheckBoxDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckBoxHomeWin.setSelected(false);
                CheckBoxAwayWon.setSelected(false);
            }
        });
        CheckBoxAwayWon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckBoxHomeWin.setSelected(false);
                CheckBoxDraw.setSelected(false);
            }
        });
        textFieldMoney.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float odd=1;
                float money;
                for(int i=0;i<coupon.betList.size();i++)
                {
                     odd *=coupon.betList.get(i).getBetOdd();
                }
                textFieldOdd.setText(Float.toString(odd));
                money=Float.parseFloat(textFieldMoney.getText());
                money*=odd;
                textFieldWon.setText(Float.toString(money));
                coupon.setBetWon(money);
                coupon.setBetOdd(odd);
            }
        });
        buttonAddCoupon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ps =conn.prepareStatement("SELECT SRODKI From KONTO where UZYTKOWNIK_ID=(?)");
                    ps.setInt(1,user_id);
                    ps.execute();
                    rs= ps.getResultSet();
                    rs.next();
                    if(textFieldMoney.getText().equals(""))
                    {
                        JOptionPane.showMessageDialog(null,
                                "Nie podano kwoty obstawienia",
                                "Inf",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else if(rs.getFloat(1)<=Float.parseFloat(textFieldMoney.getText()))
                    {
                        JOptionPane.showMessageDialog(null,
                                "Masz za mało środków. Twoje aktualne środki to: "+rs.getFloat(1),
                                "Inf",
                                JOptionPane.WARNING_MESSAGE);
                    }
                    else
                    {
                        ps =conn.prepareStatement("UPDATE KONTO SET SRODKI=(?) WHERE UZYTKOWNIK_ID=(?)");
                        ps.setFloat(1,rs.getFloat(1)-Float.parseFloat(textFieldMoney.getText()));
                        ps.setInt(2,user_id);
                        ps.execute();
                        JOptionPane.showMessageDialog(null,
                                "Kupon został utworzony",
                                "Inf",
                                JOptionPane.INFORMATION_MESSAGE);

                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
