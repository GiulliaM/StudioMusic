package model;

import java.io.Serializable;


public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L; 
    private int id;
    private String username;
    private String password;
    private String tipoUsuario;
    private Integer idClienteFk; 


    public Usuario() {
    }


    public Usuario(int id, String username, String password, String tipoUsuario, Integer idClienteFk) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        this.idClienteFk = idClienteFk;
    }


    public Usuario(String username, String password, String tipoUsuario, Integer idClienteFk) {
        this.username = username;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        this.idClienteFk = idClienteFk;
    }

    

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public Integer getIdClienteFk() {
		return idClienteFk;
	}

	public void setIdClienteFk(Integer idClienteFk) {
		this.idClienteFk = idClienteFk;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public String toString() {
        return "Usuario{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", tipoUsuario='" + tipoUsuario + '\'' +
               ", idClienteFk=" + idClienteFk +
               '}';
    }
}
