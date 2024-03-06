package businessLogic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import businessLogic.validators.Validator;

import dataAccess.ProductDAO;
import model.Comanda;
import model.Product;
public class ProductBLL {

    private List<Validator<Product>> validators;
    private ProductDAO productDAO;

    public  ProductBLL() {
        validators = new ArrayList<Validator<Product>>();
        //validators.add(new EmailValidator());
        //validators.add(new ClientAgeValidator());

        productDAO = new ProductDAO();
    }

    /**
     * Apeleaza findById pentru tabela de produse in particular, verificand daca s-a apelat cu succes
     * @param id
     * @return rezultatul functiei findById
     */
    public Product findProductById(int id) {
        Product st = productDAO.findById(id);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }



        return st;
    }

    /**
     * Apeleaza findAll pentru tabela de produse in particular, verificand daca s-a apelat cu succes
     * @return rezultatul functiei findAll
     */
    public List<Product> findAllProducts() {
        List<Product> st = productDAO.findAll();
        if (st == null) {
            throw new NoSuchElementException("eroare la gasirea elementelor");
        }

        //System.out.println(st.toString());


        return st;
    }

    /**
     * Apeleaza insert pentru tabela de produse in particular, verificand daca s-a apelat cu succes
     * @param x
     * @return rezultatul functiei insert
     */

    public Product insertProduct(Product x) {
        Product st = productDAO.insert(x);
        if (st == null) {
            throw new NoSuchElementException("Nu a mers inserarea");
        }



        return st;
    }

    /**
     * Apeleaza update pentru tabela de produse in particular, verificand daca s-a apelat cu succes
     * @param id
     * @param x
     * @return rezultatul functiei update
     */

    public Product updateProduct(int id, Product x) {
        Product st = productDAO.update(id, x);
        if (st == null) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }



        return st;
    }

    /**
     * Apeleaza delete pentru tabela de produse in particular, verificand daca s-a apelat cu succes
     * @param id
     */
    public void deleteProduct(int id)
    {
        int status = productDAO.delete(id);
        if (status == 1) {
            throw new NoSuchElementException("The student with id =" + id + " was not found!");
        }

    }

}
