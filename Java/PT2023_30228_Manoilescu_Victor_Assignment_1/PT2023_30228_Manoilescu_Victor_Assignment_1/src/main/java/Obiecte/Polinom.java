package Obiecte;

import java.util.Collections;
import java.util.HashMap;
import java.text.DecimalFormat;


public class Polinom {

    private HashMap<Integer, Double> monoame;
    DecimalFormat format = new DecimalFormat("0.##");



    public Polinom() {
        super();
        this.monoame = new HashMap<Integer, Double>();
    }

    public HashMap<Integer, Double> getMonoame() {
        return monoame;
    }

    public void setMonoame(HashMap<Integer, Double> monoame) {
        this.monoame = monoame;
    }



    public String toString() {
        String rez = new String();
        for (int i : this.monoame.keySet()) {
            if (this.monoame.get(i) > 0) {
                if (i > 1)
                    rez = " + " + format.format(this.monoame.get(i)) + "*x^" + i + rez;
                if (i == 1)
                    rez = " + " + format.format(this.monoame.get(i)) + "*x" + rez;
                if (i == 0)
                    rez = " + " + format.format(this.monoame.get(i)) + rez;
            } else if (this.monoame.get(i) < 0) {
                if (i > 1)
                    rez = " " + format.format(this.monoame.get(i)) + "*x^" + i + rez;
                if (i == 1)
                    rez = " " + format.format(this.monoame.get(i)) + "*x" + rez;
                if (i == 0)
                    rez = " " + format.format(this.monoame.get(i)) + rez;
            }
        }
        if (rez.charAt(1) == '+') {
            rez = rez.substring(2, rez.length());
        }
        return rez;
    }




    @Override
    public boolean equals(Object obj) {
        return this.monoame.equals(((Polinom)obj).getMonoame());
    }
}

