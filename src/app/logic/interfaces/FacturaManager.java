/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.interfaces;

import app.logic.pojo.FacturaBean;
import java.util.Collection;

/**
 *
 * @author Carlos
 */
public interface FacturaManager {
    public Collection<FacturaBean> getFacturas();
    public boolean deleteFactura(FacturaBean factura);
    public boolean updateFactura(FacturaBean factura);
    public boolean insertFactura(FacturaBean factura);
}
