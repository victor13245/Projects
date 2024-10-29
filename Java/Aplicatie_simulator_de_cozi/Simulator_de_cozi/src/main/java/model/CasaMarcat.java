package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CasaMarcat implements Runnable{

    private LinkedBlockingQueue<Clienti> coada;
    private AtomicInteger waitingPeriod; // waiting time total adunat de la toti clientii din coada
    private Thread thread;
    private int idCasa;


    public CasaMarcat(int id) {
        super();
        this.idCasa = id;
        this.coada = new LinkedBlockingQueue<Clienti>();
        this.waitingPeriod = new AtomicInteger();
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void addClient (Clienti client)
    {
        this.coada.add(client);
        this.waitingPeriod.set(this.waitingPeriod.get() + client.getServiceTime());
    }


    public void run()
    {
        while(true)
        {
            try {
                if(coada.size() > 0)
                {
			 /*System.out.print("Casa " + idCasa + ": ");
			 for(Clienti c : coada)
			 {
				 System.out.print(c.toString() + " ");
			 }
			 System.out.println(); */

                    this.thread.sleep(1000L * this.coada.peek().getServiceTime());
                    Clienti clientCurent = this.coada.remove();
                    //this.waitingPeriod.set(this.waitingPeriod.get() - clientCurent.getServiceTime());
                }
            }catch(Exception x) {}

        }
    }

    public LinkedBlockingQueue<Clienti> getCoada() {
        return coada;
    }

    public void setCoada(LinkedBlockingQueue<Clienti> coada) {
        this.coada = coada;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }




}

