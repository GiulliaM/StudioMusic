package main;

import dao.UsuarioDAO;
import dao.ClienteDAO;
import controller.LoginController;
import view.LoginFrame;

import javax.swing.*;

public class AppStudioMusic {
    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ClienteDAO clienteDAO = new ClienteDAO();

        LoginController loginController = new LoginController(usuarioDAO, clienteDAO);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame(loginController);
                loginFrame.setVisible(true);
            }
        });
    }
}
