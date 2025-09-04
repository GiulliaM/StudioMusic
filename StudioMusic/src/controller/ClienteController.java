package controller;

import model.Cliente;
import model.Usuario;
import dao.ClienteDAO;
import dao.UsuarioDAO;
import java.util.List;

public class ClienteController {
    private ClienteDAO clienteDAO;
    private UsuarioDAO usuarioDAO;

    public ClienteController(ClienteDAO clienteDAO, UsuarioDAO usuarioDAO) {
        this.clienteDAO = clienteDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public boolean adicionarCliente(Cliente cliente) {
        if (cliente == null || cliente.getNome().trim().isEmpty() || cliente.getCpfCnpj().trim().isEmpty()) {
            System.err.println("Dados do cliente inválidos para adição (Nome e CPF/CNPJ são obrigatórios).");
            return false;
        }
        return clienteDAO.inserirCliente(cliente);
    }

    public boolean atualizarCliente(Cliente cliente) {
        if (cliente == null || cliente.getNome().trim().isEmpty() || cliente.getCpfCnpj().trim().isEmpty()) {
            System.err.println("Dados do cliente inválidos para atualização (Nome e CPF/CNPJ são obrigatórios).");
            return false;
        }
        return clienteDAO.atualizarCliente(cliente);
    }

    public boolean desativarCliente(int id) {
        if (id <= 0) {
            System.err.println("ID do cliente inválido para desativação.");
            return false;
        }
        return clienteDAO.desativarCliente(id);
    }

    public Cliente buscarClientePorId(int id) {
        if (id <= 0) {
            System.err.println("ID do cliente inválido para busca.");
            return null;
        }
        return clienteDAO.buscarClientePorId(id);
    }

    public List<Cliente> listarClientesAtivos() {
        return clienteDAO.buscarClientesAtivos();
    }

    public List<Cliente> listarClientesInativos() {
        return clienteDAO.buscarClientesPorStatus(false);
    }

    public boolean atualizarSenhaCliente(int idCliente, String novaSenha) {
        if (idCliente <= 0 || novaSenha == null || novaSenha.trim().isEmpty()) {
            System.err.println("ClienteController: Dados inválidos para atualização de senha.");
            return false;
        }

        Usuario usuarioAssociado = usuarioDAO.buscarUsuarioPorIdClienteFk(idCliente); 

        if (usuarioAssociado == null) {
            System.err.println("ClienteController: Usuário não encontrado para o cliente ID: " + idCliente);
            return false;
        }

        return usuarioDAO.atualizarSenhaUsuario(usuarioAssociado.getId(), novaSenha);
    }

    public Usuario buscarUsuarioPorIdCliente(int idCliente) {
        return usuarioDAO.buscarUsuarioPorIdClienteFk(idCliente);
    }
}
