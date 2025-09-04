package view;

import controller.ClienteController;
import dao.ClienteDAO;
import dao.UsuarioDAO;
import model.Cliente;

import dao.AgendamentoDAO;
import dao.SalaDAO;
import controller.AgendamentoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ClienteMenuFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton gerenciarMeusAgendamentosButton;
    private JButton atualizarMeusDadosButton;
    private JButton sairButton;
    private Integer idClienteLogado;
    private ClienteController clienteController;


    public ClienteMenuFrame(Integer idClienteLogado) {
        this.idClienteLogado = idClienteLogado;
        this.clienteController = new ClienteController(new ClienteDAO(), new UsuarioDAO());

        setTitle("Menu Principal - Cliente StudioMusic");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Cliente clienteAtual = clienteController.buscarClientePorId(idClienteLogado);
        if (clienteAtual == null || !clienteAtual.isAtivo()) {
            JOptionPane.showMessageDialog(this, "Seu perfil de cliente está inativo. Não é possível acessar o sistema.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            dispose();
            return; 
        }


        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Bem-vindo ao StudioMusic (Cliente)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(25, 25, 112));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        gerenciarMeusAgendamentosButton = new JButton("Meus Agendamentos");
        styleButton(gerenciarMeusAgendamentosButton, new Color(70, 130, 180));
        buttonPanel.add(gerenciarMeusAgendamentosButton);

        atualizarMeusDadosButton = new JButton("Atualizar Meus Dados");
        styleButton(atualizarMeusDadosButton, new Color(60, 179, 113));
        buttonPanel.add(atualizarMeusDadosButton);

        sairButton = new JButton("Sair");
        styleButton(sairButton, new Color(220, 20, 60));
        buttonPanel.add(sairButton);

        panel.add(buttonPanel, BorderLayout.CENTER);
        add(panel);

        gerenciarMeusAgendamentosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                ClienteDAO clienteDAO = new ClienteDAO();
                SalaDAO salaDAO = new SalaDAO();
                AgendamentoController agendamentoController = new AgendamentoController(agendamentoDAO, clienteDAO, salaDAO);
                AgendamentoCrudFrame agendamentoCrud = new AgendamentoCrudFrame(agendamentoController, idClienteLogado);
                agendamentoCrud.setVisible(true);
            }
        });

        atualizarMeusDadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AtualizarMeusDadosFrame atualizarDadosFrame = new AtualizarMeusDadosFrame(clienteController, idClienteLogado);
                atualizarDadosFrame.setVisible(true);
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(ClienteMenuFrame.this, "Tem certeza que deseja sair?", "Sair", JOptionPane.YES_NO_OPTION);
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
