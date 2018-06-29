/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.logic.imp;

import app.config.db.DbManager;
import app.logic.interfaces.ServicioManager;
import app.logic.pojo.ServicioBean;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
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
public class ServicioManagerImp implements ServicioManager {

    private static final Logger logger = Logger.getLogger(ServicioManagerImp.class.getName());
    private static DbManager dbManager = DbManager.getDbManager();
    private static MongoClient mongoClient = dbManager.getMongoClient();
    private static MongoDatabase mongoDb = dbManager.getMongoDb();
    private static MongoCollection<Document> mongoCollection = dbManager.getMongoCollection();

    @Override
    public Collection<ServicioBean> getServicios() {
        mongoCollection = mongoDb.getCollection("servicio");

        List<ServicioBean> servicios = new ArrayList<ServicioBean>();

        MongoCursor<Document> docs = mongoCollection.find().iterator();

        while (docs.hasNext()) {
            Document doc = docs.next();
            servicios.add(new ServicioBean(doc.getInteger("id"), doc.getString("name"),
                    doc.getString("description"), doc.getDouble("price")));
        }
        docs.close();
        return servicios;
    }

    @Override
    public boolean deleteServicio(ServicioBean servicio) {
        mongoCollection = mongoDb.getCollection("servicio");
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("id", servicio.getId());
        Document doc = mongoCollection.find().first();
        mongoCollection.deleteOne(doc);

        return true;
    }

    @Override
    public boolean updateServicio(ServicioBean servicio) {
        mongoCollection = mongoDb.getCollection("servicio");

        BasicDBObject updateDocument = new BasicDBObject();
        BasicDBObject changes = new BasicDBObject();
        changes.append("name", servicio.getName());
        changes.append("description", servicio.getDescription());
        changes.append("price", servicio.getPrice());
        
        updateDocument.append("$set", changes);
        BasicDBObject searchQuery = new BasicDBObject().append("id", servicio.getId());
        mongoCollection.updateOne(searchQuery, updateDocument);
        return true;
    }

    @Override
    public boolean insertServicio(ServicioBean servicio) {
        mongoCollection = mongoDb.getCollection("servicio");
        Document doc = new Document();
        doc.put("id", dbManager.getMaxId("servicio"));
        doc.put("name", servicio.getName());
        doc.put("description", servicio.getDescription());
        doc.put("price", servicio.getPrice());
        mongoCollection.insertOne(doc);
        return true;
    }

}
