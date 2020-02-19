package view;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Shariar (Shawn) Emami
 */
@WebServlet(name = "ImageDelivery", urlPatterns = {"/image/*"})
public class ImageDelivery extends HttpServlet {

    private final String imagePath = System.getProperty("user.home")+"/KijijiImages/";


    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ServletContext cntx= request.getServletContext();
        // Get the absolute path of the image
        URI uri = URI.create(request.getRequestURL().toString());
        Path path = Paths.get(uri.getPath());
        String itemId = path.getFileName().toString();
        String fileName = imagePath + itemId;
        // retrieve mimeType dynamically
        String mime = cntx.getMimeType(fileName);
        if (mime != null) {
            response.setContentType(mime);
            File file = new File(fileName);
            response.setContentLength((int)file.length());

            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
            in.close();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Image Delivery";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }
}
