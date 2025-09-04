package view;

import model.Cliente;
import model.Usuario;
import controller.ClienteController;

import javax.swing.*;
import java.awt.*;


public class AtualizarMeusDadosFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField idField, nomeField, cpfCnpjField, telefoneField, emailField;
    private JPasswordField senhaAtualField, novaSenhaField, confirmarNovaSenhaField;
    private JButton salvarButton, cancelarButton;
    private ClienteController clienteController;
    private Integer idClienteLogado;

    public AtualizarMeusDadosFrame(ClienteController clienteController, Integer idClienteLogado) {
        this.clienteController = clienteController;
        this.idClienteLogado = idClienteLogado;

        setTitle("Atualizar Meus Dados");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Cliente clienteAtual = clienteController.buscarClientePorId(idClienteLogado);
        if (clienteAtual == null || !clienteAtual.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Seu perfil de cliente está inativo. Não é possível atualizar seus dados.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Meus Dados Cadastrais"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; idField = new JTextField(5); idField.setEditable(false); formPanel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; nomeField = new JTextField(25); formPanel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1; cpfCnpjField = new JTextField(25); formPanel.add(cpfCnpjField, gbc);
        cpfCnpjField.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; telefoneField = new JTextField(25); formPanel.add(telefoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; emailField = new JTextField(25); formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Senha Atual:"), gbc);
        gbc.gridx = 1; senhaAtualField = new JPasswordField(25); formPanel.add(senhaAtualField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Nova Senha:"), gbc);
        gbc.gridx = 1; novaSenhaField = new JPasswordField(25); formPanel.add(novaSenhaField, gbc);

        gbc.gridx = 0; gbc.gridy = 7; formPanel.add(new JLabel("Confirmar Nova Senha:"), gbc);
        gbc.gridx = 1; confirmarNovaSenhaField = new JPasswordField(25); formPanel.add(confirmarNovaSenhaField, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        salvarButton = new JButton("Salvar Alterações");
        cancelarButton = new JButton("Cancelar");

        styleButton(salvarButton, new Color(46, 139, 87));
        styleButton(cancelarButton, new Color(112, 128, 144));

        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        carregarDadosCliente();

        salvarButton.addActionListener(e -> salvarAlteracoesAction());
        cancelarButton.addActionListener(e -> dispose());
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void carregarDadosCliente() {
        if (idClienteLogado != null) {
            Cliente cliente = clienteController.buscarClientePorId(idClienteLogado);
            if (cliente != null) {
                idField.setText(String.valueOf(cliente.getId()));
                nomeField.setText(cliente.getNome());
                cpfCnpjField.setText(cliente.getCpfCnpj());
                telefoneField.setText(cliente.getTelefone());
                emailField.setText(cliente.getEmail());
                senhaAtualField.setText("");
                novaSenhaField.setText("");
                confirmarNovaSenhaField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Não foi possível carregar seus dados ou seu perfil está inativo.", "Erro", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro: ID do cliente não fornecido.", "Erro", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void salvarAlteracoesAction() {
        try {
            int id = Integer.parseInt(idField.getText());
            String nome = nomeField.getText();
            String cpfCnpj = cpfCnpjField.getText();
            String telefone = telefoneField.getText();
            String email = emailField.getText();

            if (nome.isEmpty() || cpfCnpj.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e CPF/CNPJ são campos obrigatórios.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cliente clienteOriginal = clienteController.buscarClientePorId(id);
            if (clienteOriginal == null) {
                JOptionPane.showMessageDialog(this, "Seu perfil não foi encontrado ou está inativo para atualização.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente clienteAtualizado = new Cliente(id, nome, cpfCnpj, telefone, email, clienteOriginal.isAtivo());
            boolean dadosClienteAtualizados = clienteController.atualizarCliente(clienteAtualizado);

            String senhaAtual = new String(senhaAtualField.getPassword());
            String novaSenha = new String(novaSenhaField.getPassword());
            String confirmarNovaSenha = new String(confirmarNovaSenhaField.getPassword());

            boolean senhaAtualizada = false;

            if (!senhaAtual.isEmpty() || !novaSenha.isEmpty() || !confirmarNovaSenha.isEmpty()) {
                Usuario usuarioLogado = clienteController.buscarUsuarioPorIdCliente(idClienteLogado);
                if (usuarioLogado == null || !usuarioLogado.getPassword().equals(senhaAtual)) {
                    JOptionPane.showMessageDialog(this, "Senha atual incorreta.", "Erro de Senha", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (novaSenha.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nova senha não pode ser vazia.", "Erro de Senha", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!novaSenha.equals(confirmarNovaSenha)) {
                    JOptionPane.showMessageDialog(this, "Nova senha e confirmação não coincidem.", "Erro de Senha", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                senhaAtualizada = clienteController.atualizarSenhaCliente(idClienteLogado, novaSenha);
                if (!senhaAtualizada) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar a senha.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (dadosClienteAtualizados && senhaAtualizada) {
                JOptionPane.showMessageDialog(this, "Dados e senha atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else if (dadosClienteAtualizados) {
                JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else if (senhaAtualizada) {
                JOptionPane.showMessageDialog(this, "Senha atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Nenhuma alteração foi feita ou ocorreu um erro.", "Informação", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException e) {
            System.err.println("Erro de HeadlessException: " + e.getMessage());
        } finally {
            senhaAtualField.setText("");
            novaSenhaField.setText("");
            confirmarNovaSenhaField.setText("");
        }
    }
}
