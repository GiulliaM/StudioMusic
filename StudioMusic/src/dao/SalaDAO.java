package dao;

import model.Sala;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SalaDAO {


    public boolean inserirSala(Sala sala) {
        String sql = "INSERT INTO SALAS (nome_tipo, numero_sala, capacidade, equipamentos, valor_hora) VALUES (?, ?, ?, ?, ?)"; // MODIFICADO
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, sala.getNomeTipo()); 
            stmt.setInt(2, sala.getNumeroSala());   
            stmt.setInt(3, sala.getCapacidade());
            stmt.setString(4, sala.getEquipamentos());
            stmt.setBigDecimal(5, sala.getValorHora());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        sala.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir sala: " + e.getMessage());
        }
        return false;
    }


    public Sala buscarSalaPorId(int id) {
        String sql = "SELECT * FROM SALAS WHERE id_sala = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Sala(
                            rs.getInt("id_sala"),
                            rs.getString("nome_tipo"), 
                            rs.getInt("numero_sala"),   
                            rs.getInt("capacidade"),
                            rs.getString("equipamentos"),
                            rs.getBigDecimal("valor_hora")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar sala por ID: " + e.getMessage());
        }
        return null;
    }


    public List<Sala> buscarTodasSalas() {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM SALAS";
        try (Connection conn = ConexaoBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salas.add(new Sala(
                        rs.getInt("id_sala"),
                        rs.getString("nome_tipo"), 
                        rs.getInt("numero_sala"),   
                        rs.getInt("capacidade"),
                        rs.getString("equipamentos"),
                        rs.getBigDecimal("valor_hora")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as salas: " + e.getMessage());
        }
        return salas;
    }


    public List<Sala> buscarSalasPorTipo(String nomeTipo) {
        List<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM SALAS WHERE nome_tipo = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeTipo);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    salas.add(new Sala(
                            rs.getInt("id_sala"),
                            rs.getString("nome_tipo"),
                            rs.getInt("numero_sala"),
                            rs.getInt("capacidade"),
                            rs.getString("equipamentos"),
                            rs.getBigDecimal("valor_hora")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar salas por tipo: " + e.getMessage());
        }
        return salas;
    }


    public boolean atualizarSala(Sala sala) {
        String sql = "UPDATE SALAS SET nome_tipo = ?, numero_sala = ?, capacidade = ?, equipamentos = ?, valor_hora = ? WHERE id_sala = ?"; // MODIFICADO
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sala.getNomeTipo()); 
            stmt.setInt(2, sala.getNumeroSala());   
            stmt.setInt(3, sala.getCapacidade());
            stmt.setString(4, sala.getEquipamentos());
            stmt.setBigDecimal(5, sala.getValorHora());
            stmt.setInt(6, sala.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar sala: " + e.getMessage());
        }
        return false;
    }


    public boolean excluirSala(int id) {
        String sql = "DELETE FROM SALAS WHERE id_sala = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir sala: " + e.getMessage());
        }
        return false;
    }
}