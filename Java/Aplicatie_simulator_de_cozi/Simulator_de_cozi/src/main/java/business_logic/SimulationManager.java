package business_logic;

import model.Clienti;
import model.CasaMarcat;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class SimulationManager implements Runnable{

    private int timeLimit;
    private int maxProcessingTime;
    private int minProcessingTime;
    private int maxArrivalTime;
    private int minArrivalTime;
    private int numarCase;
    private int numarClienti;
    private JTextArea textArea;
    private JButton buton;
    //private int maxClientiLaCasa;
    private SelectionPolicy policy;

    private Thread thread;
    private Scheduler scheduler;
    private List<Clienti> clientiGenerati;

    public SimulationManager(int timeLimit, int maxArrivalTime, int minArrivalTime,int maxProcessingTime, int minProcessingTime, int numarCase,
                             int numarClienti, JTextArea textArea, JButton buton) {
        super();
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;
        this.numarCase = numarCase;
        this.numarClienti = numarClienti;
        this.policy = SelectionPolicy.SHORTEST_TIME;
        this.textArea = textArea;
        this.buton = buton;

        this.clientiGenerati = new ArrayList<Clienti>();
        this.scheduler = new Scheduler(this.numarCase);
        this.genereazaNClienti();

        this.thread = new Thread(this);
        this.thread.start();
    }

    public void genereazaNClienti()
    {
        for(int i=0; i < this.numarClienti; i++)
        {
            Random random = new Random();
            Random random2 = new Random();
            int at = random.nextInt(this.minArrivalTime,this.maxArrivalTime );
            int st = random2.nextInt(this.minProcessingTime,this.maxProcessingTime + 1);
            Clienti aux = new Clienti(i + 1,at,st);
            this.clientiGenerati.add(aux);
        }
        Collections.sort(clientiGenerati);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        int currentTime = 0;
        int peakHour = 0;
        int maxcl = 0;
        float avgService = 0;
        float avgTime = 0;
        int z = 0;

        try
        {

            String aux = new String();
            FileWriter writer = new FileWriter("logs.txt");

            for(int i = 0; i < numarClienti; i++)
            {
                avgService = avgService + clientiGenerati.get(i).getServiceTime();
            }

            System.out.println("inceput");
            while(currentTime < timeLimit || clientiGenerati.size() > 0)
            {
                int nrcl = 0;
                aux = new String();
                while(clientiGenerati.size()>0 && clientiGenerati.get(0).getArrivalTime() == currentTime)
                {
                    Clienti cl = clientiGenerati.remove(0);
                    this.scheduler.dispachTask(cl);
                    float avgTime2 = 0;
                    for(int i = 0; i < numarCase; i++)
                    {
                        avgTime2 = avgTime2 + scheduler.getCaseMarcat().get(i).getWaitingPeriod().get();
                    }
                    avgTime2 = avgTime2 / numarCase;
                    avgTime = avgTime + avgTime2;
                    z++;
                }

                aux = (currentTime+1) + ": \n";
                aux = aux.concat("Waiting clients: ");
                for(Clienti c : clientiGenerati)
                {
                    aux = aux.concat(c.toString() + " ");
                }
                aux = aux.concat("\n");

                for(int i = 0; i < numarCase; i++)
                {
                    aux = aux.concat("Casa" + (i+1) + ": ");
                    for(Clienti c : scheduler.getCaseMarcat().get(i).getCoada())
                    {
                        aux = aux.concat(c.toString() + " ");

                    }
                    aux = aux.concat("\n");
                    nrcl = nrcl + scheduler.getCaseMarcat().get(i).getCoada().size();
                }

                if(nrcl > maxcl)
                {
                    maxcl = nrcl;
                    peakHour = currentTime;
                }

                textArea.setText(aux);
                writer.write(aux);

                ///

                for(int i = 0; i < numarCase; i++)
                {
                    if(scheduler.getCaseMarcat().get(i).getWaitingPeriod().get() > 0)
                    {
                        scheduler.getCaseMarcat().get(i).getWaitingPeriod().set(scheduler.getCaseMarcat().get(i).getWaitingPeriod().get() - 1);
                    }
                }
                ///

                currentTime++;
                try {
                    thread.sleep(1000);
                } catch (InterruptedException e) {}

            }


            avgService = avgService / numarClienti;
            avgTime = avgTime / z;
            aux = aux.concat("\n\nPeak hour: " + (peakHour+1) );
            aux = aux.concat("\nAverage service:  " + avgService );
            aux = aux.concat("\nAverage waiting time:  " + avgTime );

            textArea.setText(aux);
            writer.write(aux);

            writer.close();
        }catch(Exception e) {}
        System.out.println("final");
        buton.setEnabled(true);

    }

	/*public static void main(String[] args)
	{
		SimulationManager x = new SimulationManager(30,10,0,10,2,3,5,SelectionPolicy.SHORTEST_TIME);
	} */

}
