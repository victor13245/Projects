package GUI;

import business_logic.SimulationManager;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class Interfata extends JFrame{
    private JTextField textField_clienti;
    private JTextField textField_cozi;
    private JTextField textField_minat;
    private JTextField textField_maxat;
    private JTextField textField_minst;
    private JTextField textField_maxst;
    private JTextField textField_time;


    public Interfata()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 700);

        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Simulator Cozi");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblNewLabel.setBounds(20, 10, 211, 34);
        getContentPane().add(lblNewLabel);

        textField_clienti = new JTextField();
        textField_clienti.setBounds(95, 74, 96, 19);
        getContentPane().add(textField_clienti);
        textField_clienti.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Nr. Clienti");
        lblNewLabel_1.setBounds(21, 77, 63, 13);
        getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Nr. Case");
        lblNewLabel_1_1.setBounds(20, 106, 63, 13);
        getContentPane().add(lblNewLabel_1_1);

        textField_cozi = new JTextField();
        textField_cozi.setColumns(10);
        textField_cozi.setBounds(95, 103, 96, 19);
        getContentPane().add(textField_cozi);

        JTextArea textArea = new JTextArea();
        textArea.setBounds(38, 153, 733, 262);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(38, 153, 733, 262);
        scrollPane.setViewportView(textArea);
        getContentPane().add(scrollPane);
        //getContentPane().add(textArea);

        JLabel lblNewLabel_2 = new JLabel("Min Arrival Time");
        lblNewLabel_2.setBounds(218, 77, 110, 13);
        getContentPane().add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Max Arrival Time");
        lblNewLabel_3.setBounds(218, 106, 110, 13);
        getContentPane().add(lblNewLabel_3);

        textField_minat = new JTextField();
        textField_minat.setColumns(10);
        textField_minat.setBounds(338, 74, 96, 19);
        getContentPane().add(textField_minat);

        textField_maxat = new JTextField();
        textField_maxat.setColumns(10);
        textField_maxat.setBounds(338, 103, 96, 19);
        getContentPane().add(textField_maxat);

        JLabel lblNewLabel_2_1 = new JLabel("Min Selection Time");
        lblNewLabel_2_1.setBounds(444, 77, 138, 13);
        getContentPane().add(lblNewLabel_2_1);

        JLabel lblNewLabel_2_1_1 = new JLabel("Max Selection Time");
        lblNewLabel_2_1_1.setBounds(444, 106, 138, 13);
        getContentPane().add(lblNewLabel_2_1_1);

        textField_minst = new JTextField();
        textField_minst.setColumns(10);
        textField_minst.setBounds(573, 74, 96, 19);
        getContentPane().add(textField_minst);

        textField_maxst = new JTextField();
        textField_maxst.setColumns(10);
        textField_maxst.setBounds(573, 103, 96, 19);
        getContentPane().add(textField_maxst);

        JButton btn_start = new JButton("Start");
        btn_start.setFont(new Font("Tahoma", Font.BOLD, 13));
        btn_start.setBounds(794, 32, 82, 46);
        getContentPane().add(btn_start);

        JLabel lblNewLabel_4 = new JLabel("Time");
        lblNewLabel_4.setBounds(726, 106, 45, 13);
        getContentPane().add(lblNewLabel_4);

        textField_time = new JTextField();
        textField_time.setColumns(10);
        textField_time.setBounds(780, 103, 96, 19);
        getContentPane().add(textField_time);

        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    btn_start.setEnabled(false);
                    String time = textField_time.getText();
                    String nrClienti = textField_clienti.getText();
                    String nrCase = textField_cozi.getText();
                    String minat = textField_minat.getText();
                    String maxat = textField_maxat.getText();
                    String minst = textField_minst.getText();
                    String maxst = textField_maxst.getText();

                    int itime = Integer.parseInt(time);
                    int icl = Integer.parseInt(nrClienti);
                    int ica = Integer.parseInt(nrCase);
                    int iminat = Integer.parseInt(minat);
                    int imaxat = Integer.parseInt(maxat);
                    int iminst = Integer.parseInt(minst);
                    int imaxst = Integer.parseInt(maxst);

                    if(itime <= 0 || icl < 0 || ica < 0 || iminat < 0 || imaxat < iminat || iminst < 0 || imaxst < iminst)
                    {
                        throw new Exception();
                    }

                    SimulationManager x = new SimulationManager(itime,imaxat,iminat,imaxst,iminst,ica,icl,textArea,btn_start);
                }catch(Exception f)
                {
                    JOptionPane.showMessageDialog(null, "Date incorecte","Message", JOptionPane.INFORMATION_MESSAGE);
                    btn_start.setEnabled(true);
                }
            }
        });


        this.setVisible(true);
    }


	 /* public static void main(String[] args)
	  {
		  interfata i = new interfata();
	  } */
}
