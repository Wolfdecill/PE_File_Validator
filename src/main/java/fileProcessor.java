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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jonathan.mileham
 */
@WebServlet(urlPatterns = {"/fileProcessor"})
@MultipartConfig
public class fileProcessor extends HttpServlet {

    //Values to check
    private String delimiter=",";
    private int lineNumOfColumn=1;
    private String columnName="Description";
        //Value is gotten from: getPositionOfSearch
        private int positionOfColumn=-1;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processFile(request, response);
        } catch (IOException ex) {
            System.out.println("Error with getting the response");
        }
    }

    private void processFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        try {
            delimiter= request.getParameter("delimiter");
            out.print("Delimiter used: "+delimiter+"\n");
            
            
            for (Part part : request.getParts()) {
                
                if(part.getSubmittedFileName()!=null){
                    //File Name
                    out.print("\n"+part.getSubmittedFileName()+"\n");
            
                    //Read all the lines of the file and convirt to arraylist
                    BufferedReader buf= new BufferedReader(new InputStreamReader(part.getInputStream()));
                    ArrayList<String> convertedBuffer= convertBufferedReaderToArrayList(buf);

                    //Create a Map with all the keys being the different types of checks
                    getPositionOfSearch(convertedBuffer.get(lineNumOfColumn-1));
                    if(positionOfColumn!=-1){
                        Map<String, ArrayList<String>> map=createMap(convertedBuffer);
                        soutMap(map, response);

                    }else{out.print("positionOfColumn is "+positionOfColumn);}
                    
                }
            }    
            
        } 
        catch (IOException e) {out.print("Error occured while trying to read file \n"+e.toString()+"\n");} 
        catch (ServletException ex) {out.print("Error occured while trying to get file \n"+ex.toString()+"\n");}
    }

    private ArrayList<String> convertBufferedReaderToArrayList(BufferedReader reader) throws IOException{
        String line;
        ArrayList<String> lines=new ArrayList<>();
            while ( (line=reader.readLine())!=null){
                lines.add(line);
            }
            return lines;
        }
    
    private void getPositionOfSearch(String columnNames){
        String[] split=columnNames.split(delimiter);
        
        for (int count = 0; count < split.length; count++) {
            Boolean found= split[count].equalsIgnoreCase(columnName);
            if(found){
                positionOfColumn=count;
            }
        }
    }
    
    private Map<String, ArrayList<String>> createMap(ArrayList<String> lines){
        Map<String, ArrayList<String>> map = new HashMap<>();
        String key;
          
        for (String line : lines) {
            String[] split=line.split(delimiter);
            if (split.length>=positionOfColumn+1){
                key=split[positionOfColumn];
                if(!key.equalsIgnoreCase(columnName)){
                    ArrayList<String>list= map.get(key);
                        if (list==null){
                            list= new ArrayList<>();
                        }
                    list.add(line);
                    map.put(key, list);
                }
            }
        }
            
        return map;
    }
    
    private void soutMap(Map<String,ArrayList<String>> map,HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        
        List<String> keys = new ArrayList<String>(map.keySet());
        
        for (String key : keys) {
            ArrayList<String> values=map.get(key);
            out.print(key+": "+ values.size() +"\n");
        }
        
    }
}
