package controller;

import dao.UsuarioDAO;
import dao.ClienteDAO; 
import model.Usuario;
import model.Cliente; 

public class LoginController {
    private UsuarioDAO usuarioDAO;
    private ClienteDAO clienteDAO; 


    public LoginController(UsuarioDAO usuarioDAO, ClienteDAO clienteDAO) { 
        this.usuarioDAO = usuarioDAO;
        this.clienteDAO = clienteDAO; 
    }

 
    public Usuario autenticar(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.err.println("Nome de usuário e senha não podem ser vazios.");
            return null;
        }

        Usuario usuarioAutenticado = usuarioDAO.autenticar(username, password);

        if (usuarioAutenticado == null) {
            System.out.println("Usuário ou senha inválidos para " + username);
            return null; 
        }

        if ("cliente".equals(usuarioAutenticado.getTipoUsuario())) {
            if (usuarioAutenticado.getIdClienteFk() == null) {
                System.err.println("Usuário cliente sem ID de cliente associado. Login negado.");
                return null; 
            }
            Cliente clienteAssociado = clienteDAO.buscarClientePorId(usuarioAutenticado.getIdClienteFk());
            if (clienteAssociado == null || !clienteAssociado.isAtivo()) {
                System.out.println("Cliente associado ao usuário " + username + " não encontrado ou inativo. Login negado.");
                return null; 
            }
            System.out.println("Cliente " + clienteAssociado.getNome() + " (ID: " + clienteAssociado.getId() + ") está ativo. Login permitido.");
        } else {
            System.out.println("Usuário " + username + " é um " + usuarioAutenticado.getTipoUsuario() + ". Login permitido.");
        }

        return usuarioAutenticado; 
    }
}
