package interfete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MainInterface extends JFrame{

    public MainInterface()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Main Menu");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNewLabel.setBounds(194, 30, 123, 22);
        getContentPane().add(lblNewLabel);

        JButton btnAcceseazaClienti = new JButton("Acceseaza clienti");
        btnAcceseazaClienti.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnAcceseazaClienti.setBounds(165, 123, 152, 31);
        getContentPane().add(btnAcceseazaClienti);

        JButton btnAcceseazaProduse = new JButton("Acceseaza produse");
        btnAcceseazaProduse.setBounds(165, 178, 152, 31);
        getContentPane().add(btnAcceseazaProduse);

        JButton btnAcceseazaComenzi = new JButton("Acceseaza comenzi");
        btnAcceseazaComenzi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnAcceseazaComenzi.setBounds(165, 231, 152, 31);
        getContentPane().add(btnAcceseazaComenzi);

        btnAcceseazaClienti.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                InterfataClienti inter1 = new InterfataClienti();
            }


        });


        btnAcceseazaProduse.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                InterfataProduse inter2 = new InterfataProduse();
            }


        });

        btnAcceseazaComenzi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                InterfataComenzi inter3 = new InterfataComenzi();
            }


        });


        this.setVisible(true);
    }

}
