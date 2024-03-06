package interfete;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import businessLogic.*;
import dataAccess.*;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class InterfataProduse extends JFrame{


    private JTextField textField_id;
    private JTextField textField_nume;
    private JTextField textField_stoc;
    private JTextField textField_pret;
    private JTable table;
    private JScrollPane pane = new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


    public InterfataProduse()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        getContentPane().setLayout(null);

        textField_id = new JTextField();
        textField_id.setBounds(65, 73, 96, 19);
        getContentPane().add(textField_id);
        textField_id.setColumns(10);

        JLabel lblNewLabel = new JLabel("Id:");
        lblNewLabel.setBounds(10, 76, 45, 13);
        getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Nume:");
        lblNewLabel_1.setBounds(197, 76, 45, 13);
        getContentPane().add(lblNewLabel_1);

        textField_nume = new JTextField();
        textField_nume.setColumns(10);
        textField_nume.setBounds(252, 73, 96, 19);
        getContentPane().add(textField_nume);

        JLabel lblNewLabel_2 = new JLabel("Stoc:");
        lblNewLabel_2.setBounds(393, 76, 45, 13);
        getContentPane().add(lblNewLabel_2);

        textField_stoc = new JTextField();
        textField_stoc.setBounds(454, 73, 96, 19);
        getContentPane().add(textField_stoc);
        textField_stoc.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Pret:");
        lblNewLabel_3.setBounds(596, 76, 45, 13);
        getContentPane().add(lblNewLabel_3);

        textField_pret = new JTextField();
        textField_pret.setColumns(10);
        textField_pret.setBounds(651, 73, 96, 19);
        getContentPane().add(textField_pret);

        JButton btnDisplay = new JButton("Display");
        btnDisplay.setBounds(65, 129, 96, 19);
        getContentPane().add(btnDisplay);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBounds(252, 128, 96, 21);
        getContentPane().add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(454, 128, 96, 21);
        getContentPane().add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnDelete.setBounds(651, 128, 96, 21);
        getContentPane().add(btnDelete);

        table = new JTable();
        //table.setBounds(65, 204, 682, 194);
        //getContentPane().add(table);

        pane.setViewportView(table);
        pane.setBounds(65, 204, 682, 194);
        getContentPane().add(pane);

        JLabel lblNewLabel_4 = new JLabel("Interfata Produse");
        lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD, 15));
        lblNewLabel_4.setBounds(393, 26, 183, 19);
        getContentPane().add(lblNewLabel_4);


        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                try {

                    int id = Integer.parseInt(textField_id.getText());
                    String nume = textField_nume.getText();
                    int pret = Integer.parseInt(textField_pret.getText());
                    int stoc = Integer.parseInt(textField_stoc.getText());

                    if (pret < 0 || id < 0 || stoc < 0)
                    {
                        throw new Exception();
                    }

                    ProductBLL x = new ProductBLL();
                    Product a = new Product(id,nume,stoc,pret);
                    Product b = x.insertProduct(a);

                }
                catch(Exception f)
                {
                    JOptionPane.showMessageDialog(null, "Date incorecte","Message", JOptionPane.INFORMATION_MESSAGE);
                    //btn_start.setEnabled(true);
                }

            }


        });

        btnUpdate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                try {

                    int id = Integer.parseInt(textField_id.getText());
                    String nume = textField_nume.getText();
                    int pret = Integer.parseInt(textField_pret.getText());
                    int stoc = Integer.parseInt(textField_stoc.getText());

                    if (pret < 0 || id < 0 || stoc < 0)
                    {
                        throw new Exception();
                    }

                    ProductBLL x = new ProductBLL();
                    Product a = new Product(id,nume,stoc,pret);
                    Product b = x.updateProduct(id,a);

                }
                catch(Exception f)
                {
                    JOptionPane.showMessageDialog(null, "Date incorecte","Message", JOptionPane.INFORMATION_MESSAGE);
                    //btn_start.setEnabled(true);
                }

            }


        });

        btnDelete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                try {

                    int id = Integer.parseInt(textField_id.getText());

                    ProductBLL x = new ProductBLL();
                    x.deleteProduct(id);

                }
                catch(Exception f)
                {
                    JOptionPane.showMessageDialog(null, "Date incorecte","Message", JOptionPane.INFORMATION_MESSAGE);
                    //btn_start.setEnabled(true);
                }

            }


        });

        btnDisplay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                //try {

                //int idClient = Integer.parseInt(textField_id.getText());

                ProductDAO x = new ProductDAO();
                ProductBLL y = new ProductBLL();

                List<Product> lista = y.findAllProducts();
                String[][] elem = new String[1000][4];
                elem = x.formMatrix(lista);

                //String[] coloane = new String[4];
                //String[][] continut = new String[1000][4];
                DefaultTableModel z = new DefaultTableModel();
                boolean primaIteratie = true;
                int i=0;
                for(String[] a : elem)
                {
                    if(a[0] != null)
                    {
                        if(primaIteratie)
                        {
                            z.addColumn(a[0]);
                            z.addColumn(a[1]);
                            z.addColumn(a[2]);
                            z.addColumn(a[3]);
                            primaIteratie = false;
                        }
                        else
                        {
                            z.addRow(a);
                            i++;
                        }
                    }

                }
                //z.setDataVector(continut,coloane);
                table.setModel(z);

//                }
//                catch(Exception f)
//                {
//                    JOptionPane.showMessageDialog(null, "Date incorecte","Message", JOptionPane.INFORMATION_MESSAGE);
//                    //btn_start.setEnabled(true);
//                }

            }


        });

        this.setVisible(true);
    }

}

