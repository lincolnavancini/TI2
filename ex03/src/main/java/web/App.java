package web;

import static spark.Spark.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.Usuario;
import dao.UsuarioDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");
        
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        });
        
        System.out.println("🚀 Servidor Spark iniciado em: http://localhost:4567");

        get("/", (req, res) -> {
            res.redirect("/formulario.html");
            return null;
        });

        get("/api/usuarios", (req, res) -> {
            res.type("application/json");
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            return mapper.writeValueAsString(usuarios);
        });

        get("/api/usuarios/:id", (req, res) -> {
            res.type("application/json");
            int id = Integer.parseInt(req.params(":id"));
            Usuario usuario = usuarioDAO.buscarPorId(id);
            
            if (usuario != null) {
                return mapper.writeValueAsString(usuario);
            } else {
                res.status(404);
                return "{\"error\": \"Usuário não encontrado\"}";
            }
        });

        post("/api/usuarios", (req, res) -> {
            res.type("application/json");
            try {
                System.out.println("📥 Recebendo POST - Query: " + req.queryString());
                
                String nome = req.queryParams("nome");
                String email = req.queryParams("email");
                String idadeStr = req.queryParams("idade");
                
                System.out.println("📋 Dados recebidos - Nome: " + nome + ", Email: " + email + ", Idade: " + idadeStr);
                
                if (nome == null || nome.trim().isEmpty()) {
                    res.status(400);
                    return "{\"error\": \"Nome é obrigatório\"}";
                }
                if (email == null || email.trim().isEmpty()) {
                    res.status(400);
                    return "{\"error\": \"Email é obrigatório\"}";
                }
                if (idadeStr == null || idadeStr.trim().isEmpty()) {
                    res.status(400);
                    return "{\"error\": \"Idade é obrigatória\"}";
                }
                
                int idade = Integer.parseInt(idadeStr.trim());
                Usuario usuario = new Usuario(nome.trim(), email.trim(), idade);
                int idGerado = usuarioDAO.inserir(usuario);
                
                if (idGerado != -1) {
                    return "{\"success\": true, \"id\": " + idGerado + ", \"message\": \"Usuário criado com sucesso!\"}";
                } else {
                    res.status(400);
                    return "{\"error\": \"Email já está em uso. Por favor, use outro email.\"}";
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"error\": \"Idade deve ser um número válido\"}";
            } catch (Exception e) {
                res.status(400);
                return "{\"error\": \"Erro no servidor\"}";
            }
        });

        put("/api/usuarios/:id", (req, res) -> {
            res.type("application/json");
            try {
                int id = Integer.parseInt(req.params(":id"));
                
                String nome = req.queryParams("nome");
                String email = req.queryParams("email");
                String idadeStr = req.queryParams("idade");
                
                System.out.println("📥 Recebendo PUT - ID: " + id + ", Nome: " + nome + ", Email: " + email + ", Idade: " + idadeStr);
                
                if (nome == null || nome.trim().isEmpty()) {
                    res.status(400);
                    return "{\"error\": \"Nome é obrigatório\"}";
                }
                if (email == null || email.trim().isEmpty()) {
                    res.status(400);
                    return "{\"error\": \"Email é obrigatório\"}";
                }
                if (idadeStr == null || idadeStr.trim().isEmpty()) {
                    res.status(400);
                    return "{\"error\": \"Idade é obrigatória\"}";
                }
                
                int idade = Integer.parseInt(idadeStr.trim());
                Usuario usuario = new Usuario(nome.trim(), email.trim(), idade);
                usuario.setId(id);
                
                boolean atualizado = usuarioDAO.atualizar(usuario);
                
                if (atualizado) {
                    return "{\"success\": true, \"message\": \"Usuário atualizado com sucesso!\"}";
                } else {
                    res.status(404);
                    return "{\"error\": \"Usuário não encontrado\"}";
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"error\": \"Idade deve ser um número válido\"}";
            } catch (Exception e) {
                res.status(400);
                return "{\"error\": \"Erro no servidor\"}";
            }
        });

        delete("/api/usuarios/:id", (req, res) -> {
            res.type("application/json");
            try {
                int id = Integer.parseInt(req.params(":id"));
                boolean excluido = usuarioDAO.excluir(id);
                
                if (excluido) {
                    return "{\"success\": true, \"message\": \"Usuário excluído com sucesso!\"}";
                } else {
                    res.status(404);
                    return "{\"error\": \"Usuário não encontrado\"}";
                }
            } catch (Exception e) {
                res.status(400);
                return "{\"error\": \"ID inválido\"}";
            }
        });

        notFound((req, res) -> {
            res.type("application/json");
            return "{\"error\": \"Endpoint não encontrado\"}";
        });
    }
}