package controller;

import dao.ClienteDAO;
import dao.UsuarioDAO;
import model.Cliente;
import model.Usuario;

public class CadastroClienteController {
    private ClienteDAO clienteDAO;
    private UsuarioDAO usuarioDAO;

    public CadastroClienteController(ClienteDAO clienteDAO, UsuarioDAO usuarioDAO) {
        this.clienteDAO = clienteDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public boolean cadastrarNovoCliente(String nome, String cpfCnpj, String telefone, String email, String username, String password) {
        if (nome.trim().isEmpty() || cpfCnpj.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            System.err.println("Nome, CPF/CNPJ, Usuário e Senha são campos obrigatórios para o cadastro.");
            return false;
        }

        if (usuarioDAO.buscarUsuarioPorUsername(username) != null) {
            System.err.println("Nome de usuário já existe. Por favor, escolha outro.");
            return false;
        }

        Cliente novoCliente = new Cliente(nome, cpfCnpj, telefone, email);

        if (!clienteDAO.inserirCliente(novoCliente)) {
            System.err.println("Erro ao inserir o cliente no banco de dados.");
            return false;
        }

        Usuario novoUsuario = new Usuario(username, password, "cliente", novoCliente.getId());

        if (!usuarioDAO.inserirUsuario(novoUsuario)) {
            System.err.println("Erro ao inserir o usuário no banco de dados. Cliente cadastrado, mas usuário não.");
            return false;
        }

        return true;
    }

    public boolean cadastrarClientePorAdmin(String nome, String cpfCnpj, String telefone, String email, String username, String password) {
        if (nome.trim().isEmpty() || cpfCnpj.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            System.err.println("Nome, CPF/CNPJ, Usuário e Senha são campos obrigatórios para o cadastro via admin.");
            return false;
        }

        if (usuarioDAO.buscarUsuarioPorUsername(username) != null) {
            System.err.println("Nome de usuário já existe. Por favor, escolha outro.");
            return false;
        }

        Cliente novoCliente = new Cliente(nome, cpfCnpj, telefone, email);

        if (!clienteDAO.inserirCliente(novoCliente)) {
            System.err.println("Erro ao inserir o cliente no banco de dados (admin).");
            return false;
        }

        Usuario novoUsuario = new Usuario(username, password, "cliente", novoCliente.getId());

        if (!usuarioDAO.inserirUsuario(novoUsuario)) {
            System.err.println("Erro ao inserir o usuário no banco de dados (admin). Cliente cadastrado, mas usuário não.");
            return false;
        }

        return true;
    }
}
