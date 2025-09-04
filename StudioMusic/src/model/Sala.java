package model;

import java.io.Serializable;
import java.math.BigDecimal;


public class Sala implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nomeTipo; 
    private int numeroSala; 
    private int capacidade;
    private String equipamentos;
    private BigDecimal valorHora;

    public Sala() {
        
    }


    public Sala(String nomeTipo, int numeroSala, int capacidade, String equipamentos, BigDecimal valorHora) {
        this.nomeTipo = nomeTipo;
        this.numeroSala = numeroSala;
        this.capacidade = capacidade;
        this.equipamentos = equipamentos;
        this.valorHora = valorHora;
    }


    public Sala(int id, String nomeTipo, int numeroSala, int capacidade, String equipamentos, BigDecimal valorHora) {
        this.id = id;
        this.nomeTipo = nomeTipo;
        this.numeroSala = numeroSala;
        this.capacidade = capacidade;
        this.equipamentos = equipamentos;
        this.valorHora = valorHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeTipo() { 
        return nomeTipo;
    }

    public void setNomeTipo(String nomeTipo) { 
        this.nomeTipo = nomeTipo;
    }

    public int getNumeroSala() { 
        return numeroSala;
    }

    public void setNumeroSala(int numeroSala) { 
        this.numeroSala = numeroSala;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getEquipamentos() {
        return equipamentos;
    }

    public void setEquipamentos(String equipamentos) {
        this.equipamentos = equipamentos;
    }

    public BigDecimal getValorHora() {
        return valorHora;
    }

    public void setValorHora(BigDecimal valorHora) {
        this.valorHora = valorHora;
    }
    
    @Override
    public String toString() {
        return "Sala [ID=" + id + ", Tipo=" + nomeTipo + ", Número=" + numeroSala +
               ", Capacidade=" + capacidade + ", Valor/Hora=" + valorHora + "]";
    }


}