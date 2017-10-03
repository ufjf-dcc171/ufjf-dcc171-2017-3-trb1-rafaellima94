package ufjf.dcc171;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControlePedidos extends JFrame {
    private List<Mesas> dtMesas;
    private JList<Mesas> lstMesas = new JList<>(new DefaultListModel<>());
    private List<Itens> dtItens;
    private JList<Itens> lstItens = new JList<>(new DefaultListModel<>());
    
    private JLabel lblItem = new JLabel("Nome do item");
    private JTextField txtItem = new JTextField(10);
    private JLabel lblQuantidade = new JLabel("Quantidade");
    private JTextField txtQuantidade = new JTextField(10);
    private JLabel lblValor = new JLabel("Valor");
    private JTextField txtValor = new JTextField(10);
    private JLabel lblItens = new JLabel("Itens");
    private JScrollPane scrollItens = new JScrollPane();
    
    private JButton btnApagarItem = new JButton("Apagar item");
    private JButton btnNovoItem = new JButton("Novo item");
    private JButton btnFecharPedido = new JButton("Fechar pedido");
    
    private JButton btnAbrirPedido = new JButton("Novo pedido");
            
    public ControlePedidos(List<Mesas> dtMesas) throws HeadlessException{
        super("Controle de Pedidos");
        
        this.dtMesas = dtMesas;
        lstMesas.setModel(new MesasListModel(dtMesas));
        add(new JScrollPane(lstMesas), BorderLayout.WEST);
        
        JPanel dadosPedidos = new JPanel(new GridLayout(9,1));
        dadosPedidos.add(lblItem);
        dadosPedidos.add(txtItem);
        dadosPedidos.add(lblQuantidade);
        dadosPedidos.add(txtQuantidade);
        dadosPedidos.add(lblValor);
        dadosPedidos.add(txtValor);
        dadosPedidos.add(lblItens);
        dadosPedidos.add(scrollItens);
        JPanel controleItens = new JPanel(new GridLayout(1,3));
        controleItens.add(btnApagarItem);
        controleItens.add(btnNovoItem);
        controleItens.add(btnFecharPedido);
        dadosPedidos.add(controleItens);
        add(dadosPedidos, BorderLayout.CENTER);
        
        JPanel controlePedidos = new JPanel(new GridLayout(1,1));
        controlePedidos.add(btnAbrirPedido);
        add(controlePedidos, BorderLayout.SOUTH);
        
        dadosPedidos.setVisible(false);
        controlePedidos.setVisible(false);
        
        lstMesas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Mesas selMesa = lstMesas.getSelectedValue();
                
                if(selMesa.getPedido() == null || selMesa.getPedido().getHoraAbertura() == null) {
                    dadosPedidos.setVisible(false);
                    controlePedidos.setVisible(true);
                } else if(selMesa.getPedido().getItens() == null) {
                    dadosPedidos.setVisible(true);
                    controlePedidos.setVisible(false);
                    
                    dtItens = new ArrayList<>();
                    lstItens.setModel(new ItensListModel(dtItens));
                    scrollItens.setViewportView(lstItens);
                } else {
                    dadosPedidos.setVisible(true);
                    controlePedidos.setVisible(false);
                    
                    dtItens = selMesa.getPedido().getItens();
                    lstItens.setModel(new ItensListModel(dtItens));
                    scrollItens.setViewportView(lstItens);
                }
            }
        });
        
        btnAbrirPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mesas selMesa = lstMesas.getSelectedValue();
                Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Brazil/East"));
                String horaAtual = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
                Pedidos pedido = new Pedidos(horaAtual);
                selMesa.setPedido(pedido);
                dadosPedidos.setVisible(true);
                controlePedidos.setVisible(false);
                dtItens = new ArrayList<>();
                lstItens.setModel(new ItensListModel(dtItens));
                scrollItens.setViewportView(lstItens);
            }
        });
        
        btnNovoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtItem.getText().isEmpty() && !txtQuantidade.getText().isEmpty() && !txtValor.getText().isEmpty()) {
                    Integer qtd = Integer.parseInt(txtQuantidade.getText());
                    double val = Double.parseDouble(txtValor.getText());
                    
                    Itens item = new Itens(txtItem.getText(), qtd, val);
                    dtItens.add(item);
                    lstItens.clearSelection();
                    lstItens.updateUI();
                }
            }
        });
        
        btnApagarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!lstItens.isSelectionEmpty()){
                    dtItens.remove(lstItens.getSelectedValue());
                    lstItens.clearSelection();
                    lstItens.updateUI();
                }
            }
        });
        
        btnFecharPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mesas selMesa = lstMesas.getSelectedValue();
                Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Brazil/East"));
                String horaAtual = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
                selMesa.getPedido().setHoraFechamento(horaAtual);
                double valTotal = 0;
                
                StringBuilder s = new StringBuilder("<html><body><h1>Pedido fechado com sucesso!</h1>");
                for(Itens item : selMesa.getPedido().getItens()) {
                    valTotal += item.getValor()*item.getQuantidade();
                    s.append("<p>");
                    s.append(item);
                    s.append("</p>");
                }
                s.append("</br><p>Valor total: R$");
                s.append(valTotal);
                s.append("</p></body></html>");
                
                JOptionPane.showMessageDialog(null, s, "Fechamento", JOptionPane.INFORMATION_MESSAGE);
                
                selMesa.setPedido(new Pedidos());
                dadosPedidos.setVisible(false);
                controlePedidos.setVisible(true);
            }
        });
    }
}
