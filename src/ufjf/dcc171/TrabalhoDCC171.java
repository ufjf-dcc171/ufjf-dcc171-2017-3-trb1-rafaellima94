package ufjf.dcc171;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public class TrabalhoDCC171 {
    public static void main(String[] args) {
        List<Mesas> dados = getAllMesas();
        ControlePedidos controle = new ControlePedidos(dados);
        controle.setSize(600, 500);
        controle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controle.setLocationRelativeTo(null);
        controle.setVisible(true);
    }

    private static List<Mesas> getAllMesas() {
        List<Mesas> mesas = new ArrayList<Mesas>();
        
        for(int i = 1; i < 11; i++) {
            mesas.add(new Mesas(i));
        }
        
        Mesas fullMesa = new Mesas(11);
        Itens item = new Itens("Picanha", 2, 60.00);
        List<Itens> itens = new ArrayList<Itens>();
        itens.add(item);
        Pedidos pedido = new Pedidos("10:10");
        pedido.setItens(itens);
        fullMesa.setPedido(pedido);
        mesas.add(fullMesa);
        
        return mesas;
    }
}
