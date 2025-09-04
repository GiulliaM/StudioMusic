package dao;

import model.Agendamento;
import model.Sala;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {


    public boolean inserirAgendamento(Agendamento agendamento) {
        String sql = "INSERT INTO AGENDAMENTOS (id_cliente, id_sala, data_agendamento, hora_inicio, hora_fim, status, valor_total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, agendamento.getIdCliente());
            stmt.setInt(2, agendamento.getIdSala());
            stmt.setDate(3, java.sql.Date.valueOf(agendamento.getDataAgendamento()));
            stmt.setTime(4, java.sql.Time.valueOf(agendamento.getHoraInicio()));
            stmt.setTime(5, java.sql.Time.valueOf(agendamento.getHoraFim()));
            stmt.setString(6, agendamento.getStatus());
            stmt.setBigDecimal(7, agendamento.getValorTotal());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        agendamento.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir agendamento: " + e.getMessage());
            e.printStackTrace(); 
        }
        return false;
    }


    public Agendamento buscarAgendamentoPorId(int id) {
        String sql = "SELECT * FROM AGENDAMENTOS WHERE id_agendamento = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Agendamento(
                            rs.getInt("id_agendamento"),
                            rs.getInt("id_cliente"),
                            rs.getInt("id_sala"),
                            rs.getDate("data_agendamento").toLocalDate(),
                            rs.getTime("hora_inicio").toLocalTime(),
                            rs.getTime("hora_fim").toLocalTime(),
                            rs.getString("status"),
                            rs.getBigDecimal("valor_total")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar agendamento por ID: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizarAgendamento(Agendamento agendamento) {
        String sql = "UPDATE AGENDAMENTOS SET id_cliente = ?, id_sala = ?, data_agendamento = ?, hora_inicio = ?, hora_fim = ?, status = ?, valor_total = ? WHERE id_agendamento = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, agendamento.getIdCliente());
            stmt.setInt(2, agendamento.getIdSala());
            stmt.setDate(3, java.sql.Date.valueOf(agendamento.getDataAgendamento()));
            stmt.setTime(4, java.sql.Time.valueOf(agendamento.getHoraInicio()));
            stmt.setTime(5, java.sql.Time.valueOf(agendamento.getHoraFim()));
            stmt.setString(6, agendamento.getStatus());
            stmt.setBigDecimal(7, agendamento.getValorTotal());
            stmt.setInt(8, agendamento.getId());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar agendamento: " + e.getMessage());
        }
        return false;
    }


    public boolean excluirAgendamento(int id) {
        String sql = "DELETE FROM AGENDAMENTOS WHERE id_agendamento = ?";
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir agendamento: " + e.getMessage());
        }
        return false;
    }


    public List<Agendamento> buscarTodosAgendamentos() {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT * FROM AGENDAMENTOS";
        try (Connection conn = ConexaoBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                agendamentos.add(new Agendamento(
                        rs.getInt("id_agendamento"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_sala"),
                        rs.getDate("data_agendamento").toLocalDate(),
                        rs.getTime("hora_inicio").toLocalTime(),
                        rs.getTime("hora_fim").toLocalTime(),
                        rs.getString("status"),
                        rs.getBigDecimal("valor_total")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os agendamentos: " + e.getMessage());
        }
        return agendamentos;
    }

    public Sala encontrarSalaDisponivel(String nomeTipoSala, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim) {

        return null; 
    }

    
    public List<Agendamento> buscarConflitosDeAgendamento(int idSala, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim, Integer idAgendamentoExcluir) {
        List<Agendamento> conflitos = new ArrayList<>();
        String sql = "SELECT * FROM AGENDAMENTOS WHERE id_sala = ? AND data_agendamento = ? AND " +
                     "(" +
                     "    (hora_inicio < ? AND hora_fim > ?) " + 
                     ")";

        if (idAgendamentoExcluir != null) {
            sql += " AND id_agendamento != ?";
        }

        System.out.println("\n--- DEBUG DAO: Buscando conflitos ---");
        System.out.println("Sala ID: " + idSala + ", Data: " + dataAgendamento + ", Horário: " + horaInicio + " - " + horaFim + (idAgendamentoExcluir != null ? ", Excluindo Agendamento ID: " + idAgendamentoExcluir : ""));
        System.out.println("SQL Query: " + sql);

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSala);
            stmt.setDate(2, java.sql.Date.valueOf(dataAgendamento));
            stmt.setTime(3, java.sql.Time.valueOf(horaFim));    
            stmt.setTime(4, java.sql.Time.valueOf(horaInicio)); 

            System.out.println("Param 1 (id_sala): " + idSala);
            System.out.println("Param 2 (data_agendamento): " + dataAgendamento);
            System.out.println("Param 3 (hora_fim_nova): " + horaFim);
            System.out.println("Param 4 (hora_inicio_nova): " + horaInicio);


            if (idAgendamentoExcluir != null) {
                stmt.setInt(5, idAgendamentoExcluir);
                System.out.println("Param 5 (id_agendamento_excluir): " + idAgendamentoExcluir);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Agendamento conflito = new Agendamento(
                            rs.getInt("id_agendamento"),
                            rs.getInt("id_cliente"),
                            rs.getInt("id_sala"),
                            rs.getDate("data_agendamento").toLocalDate(),
                            rs.getTime("hora_inicio").toLocalTime(),
                            rs.getTime("hora_fim").toLocalTime(),
                            rs.getString("status"),
                            rs.getBigDecimal("valor_total")
                    );
                    conflitos.add(conflito);
                    System.out.println("Conflito encontrado: " + conflito.toString());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar conflitos de agendamento: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("--- Fim da busca de conflitos. Conflitos encontrados: " + conflitos.size() + " ---");
        return conflitos;
    }
}
