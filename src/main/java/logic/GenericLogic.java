package logic;

import dal.AccountDAL;
import dal.GenericDAL;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author Shariar (Shawn) Emami
 * @param <E> - entity type
 * @param <T> - DAO type
 */
public abstract class GenericLogic< E, T extends GenericDAL<E>>{
    
    private final T DAO;

    GenericLogic(T dao) {
        this.DAO =  dao;
    }
    
    protected final T dao(){
        return DAO;
    }
    
    protected <R> R get( Supplier<R> supplier){
        R r;
        DAO.beginTransaction();
        r = supplier.get();
        DAO.closeTransaction();
        return r;
    }
    
    public void add( E entity){
        DAO.beginTransaction();
        DAO.save(entity);
        DAO.commitAndCloseTransaction();
    }
    
    public void delete( E entity){
        DAO.beginTransaction();
        DAO.delete(entity);
        DAO.commitAndCloseTransaction();
    }
    
    public void detach( E entity){
        DAO.beginTransaction();
        DAO.detach(entity);
        DAO.commitAndCloseTransaction();
    }
    
    public E update( E entity){
        DAO.beginTransaction();
        E e = DAO.update(entity);
        DAO.commitAndCloseTransaction();
        return e;
    }
    
    /**
     * this method is used to send a list of all names to be used form table
     * column headers. by having all names in one location there is less chance of mistakes.
     * 
     * this list must be in the same order as getColumnCodes and extractDataAsList
     * 
     * @return list of all column display names.
     */
    abstract public List<String> getColumnNames();
    
    /**
     * this method returns a list of column names that match the official column
     * names in the db. by having all names in one location there is less chance of mistakes.
     * 
     * this list must be in the same order as getColumnNames and extractDataAsList
     * 
     * @return list of all column names in DB.
     */
    abstract public List<String> getColumnCodes();
    
    /**
     * return the list of values of all columns (variables) in given entity.
     * 
     * this list must be in the same order as getColumnNames and getColumnCodes
     * 
     * @param e - given Entity to extract data from.
     * @return list of extracted values
     */
    abstract public List<?> extractDataAsList( E e);
    
    abstract public E createEntity(Map<String, String[]> requestData);
    
    abstract public List<E> getAll();
    
    abstract public E getWithId( int id);
    
    public List<E> search( String search){
        throw new UnsupportedOperationException("Method: search( String) not implemented");
    }

    protected String validateInput(boolean nullable, String name, String value, int length) {
        if (value == null && !nullable) {
            throw new IllegalArgumentException(name + " can not be null");
        }
        if (value != null && value.length() > length) {
            throw new IllegalArgumentException(name + " exceeds limit " + length);
        }
        return value;
    }
}
