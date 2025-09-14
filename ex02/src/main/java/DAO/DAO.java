package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {
    private Connection connection;

    public DAO() {
        conectar();
    }

    private void conectar() {
        try {
            String url = "jdbc:postgresql://localhost:5432/teste";
            String usuario = "postgres";
            String senha = "1234";

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, usuario, senha);
        } catch (Exception e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }

    public int inserir(Usuario usuario) {
        String sql = "INSERT INTO tabela_ex02 (nome, email, idade) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getIdade());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("✅ INSERT executado - ID gerado: " + idGerado);
                return idGerado;
            }
            return -1;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir: " + e.getMessage());
            return -1;
        }
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM tabela_ex02 ORDER BY id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setIdade(rs.getInt("idade"));
                lista.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE tabela_ex02 SET nome = ?, email = ?, idade = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setInt(3, usuario.getIdade());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM tabela_ex02 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir: " + e.getMessage());
            return false;
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM tabela_ex02 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setIdade(rs.getInt("idade"));
                return usuario;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar: " + e.getMessage());
        }
        return null;
    }

    public void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}