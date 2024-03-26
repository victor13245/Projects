package org.example;
import Metode.Operatii;
import Obiecte.Polinom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashMap;


public class Op_test {
    @Test
    public void testSum() {

        Operatii op = new Operatii();

        Polinom pol1 = new Polinom();
        HashMap<Integer, Double> p1 = new HashMap<Integer, Double>();
        p1.put(0,2.0);
        p1.put(1,1.0);
        p1.put(2,1.0);
        pol1.setMonoame(p1);

        Polinom pol2 = new Polinom();
        HashMap<Integer, Double> p2 = new HashMap<Integer, Double>();
        p2.put(0,1.0);
        p2.put(1,3.0);
        p2.put(2,2.0);
        pol2.setMonoame(p2);

        Polinom rez2 = new Polinom();
        HashMap<Integer, Double> p3 = new HashMap<Integer, Double>();
        p3.put(0,3.0);
        p3.put(1,4.0);
        p3.put(2,3.0);
        rez2.setMonoame(p3);

        Polinom rez = new Polinom();
        op.suma(pol1.getMonoame(),pol2.getMonoame(),rez);
        assertEquals(rez,rez2);

    }


    @Test
    public void testScadere() {

        Operatii op = new Operatii();

        Polinom pol1 = new Polinom();
        HashMap<Integer, Double> p1 = new HashMap<Integer, Double>();
        p1.put(0,2.0);
        p1.put(1,1.0);
        p1.put(2,1.0);
        pol1.setMonoame(p1);

        Polinom pol2 = new Polinom();
        HashMap<Integer, Double> p2 = new HashMap<Integer, Double>();
        p2.put(0,1.0);
        p2.put(1,3.0);
        p2.put(2,2.0);
        pol2.setMonoame(p2);

        Polinom rez2 = new Polinom();
        HashMap<Integer, Double> p3 = new HashMap<Integer, Double>();
        p3.put(0,1.0);
        p3.put(1,-2.0);
        p3.put(2,-1.0);
        rez2.setMonoame(p3);

        Polinom rez = new Polinom();
        op.scadere(pol1.getMonoame(),pol2.getMonoame(),rez);
        assertEquals(rez,rez2);

    }

    @Test
    public void testInmultire() {

        Operatii op = new Operatii();

        Polinom pol1 = new Polinom();
        HashMap<Integer, Double> p1 = new HashMap<Integer, Double>();
        p1.put(0,2.0);
        p1.put(1,1.0);
        pol1.setMonoame(p1);

        Polinom pol2 = new Polinom();
        HashMap<Integer, Double> p2 = new HashMap<Integer, Double>();
        p2.put(0,1.0);
        p2.put(1,3.0);
        p2.put(2,1.0);
        pol2.setMonoame(p2);

        Polinom rez2 = new Polinom();
        HashMap<Integer, Double> p3 = new HashMap<Integer, Double>();
        p3.put(0,2.0);
        p3.put(1,7.0);
        p3.put(2,5.0);
        p3.put(3,1.0);
        rez2.setMonoame(p3);

        Polinom rez = new Polinom();
        op.inmultire(pol1.getMonoame(),pol2.getMonoame(),rez);
        assertEquals(rez,rez2);

    }

    @Test
    public void testImpartire() {

        Operatii op = new Operatii();

        Polinom pol1 = new Polinom();
        HashMap<Integer, Double> p1 = new HashMap<Integer, Double>();
        p1.put(0,1.0);
        p1.put(1,2.0);
        p1.put(2,1.0);
        pol1.setMonoame(p1);

        Polinom pol2 = new Polinom();
        HashMap<Integer, Double> p2 = new HashMap<Integer, Double>();
        p2.put(0,4.0);
        p2.put(1,1.0);
        //p2.put(2,2.0);
        pol2.setMonoame(p2);

        Polinom rez2 = new Polinom();
        HashMap<Integer, Double> p3 = new HashMap<Integer, Double>();
        p3.put(0,-2.0);
        p3.put(1,1.0);
        rez2.setMonoame(p3);
        String rest2 = " 9";

        Polinom rez = new Polinom();
        String rest = op.impartire(pol1.getMonoame(),pol2.getMonoame(),rez);
        assertEquals(rez,rez2);
        assertEquals(rest,rest2);

    }

    @Test
    public void testDerivare() {

        Operatii op = new Operatii();

        Polinom pol1 = new Polinom();
        HashMap<Integer, Double> p1 = new HashMap<Integer, Double>();
        p1.put(0,2.0);
        p1.put(1,1.0);
        p1.put(2,1.0);
        pol1.setMonoame(p1);

        Polinom rez2 = new Polinom();
        HashMap<Integer, Double> p3 = new HashMap<Integer, Double>();
        p3.put(0,1.0);
        p3.put(1,2.0);
        //p3.put(2,3.0);
        rez2.setMonoame(p3);

        Polinom rez = new Polinom();
        op.derivare(pol1.getMonoame(),rez);
        assertEquals(rez,rez2);

    }

    @Test
    public void testIntegrala() {

        Operatii op = new Operatii();

        Polinom pol1 = new Polinom();
        HashMap<Integer, Double> p1 = new HashMap<Integer, Double>();
        p1.put(0,2.0);
        p1.put(1,1.0);
        p1.put(2,1.0);
        pol1.setMonoame(p1);

        Polinom rez2 = new Polinom();
        HashMap<Integer, Double> p3 = new HashMap<Integer, Double>();
        p3.put(1,2.0);
        p3.put(2,1/2.0);
        p3.put(3,1/3.0);
        rez2.setMonoame(p3);

        Polinom rez = new Polinom();
        op.integrare(pol1.getMonoame(),rez);
        assertEquals(rez,rez2);

    }

}
