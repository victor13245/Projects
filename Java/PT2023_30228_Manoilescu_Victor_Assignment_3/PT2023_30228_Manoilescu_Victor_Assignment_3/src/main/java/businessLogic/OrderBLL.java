package businessLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import businessLogic.validators.Validator;
import dataAccess.OrderDAO;
import model.Comanda;
import model.Product;

public class OrderBLL {

    private List<Validator<Comanda>> validators;
    private OrderDAO orderDAO;

    public OrderBLL() {
        validators = new ArrayList<Validator<Comanda>>();
        //validators.add(new EmailValidator());
        //validators.add(new ClientAgeValidator());

        orderDAO = new OrderDAO();
    }

    /**
     * Apeleaza findById pentru tabela de comenzi in particular, verificand daca s-a apelat cu succes
     * @param id
     * @return rezultatul functiei findById
     */

    public Comanda findOrderById(int id) {
        Comanda st = orderDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }



        return st;
    }

    /**
     * Apeleaza findAll pentru tabela de comenzi in particular, verificand daca s-a apelat cu succes
     * @return rezultatul functiei findAll
     */
    public List<Comanda> findAllOrders() {
        List<Comanda> st = orderDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("eroare la gasirea elementelor");
        }

        //System.out.println(st.toString());


        return st;
    }

    /**
     * Apeleaza insert pentru tabela de comenzi in particular, verificand daca s-a apelat cu succes
     * @param x
     * @return rezultatul functiei insert
     */

    public Comanda insertOrder(Comanda x) {
        Comanda st = orderDAO.insert(x);
        if (st == null) {
            throw new NoSuchElementException("Nu a mers inserarea");
        }



        return st;
    }

    /**
     * Apeleaza update pentru tabela de comenzi in particular, verificand daca s-a apelat cu succes
     * @param id
     * @param x
     * @return rezultatul functiei update
     */
    public Comanda updateOrder(int id, Comanda x) {
        Comanda st = orderDAO.update(id, x);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }



        return st;
    }

    /**
     * Apeleaza delete pentru tabela de comenzi in particular, verificand daca s-a apelat cu succes
     * @param id
     */
    public void deleteOrder(int id)
    {
        int status = orderDAO.delete(id);
        if (status == 1) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }

    }

}
