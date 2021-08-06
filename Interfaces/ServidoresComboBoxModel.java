
package Interfaces;

import Interfaces_Remotas.Servicos_servidor;
import java.rmi.Naming;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author felipe
 */
public class ServidoresComboBoxModel extends AbstractListModel implements ComboBoxModel{
    
    private List<String> servidores_dispo;
    private String selecionado;
    
    public ServidoresComboBoxModel()
    {
        Servicos_servidor lista = null;
        
        try{
            
            lista = (Servicos_servidor) Naming.lookup("//192.168.0.13/servico_servidor");
            this.servidores_dispo = lista.Listar();
        }
        catch(Exception e){
            JFrame frame = new JFrame("Erro");
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public int getSize() {
        return servidores_dispo.size();
    }

    @Override
    public Object getElementAt(int index) {
        return this.servidores_dispo.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem){
            this.selecionado = (String) anItem;
            fireContentsChanged(this.servidores_dispo,0,this.servidores_dispo.size());
    }

    @Override
    public Object getSelectedItem() {
        return this.selecionado;
    }
 
    public void addServidores(String s){
        this.servidores_dispo.add(s);
    }
    
    public void reset(){
        this.servidores_dispo.clear();
    }
}
