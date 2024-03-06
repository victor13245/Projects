package Metode;
import Obiecte.Polinom;
import java.util.Collections;
import java.util.HashMap;

public class Operatii {


    public Operatii() {
    }

    public void suma(HashMap<Integer, Double> pol1, HashMap<Integer, Double> pol2, Polinom rezultat) {
        HashMap<Integer, Double> rez = pol1;

        for (int j : pol2.keySet()) {
            if (pol1.containsKey(j)) {
                double auxiliar = pol1.get(j) + pol2.get(j);

                rez.put(j, auxiliar);
            } else {

                rez.put(j, pol2.get(j));
            }
        }
        rezultat.setMonoame(rez);
        // return rez;
    }

    public void scadere(HashMap<Integer, Double> pol1, HashMap<Integer, Double> pol2, Polinom rezultat) {
        HashMap<Integer, Double> rez = pol1;

        for (int j : pol2.keySet()) {
            if (pol1.containsKey(j)) {
                double auxiliar = pol1.get(j) - pol2.get(j);
                rez.remove(j);
                rez.put(j, auxiliar);
            } else {
                rez.put(j, -1 * pol2.get(j));
            }
        }
        rezultat.setMonoame(rez);
    }

    public void inmultire(HashMap<Integer, Double> pol1, HashMap<Integer, Double> pol2, Polinom rezultat) {
        HashMap<Integer, Double> rez = new HashMap<Integer, Double>();
        for (int i : pol1.keySet()) {
            for (int j : pol2.keySet()) {
                int putere = i + j;
                double val = pol1.get(i) * pol2.get(j);
                if (rez.containsKey(putere)) {
                    val = val + rez.get(putere);
                    rez.put(putere, val);
                } else {
                    rez.put(putere, val);
                }
            }
        }
        rezultat.setMonoame(rez);
    }

    public void derivare(HashMap<Integer, Double> pol1, Polinom rezultat) {
        HashMap<Integer, Double> rez = new HashMap<Integer, Double>();
        for (int i : pol1.keySet()) {
            if (i != 0) {

                int newpow = i - 1;
                double newval = i * pol1.get(i);
                rez.put(newpow, newval);
            }
        }
        rezultat.setMonoame(rez);
    }

    public void integrare(HashMap<Integer, Double> pol1, Polinom rezultat) {
        HashMap<Integer, Double> rez = new HashMap<Integer, Double>();
        for (int i : pol1.keySet()) {
            int newpow = i + 1;
            double newval = pol1.get(i) / (i + 1);
            rez.put(newpow, newval);
        }
        rezultat.setMonoame(rez);
    }

    public static int greatestKey(HashMap<Integer, Double> pol1) {
        int max = 0;
        for (int i : pol1.keySet()) {
            if (i > max && pol1.get(i) != 0) {
                max = i;
            }
        }
        return max;
    }

    public static HashMap<Integer, Double> impartireScalar(HashMap<Integer, Double> pol, double scalar)
    {
        HashMap<Integer, Double> rez = pol;
        //System.out.println("ceva");
        for (int i : pol.keySet()) {
            double newval = pol.get(i) / scalar;
            rez.put(i, newval);
        }

        return rez;
    }

    public static boolean isEmpty(HashMap<Integer, Double> pol) {
        for(int i : pol.keySet())
        {
            if(pol.get(i) != 0)
                return false;
        }
        return true;
    }
    public String impartire(HashMap<Integer, Double> pol1, HashMap<Integer, Double> pol2, Polinom rezultat) {
        Operatii op = new Operatii();
        HashMap<Integer, Double> rez = new HashMap<Integer, Double>(); String rest = "Nu am";
        if(isEmpty(pol2))
            rezultat.setMonoame(rez);
        else
        if(greatestKey(pol2) > greatestKey(pol1))
            rezultat.setMonoame(rez);
        else
        if(greatestKey(pol2) == 0)
            rezultat.setMonoame(impartireScalar(pol1, pol2.get(0)));
        else
        {
            Polinom aux = new Polinom();
            aux.setMonoame(pol1);
            while(greatestKey(aux.getMonoame()) > greatestKey(pol2))
            {
                HashMap<Integer, Double> aux3 = aux.getMonoame();
                int powdif = greatestKey(aux3) - greatestKey(pol2);
                double valdif = aux3.get(greatestKey(aux3)) / pol2.get(greatestKey(pol2));
                HashMap<Integer, Double> aux2 = new HashMap<Integer, Double>();
                aux2.put(powdif, valdif);
                rez.put(powdif, valdif);
                Polinom auxpol = new Polinom();
                op.inmultire(pol2, aux2,auxpol);
                op.scadere(aux3, auxpol.getMonoame(),aux);
                aux3 = aux.getMonoame();
                aux3.values().removeAll(Collections.singleton(0));
                aux.setMonoame(aux3);
            }
            if(greatestKey(pol2) == greatestKey(aux.getMonoame()))
            { int g2 = greatestKey(pol2);
                double valdif = aux.getMonoame().get(g2) / pol2.get(g2);
                rez.put(0, valdif);
                HashMap<Integer, Double> aux2 = new HashMap<Integer, Double>();
                HashMap<Integer, Double> aux3 = aux.getMonoame();
                aux2.put(0, valdif);
                Polinom auxpol = new Polinom();
                op.inmultire(pol2, aux2, auxpol);
                op.scadere(aux3, auxpol.getMonoame(),aux);
                aux3 = aux.getMonoame();
                aux3.values().removeAll(Collections.singleton(0));
                aux.setMonoame(aux3);
                if(!isEmpty(aux3))
                {if(aux.toString().charAt(1) == '-')
                {
                    op.suma(aux3,pol1,aux);
                    HashMap<Integer, Double> min = new HashMap<Integer, Double>();
                    Polinom auxrez = new Polinom();
                    min.put(0,1.0);
                    op.scadere(rez,min,auxrez);
                    rez = auxrez.getMonoame();
                }
                    rest = aux.toString();
                }
            }
            rezultat.setMonoame(rez);
        }
        return rest;
    }

}
