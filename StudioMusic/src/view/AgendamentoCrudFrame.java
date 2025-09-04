package view;

import model.Agendamento;
import model.Cliente;
import model.Sala;
import controller.AgendamentoController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class AgendamentoCrudFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField idField, dataField, horaInicioField, horaFimField, valorPorHoraField;
    private JComboBox<String> clienteComboBox, salaComboBox, statusComboBox;
    private JButton adicionarButton, atualizarButton, excluirButton, buscarButton, limparButton, listarTodosButton;
    private JTable agendamentoTable;
    private DefaultTableModel tableModel;
    private AgendamentoController agendamentoController;
    private Integer idClienteLogado;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    public AgendamentoCrudFrame(AgendamentoController agendamentoController, Integer idClienteLogado) {
        this.agendamentoController = agendamentoController;
        this.idClienteLogado = idClienteLogado;

        setTitle("Gerenciamento de Agendamentos");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        if (this.idClienteLogado != null) {
            Cliente clienteAtual = agendamentoController.buscarClientePorId(idClienteLogado);
            if (clienteAtual == null || !clienteAtual.isAtivo()) {
                JOptionPane.showMessageDialog(this, "Seu perfil de cliente está inativo. Não é possível gerenciar agendamentos.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Dados do Agendamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; idField = new JTextField(5); idField.setEditable(false); formPanel.add(idField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 3; gbc.gridwidth = 3; clienteComboBox = new JComboBox<>(); formPanel.add(clienteComboBox, gbc);
        gbc.gridwidth = 1;

        if (this.idClienteLogado != null) {
            clienteComboBox.setEnabled(false);
        }


        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Sala (Tipo):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; salaComboBox = new JComboBox<>(); formPanel.add(salaComboBox, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 3; formPanel.add(new JLabel("Data (DD/MM/YYYY):"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2; dataField = new JTextField(10); formPanel.add(dataField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Hora Início (HH:MM):"), gbc);
        gbc.gridx = 1; horaInicioField = new JTextField(5); formPanel.add(horaInicioField, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Hora Fim (HH:MM):"), gbc);
        gbc.gridx = 3; horaFimField = new JTextField(5); formPanel.add(horaFimField, gbc);
        gbc.gridx = 4; formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5; statusComboBox = new JComboBox<>(new String[]{"Confirmado", "Pendente", "Cancelado"}); formPanel.add(statusComboBox, gbc);

        if (this.idClienteLogado != null) {
            statusComboBox.setSelectedItem("Pendente");
            statusComboBox.setEnabled(false);
        }

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Valor por Hora:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; valorPorHoraField = new JTextField(10); valorPorHoraField.setEditable(false); formPanel.add(valorPorHoraField, gbc);
        gbc.gridwidth = 1;

        mainPanel.add(formPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        adicionarButton = new JButton("Adicionar");
        atualizarButton = new JButton("Atualizar");
        excluirButton = new JButton("Excluir");
        buscarButton = new JButton("Buscar por ID");
        limparButton = new JButton("Limpar Campos");
        listarTodosButton = new JButton("Listar Todos");

        styleCrudButton(adicionarButton, new Color(46, 139, 87));
        styleCrudButton(atualizarButton, new Color(30, 144, 255));
        styleCrudButton(excluirButton, new Color(255, 69, 0));
        styleCrudButton(buscarButton, new Color(100, 149, 237));
        styleCrudButton(limparButton, new Color(112, 128, 144));
        styleCrudButton(listarTodosButton, new Color(75, 0, 130));

        buttonPanel.add(adicionarButton);
        buttonPanel.add(atualizarButton);
        buttonPanel.add(excluirButton);
        buttonPanel.add(buscarButton);
        buttonPanel.add(limparButton);
        buttonPanel.add(listarTodosButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        String[] columnNames = {"ID", "Cliente", "Sala (Tipo)", "Número", "Data", "Início", "Fim", "Status", "Valor Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        agendamentoTable = new JTable(tableModel);
        agendamentoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        agendamentoTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(agendamentoTable);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        carregarClientesNoComboBox();
        carregarSalasNoComboBox();
        exibirValorPorHoraAction(); 

        adicionarButton.addActionListener(e -> adicionarAgendamentoAction());
        atualizarButton.addActionListener(e -> atualizarAgendamentoAction());
        excluirButton.addActionListener(e -> excluirAgendamentoAction());
        buscarButton.addActionListener(e -> buscarAgendamentoPorIdAction());
        limparButton.addActionListener(e -> limparCampos());
        listarTodosButton.addActionListener(e -> carregarAgendamentosNaTabela());

        agendamentoTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && agendamentoTable.getSelectedRow() != -1) {
                preencherCamposComSelecao();
            }
        });

        salaComboBox.addActionListener(e -> exibirValorPorHoraAction());


        carregarAgendamentosNaTabela();
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
        if (idClienteLogado == null) { 
            clienteComboBox.setSelectedIndex(-1);
        }
        salaComboBox.setSelectedIndex(-1);
        dataField.setText("");
        horaInicioField.setText("");
        horaFimField.setText("");
        if (idClienteLogado == null) { 
            statusComboBox.setSelectedIndex(0); 
        } else {
            statusComboBox.setSelectedItem("Pendente"); 
        }
        valorPorHoraField.setText("0.00"); 
        agendamentoTable.clearSelection();
    }


    private void carregarClientesNoComboBox() {
        clienteComboBox.removeAllItems();
        List<Cliente> clientes = agendamentoController.listarTodosClientes();
        for (Cliente cliente : clientes) {
            clienteComboBox.addItem(cliente.getId() + " - " + cliente.getNome());
        }
        if (idClienteLogado == null) { 
            clienteComboBox.setSelectedIndex(-1);
        } else { 
            Cliente clienteLogado = agendamentoController.buscarClientePorId(idClienteLogado);
            if (clienteLogado != null) {
                clienteComboBox.addItem(clienteLogado.getId() + " - " + clienteLogado.getNome()); 
                clienteComboBox.setSelectedItem(clienteLogado.getId() + " - " + clienteLogado.getNome());
            }
            clienteComboBox.setEnabled(false);
        }
    }


    private void carregarSalasNoComboBox() {
        salaComboBox.removeAllItems();
        List<String> tiposSalas = agendamentoController.listarTodasSalas().stream()
                                    .map(Sala::getNomeTipo)
                                    .distinct()
                                    .sorted() 
                                    .collect(Collectors.toList());
        for (String tipo : tiposSalas) {
            salaComboBox.addItem(tipo);
        }
        salaComboBox.setSelectedIndex(-1);
    }


    private void carregarAgendamentosNaTabela() {
        tableModel.setRowCount(0); 
        List<Agendamento> agendamentos;

        if (idClienteLogado != null) {
            agendamentos = agendamentoController.listarTodosAgendamentos().stream()
                               .filter(a -> Objects.equals(a.getIdCliente(), idClienteLogado))
                               .collect(Collectors.toList());
        } else {

            agendamentos = agendamentoController.listarTodosAgendamentos();
        }


        for (Agendamento agendamento : agendamentos) {
            Cliente cliente = agendamentoController.buscarClientePorId(agendamento.getIdCliente());
            Sala sala = agendamentoController.buscarSalaPorId(agendamento.getIdSala());

            String nomeCliente = (cliente != null) ? cliente.getNome() : "Desconhecido";
            String nomeTipoSala = (sala != null) ? sala.getNomeTipo() : "Desconhecida";
            Integer numeroSala = (sala != null) ? sala.getNumeroSala() : null;

            tableModel.addRow(new Object[]{
                    agendamento.getId(),
                    nomeCliente,
                    nomeTipoSala,
                    numeroSala,
                    agendamento.getDataAgendamento().format(DATE_FORMATTER), 
                    agendamento.getHoraInicio().format(TIME_FORMATTER),     
                    agendamento.getHoraFim().format(TIME_FORMATTER),      
                    agendamento.getStatus(),
                    agendamento.getValorTotal() 
            });
        }
        agendamentoTable.revalidate();
        agendamentoTable.repaint();
    }


    private void preencherCamposComSelecao() {
        int selectedRow = agendamentoTable.getSelectedRow();
        if (selectedRow >= 0) {
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());

            if (idClienteLogado == null) {
                String clienteDisplay = tableModel.getValueAt(selectedRow, 1).toString();
                for (int i = 0; i < clienteComboBox.getItemCount(); i++) {
                    if (clienteComboBox.getItemAt(i).contains(clienteDisplay)) {
                        clienteComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            }

            String salaTipoDisplay = tableModel.getValueAt(selectedRow, 2).toString();
            salaComboBox.setSelectedItem(salaTipoDisplay);

            dataField.setText(tableModel.getValueAt(selectedRow, 4).toString()); 
            horaInicioField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            horaFimField.setText(tableModel.getValueAt(selectedRow, 6).toString()); 
            statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 7).toString());


            Integer numeroSalaExibido = (Integer) tableModel.getValueAt(selectedRow, 3); 
            String tipoSalaExibido = (String) tableModel.getValueAt(selectedRow, 2); 

            Sala salaAgendada = agendamentoController.listarTodasSalas().stream()
                                    .filter(s -> s.getNomeTipo().equals(tipoSalaExibido) && Objects.equals(s.getNumeroSala(), numeroSalaExibido)) // Use Objects.equals para Integer
                                    .findFirst()
                                    .orElse(null);

            if (salaAgendada != null) {
                valorPorHoraField.setText(salaAgendada.getValorHora().toString());
            } else {
                valorPorHoraField.setText("0.00");
            }
        }
    }


    private void exibirValorPorHoraAction() {
        if (salaComboBox.getSelectedItem() == null) {
            valorPorHoraField.setText("0.00");
            return;
        }

        String selectedSalaTipo = (String) salaComboBox.getSelectedItem();
        List<Sala> salasDoTipo = agendamentoController.listarTodasSalas().stream()
                                    .filter(s -> s.getNomeTipo().equals(selectedSalaTipo))
                                    .collect(Collectors.toList());
        if (!salasDoTipo.isEmpty()) {
            valorPorHoraField.setText(salasDoTipo.get(0).getValorHora().toString());
        } else {
            valorPorHoraField.setText("0.00");
        }
    }



    private void adicionarAgendamentoAction() {
        try {
            if (clienteComboBox.getSelectedItem() == null || salaComboBox.getSelectedItem() == null ||
                dataField.getText().isEmpty() || horaInicioField.getText().isEmpty() || horaFimField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos de agendamento são obrigatórios.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idCliente;
            if (idClienteLogado != null) {
                idCliente = idClienteLogado;
            } else {
                idCliente = Integer.parseInt(((String) clienteComboBox.getSelectedItem()).split(" - ")[0]);
            }

            String selectedSalaTipo = (String) salaComboBox.getSelectedItem();
            LocalDate dataAgendamento = LocalDate.parse(dataField.getText(), DATE_FORMATTER);
            LocalTime horaInicio = LocalTime.parse(horaInicioField.getText(), TIME_FORMATTER);
            LocalTime horaFim = LocalTime.parse(horaFimField.getText(), TIME_FORMATTER);
            String status = (String) statusComboBox.getSelectedItem();

            if (horaFim.isBefore(horaInicio) || horaFim.equals(horaInicio)) {
                JOptionPane.showMessageDialog(this, "A hora de fim deve ser posterior à hora de início.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Sala salaDisponivel = agendamentoController.encontrarSalaDisponivel(selectedSalaTipo, dataAgendamento, horaInicio, horaFim);

            if (salaDisponivel == null) {
                JOptionPane.showMessageDialog(this, "Nenhuma sala do tipo '" + selectedSalaTipo + "' disponível para este horário.", "Sala Indisponível", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Agendamento novoAgendamento = new Agendamento(idCliente, salaDisponivel.getId(), dataAgendamento, horaInicio, horaFim, status, BigDecimal.ZERO);
            if (agendamentoController.adicionarAgendamento(novoAgendamento)) {
                JOptionPane.showMessageDialog(this, "Agendamento adicionado com sucesso!\nSala atribuída: " + salaDisponivel.getNomeTipo() + " - " + salaDisponivel.getNumeroSala(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarAgendamentosNaTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar agendamento. Verifique os dados ou conflitos de horário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Verifique os formatos de data (DD/MM/YYYY) e hora (HH:MM).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            System.err.println("Erro de formato: " + e.getMessage());
        } catch (HeadlessException e) {
            System.err.println("Erro de HeadlessException: " + e.getMessage());
        }
    }


    private void atualizarAgendamentoAction() {
        try {
            int id = Integer.parseInt(idField.getText());
            if (clienteComboBox.getSelectedItem() == null || salaComboBox.getSelectedItem() == null ||
                dataField.getText().isEmpty() || horaInicioField.getText().isEmpty() || horaFimField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos de agendamento são obrigatórios.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idCliente;
            if (idClienteLogado != null) {
                idCliente = idClienteLogado;
            } else {
                idCliente = Integer.parseInt(((String) clienteComboBox.getSelectedItem()).split(" - ")[0]);
            }

            String selectedSalaTipo = (String) salaComboBox.getSelectedItem();
            LocalDate dataAgendamento = LocalDate.parse(dataField.getText(), DATE_FORMATTER);
            LocalTime horaInicio = LocalTime.parse(horaInicioField.getText(), TIME_FORMATTER);
            LocalTime horaFim = LocalTime.parse(horaFimField.getText(), TIME_FORMATTER);
            String status = (String) statusComboBox.getSelectedItem();

            if (horaFim.isBefore(horaInicio) || horaFim.equals(horaInicio)) {
                JOptionPane.showMessageDialog(this, "A hora de fim deve ser posterior à hora de início.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Agendamento agendamentoOriginal = agendamentoController.buscarAgendamentoPorId(id);
            int idSalaAtribuida = -1;

            if (agendamentoOriginal != null) {
                Sala salaOriginal = agendamentoController.buscarSalaPorId(agendamentoOriginal.getIdSala());
                boolean mudouTipoSala = (salaOriginal == null) || !selectedSalaTipo.equals(salaOriginal.getNomeTipo());
                boolean mudouData = !dataAgendamento.equals(agendamentoOriginal.getDataAgendamento());
                boolean mudouHoraInicio = !horaInicio.equals(agendamentoOriginal.getHoraInicio());
                boolean mudouHoraFim = !horaFim.equals(agendamentoOriginal.getHoraFim());

                if (mudouTipoSala || mudouData || mudouHoraInicio || mudouHoraFim) {
                    Sala novaSalaDisponivel = agendamentoController.encontrarSalaDisponivelParaAtualizacao(
                                                    selectedSalaTipo, dataAgendamento, horaInicio, horaFim, id);
                    if (novaSalaDisponivel == null) {
                        JOptionPane.showMessageDialog(this, "Nenhuma sala do tipo '" + selectedSalaTipo + "' disponível para este horário para atualização.", "Sala Indisponível", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    idSalaAtribuida = novaSalaDisponivel.getId();
                } else {
                    idSalaAtribuida = agendamentoOriginal.getIdSala();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Agendamento original não encontrado para atualização.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Agendamento agendamentoAtualizado = new Agendamento(id, idCliente, idSalaAtribuida, dataAgendamento, horaInicio, horaFim, status, BigDecimal.ZERO);
            if (agendamentoController.atualizarAgendamento(agendamentoAtualizado)) {
                Sala salaFinal = agendamentoController.buscarSalaPorId(idSalaAtribuida);
                String msgSalaAtribuida = (salaFinal != null) ? "\nSala atribuída: " + salaFinal.getNomeTipo() + " - " + salaFinal.getNumeroSala() : "";
                JOptionPane.showMessageDialog(this, "Agendamento atualizado com sucesso!" + msgSalaAtribuida, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                carregarAgendamentosNaTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar agendamento. Verifique o ID ou dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Verifique os formatos de data (DD/MM/YYYY) e hora (HH:MM).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            System.err.println("Erro de formato: " + e.getMessage());
        } catch (HeadlessException e) {
            System.err.println("Erro de HeadlessException: " + e.getMessage());
        }
    }

    private void excluirAgendamentoAction() {
        try {
            int id = Integer.parseInt(idField.getText());
            if (idClienteLogado != null) {
                Agendamento agendamento = agendamentoController.buscarAgendamentoPorId(id);
                if (agendamento == null || !Objects.equals(agendamento.getIdCliente(), idClienteLogado)) {
                    JOptionPane.showMessageDialog(this, "Você só pode excluir seus próprios agendamentos.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este agendamento?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (agendamentoController.excluirAgendamento(id)) {
                    JOptionPane.showMessageDialog(this, "Agendamento excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                    carregarAgendamentosNaTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir agendamento. ID não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Selecione um agendamento na tabela ou digite um ID válido para excluir.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void buscarAgendamentoPorIdAction() {
        try {
            String input = JOptionPane.showInputDialog(this, "Digite o ID do agendamento para buscar:");
            if (input == null) return;
            int id = Integer.parseInt(input);
            Agendamento agendamento = agendamentoController.buscarAgendamentoPorId(id);
            if (agendamento != null) {
                if (idClienteLogado != null && !Objects.equals(agendamento.getIdCliente(), idClienteLogado)) {
                    JOptionPane.showMessageDialog(this, "Você só pode visualizar seus próprios agendamentos.", "Acesso Negado", JOptionPane.ERROR_MESSAGE);
                    limparCampos();
                    return;
                }

                idField.setText(String.valueOf(agendamento.getId()));

                Cliente cliente = agendamentoController.buscarClientePorId(agendamento.getIdCliente());
                if (cliente != null) {
                    clienteComboBox.setSelectedItem(cliente.getId() + " - " + cliente.getNome());
                } else {
                    clienteComboBox.setSelectedIndex(-1);
                }

                Sala sala = agendamentoController.buscarSalaPorId(agendamento.getIdSala());
                if (sala != null) {
                    salaComboBox.setSelectedItem(sala.getNomeTipo());
                } else {
                    salaComboBox.setSelectedIndex(-1);
                }

                dataField.setText(agendamento.getDataAgendamento().format(DATE_FORMATTER));
                horaInicioField.setText(agendamento.getHoraInicio().format(TIME_FORMATTER));
                horaFimField.setText(agendamento.getHoraFim().format(TIME_FORMATTER));
                statusComboBox.setSelectedItem(agendamento.getStatus());
                if (sala != null) {
                    valorPorHoraField.setText(sala.getValorHora().toString());
                } else {
                    valorPorHoraField.setText("0.00");
                }

                JOptionPane.showMessageDialog(this, "Agendamento encontrado!", "Busca", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Agendamento com ID " + id + " não encontrado.", "Busca", JOptionPane.WARNING_MESSAGE);
                limparCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, digite um ID numérico válido.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }
}