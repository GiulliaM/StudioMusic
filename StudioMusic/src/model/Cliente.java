package model;

import java.io.Serializable;


public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private String cpfCnpj;
    private String telefone;
    private String email;
    private boolean ativo; 


    public Cliente() {
    }


    public Cliente(String nome, String cpfCnpj, String telefone, String email) {
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.telefone = telefone;
        this.email = email;
        this.ativo = true; 
    }


    public Cliente(int id, String nome, String cpfCnpj, String telefone, String email, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpfCnpj = cpfCnpj;
        this.telefone = telefone;
        this.email = email;
        this.ativo = ativo;
    }

    

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public String toString() {
        return "Cliente{" +
               "id=" + id +
               ", nome='" + nome + '\'' +
               ", cpfCnpj='" + cpfCnpj + '\'' +
               ", telefone='" + telefone + '\'' +
               ", email='" + email + '\'' +
               ", ativo=" + ativo +
               '}';
    }
}
