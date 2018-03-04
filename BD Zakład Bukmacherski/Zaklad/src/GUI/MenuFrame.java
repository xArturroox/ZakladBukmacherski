package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Class.User;
import oracle.sql.DATE;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import Class.Coupon;

/**
 * Created by user on 2018-01-03.
 */
public class MenuFrame {
    public JPanel mainPanel;
    private JButton buttonHisotry;
    private JButton ButtonWithDraw;
    private JButton buttonDeposit;
    private JButton buttonBet;
    private JTextField textFieldName;
    private JTextField textFieldSurname;
    private JLabel labelName;
    private JLabel labelSurname;
    private JTextField textFieldEmail;
    private JTextField textFieldPhone;
    private JTextField textFieldMoney;
    private JLabel labelEmail;
    private JLabel labelPhone;
    private JLabel labelMoney;
    private JTextField textFieldPremissions;
    private JLabel labelPremissions;
    private JButton buttonAdd;
    User user;
    Connection conn;
    ResultSet rs;
    PreparedStatement ps;
    Coupon coupon;

    public void setLabel()
    {
        String uprawnienia;
        textFieldName.setText(user.getName());
        textFieldSurname.setText(user.getSurname());
        textFieldEmail.setText(user.getEmail());
        Integer userPhone;
        userPhone = user.getPhoneNumber();
        textFieldPhone.setText(userPhone.toString());
        textFieldMoney.setText(Float.toString(user.getMoney()));
        if (user.getPermissions() == 2)
        {
            uprawnienia = "Administrator";
        }
        else
        {
            uprawnienia = "Standardowe";
        }
        textFieldPremissions.setText((uprawnienia));

    }

    public MenuFrame(User user)
    {
        this.user=new User();
        this.user=user;
        coupon=new Coupon();

        setLabel();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","bukmacher","oracle");
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        buttonHisotry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HistoryFrame historyFrame=new HistoryFrame();
                DefaultTableModel model = new DefaultTableModel(new Object[]{"KURS", "POSTAWIONE ŚRODKI", "MOŻLIWA WYGRANA","STATUS"}, 0)
                {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        //all cells false
                        return false;
                    }
                };
                try {
                    ps =conn.prepareStatement("SELECT TRUNC(KURS,2),TRUNC(POSTAWIONE_SRODKI,2),TRUNC(WYGRANA,2),STATUS From KUPON where UZYTKOWNIK_ID=(?)");
                    ps.setInt(1,user.getUser_id());
                    ps.execute();
                    rs= ps.getResultSet();
                    //model.addRow(new Object[]{"KURS", "POSTAWIONE_SRODKI", "WYGRANA","STATUS"});
                    while (rs.next())
                    {
                        String a = rs.getString(1);
                        String b = rs.getString(2);
                        String c = rs.getString(3);
                        int d = rs.getInt(4);
                        String f;
                        if(d==1)
                        {
                            f="Wygrana";
                        }
                        else f="Przegrana";
                        model.addRow(new Object[]{a, b, c,f});

                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                historyFrame.tableHistory.setModel(model);
                JFrame hisFrame =new JFrame ("Historia");
                hisFrame.setContentPane(historyFrame.mainPanel);
                hisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                hisFrame.pack();
                hisFrame.setVisible(true);
                hisFrame.setLocationRelativeTo(null);
            }
        });
        ButtonWithDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame drawFrame=new JFrame("Wypłata");
                WithDrawFrame withdrawFrame=new WithDrawFrame(user.getUser_id());
                drawFrame.setContentPane(withdrawFrame.mainPanel);
                drawFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                drawFrame.pack();
                drawFrame.setVisible(true);
                drawFrame.setLocationRelativeTo(null);
                drawFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            ps =conn.prepareStatement("SELECT SRODKI From KONTO where UZYTKOWNIK_ID=(?)");
                            ps.setInt(1,user.getUser_id());
                            ps.execute();
                            rs= ps.getResultSet();
                            rs.next();
                            user.setMoney(rs.getFloat(1));
                            setLabel();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        buttonDeposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame depositFrame=new JFrame("Wpłata");
                DepositFrame depFrame=new DepositFrame(user.getUser_id());
                depositFrame.setContentPane(depFrame.mainPanel);
                depositFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                depositFrame.pack();
                depositFrame.setVisible(true);
                depositFrame.setLocationRelativeTo(null);
                depositFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            ps =conn.prepareStatement("SELECT SRODKI From KONTO where UZYTKOWNIK_ID=(?)");
                            ps.setInt(1,user.getUser_id());
                            ps.execute();
                            rs= ps.getResultSet();
                            rs.next();
                            user.setMoney(rs.getFloat(1));
                            setLabel();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
        buttonBet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BetFrame BetFrame=new BetFrame();
                DefaultTableModel model = new DefaultTableModel(new Object[]{"Wydarzenie ID", "Dyscyplina", "Gospodarze","Goście", "Data Rozpoczęcia","Data Zakończenia","Wygrana Gospodarze","Wygrana Goście","Remis"}, 0);
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
                if(user.getPermissions()==2)
                {
                    BetFrame.buttonAdd.setVisible(true);
                    BetFrame.buttonAdd.setEnabled(true);
                    BetFrame.buttonChange.setVisible(true);
                    BetFrame.buttonChange.setEnabled(true);
                    BetFrame.buttonRemove.setVisible(true);
                    BetFrame.buttonRemove.setEnabled(true);
                }
                BetFrame.tableBet.setModel(model);
                JFrame betFrame=new JFrame("Zakłady");
                betFrame.setContentPane(BetFrame.mainPanel);
                betFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                betFrame.pack();
                betFrame.setVisible(true);
                betFrame.setLocationRelativeTo(null);
            }
        });
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCouponFrame addCouponFrame = new AddCouponFrame(coupon,user.getUser_id());
                JFrame couponFrame=new JFrame("Coupon");
                couponFrame.setContentPane(addCouponFrame.mainPanel);
                couponFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                couponFrame.pack();
                couponFrame.setVisible(true);
                couponFrame.setLocationRelativeTo(null);
                couponFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        try {
                            ps =conn.prepareStatement("INSERT INTO KUPON (KUPON_ID, UZYTKOWNIK_ID, KURS, POSTAWIONE_SRODKI, WYGRANA, STATUS) VALUES (dodajKupon_seq.nextval,(?),(?),(?),(?),(?))");
                            ps.setInt(1,user.getUser_id());
                            ps.setFloat(2,coupon.getBetOdd());
                            ps.setFloat(3,coupon.getBetMoney());
                            ps.setFloat(4,coupon.getBetWon());
                            ps.setInt(5,0);
                            ps.execute();

                            for(int i=0;i<coupon.betList.size();i++)
                            {
                                ps =conn.prepareStatement("INSERT INTO ZAKLAD (ZAKLAD_ID, KUPON_ID, WYDARZENIE_ID, DATA_ROZPOCZECIA, DATA_ZAKONCZENIA, STATUS) VALUES (dodajZaklad_seq.nextval,dodajKupon_seq.currval,(?),(?),(?),(?))");
                                ps.setInt(1,coupon.betList.get(i).getEventID());
                                ps.setDate(2,Date.valueOf(coupon.betList.get(i).getDateStart()));
                                ps.setDate(3,Date.valueOf(coupon.betList.get(i).getDateEnd()));
                                ps.setInt(4,coupon.betList.get(i).getBetStatus());
                                ps.execute();
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
