package business_logic;

import model.CasaMarcat;
import model.Clienti;

import java.util.List;

public interface Strategy {
    public void addTask(List<CasaMarcat> caseMarcat, Clienti client);
}
