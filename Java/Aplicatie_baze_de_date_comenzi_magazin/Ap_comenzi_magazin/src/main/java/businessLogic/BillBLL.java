package businessLogic;

import businessLogic.validators.Validator;
import dataAccess.BillDAO;
import model.Bill;



import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BillBLL {

    private List<Validator<Bill>> validators;
    private BillDAO billDAO;

    public BillBLL() {
        validators = new ArrayList<Validator<Bill>>();
        //validators.add(new EmailValidator());
        //validators.add(new ClientAgeValidator());

        billDAO = new BillDAO();
    }
    public Bill insertBill(Bill x) {
        Bill st = billDAO.insert(x);
        if (st == null) {
            throw new NoSuchElementException("Nu a mers inserarea");
        }



        return st;
    }

}
