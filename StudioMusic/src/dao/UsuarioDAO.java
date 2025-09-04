package dao;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario autenticar(String username, String password) {
        String sql = "SELECT id_usuario, username, password, tipo_usuario, id_cliente_fk FROM USUARIOS WHERE username = ? AND password = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer idClienteFk = (Integer) rs.getObject("id_cliente_fk");
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("tipo_usuario"),
                        idClienteFk
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
        }
        return null;
    }

    public boolean inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO USUARIOS (username, password, tipo_usuario, id_cliente_fk) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPassword());
            stmt.setString(3, usuario.getTipoUsuario());
            if (usuario.getIdClienteFk() != null) {
                stmt.setInt(4, usuario.getIdClienteFk());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
        }
        return false;
    }

    public Usuario buscarUsuarioPorUsername(String username) {
        String sql = "SELECT id_usuario, username, password, tipo_usuario, id_cliente_fk FROM USUARIOS WHERE username = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer idClienteFk = (Integer) rs.getObject("id_cliente_fk");
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("tipo_usuario"),
                        idClienteFk
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por username: " + e.getMessage());
        }
        return null;
    }

    public Usuario buscarUsuarioPorId(int id) {
        String sql = "SELECT id_usuario, username, password, tipo_usuario, id_cliente_fk FROM USUARIOS WHERE id_usuario = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer idClienteFk = (Integer) rs.getObject("id_cliente_fk");
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("tipo_usuario"),
                        idClienteFk
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizarSenhaUsuario(int idUsuario, String novaSenha) {
        String sql = "UPDATE USUARIOS SET password = ? WHERE id_usuario = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaSenha);
            stmt.setInt(2, idUsuario);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; 
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar senha do usuário: " + e.getMessage());
        }
        return false;
    }

    public List<Usuario> buscarTodosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, username, password, tipo_usuario, id_cliente_fk FROM USUARIOS";
        try (Connection conn = ConexaoBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Integer idClienteFk = (Integer) rs.getObject("id_cliente_fk");
                usuarios.add(new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("tipo_usuario"),
                    idClienteFk
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os usuários: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario buscarUsuarioPorIdClienteFk(int idClienteFk) {
        String sql = "SELECT id_usuario, username, password, tipo_usuario, id_cliente_fk FROM USUARIOS WHERE id_cliente_fk = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idClienteFk);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer fk = (Integer) rs.getObject("id_cliente_fk");
                    return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("tipo_usuario"),
                        fk
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por id_cliente_fk: " + e.getMessage());
        }
        return null;
    }
}
