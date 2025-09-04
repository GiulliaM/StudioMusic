package view;

import controller.CadastroClienteController;

import javax.swing.*;
import java.awt.*;

public class CadastroClienteFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField nomeField, cpfCnpjField, telefoneField, emailField, usernameField;
    private JPasswordField passwordField;
    private JButton cadastrarButton, voltarButton;
    private CadastroClienteController cadastroController;

    public CadastroClienteFrame(CadastroClienteController cadastroController) {
        this.cadastroController = cadastroController;

        setTitle("Cadastro de Cliente - StudioMusic");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 255)); 
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Cadastro de Cliente");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 70, 120));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; nomeField = criarCampo(); panel.add(nomeField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1; cpfCnpjField = criarCampo(); panel.add(cpfCnpjField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; telefoneField = criarCampo(); panel.add(telefoneField, gbc);

        gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; emailField = criarCampo(); panel.add(emailField, gbc);

        gbc.gridy = 5; gbc.gridx = 0; panel.add(new JLabel("Usuário (Login):"), gbc);
        gbc.gridx = 1; usernameField = criarCampo(); panel.add(usernameField, gbc);

        gbc.gridy = 6; gbc.gridx = 0; panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 250), 1));
        panel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 255));

        cadastrarButton = new JButton("Cadastrar");
        voltarButton = new JButton("Voltar ao Login");

        estilizarBotao(cadastrarButton, new Color(100, 149, 237));   
        estilizarBotao(voltarButton, new Color(169, 169, 169));      

        buttonPanel.add(cadastrarButton);
        buttonPanel.add(voltarButton);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        cadastrarButton.addActionListener(e -> realizarCadastro());
        voltarButton.addActionListener(e -> dispose());
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField(20);
        campo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 250), 1));
        return campo;
    }

    private void estilizarBotao(JButton botao, Color corFundo) {
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setBackground(corFundo);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
    }

    private void realizarCadastro() {
        String nome = nomeField.getText();
        String cpfCnpj = cpfCnpjField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (cadastroController.cadastrarNovoCliente(nome, cpfCnpj, telefone, email, username, password)) {
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso! Você já pode fazer login.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro no cadastro. Verifique os dados e se o usuário já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
