/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author jonathan.mileham
 */
@WebServlet(urlPatterns = {"/fileProcessor"})
@MultipartConfig
public class fileProcessor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        getFiles(request,response);
    }
    
    private void getFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{ 
        String line;
        PrintWriter out = response.getWriter();

            for (Part part : request.getParts()) {
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(part.getInputStream()));
                
                while ((line=bufReader.readLine())!=null) {
                    
                    out.print(line+"\n");
                    
                }
                
                bufReader.close();
            }
        
       
        
    }
    
    private int check_GP(){
        return 0;
    }


}
