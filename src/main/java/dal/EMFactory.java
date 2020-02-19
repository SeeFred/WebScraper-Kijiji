package dal;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * do not modify this class
 * 
 * lazy initialize EntityManagerFactory when the first servlet is created.
 * do not create again.
 * 
 * @see https://www.deadcoderising.com/execute-code-on-webapp-startup-and-shutdown-using-servletcontextlistener/
 * @see https://docs.oracle.com/javaee/7/api/javax/servlet/ServletContextListener.html
 * 
 * @author Shariar (Shawn) Emami
 */
@WebListener
public class EMFactory implements ServletContextListener{
    
    private static EntityManagerFactory emFactory;
    
    /**
     * this method is triggered when the web application is starting the
     * initialization. This will be invoked before any of the filters
     * and servlets are initialized.
     * @param sce 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce){
        Logger.getLogger( getClass().getName()).log(Level.INFO, "contextInitialized");
        if(emFactory==null){
            Logger.getLogger( getClass().getName()).log(Level.INFO, "contextInitialized: creating EntityManagerFactory");
            emFactory = Persistence.createEntityManagerFactory("JPA-Tomcat-WebScraper");
        }
    }
 
    /**
     * this method is triggered when the ServletContext is about to be 
     * destroyed. This will be invoked after all the servlets and filters 
     * have been destroyed.
     * @param sce 
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        Logger.getLogger( getClass().getName()).log(Level.INFO, "contextDestroyed");
        if(emFactory!=null){
            Logger.getLogger( getClass().getName()).log(Level.INFO, "contextDestroyed: closing EntityManagerFactory");
            emFactory.close();
        }
    }
    
    public static EntityManagerFactory getEMFactory(){
        Objects.requireNonNull(emFactory, "Entity Manager Factory not initilized");
        Logger.getLogger( EMFactory.class.getName()).log(Level.INFO, "getEMFactory");
        return emFactory;
    }
}
