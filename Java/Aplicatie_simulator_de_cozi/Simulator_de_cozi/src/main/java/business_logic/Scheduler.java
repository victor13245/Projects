package business_logic;

import model.CasaMarcat;
import model.Clienti;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private List<CasaMarcat> caseMarcat;
    private int maxNoCase;
    //private int maxClientiPeCasa;
    private Strategy strategie;


    public List<CasaMarcat> getCaseMarcat() {
        return caseMarcat;
    }
    public void setCaseMarcat(List<CasaMarcat> caseMarcat) {
        this.caseMarcat = caseMarcat;
    }
    public int getMaxNoCase() {
        return maxNoCase;
    }
    public void setMaxNoCase(int maxNoCase) {
        this.maxNoCase = maxNoCase;
    }

    public Strategy getStrategie() {
        return strategie;
    }
    public void setStrategie(Strategy strategie) {
        this.strategie = strategie;
    }


    public Scheduler(int maxNoCase) {

        this.strategie = new ConcreteStrategyTime();
        this.maxNoCase = maxNoCase;
        this.caseMarcat = new ArrayList<CasaMarcat>();

        for(int i = 0; i < maxNoCase; i++)
        {
            CasaMarcat x = new CasaMarcat(i + 1);
            this.caseMarcat.add(x);
        }

    }

    public void changeStrategy(SelectionPolicy policy)
    {

        if(policy == SelectionPolicy.SHORTEST_QUEUE)
        {
            this.strategie = new ConcreteStrategyQueue();
        }
        else
        {
            this.strategie = new ConcreteStrategyTime();
        }

    }

    public void dispachTask(Clienti c)
    {
        this.strategie.addTask(this.caseMarcat, c);
    }


}
