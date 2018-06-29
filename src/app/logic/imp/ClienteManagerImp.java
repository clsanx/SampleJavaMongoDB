/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.imp;

import app.config.db.DbManager;
import app.logic.interfaces.ClienteManager;
import app.logic.pojo.ClienteBean;
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
public class ClienteManagerImp implements ClienteManager {

    private static final Logger logger = Logger.getLogger(ClienteManagerImp.class.getName());
    private static DbManager dbManager = DbManager.getDbManager();
    private static MongoClient mongoClient = dbManager.getMongoClient();
    private static MongoDatabase mongoDb = dbManager.getMongoDb();
    private static MongoCollection<Document> mongoCollection = dbManager.getMongoCollection();

    @Override
    public Collection<ClienteBean> getClientes() {
        mongoCollection = mongoDb.getCollection("cliente");

        List<ClienteBean> clientes = new ArrayList<ClienteBean>();

        MongoCursor<Document> docs = mongoCollection.find().iterator();

        while (docs.hasNext()) {
            Document doc = docs.next();
            clientes.add(new ClienteBean(doc.getInteger("id"), doc.getString("name"),
                    doc.getString("lastname"), doc.getString("phone"),
                    doc.getString("email"), doc.getString("zipcode")));
        }
        docs.close();
        return clientes;
    }

    @Override
    public boolean deleteCliente(ClienteBean cliente) {
        mongoCollection = mongoDb.getCollection("cliente");
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("id", cliente.getId());
        Document doc = mongoCollection.find().first();
        mongoCollection.deleteOne(doc);
        return true;
    }

    @Override
    public boolean updateCliente(ClienteBean cliente) {
        mongoCollection = mongoDb.getCollection("cliente");
        BasicDBObject updateDocument = new BasicDBObject();
        BasicDBObject changes = new BasicDBObject();
        changes.append("name", cliente.getName());
        changes.append("lastname", cliente.getLastname());
        changes.append("phone", cliente.getPhone());
        changes.append("email", cliente.getEmail());
        changes.append("zipcode", cliente.getZipcode());
        
        updateDocument.append("$set", changes);
        BasicDBObject searchQuery = new BasicDBObject().append("id", cliente.getId());
        mongoCollection.updateOne(searchQuery, updateDocument);
        return true;
    }

    @Override
    public boolean insertCliente(ClienteBean cliente) {
        mongoCollection = mongoDb.getCollection("cliente");
        Document doc = new Document();
        doc.put("id", dbManager.getMaxId("cliente"));
        doc.put("name", cliente.getName());
        doc.put("lastname", cliente.getLastname());
        doc.put("phone", cliente.getPhone());
        doc.put("email", cliente.getEmail());
        doc.put("zipcode", cliente.getZipcode());
        mongoCollection.insertOne(doc);
        return true;
    }

}
