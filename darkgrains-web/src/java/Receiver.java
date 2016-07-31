/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Luís
 */
public class Receiver extends HttpServlet {
    private Connection c;
    
    @Override
    public void init(ServletConfig config) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/darkgrains", "root", "");
        } catch ( ClassNotFoundException | SQLException e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        
//        String path = Receiver.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String decodedPath = URLDecoder.decode(path, "UTF-8");
//        System.out.println("LOLO" + decodedPath);
        
        
        
        
        if(request.getParameter("action") == null) {
            response.setStatus(400);
            return;
        }
            
        PrintWriter out = response.getWriter();
        response.setStatus(200);
        switch(request.getParameter("action")) {
            /*
            *   Registra o user de acordo com os parametros: n (nome), pw (senha), em (email)
            *   Retorna 200 se criado com sucesso, ou 422 com mensagem user-friendly caso falhe
            */
            case "regusr":
                try {
                    Response r = registerUserAction(request.getParameter("n"), request.getParameter("pw"), request.getParameter("em"));
                    out.print(r.toJSON());
                } catch(Exception e) {
                    response.setStatus(400);
                }
                break;
                
            case "recust":
                try {
                    Response r = registerUserAction(request.getParameter("n"), request.getParameter("pw"), request.getParameter("em"));
                    out.print(r.toJSON());
                } catch(Exception e) {
                    response.setStatus(400);
                }
                break;
            /*
            *   Efetua o login do usuário no sistema e retorna o hash da sessão.
            *   Retorna o hash já existente se já logado
            */
            case "logusr":
                try {
                    Response r = loginUserAction(request.getParameter("n"), request.getParameter("pw"));
                    out.print(r.toJSON());
                } catch(Exception e) {
                    Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, e);
                    response.setStatus(400);
                }
                break;
            /*
            *   Diz ao servidor que ainda está conectado e não destrói a sessão.    
            */
            case "upd":
                try {
                    Response r = updateClientAction(request.getParameter("h"));
                    out.print(r.toJSON());
                } catch(Exception e) {
                    Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, e);
                    response.setStatus(400);
                }
                break;
            /*
            *   Diz ao servidor que ainda está conectado e não destrói a sessão.    
            */
            case "end":
                try {
                    Response r = endClientAction(request.getParameter("h"));
                    out.print(r.toJSON());
                } catch(Exception e) {
                    Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, e);
                    response.setStatus(400);
                }
                break;
            /*
            *   Obtém a lista de mundos pertencentes á usuário.
            */
            case "gtwd":
                try {
                    Response r = getWorldListAction(request.getParameter("h"));
                    out.print(r.toJSON());
                } catch(Exception e) {
                    Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, e);
                    response.setStatus(400);
                }
                break;
                
            case "delwd": {
                try {
                    //System.out.println(request.getParameter("h") + " - " + request.getParameter("w"));
                    
                    int w = Integer.parseInt(request.getParameter("w"));
                    Response r = deleteWorldAction(request.getParameter("h"), w);
                    out.print(r.toJSON());
                } catch(Exception e) {
                    Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, e);
                    response.setStatus(400);
                }
            }
            
            default:
                response.setStatus(422);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        //processRequest(request, response);
        response.setStatus(403);
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
        processRequest(request, response);
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
    
    private boolean userExists(String username) {
        try {
            PreparedStatement stmt = c.prepareStatement("select * from player where name like ?");
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Query error");
        }
    }
    
    private boolean emailExists(String email) {
        try {
            PreparedStatement stmt = c.prepareStatement("select * from player where email like ?");
            stmt.setString(1, email);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Query error");
        }
    }

    private Response registerUserAction(String name, String password, String email) {
        if(name.length() < 5) {
            return new Response(false, "O nome precisa conter, no mínimo, 5 caracteres.");
        }
        if(name.length() > 45) {
            return new Response(false, "O nome pode conter, no no máximo, 45 caracteres.");
        }
        if(password.length() < 4) {
            return new Response(false, "A senha precisa conter, no mínimo, 4 caracteres.");
        }
        if(password.length() > 45) {
            return new Response(false, "A senha pode conter, no no máximo, 45 caracteres.");
        }
        if(!email.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
            return new Response(false, "O endereço de email não é válido.");
        }
        if(userExists(name)) {
            return new Response(false, "Usuário já registrado.");
        }
        if (emailExists(email)) {
            return new Response(false, "Email já registrado.");
        }
        
        try {
            PreparedStatement stmt = c.prepareStatement("insert into player values (NULL,?,?,?)");
            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            
            return new Response(true, "");
        } catch (SQLException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            return new Response(false, "Houve um erro inesperado.");
        }
    }
    
    private Response loginUserAction(String name, String password) {
        
        try {
            PreparedStatement stmt = c.prepareStatement("select * from player where name like ? and password=?");
            stmt.setString(1, name);
            stmt.setString(2, password);
            
            
            ResultSet rs = stmt.executeQuery();
            boolean success = rs.next();
            
            if(success) {
                return new Response(true, Session.registerSession(name, rs.getInt("id")).getHash());
            } else {
                return new Response(false, "Não foi possível fazer o login. Confira as informações tente novamente.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Query error");
        }
    }
    
    private Response updateClientAction(String hash) {
        return Session.validate(hash) == null ? new Response(false, "") : new Response(true, "");
    }
    
    private Response endClientAction(String hash) {
        if(Session.validate(hash) != null) {
            Session.end(hash);
        }
        return  new Response(true, "");
    }
    
    private Response getWorldListAction(String hash) {
        Session s = Session.validate(hash);
        if(s == null) return new Response(false, "");
        
        JSONArray list = new JSONArray();
        int userId = s.getUserId();
        
        try {
            PreparedStatement st = c.prepareStatement("select * from world where player_id = ? limit 5");
            st.setInt(1, userId);
            ResultSet r = st.executeQuery();
            
            while(r.next()) {
                JSONObject o = new JSONObject();
                o.put("id", r.getInt("id"));
                o.put("name", r.getString("name"));
                o.put("crimerate", r.getDouble("crimerate"));
                o.put("created", r.getDate("created").getTime());
                o.put("difficulty", r.getInt("difficulty"));
                list.add(o);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return new Response(true, list.toJSONString());
    }

    private Response deleteWorldAction(String hash, int world) {
        Session s = Session.validate(hash);
        if(s == null) return new Response(false, "");
        try {
            PreparedStatement st = c.prepareStatement("delete from world where id = ?");
            st.setInt(1, world);
            st.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        return new Response(true, "");
    }
}
