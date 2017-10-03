package ufjf.dcc171;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ControlePedidos extends JFrame {
    private List<Mesas> dtMesas;
    private JList<Mesas> lstMesas = new JList<>(new DefaultListModel<>());
    
    private JLabel lblQuantidade = new JLabel("Quantidade");
    private JTextField txtQuantidade = new JTextField(10);
    private JLabel lblItem = new JLabel("Item");
    private JTextField txtItem = new JTextField(10);
    private JLabel lblValor = new JLabel("Valor");
    private JTextField txtValor = new JTextField(10);
    
    private JButton btnAbrirPedido = new JButton("Novo pedido");
    private JButton btnFecharPedido = new JButton("Fechar pedido");
            
    public ControlePedidos(List<Mesas> dtMesas) throws HeadlessException{
        super("Controle de Pedidos");
        
        this.dtMesas = dtMesas;
        lstMesas.setModel(new MesasListModel(dtMesas));
        add(new JScrollPane(lstMesas), BorderLayout.CENTER);
        
        JPanel dadosPedidos = new JPanel(new GridLayout(5,2));
        dadosPedidos.add(lblQuantidade);
        dadosPedidos.add(txtQuantidade);
        dadosPedidos.add(lblItem);
        dadosPedidos.add(txtItem);
        dadosPedidos.add(lblValor);
        dadosPedidos.add(txtValor);
        add(dadosPedidos, BorderLayout.EAST);
        
        JPanel controlePedidos = new JPanel(new GridLayout(2,1));
        controlePedidos.add(btnAbrirPedido);
        controlePedidos.add(btnFecharPedido);
        add(controlePedidos, BorderLayout.SOUTH);
        
        dadosPedidos.setVisible(false);
        controlePedidos.setVisible(false);
        
        lstMesas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Mesas selMesa = lstMesas.getSelectedValue();
                
                if(selMesa.getPedido() == null) {
                    dadosPedidos.setVisible(false);
                    controlePedidos.setVisible(true);
                } else {
                    dadosPedidos.setVisible(true);
                    controlePedidos.setVisible(false);
                }
            }
        });
    }
}
