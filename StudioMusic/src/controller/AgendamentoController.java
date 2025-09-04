package controller;

import dao.AgendamentoDAO;
import dao.ClienteDAO;
import dao.SalaDAO;
import model.Agendamento;
import model.Cliente;
import model.Sala;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;



public class AgendamentoController {
    private AgendamentoDAO agendamentoDAO;
    private ClienteDAO clienteDAO;
    private SalaDAO salaDAO;


    public AgendamentoController(AgendamentoDAO agendamentoDAO, ClienteDAO clienteDAO, SalaDAO salaDAO) {
        this.agendamentoDAO = agendamentoDAO;
        this.clienteDAO = clienteDAO;
        this.salaDAO = salaDAO;
    }


    public boolean adicionarAgendamento(Agendamento agendamento) {
        if (agendamento.getDataAgendamento() == null || agendamento.getHoraInicio() == null || agendamento.getHoraFim() == null ||
            agendamento.getHoraInicio().isAfter(agendamento.getHoraFim()) || agendamento.getHoraInicio().equals(agendamento.getHoraFim())) {
            System.err.println("Dados de agendamento inválidos (data/hora).");
            return false;
        }

        BigDecimal valorTotal = calcularValorTotal(agendamento.getIdSala(), agendamento.getHoraInicio(), agendamento.getHoraFim());
        agendamento.setValorTotal(valorTotal);

        return agendamentoDAO.inserirAgendamento(agendamento);
    }


    public boolean atualizarAgendamento(Agendamento agendamento) {
        if (agendamento.getDataAgendamento() == null || agendamento.getHoraInicio() == null || agendamento.getHoraFim() == null ||
            agendamento.getHoraInicio().isAfter(agendamento.getHoraFim()) || agendamento.getHoraInicio().equals(agendamento.getHoraFim())) {
            System.err.println("Dados de agendamento inválidos (data/hora).");
            return false;
        }

        BigDecimal valorTotal = calcularValorTotal(agendamento.getIdSala(), agendamento.getHoraInicio(), agendamento.getHoraFim());
        agendamento.setValorTotal(valorTotal);

        return agendamentoDAO.atualizarAgendamento(agendamento);
    }


    public boolean excluirAgendamento(int id) {
        return agendamentoDAO.excluirAgendamento(id);
    }


    public Agendamento buscarAgendamentoPorId(int id) {
        return agendamentoDAO.buscarAgendamentoPorId(id);
    }


    public List<Agendamento> listarTodosAgendamentos() {
        return agendamentoDAO.buscarTodosAgendamentos();
    }


    public Cliente buscarClientePorId(int id) {
        return clienteDAO.buscarClientePorId(id);
    }


    public List<Cliente> listarTodosClientes() { 
        return clienteDAO.buscarClientesAtivos();
    }


    public Sala buscarSalaPorId(int id) {
        return salaDAO.buscarSalaPorId(id);
    }


    public List<Sala> listarTodasSalas() {
        return salaDAO.buscarTodasSalas();
    }


    public Sala encontrarSalaDisponivel(String nomeTipoSala, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim) {
        System.out.println("\n--- Encontrando sala disponível ---");
        System.out.println("Tipo desejado: " + nomeTipoSala + ", Data: " + dataAgendamento + ", Horário: " + horaInicio + " - " + horaFim);

        List<Sala> salasDoTipo = salaDAO.buscarSalasPorTipo(nomeTipoSala); 

        if (salasDoTipo.isEmpty()) {
            System.out.println("Nenhuma sala encontrada para o tipo: " + nomeTipoSala);
            return null;
        }

        salasDoTipo.sort((s1, s2) -> Integer.compare(s1.getNumeroSala(), s2.getNumeroSala()));

        for (Sala sala : salasDoTipo) {
            System.out.println("Verificando disponibilidade para Sala ID: " + sala.getId() + ", Tipo: " + sala.getNomeTipo() + ", Número: " + sala.getNumeroSala());
            List<Agendamento> conflitos = agendamentoDAO.buscarConflitosDeAgendamento(
                sala.getId(), dataAgendamento, horaInicio, horaFim, null);

            if (conflitos.isEmpty()) {
                System.out.println("Sala ID " + sala.getId() + " (Número: " + sala.getNumeroSala() + ") está DISPONÍVEL.");
                return sala; 
            } else {
                System.out.println("Sala ID " + sala.getId() + " (Número: " + sala.getNumeroSala() + ") está OCUPADA. Conflitos: " + conflitos.size());
            }
        }
        System.out.println("Todas as salas do tipo " + nomeTipoSala + " estão ocupadas.");
        return null; 
    }


    public Sala encontrarSalaDisponivelParaAtualizacao(String nomeTipoSala, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim, Integer idAgendamentoExcluir) {
        System.out.println("\n--- Encontrando sala disponível para ATUALIZAÇÃO ---");
        System.out.println("Tipo desejado: " + nomeTipoSala + ", Data: " + dataAgendamento + ", Horário: " + horaInicio + " - " + horaFim + ", Excluindo ID: " + idAgendamentoExcluir);

        List<Sala> salasDoTipo = salaDAO.buscarSalasPorTipo(nomeTipoSala);

        if (salasDoTipo.isEmpty()) {
            System.out.println("Nenhuma sala encontrada para o tipo: " + nomeTipoSala);
            return null;
        }

        salasDoTipo.sort((s1, s2) -> Integer.compare(s1.getNumeroSala(), s2.getNumeroSala()));

        for (Sala sala : salasDoTipo) {
            System.out.println("Verificando disponibilidade para Sala ID: " + sala.getId() + ", Tipo: " + sala.getNomeTipo() + ", Número: " + sala.getNumeroSala());
            List<Agendamento> conflitos = agendamentoDAO.buscarConflitosDeAgendamento(
                sala.getId(), dataAgendamento, horaInicio, horaFim, idAgendamentoExcluir);
            if (conflitos.isEmpty()) {
                System.out.println("Sala ID " + sala.getId() + " (Número: " + sala.getNumeroSala() + ") está DISPONÍVEL para atualização.");
                return sala; 
            } else {
                System.out.println("Sala ID " + sala.getId() + " (Número: " + sala.getNumeroSala() + ") está OCUPADA para atualização. Conflitos: " + conflitos.size());
            }
        }
        System.out.println("Todas as salas do tipo " + nomeTipoSala + " estão ocupadas para atualização.");
        return null; 
    }


    public BigDecimal calcularValorTotal(int idSala, LocalTime horaInicio, LocalTime horaFim) {
        Sala sala = salaDAO.buscarSalaPorId(idSala);
        if (sala == null) {
            System.err.println("Sala não encontrada para calcular valor total: " + idSala);
            return BigDecimal.ZERO;
        }

        long minutos = Duration.between(horaInicio, horaFim).toMinutes();

        BigDecimal duracaoHoras = BigDecimal.valueOf(minutos).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        return duracaoHoras.multiply(sala.getValorHora());
    }
}
