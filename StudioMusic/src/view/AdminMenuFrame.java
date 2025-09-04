package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.ClienteDAO;
import dao.UsuarioDAO;
import dao.AgendamentoDAO;
import dao.SalaDAO;
import controller.ClienteController;
import controller.AgendamentoController;

public class AdminMenuFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton gerenciarClientesButton;
    private JButton gerenciarAgendamentosButton;
    private JButton sairButton;

    public AdminMenuFrame() {
        setTitle("Menu Principal - Administrador StudioMusic");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Bem-vindo ao StudioMusic (Admin)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(25, 25, 112));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        gerenciarClientesButton = new JButton("Gerenciar Clientes");
        styleButton(gerenciarClientesButton, new Color(60, 179, 113));
        buttonPanel.add(gerenciarClientesButton);

        gerenciarAgendamentosButton = new JButton("Gerenciar Agendamentos");
        styleButton(gerenciarAgendamentosButton, new Color(70, 130, 180));
        buttonPanel.add(gerenciarAgendamentosButton);

        sairButton = new JButton("Sair");
        styleButton(sairButton, new Color(220, 20, 60));
        buttonPanel.add(sairButton);

        panel.add(buttonPanel, BorderLayout.CENTER);
        add(panel);

        gerenciarClientesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClienteDAO clienteDAO = new ClienteDAO();
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                ClienteController clienteController = new ClienteController(clienteDAO, usuarioDAO);
                ClienteCrudFrame clienteCrud = new ClienteCrudFrame(clienteController);
                clienteCrud.setVisible(true);
            }
        });

        gerenciarAgendamentosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                ClienteDAO clienteDAO = new ClienteDAO();
                SalaDAO salaDAO = new SalaDAO();
                AgendamentoController agendamentoController = new AgendamentoController(agendamentoDAO, clienteDAO, salaDAO);
                AgendamentoCrudFrame agendamentoCrud = new AgendamentoCrudFrame(agendamentoController, null);
                agendamentoCrud.setVisible(true);
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(AdminMenuFrame.this, "Tem certeza que deseja sair?", "Sair", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 50));
    }
}
