package view;

import model.Cliente;
import model.Usuario;
import controller.ClienteController;
import controller.CadastroClienteController;
import dao.UsuarioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClienteCrudFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField idField, nomeField, cpfCnpjField, telefoneField, emailField, usernameField;
    private JPasswordField passwordField;
    private JButton adicionarButton, atualizarButton, desativarButton, buscarButton, limparButton, toggleActiveInactiveButton, redefinirSenhaButton;
    private JTable clienteTable;
    private DefaultTableModel tableModel;
    private ClienteController clienteController;
    private CadastroClienteController cadastroClienteController;

    private boolean showingInactiveClients = false;

    public ClienteCrudFrame(ClienteController clienteController) {
        this.clienteController = clienteController;
        this.cadastroClienteController = new CadastroClienteController(new dao.ClienteDAO(), new UsuarioDAO());

        setTitle("Gerenciamento de Clientes");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; idField = new JTextField(5); idField.setEditable(false); formPanel.add(idField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 3; nomeField = new JTextField(20); formPanel.add(nomeField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; cpfCnpjField = new JTextField(15); formPanel.add(cpfCnpjField, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 3; formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2; telefoneField = new JTextField(15); formPanel.add(telefoneField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 5; emailField = new JTextField(25); formPanel.add(emailField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Usuário (Login):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        usernameField = new JTextField(15); 
        formPanel.add(usernameField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 3; formPanel.add(new JLabel("Senha (Login):"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);
        gbc.gridwidth = 1;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        adicionarButton = new JButton("Adicionar");
        atualizarButton = new JButton("Atualizar");
        desativarButton = new JButton("Desativar");
        buscarButton = new JButton("Buscar por ID");
        limparButton = new JButton("Limpar Campos");
        toggleActiveInactiveButton = new JButton("Ver Desativados");
        redefinirSenhaButton = new JButton("Redefinir Senha");

        styleCrudButton(adicionarButton, new Color(46, 139, 87));
        styleCrudButton(atualizarButton, new Color(30, 144, 255));
        styleCrudButton(desativarButton, new Color(255, 69, 0));
        styleCrudButton(buscarButton, new Color(100, 149, 237));
        styleCrudButton(limparButton, new Color(112, 128, 144));
        styleCrudButton(toggleActiveInactiveButton, new Color(75, 0, 130));
        styleCrudButton(redefinirSenhaButton, new Color(200, 100, 0));

        buttonPanel.add(adicionarButton);
        buttonPanel.add(atualizarButton);
        buttonPanel.add(desativarButton);
        buttonPanel.add(buscarButton);
        buttonPanel.add(limparButton);
        buttonPanel.add(toggleActiveInactiveButton);
        buttonPanel.add(redefinirSenhaButton);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(formPanel);
        topPanel.add(buttonPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nome", "CPF/CNPJ", "Telefone", "E-mail"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        clienteTable = new JTable(tableModel);
        clienteTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clienteTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(clienteTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        adicionarButton.addActionListener(e -> adicionarClienteAction());
        atualizarButton.addActionListener(e -> atualizarClienteAction());
        desativarButton.addActionListener(e -> desativarClienteAction());
        buscarButton.addActionListener(e -> buscarClientePorIdAction());
        limparButton.addActionListener(e -> limparCampos());
        toggleActiveInactiveButton.addActionListener(e -> toggleClientListAction());
        redefinirSenhaButton.addActionListener(e -> redefinirSenhaClienteAction());

        clienteTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && clienteTable.getSelectedRow() != -1) {
                preencherCamposComSelecao();
            }
        });

        carregarClientesNaTabela();
    }

    private void styleCrudButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void limparCampos() {
        idField.setText("");
        nomeField.setText("");
        cpfCnpjField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        clienteTable.clearSelection();
    }

    private void toggleClientListAction() {
        showingInactiveClients = !showingInactiveClients;
        toggleActiveInactiveButton.setText(showingInactiveClients ? "Ver Ativos" : "Ver Desativados");
        toggleActiveInactiveButton.setBackground(showingInactiveClients ? new Color(139, 69, 19) : new Color(75, 0, 130));
        carregarClientesNaTabela();
    }

    private void carregarClientesNaTabela() {
        tableModel.setRowCount(0);
        List<Cliente> clientes = showingInactiveClients ? clienteController.listarClientesInativos() : clienteController.listarClientesAtivos();

        for (Cliente cliente : clientes) {
            tableModel.addRow(new Object[]{
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpfCnpj(),
                cliente.getTelefone(),
                cliente.getEmail()
            });
        }

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void preencherCamposComSelecao() {
        int selectedRow = clienteTable.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            nomeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            cpfCnpjField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            telefoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());

            int idClienteSelecionado = Integer.parseInt(idField.getText());
            Usuario usuarioAssociado = clienteController.buscarUsuarioPorIdCliente(idClienteSelecionado);
            if (usuarioAssociado != null) {
                usernameField.setText(usuarioAssociado.getUsername());
            } else {
                usernameField.setText("");
            }
            passwordField.setText("");
        }
    }

    private void adicionarClienteAction() {
        try {
            String nome = nomeField.getText();
            String cpfCnpj = cpfCnpjField.getText();
            String telefone = telefoneField.getText();
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (nome.isEmpty() || cpfCnpj.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome, CPF/CNPJ, Usuário e Senha são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cadastroClienteController.cadastrarClientePorAdmin(nome, cpfCnpj, telefone, email, username, password)) {
                JOptionPane.showMessageDialog(this, "Cliente e usuário adicionados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarClientesNaTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar cliente. CPF/CNPJ ou Usuário já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarClienteAction() {
        try {
            int id = Integer.parseInt(idField.getText());
            String nome = nomeField.getText();
            String cpfCnpj = cpfCnpjField.getText();
            String telefone = telefoneField.getText();
            String email = emailField.getText();

            if (nome.isEmpty() || cpfCnpj.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e CPF/CNPJ são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cliente clienteOriginal = clienteController.buscarClientePorId(id);
            if (clienteOriginal == null) {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente clienteAtualizado = new Cliente(id, nome, cpfCnpj, telefone, email, clienteOriginal.isAtivo());
            if (clienteController.atualizarCliente(clienteAtualizado)) {
                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarClientesNaTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desativarClienteAction() {
        try {
            int id = Integer.parseInt(idField.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente DESATIVAR este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (clienteController.desativarCliente(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente desativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                    carregarClientesNaTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao desativar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido para desativação.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarClientePorIdAction() {
        try {
            String input = JOptionPane.showInputDialog(this, "Digite o ID do cliente:");
            if (input == null) return;
            int id = Integer.parseInt(input);
            Cliente cliente = clienteController.buscarClientePorId(id);
            if (cliente != null) {
                idField.setText(String.valueOf(cliente.getId()));
                nomeField.setText(cliente.getNome());
                cpfCnpjField.setText(cliente.getCpfCnpj());
                telefoneField.setText(cliente.getTelefone());
                emailField.setText(cliente.getEmail());
                Usuario usuario = clienteController.buscarUsuarioPorIdCliente(id);
                usernameField.setText(usuario != null ? usuario.getUsername() : "");
                passwordField.setText("");
                JOptionPane.showMessageDialog(this, "Cliente encontrado!", "Busca", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado.", "Busca", JOptionPane.WARNING_MESSAGE);
                limparCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite um ID numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void redefinirSenhaClienteAction() {
        try {
            int idCliente = Integer.parseInt(idField.getText());
            if (idCliente <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente válido.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String novaSenha = JOptionPane.showInputDialog(this, "Digite a NOVA senha para o cliente ID " + idCliente + ":");
            if (novaSenha == null || novaSenha.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Senha inválida.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (clienteController.atualizarSenhaCliente(idCliente, novaSenha)) {
                JOptionPane.showMessageDialog(this, "Senha redefinida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao redefinir senha.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
