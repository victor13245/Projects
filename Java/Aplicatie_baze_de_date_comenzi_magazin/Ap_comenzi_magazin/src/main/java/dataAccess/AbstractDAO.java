package dataAccess;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;
import model.Client;

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }


    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        //sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }


    private String createInsertQuery(String field1,String field2,String field3,String field4) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        //sb.append(" * ");
        //sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" VALUES (" + "\'" + field1 + "\'" + ", " + "\'" + field2 + "\'" + ", " + "\'" + field3 + "\'" + ", " + "\'" + field4 + "\'" + ")");
        return sb.toString();
    }

    private String createUpdateQuery(String param1, String param2, String param3, String field2,String field3,String field4, int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        //sb.append(" * ");
        //sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" SET " + param1 + " = " + "\'" + field2 + "\'" + ", " + param2 +" = " + "\'" + field3 + "\'" + ", " + param3 + " = " + "\'" + field4 + "\'" + " " );
        sb.append("WHERE id = " + id);
        return sb.toString();
    }


//

    private String createDeleteQuery(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        //sb.append(" * ");
        //sb.append(" FROM ");
        sb.append(type.getSimpleName());
        //sb.append(" SET idClient = " + field2 + ", idProduct =" + field3 + ", cantitate = "+ field4 + " " );
        sb.append(" WHERE id = " + id);
        return sb.toString();
    }

    /**
     * selecteaza toate elementele dintr-o tabela
     * @return lista de tipul tabelei cu toate elementele din  ea in caz de succes; in caz de esec returneaza null
     */
    public List<T> findAll() {
        // TODO:
        List<T> list = new ArrayList<T>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            //statement.setInt(1, id);
            resultSet = statement.executeQuery();
            //System.out.println("merge");

                List<T> aux = createObjects(resultSet);
                int nrelemente = 0;
                for(T elem : aux)
                {
                    list.add(elem);
                    nrelemente++;
                }

//                for(int i = 0; i < nrelemente/2; i++)
//                    list.remove(i);

            //System.out.println("merge");
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * selecteaza elementul dintr-o tabela ce corespunde id-ului primit ca parametru
     * @param id
     * @return elementuk de tipul tabelei in caz de succes; in caz de esec returneaza null
     */

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Sterge din tabela elementul cu id-ul primit ca parametru
     * @param id
     * @return 0 in caz de succes; 1 in caz de esec
     */

    public int delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createDeleteQuery(id);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            //statement.setInt(1, id);
            statement.executeUpdate();

            return 0;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return 1;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * insereaza in tabela elementul primit ca parametru
     * @param t
     * @return elementul inserat in caz de succes; null in caz de esec
     */
    public T insert(T t) {
        // TODO:
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Object> list = new ArrayList<Object>();

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(t);
                list.add(value);
                //System.out.println(field.getName() + "=" + value);


            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String query = "empti";

        String field1,field2,field3,field4;
            field1 = list.get(0).toString();
            field2 = list.get(1).toString();
            field3 = list.get(2).toString();
            field4 = list.get(3).toString();
            query = createInsertQuery(field1,field2,field3,field4);
//        if(t.getClass().getSimpleName().equals("Client"))
//        {
//            System.out.println("am ajuns aici");
//            int field1, field4;
//            String field2,field3;
//            field1 = (int)list.get(0);
//            field2 = (String) list.get(1);
//            field3 = (String) list.get(2);
//            field4 = (int)list.get(3);
//            query = createInsertQueryClient(field1,field2,field3,field4);
//        }
//        if(t.getClass().getSimpleName().equals("Comanda"))
//        {
//            System.out.println("am ajuns aici 2");
//            int field1, field4;
//            int field2,field3;
//            field1 = (int)list.get(0);
//            field2 = (int)list.get(1);
//            field3 = (int)list.get(2);
//            field4 = (int)list.get(3);
//            query = createInsertQueryOrder(field1,field2,field3,field4);
//        }
//        if(t.getClass().getSimpleName().equals("Product"))
//        {
//            System.out.println("am ajuns aici 3");
//            int field1, field4, field3;
//            String field2;
//            field1 = (int)list.get(0);
//            field2 = (String) list.get(1);
//            field3 = (int) list.get(2);
//            field4 = (int)list.get(3);
//            query = createInsertQueryProduct(field1,field2,field3,field4);
//        }


        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.executeUpdate();

            return t;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * updateaza in tabela elementul cu id-ul primit ca parametru 1, schimbandu-i valorile in cele ale elementului primit ca parametru 2
     * @param id
     * @param t
     * @return elementul updatat in caz de succes, null in caz de esec
     */
    public T update(int id,T t) {
        // TODO:
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Object> list = new ArrayList<Object>();
        List<String> listaCol = new ArrayList<String>();

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(t);
                listaCol.add((field.getName()));
                list.add(value);
                //System.out.println(field.getName() + "=" + value);


            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String query = "empti";
        String field2,field3, field4;
        String col1,col2,col3;

        //field1 = list.get(0).toString();
        field2 = list.get(1).toString();
        field3 = list.get(2).toString();
        field4 = list.get(3).toString();

        col1 = listaCol.get(1);
        col2 = listaCol.get(2);
        col3 = listaCol.get(3);

        query = createUpdateQuery(col1,col2,col3,field2,field3,field4,id);

//        if(t.getClass().getSimpleName().equals("Client"))
//        {
//            System.out.println("am ajuns aici");
//            int field1, field4;
//            String field2,field3;
//            field1 = (int)list.get(0);
//            field2 = (String) list.get(1);
//            field3 = (String) list.get(2);
//            field4 = (int)list.get(3);
//            query = createUpdateQueryClient(field2,field3,field4,id);
//        }
//        if(t.getClass().getSimpleName().equals("Comanda"))
//        {
//            System.out.println("am ajuns aici 2");
//            int field1, field4;
//            int field2,field3;
//            field1 = (int)list.get(0);
//            field2 = (int)list.get(1);
//            field3 = (int)list.get(2);
//            field4 = (int)list.get(3);
//            query = createUpdateQueryOrder(field2,field3,field4,id);
//        }
//        if(t.getClass().getSimpleName().equals("Product"))
//        {
//            System.out.println("am ajuns aici 3");
//            int field1, field4, field3;
//            String field2;
//            field1 = (int)list.get(0);
//            field2 = (String) list.get(1);
//            field3 = (int) list.get(2);
//            field4 = (int)list.get(3);
//            query = createUpdateQueryProduct(field2,field3,field4,id);
//        }


        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            statement.executeUpdate();

            return t;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * transforma lista de elemente din parametru intr-o matrice de stringuri pentru a-i putea insera datele in interfata
     * @param elemente
     * @return matricea formata
     */
    public String[][] formMatrix(List<T> elemente)
    {
        String[][] x = new String[1000][4];
        int i = 0;
        int j = 0;
        for (Field field : elemente.get(0).getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                x[i][j] = field.getName();
                j++;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } /*catch (IllegalAccessException e) {
                e.printStackTrace();
            } */
        }

        i = 1;
        for(T t : elemente)
        {
            j = 0;
            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(t);
                    //list.add(value);
                    //System.out.println(field.getName() + "=" + value);
                    x[i][j] = value.toString();
                    j++;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            i++;

        }
        return x;
    }


}
