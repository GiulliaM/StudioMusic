package dao;

import model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ClienteDAO {


    public boolean inserirCliente(Cliente cliente) {
        String sql = "INSERT INTO CLIENTES (nome, cpf_cnpj, telefone, email, ativo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpfCnpj());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setBoolean(5, cliente.isAtivo()); 

            int linhasAfetadas = stmt.executeUpdate(); 
            if (linhasAfetadas > 0) { 
                try (ResultSet rs = stmt.getGeneratedKeys()) { 
                    if (rs.next()) { 
                        cliente.setId(rs.getInt(1)); 
                    }
                }
                return true; 
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
        }
        return false; 
    }


    public Cliente buscarClientePorId(int id) {
        String sql = "SELECT * FROM CLIENTES WHERE id_cliente = ? AND ativo = 1";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); 
            try (ResultSet rs = stmt.executeQuery()) { 
                if (rs.next()) { 
                    return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nome"),
                            rs.getString("cpf_cnpj"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getBoolean("ativo") 
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por ID: " + e.getMessage());
        }
        return null; 
    }


    public List<Cliente> buscarClientesPorStatus(boolean ativo) { 
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTES WHERE ativo = ?"; 
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, ativo); 
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("nome"),
                            rs.getString("cpf_cnpj"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getBoolean("ativo")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar clientes por status: " + e.getMessage());
        }
        return clientes;
    }


    public List<Cliente> buscarClientesAtivos() { 
        return buscarClientesPorStatus(true); 
    }


    public boolean atualizarCliente(Cliente cliente) {
        String sql = "UPDATE CLIENTES SET nome = ?, cpf_cnpj = ?, telefone = ?, email = ?, ativo = ? WHERE id_cliente = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpfCnpj());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setBoolean(5, cliente.isAtivo()); 
            stmt.setInt(6, cliente.getId()); 

            int linhasAfetadas = stmt.executeUpdate(); 
            return linhasAfetadas > 0; 
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
        }
        return false; 
    }


    public boolean desativarCliente(int id) {
        String sql = "UPDATE CLIENTES SET ativo = 0 WHERE id_cliente = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao desativar cliente: " + e.getMessage());
        }
        return false;
    }


}
