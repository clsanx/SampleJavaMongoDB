/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.imp;

import app.config.db.DbManager;
import app.logic.interfaces.FacturaManager;
import app.logic.pojo.ClienteBean;
import app.logic.pojo.FacturaBean;
import app.logic.pojo.ServicioBean;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import org.bson.Document;

/**
 *
 * @author Carlos Santos
 */
public class FacturaManagerImp implements FacturaManager {

    private static final Logger logger = Logger.getLogger(FacturaManagerImp.class.getName());
    private static DbManager dbManager = DbManager.getDbManager();
    private static MongoClient mongoClient = dbManager.getMongoClient();
    private static MongoDatabase mongoDb = dbManager.getMongoDb();
    private static MongoCollection<Document> mongoCollection = dbManager.getMongoCollection();

    @Override
    public Collection<FacturaBean> getFacturas() {
        mongoCollection = mongoDb.getCollection("factura");

        List<FacturaBean> facturas = new ArrayList<FacturaBean>();

        MongoCursor<Document> docs = mongoCollection.find().iterator();

        while (docs.hasNext()) {
            Document doc = docs.next();

            // Cliente
            Document docCliente = (Document) doc.get("cliente");
            ClienteBean cliente = new ClienteBean(docCliente.getInteger("id"), docCliente.getString("name"),
                    docCliente.getString("lastname"), docCliente.getString("phone"), docCliente.getString("email"), docCliente.getString("zipcode"));

            // Servicios
            ArrayList<Document> docServicios = (ArrayList<Document>) doc.get("servicios");
            List<ServicioBean> servicios = new ArrayList<>();
            docServicios.forEach(ds -> servicios.add(new ServicioBean(ds.getInteger("id"), ds.getString("name"), ds.getString("description"), ds.getDouble("price"))));

            // Facturas
            facturas.add(new FacturaBean(doc.getInteger("id"), cliente, servicios, doc.getDate("date"), doc.getDouble("total")));

        }
        docs.close();

        return facturas;
    }

    @Override
    public boolean deleteFactura(FacturaBean factura) {
        mongoCollection = mongoDb.getCollection("factura");
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("id", factura.getId());
        Document doc = mongoCollection.find().first();
        mongoCollection.deleteOne(doc);
        return true;
    }

    @Override
    public boolean updateFactura(FacturaBean factura) {
        mongoCollection = mongoDb.getCollection("factura");
        BasicDBObject updateDocument = new BasicDBObject();
        BasicDBObject changes = new BasicDBObject();
        changes.append("date", factura.getDate());
        changes.append("total", factura.getTotal());
        
        // Cliente
        Document docCliente = new Document();
        docCliente.put("id", factura.getCliente().getId());
        docCliente.put("name", factura.getCliente().getName());
        docCliente.put("lastname", factura.getCliente().getLastname());
        docCliente.put("phone", factura.getCliente().getPhone());
        docCliente.put("email", factura.getCliente().getEmail());
        docCliente.put("zipcode", factura.getCliente().getZipcode());
        changes.append("cliente", docCliente);
        
        // Servicios
        ArrayList<Document> docServicios = new ArrayList<>();
        for (ServicioBean servicio : factura.getServicios()) {
            Document docServicio = new Document();
            docServicio.put("id", servicio.getId());
            docServicio.put("name", servicio.getName());
            docServicio.put("descripcion", servicio.getDescription());
            docServicio.put("price", servicio.getPrice());
            docServicios.add(docServicio);
        }
        changes.append("servicios", docServicios);

        updateDocument.append("$set", changes);
        BasicDBObject searchQuery = new BasicDBObject().append("id", factura.getId());
        mongoCollection.updateOne(searchQuery, updateDocument);
        return true;
    }

    @Override
    public boolean insertFactura(FacturaBean factura) {
        mongoCollection = mongoDb.getCollection("factura");

        Document docFactura = new Document();
        docFactura.put("id", dbManager.getMaxId("factura"));
        docFactura.put("date", factura.getDate());
        docFactura.put("total", factura.getTotal());

        // Doc cliente
        Document docCliente = new Document();
        docCliente.put("id", factura.getCliente().getId());
        docCliente.put("name", factura.getCliente().getName());
        docCliente.put("lastname", factura.getCliente().getLastname());
        docCliente.put("phone", factura.getCliente().getPhone());
        docCliente.put("email", factura.getCliente().getEmail());
        docCliente.put("zipcode", factura.getCliente().getZipcode());

        // Add cliente to factura
        docFactura.put("cliente", docCliente);

        // ArrayList Servicios
        ArrayList<Document> docServicios = new ArrayList<>();

        for (ServicioBean servicio : factura.getServicios()) {
            Document docServicio = new Document();
            docServicio.put("id", servicio.getId());
            docServicio.put("name", servicio.getName());
            docServicio.put("descripcion", servicio.getDescription());
            docServicio.put("price", servicio.getPrice());
            docServicios.add(docServicio);
        }
        docFactura.put("servicios", docServicios);
        mongoCollection.insertOne(docFactura);
        return true;
    }

}
