/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.interfaces;

import app.logic.pojo.ClienteBean;
import java.util.Collection;

/**
 *
 * @author Carlos
 */
public interface ClienteManager {
    public Collection<ClienteBean> getClientes();
    public boolean deleteCliente(ClienteBean cliente);
    public boolean updateCliente(ClienteBean cliente);
    public boolean insertCliente(ClienteBean cliente);
}
