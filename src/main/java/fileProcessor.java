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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathan.mileham
 */
@WebServlet(urlPatterns = {"/fileProcessor"})
@MultipartConfig
public class fileProcessor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        
        try {
            getFiles(request,response);
        } catch (IOException ex) {
            System.out.println("Error accessing the response");
        }
    }
    
    private void getFiles(HttpServletRequest request, HttpServletResponse response) throws IOException{      
        String delimiter;
        BufferedReader bufReader = null;
        PrintWriter out = response.getWriter();
        int gp,sp,dental,optom,path,aux,radio;
        
        try {     
            delimiter= request.getParameter("delimiter");
            out.print("Delimiter used: "+delimiter+"\n");
            //Each "part" is a different file
            for (Part part : request.getParts()) {
                
                if(part.getSubmittedFileName()!=null){
                    //File Name
                    out.print("\n"+part.getSubmittedFileName()+"\n");
                
                    //Convert "part" to someting readable
                    bufReader = new BufferedReader(new InputStreamReader(part.getInputStream()));
                    ArrayList<String> convertedBuffer= convertBufferedReaderToArrayList(bufReader);
                    
                    //Get how many occurances of a search their is
                    gp= searchResults(convertedBuffer,delimiter,"GP");
                    sp= searchResults(convertedBuffer,delimiter,"Specialists");
                    dental= searchResults(convertedBuffer,delimiter,"Dental");
                    optom= searchResults(convertedBuffer,delimiter,"Optom");
                    path= searchResults(convertedBuffer,delimiter,"Pathology");
                    aux= searchResults(convertedBuffer,delimiter,"Auxiliary");
                    radio= searchResults(convertedBuffer,delimiter,"Radiology");

                    out.print("GP:"+gp+" SP:"+sp+" Dental:"+dental+" Optom:"+optom+" Path:"+path+" Aux:"+aux+" Radio:"+radio+"\n");
                }
            }    
        }
        catch (IOException e) {out.print("Error occured while trying to read file \n");} 
        catch (ServletException ex) {out.print("Error occured while trying to get file \n");}
            
            
        
    }
    
    private int searchResults(ArrayList<String> convertedBuffer ,String delimiter, String search){
        int count=0;
        int loop=0;
        String line; 
        //Change this to change what it is searching for
        String contains= delimiter+search;
        
        while(loop<=convertedBuffer.size()-1){
            if(convertedBuffer.get(loop).contains(contains)){
                count++;
            }
            loop++;
        }
        return count;
    }

    private ArrayList<String> convertBufferedReaderToArrayList(BufferedReader reader) throws IOException{
        String line;
        ArrayList<String> lines=new ArrayList<>();
            while ( (line=reader.readLine())!=null){
                lines.add(line);
            }
            return lines;
        }

}
