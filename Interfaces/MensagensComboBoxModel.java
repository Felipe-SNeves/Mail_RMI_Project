
package Interfaces;

import Interfaces_Remotas.Servicos_mensagem;
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
public class MensagensComboBoxModel extends AbstractListModel implements ComboBoxModel{
    
    private List<String> assuntos;
    private String selecionado;
    
    public MensagensComboBoxModel(String email)
    {
        Servicos_mensagem lista = null;
        
        try{
            
            lista = (Servicos_mensagem) Naming.lookup("//192.168.0.13/servico_mensagem");
            this.assuntos = lista.Listar(email);
        }
        catch(Exception e){
            JFrame frame = new JFrame("Erro");
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public int getSize() {
        return assuntos.size();
    }

    @Override
    public Object getElementAt(int index) {
        return this.assuntos.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem){
            this.selecionado = (String) anItem;
            fireContentsChanged(this.assuntos,0,this.assuntos.size());
    }

    @Override
    public Object getSelectedItem() {
        return this.selecionado;
    }
 
    public void addServidores(String s){
        this.assuntos.add(s);
    }
    
    public void reset(){
        this.assuntos.clear();
    }
}