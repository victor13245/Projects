package interfete;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import businessLogic.*;
import dataAccess.*;
import model.*;

import java.util.List;


public class InterfataComenzi extends JFrame{


    private JTextField textField_id;
    private JTextField textField_idClient;
    private JTextField textField_idProdus;
    private JTextField textField_cantitate;
    private JTable table;
    private JScrollPane pane = new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


    public InterfataComenzi()
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

        JLabel lblNewLabel_1 = new JLabel("idClient:");
        lblNewLabel_1.setBounds(197, 76, 56, 13);
        getContentPane().add(lblNewLabel_1);

        textField_idClient = new JTextField();
        textField_idClient.setColumns(10);
        textField_idClient.setBounds(263, 73, 96, 19);
        getContentPane().add(textField_idClient);

        JLabel lblNewLabel_2 = new JLabel("IdProdus:");
        lblNewLabel_2.setBounds(393, 76, 77, 13);
        getContentPane().add(lblNewLabel_2);

        textField_idProdus = new JTextField();
        textField_idProdus.setBounds(480, 73, 96, 19);
        getContentPane().add(textField_idProdus);
        textField_idProdus.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Cantitate:");
        lblNewLabel_3.setBounds(599, 76, 54, 13);
        getContentPane().add(lblNewLabel_3);

        textField_cantitate = new JTextField();
        textField_cantitate.setColumns(10);
        textField_cantitate.setBounds(686, 73, 96, 19);
        getContentPane().add(textField_cantitate);

        JButton btnDisplay = new JButton("Display");
        btnDisplay.setBounds(65, 129, 96, 19);
        getContentPane().add(btnDisplay);

        JButton btnAdd = new JButton("Add");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnAdd.setBounds(263, 128, 96, 21);
        getContentPane().add(btnAdd);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(480, 128, 96, 21);
        getContentPane().add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnDelete.setBounds(686, 128, 96, 21);
        getContentPane().add(btnDelete);

        table = new JTable();
        //table.setBounds(65, 204, 682, 194);
        //getContentPane().add(table);

        pane.setViewportView(table);
        pane.setBounds(65, 204, 682, 194);
        getContentPane().add(pane);

        JLabel lblNewLabel_4 = new JLabel("Interfata Comenzi");
        lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD, 15));
        lblNewLabel_4.setBounds(393, 26, 183, 19);
        getContentPane().add(lblNewLabel_4);


        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

                try {

                    int id = Integer.parseInt(textField_id.getText());
                    int idClient= Integer.parseInt(textField_idClient.getText());
                    int idProdus = Integer.parseInt(textField_idProdus.getText());
                    int cantitate = Integer.parseInt(textField_cantitate.getText());;

                    if (cantitate < 0 || idClient < 0)
                    {
                        throw new Exception();
                    }

                    ProductBLL x = new ProductBLL();
                    Product a = x.findProductById(idProdus);

                    if(a.getStoc() < cantitate)
                    {
                        throw new Exception();
                    }

                    OrderBLL y = new OrderBLL();
                    Comanda b = new Comanda(id,idClient,idProdus,cantitate);
                    y.insertOrder(b);

                    BillBLL bil = new BillBLL();
                    Bill g = new Bill(id,idClient,idProdus,cantitate);
                    bil.insertBill(g);

                    a.setStoc(a.getStoc() - cantitate);
                    x.updateProduct(a.getId(),a);


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
                    int idClient= Integer.parseInt(textField_idClient.getText());
                    int idProdus = Integer.parseInt(textField_idProdus.getText());
                    int cantitate = Integer.parseInt(textField_cantitate.getText());;

                    if (cantitate < 0 || id < 0)
                    {
                        throw new Exception();
                    }

                    ProductBLL x = new ProductBLL();
                    Product a = x.findProductById(idProdus);

                    OrderBLL y = new OrderBLL();
                    Comanda c = y.findOrderById(id);

                    if(idProdus != c.getIdProduct()) {
                        if (a.getStoc() < cantitate) {
                            throw new Exception();
                        }
                        Comanda b = new Comanda(id,idClient,idProdus,cantitate);
                        y.updateOrder(id,b);

                        a.setStoc(a.getStoc() - cantitate);
                        x.updateProduct(a.getId(),a);

                        Product d = x.findProductById(c.getIdProduct());
                        d.setStoc(d.getStoc() + cantitate);
                        x.updateProduct(d.getId(),d);
                    }
                    else
                    {
                        int cantitatenoua = cantitate;
                        if(c.getCantitate() != cantitate)
                            cantitatenoua = cantitate - c.getCantitate();
                        if (a.getStoc() < cantitatenoua) {
                                throw new Exception();
                        }

                        Comanda b = new Comanda(id,idClient,idProdus,cantitate);
                        y.updateOrder(id,b);

                        a.setStoc(a.getStoc() - cantitatenoua);
                        x.updateProduct(a.getId(),a);

                    }


//                    Comanda b = new Comanda(id,idClient,idProdus,cantitate);
//                    y.updateOrder(id,b);
//
//                    a.setStoc(a.getStoc() - cantitate);
//                    x.updateProduct(a.getId(),a);

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

                    OrderBLL x = new OrderBLL();

                    Comanda c = x.findOrderById(id);
                    ProductBLL y = new ProductBLL();
                    Product newprod = y.findProductById(c.getIdProduct());
                    newprod.setStoc(newprod.getStoc() + c.getCantitate());
                    y.updateProduct(newprod.getId(),newprod);

                    x.deleteOrder(id);



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

                OrderDAO x = new OrderDAO();
                OrderBLL y = new OrderBLL();

                List<Comanda> lista = y.findAllOrders();
                String[][] elem = new String[1000][4];
                elem = x.formMatrix(lista);

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
