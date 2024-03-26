package org.example;
import Obiecte.Polinom;
import Metode.Operatii;
import Exceptii.InvalidDataEx;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends JFrame{
    private JTextField textField_pol1;
    private JTextField textField_pol2;
    private JButton btnDerivare;


    public static boolean isEmpty(HashMap<Integer, Double> pol) {
        for(int i : pol.keySet())
        {
            if(pol.get(i) != 0)
                return false;
        }
        return true;
    }

    public static void pattern(String src) throws InvalidDataEx
    {
        Pattern pattern = Pattern.compile("[0-9]+");

    }

    public static void check(String src) throws InvalidDataEx
    {
        Pattern pattern = Pattern.compile("^[\\^0-9x\\*\\+\\-\s]*$");
        Matcher matcher = pattern.matcher(src);
        if(!matcher.find())
            throw new InvalidDataEx("caracter invalid");
        else
        {
            String aux = src.replaceAll("\\s+", "");
            pattern = Pattern.compile("(?=^((?!\\+\\+).)*$)(?=^((?!\\-\\-).)*$)(?=^((?!\\+\\-).)*$)(?=^((?!\\-\\+).)*$)(?=^((?!\\*\\*).)*$)(?=^((?!\\^\\^).)*$)(?=^((?!\\*\\^).)*$)(?=^((?!\\^\\*).)*$)(?=^((?!\\*\\-).)*$)(?=^((?!\\-\\*).)*$)(?=^((?!\\^\\-).)*$)(?=^((?!\\-\\^).)*$)(?=^((?!\\+\\*).)*$)(?=^((?!\\*\\+).)*$)(?=^((?!\\^\\+).)*$)(?=^((?!\\+\\^).)*$)(?=^((?!xx).)*$)");
            matcher = pattern.matcher(aux);
            if(!matcher.find())
                throw new InvalidDataEx("operatie invalida");
        }
    }

    public static void citireRegex(Polinom dest, String text)
    {
        String result = text.replaceAll("\\s+", "");
        Pattern pattern = Pattern.compile("[+-]?[0-9]+\\*x\\^[0-9]+|[+-]?x\\^[0-9]+|[+-]?[0-9]+x\\^[0-9]+|[+-]?[0-9]+\\*x|[+-]?[0-9]+x|[+-]?[0-9]+|[+-]?x");
        Matcher matcher = pattern.matcher(result);
        HashMap<Integer, Double> aux = new HashMap<Integer, Double>();
        while(matcher.find())
        {
            int semn = 1;
            int posval = 0;
            int pospow = 2;
            String x = matcher.group();
            if(x.charAt(0) == '-')
            {
                semn = -1;
            }
            if(x.charAt(0) == 'x' || ((x.charAt(0) == '+' || x.charAt(0) == '-') && x.charAt(1) == 'x'))
                posval = 1;
            if(!x.contains("^"))
                pospow = 1;
            if(!x.contains("x"))
                pospow = 0;
            String newx = x.replaceAll("[^0-9]", ",");
            int[] numerals = new int[2];
            int nrelem = 0;
            String[] rez = newx.split(",");
            for (int i = 0; i < rez.length; i++) {
                rez[i] = rez[i].trim();
                if(rez[i] != "")
                {

                    numerals[nrelem] = Integer.parseInt(rez[i]);
                    nrelem++;
                }

            }
            if(pospow != 2)
                numerals[1] = pospow;
            if(posval == 1)
                if(pospow == 2)
                {numerals[1] = numerals[0]; numerals[0] = 1;}
                else
                    numerals[0] = 1;

            aux.put(numerals[1], semn * (double)numerals[0]);

        }
        dest.setMonoame(aux);
    }




    public Main() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Calculator de polinoame");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblNewLabel.setBounds(109, 10, 264, 36);
        getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Polinom 1:");
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(10, 81, 79, 13);
        getContentPane().add(lblNewLabel_1);

        textField_pol1 = new JTextField();
        textField_pol1.setBounds(107, 79, 179, 19);
        getContentPane().add(textField_pol1);
        textField_pol1.setColumns(10);

        JLabel lblNewLabel_1_1 = new JLabel("Polinom 2:");
        lblNewLabel_1_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblNewLabel_1_1.setBounds(10, 116, 79, 13);
        getContentPane().add(lblNewLabel_1_1);

        textField_pol2 = new JTextField();
        textField_pol2.setColumns(10);
        textField_pol2.setBounds(107, 114, 179, 19);
        getContentPane().add(textField_pol2);

        JButton btnSuma = new JButton("Suma");
        btnSuma.setFont(new Font("Tahoma", Font.ITALIC, 10));
        btnSuma.setBounds(10, 166, 85, 21);
        getContentPane().add(btnSuma);

        JButton btnScadere = new JButton("Scadere");
        btnScadere.setFont(new Font("Tahoma", Font.ITALIC, 10));
        btnScadere.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnScadere.setBounds(10, 209, 85, 21);
        getContentPane().add(btnScadere);

        JButton btnInmultire = new JButton("Inmultire");
        btnInmultire.setFont(new Font("Tahoma", Font.ITALIC, 10));
        btnInmultire.setBounds(116, 166, 85, 21);
        getContentPane().add(btnInmultire);

        JButton btnImpartire = new JButton("Impartire");
        btnImpartire.setFont(new Font("Tahoma", Font.ITALIC, 10));
        btnImpartire.setBounds(116, 209, 85, 21);
        getContentPane().add(btnImpartire);

        btnDerivare = new JButton("Derivare");
        btnDerivare.setFont(new Font("Tahoma", Font.ITALIC, 10));
        btnDerivare.setBounds(223, 166, 85, 21);
        getContentPane().add(btnDerivare);

        JButton btnIntegrare = new JButton("Integrare");
        btnIntegrare.setFont(new Font("Tahoma", Font.ITALIC, 10));
        btnIntegrare.setBounds(223, 209, 85, 21);
        getContentPane().add(btnIntegrare);

        JLabel lblNewLabel_1_1_1 = new JLabel("Rezultat:");
        lblNewLabel_1_1_1.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lblNewLabel_1_1_1.setBounds(10, 279, 79, 13);
        getContentPane().add(lblNewLabel_1_1_1);

        JLabel lbl_rezultat = new JLabel("Aici apare raspunsul");
        lbl_rezultat.setBounds(105, 279, 349, 14);
        getContentPane().add(lbl_rezultat);



        btnSuma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try
                {
                    String pol1 = textField_pol1.getText();
                    String pol2 = textField_pol2.getText();
                    Polinom a = new Polinom();
                    Polinom b = new Polinom();
                    check(textField_pol1.getText());
                    check(textField_pol2.getText());
                    citireRegex(a,pol1);
                    citireRegex(b,pol2);
                    Polinom rez = new Polinom();
                    Operatii op = new Operatii();
                    op.suma(a.getMonoame(), b.getMonoame(),rez);

                    if(!isEmpty(rez.getMonoame()))
                        lbl_rezultat.setText(rez.toString());
                    else
                        lbl_rezultat.setText("0");
                } catch (InvalidDataEx exc) { JOptionPane.showMessageDialog(null, "date invalide",
                        "Message", JOptionPane.INFORMATION_MESSAGE); }

            }
        });

        btnScadere.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try
                {
                    String pol1 = textField_pol1.getText();
                    String pol2 = textField_pol2.getText();
                    Polinom a = new Polinom();
                    Polinom b = new Polinom();

                    check(textField_pol1.getText());
                    check(textField_pol2.getText());
                    citireRegex(a,pol1);
                    citireRegex(b,pol2);

                    Polinom rez = new Polinom();
                    Operatii op = new Operatii();
                    op.scadere(a.getMonoame(), b.getMonoame(),rez);

                    if(!isEmpty(rez.getMonoame()))
                        lbl_rezultat.setText(rez.toString());
                    else
                        lbl_rezultat.setText("0");

                } catch (InvalidDataEx exc) { JOptionPane.showMessageDialog(null, "date invalide",
                        "Message", JOptionPane.INFORMATION_MESSAGE); }

            }
        });


        btnInmultire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try
                {
                    String pol1 = textField_pol1.getText();
                    String pol2 = textField_pol2.getText();
                    Polinom a = new Polinom();
                    Polinom b = new Polinom();
                    check(textField_pol1.getText());
                    check(textField_pol2.getText());
                    citireRegex(a,pol1);
                    citireRegex(b,pol2);
                    Polinom rez = new Polinom();
                    Operatii op = new Operatii();
                    op.inmultire(a.getMonoame(), b.getMonoame(),rez);
                    if(!isEmpty(rez.getMonoame()))
                        lbl_rezultat.setText(rez.toString());
                    else
                        lbl_rezultat.setText("0");
                } catch (InvalidDataEx exc) { JOptionPane.showMessageDialog(null, "date invalide",
                        "Message", JOptionPane.INFORMATION_MESSAGE); }
            }
        });

        btnDerivare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try
                {
                    String pol1 = textField_pol1.getText();
                    Polinom a = new Polinom();

                    check(textField_pol1.getText());
                    citireRegex(a,pol1);

                    Polinom rez = new Polinom();
                    Operatii op = new Operatii();
                    op.derivare(a.getMonoame(),rez);
                    if(!isEmpty(rez.getMonoame()))
                        lbl_rezultat.setText(rez.toString());
                    else
                        lbl_rezultat.setText("0");
                } catch (InvalidDataEx exc) { JOptionPane.showMessageDialog(null, "date invalide",
                        "Message", JOptionPane.INFORMATION_MESSAGE); }
            }
        });


        btnIntegrare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try
                {
                    String pol1 = textField_pol1.getText();
                    Polinom a = new Polinom();
                    check(textField_pol1.getText());
                    citireRegex(a,pol1);

                    Polinom rez = new Polinom();
                    Operatii op = new Operatii();
                    op.integrare(a.getMonoame(),rez);
                    if(!isEmpty(rez.getMonoame()))
                        lbl_rezultat.setText(rez.toString() + " + C");
                    else
                        lbl_rezultat.setText("0 + C");
                } catch (InvalidDataEx exc) { JOptionPane.showMessageDialog(null, "date invalide",
                        "Message", JOptionPane.INFORMATION_MESSAGE); }
            }
        });

        btnImpartire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try
                {
                    String pol1 = textField_pol1.getText();
                    String pol2 = textField_pol2.getText();
                    Polinom a = new Polinom();
                    Polinom b = new Polinom();
                    check(textField_pol1.getText());
                    check(textField_pol2.getText());
                    citireRegex(a,pol1);
                    citireRegex(b,pol2);
                    Polinom rez = new Polinom();
                    String rest;
                    Operatii op = new Operatii();
                    rest = op.impartire(a.getMonoame(), b.getMonoame(),rez);
                    if(!isEmpty(rez.getMonoame()))
                        if(rest == "Nu am")
                            lbl_rezultat.setText(rez.toString());
                        else
                            lbl_rezultat.setText(rez.toString() + " rest " + rest);
                    else
                    if(rest == "Nu am")
                        lbl_rezultat.setText("0");
                    else
                        lbl_rezultat.setText("0 rest " + pol1.toString());

                } catch (InvalidDataEx exc) { JOptionPane.showMessageDialog(null, "date invalide",
                        "Message", JOptionPane.INFORMATION_MESSAGE); }

            }
        });

        this.setVisible(true);
    }




    public static void main(String[] args)
    {
        Main a = new Main();

        String aux = "3*x^2 - 2*x + 2";
        Pattern pattern = Pattern.compile("^[\\^0-9x\\*\\+\\-\s]*$");
        pattern = Pattern.compile("(?=^((?!\\+\\+).)*$)(?=^((?!\\-\\-).)*$)(?=^((?!\\+\\-).)*$)(?=^((?!\\-\\+).)*$)(?=^((?!\\*\\*).)*$)(?=^((?!\\^\\^).)*$)(?=^((?!\\*\\^).)*$)(?=^((?!\\^\\*).)*$)(?=^((?!\\*\\-).)*$)(?=^((?!\\-\\*).)*$)(?=^((?!\\^\\-).)*$)(?=^((?!\\-\\^).)*$)(?=^((?!\\+\\*).)*$)(?=^((?!\\*\\+).)*$)(?=^((?!\\^\\+).)*$)(?=^((?!\\+\\^).)*$)(?=^((?!xx).)*$)");
        Matcher matcher = pattern.matcher(aux);

    }

}
