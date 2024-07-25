/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Priya
 */
public class Login extends HttpServlet {
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
        PrintWriter out = response.getWriter();
        
        try{
            String name = request.getParameter("username");
            String password = request.getParameter("password");
            
            boolean valid = validateUser(name, password);
            
            if(valid){
                HttpSession session = request.getSession(true);
                session.setAttribute("username", name);
                
                Cookie cookie = new Cookie("SESSIONid", session.getId());
                response.addCookie(cookie);
                
                response.sendRedirect("welcome.html?sessionid="+session.getId());
            }else{
                out.write("Error! Invalid User!");
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            out.close();
        }
    }
    
    private boolean validateUser(String username, String password) throws SQLException{
        boolean status = false;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        String DB_URL = "jdbc:mysql://localhost:3306/bca-sem-5b";
        Connection conn = DriverManager.getConnection(DB_URL, "root", "");
        
        String SQL = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(SQL);
        
        stmt.setString(1, username);
        stmt.setString(2, password);
        
        ResultSet rs = stmt.executeQuery();
        status = rs.next();
        
        rs.close();
        stmt.close();
        conn.close();
        
        return status;
    }
    
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
