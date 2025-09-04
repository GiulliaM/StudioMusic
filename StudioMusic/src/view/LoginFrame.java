package view;

import controller.LoginController;
import dao.UsuarioDAO;
import dao.ClienteDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private LoginController loginController;

    public LoginFrame(LoginController loginController) {
        this.loginController = loginController;

        setTitle("Login - StudioMusic");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel tituloLabel = new JLabel("Acesse sua conta - StudioMusic");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(tituloLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Usuário:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Entrar");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(50, 90, 120), 2));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(loginButton, gbc);

        gbc.gridy = 4;
        registerButton = new JButton("Cadastrar-se");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(100, 149, 237));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(80, 120, 180), 2));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(registerButton, gbc);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClienteDAO clienteDAO = new ClienteDAO();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                controller.CadastroClienteController cadastroController = new controller.CadastroClienteController(clienteDAO, usuarioDAO);
                CadastroClienteFrame cadastroFrame = new CadastroClienteFrame(cadastroController);
                cadastroFrame.setVisible(true);
            }
        });
    }

    private void realizarLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Usuario usuarioLogado = loginController.autenticar(username, password);

        if (usuarioLogado != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            if ("admin".equals(usuarioLogado.getTipoUsuario())) {
                AdminMenuFrame adminMenu = new AdminMenuFrame();
                adminMenu.setVisible(true);
            } else if ("cliente".equals(usuarioLogado.getTipoUsuario())) {
                if (usuarioLogado.getIdClienteFk() != null) {
                    ClienteMenuFrame clienteMenu = new ClienteMenuFrame(usuarioLogado.getIdClienteFk());
                    clienteMenu.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Usuário cliente sem associação de cliente válida.", "Erro Interno", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}
