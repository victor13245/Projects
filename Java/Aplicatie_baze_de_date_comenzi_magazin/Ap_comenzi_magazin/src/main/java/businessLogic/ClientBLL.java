package businessLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import businessLogic.validators.Validator;
import dataAccess.ClientDAO;
import model.Client;
import model.Product;


public class ClientBLL {

    private List<Validator<Client>> validators;
    private ClientDAO clientDAO;

    public ClientBLL() {
        validators = new ArrayList<Validator<Client>>();
        //validators.add(new EmailValidator());
        //validators.add(new ClientAgeValidator());

        clientDAO = new ClientDAO();
    }

    /**
     * Apeleaza findById pentru tabela de clienti in particular, verificand daca s-a apelat cu succes
     * @param id
     * @return rezultatul functiei findById
     */
    public Client findClientById(int id) {
        Client st = clientDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }



        return st;
    }

    /**
     * Apeleaza findAll pentru tabela de clienti in particular, verificand daca s-a apelat cu succes
     * @return rezultatul functiei findAll
     */
    public List<Client> findAllClients() {
        List<Client> st = clientDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("eroare la gasirea elementelor");
        }

        //System.out.println(st.toString());


        return st;
    }

    /**
     * Apeleaza insert pentru tabela de clienti in particular, verificand daca s-a apelat cu succes
     * @param x
     * @return rezultatul functiei insert
     */

    public Client insertClient(Client x) {
        Client st = clientDAO.insert(x);
        if (st == null) {
            throw new NoSuchElementException("Nu a mers inserarea");
        }



        return st;
    }

    /**
     * Apeleaza update pentru tabela de clienti in particular, verificand daca s-a apelat cu succes
     * @param id
     * @param x
     * @return rezultatul functiei update
     */

    public Client updateClient(int id, Client x) {
        Client st = clientDAO.update(id, x);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }



        return st;
    }

    /**
     * Apeleaza delete pentru tabela de clienti in particular, verificand daca s-a apelat cu succes
     * @param id
     */
    public void deleteClient(int id)
    {
        int status = clientDAO.delete(id);
        if (status == 1) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }

    }



}
