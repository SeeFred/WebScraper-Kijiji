package view;

import common.FileUtility;
import entity.Category;
import entity.Image;
import entity.Item;
import logic.AccountLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.Account;
import logic.CategoryLogic;
import logic.ImageLogic;
import logic.ItemLogic;
import org.apache.tomcat.util.buf.StringUtils;
import scraper.kijiji.Kijiji;
import scraper.kijiji.KijijiItem;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.annotation.WebServlet;

import static logic.CategoryLogic.ID;
import static logic.ImageLogic.NAME;
import static logic.ImageLogic.PATH;
import static logic.ItemLogic.*;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@WebServlet(name = "Kijiji", urlPatterns = {"/Kijiji"})
public class KijijiView extends HttpServlet {

    private Kijiji kijiji = new Kijiji();
    private CategoryLogic categoryLogic = new CategoryLogic();
    private ItemLogic itemLogic = new ItemLogic();
    private ImageLogic imageLogic = new ImageLogic();

    private final String imagePath = System.getProperty("user.home")+"/KijijiImages/";


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>KijijiView</title>");
            out.println("<link rel=\"stylesheet\" href=\"style/KijijiStyle.css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("<form method=\"post\">");
            out.println("<div class=\"center-column\">");
            List<Item> entities = itemLogic.getAll();
            long counter = 0;
            for (Item entity : entities) {
                out.println("<div class=\"item\">");
                out.println("<div class=\"image\">");
                Object image;
                out.printf("<img src=\"image/%s.jpg\" style=\"max-width: 250px; max-height: 200px;\" />", entity.getId());
                out.println("</div>");
                out.printf("<div class=\"details\">");
                out.println("<div class=\"title\">");
                out.printf("<a href=\">%s\" target=\"_blank\">%s</a>", entity.getUrl(), entity.getTitle());
                out.println("</div>");
                out.printf("<div class=\"price\">%s</div>", entity.getPrice());
                out.printf("<div class=\"date\">%s</div>", entity.getDate());
                out.printf("<div class=\"location\">%s</div>", entity.getLocation());
                out.printf("<div class=\"description\">%s</div>", entity.getDescription());
                out.println("</div>");
                out.println("</div>");
            }
            out.println("</div>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        FileUtility.createDirectory(imagePath);

        List<Category> categoryList = categoryLogic.getAll();
//        for(Category category:categoryList){
            kijiji.downloadPage(categoryList.get(0).getUrl());
            kijiji.findAllItems();
            kijiji.proccessItems(item -> {
                Item entity = itemLogic.getWithUrl(item.getUrl());
                if (entity == null) {
                    entity = itemLogic.createEntity(buildMapFromKijijiItem(item, categoryList.get(0)));

                    itemLogic.add(entity);
                }

            });

        processRequest(request, response);
    }

    private Map<String, String[]> buildMapFromKijijiItem(KijijiItem item, Category category) {
        Map<String, String[]> result = new HashMap<>();

        result.put(ID, new String[]{item.getId()});
        result.put(DESCRIPTION, new String[]{item.getDescription()});
        result.put(URL, new String[]{item.getUrl()});
        result.put(PRICE, new String[]{item.getPrice()});
        result.put(TITLE, new String[]{item.getTitle()});
        result.put(LOCATION, new String[]{item.getLocation()});
        result.put(DATE,new String[]{item.getDate()});

        FileUtility.downloadAndSaveFile(item.getImageUrl(), imagePath, item.getId() + ".jpg");
        Map<String, String[]> imageMap = new HashMap<>();
        imageMap.put(PATH, new String[]{imagePath + item.getId() + ".jpg"});
        imageMap.put(NAME, new String[]{item.getImageName()});
        imageMap.put(URL, new String[]{item.getImageUrl()});

        Image image = imageLogic.createEntity(imageMap);
        imageLogic.add(image);
        result.put(IMAGE_ID, new String[]{image.getId().toString()});

        result.put(CATEGORY_ID, new String[]{category.getId().toString()});

        return result;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        processRequest(request, response);
    }


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Sample of Account View Fancy";
    }

    private static final boolean DEBUG = true;

    public void log( String msg) {
        if(DEBUG){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log( message);
        }
    }

    public void log( String msg, Throwable t) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log( message, t);
    }
}

