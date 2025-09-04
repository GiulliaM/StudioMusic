package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate; 
import java.time.LocalTime; 

public class Agendamento implements Serializable {
    private static final long serialVersionUID = 1L; 
    private int id;
    private int idCliente;
    private int idSala;
    private LocalDate dataAgendamento;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String status;
    private BigDecimal valorTotal;


    public Agendamento() {
        
    }


    public Agendamento(int idCliente, int idSala, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim, String status, BigDecimal valorTotal) {
        this.idCliente = idCliente;
        this.idSala = idSala;
        this.dataAgendamento = dataAgendamento;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.status = status;
        this.valorTotal = valorTotal;
    }


    public Agendamento(int id, int idCliente, int idSala, LocalDate dataAgendamento, LocalTime horaInicio, LocalTime horaFim, String status, BigDecimal valorTotal) {
        this.id = id;
        this.idCliente = idCliente;
        this.idSala = idSala;
        this.dataAgendamento = dataAgendamento;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.status = status;
        this.valorTotal = valorTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdSala() {
        return idSala;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public String toString() {
        return "Agendamento{" +
               "id=" + id +
               ", idCliente=" + idCliente +
               ", idSala=" + idSala +
               ", dataAgendamento=" + dataAgendamento +
               ", horaInicio=" + horaInicio +
               ", horaFim=" + horaFim +
               ", status='" + status + '\'' +
               ", valorTotal=" + valorTotal +
               '}';
    }
}

