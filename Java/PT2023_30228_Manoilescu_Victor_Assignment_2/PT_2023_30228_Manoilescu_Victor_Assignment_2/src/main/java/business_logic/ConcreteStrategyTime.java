package business_logic;

import model.CasaMarcat;
import model.Clienti;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteStrategyTime implements Strategy{



    @Override
    public void addTask(List<CasaMarcat> caseMarcat, Clienti client) {
        // TODO Auto-generated method stub
        AtomicInteger min = new AtomicInteger();
        min.set(1000000);

        for(CasaMarcat i : caseMarcat)
        {
            int a = i.getWaitingPeriod().get();
            int b = min.get();
            if(a < b)
            {
                min.set(a);
            }
        }

        for(CasaMarcat i : caseMarcat)
        {
            if(i.getWaitingPeriod().get() == min.get())
            {
                i.addClient(client);
                //System.out.println(i.getWaitingPeriod().get());
                break;
            }
        }


    }

}

