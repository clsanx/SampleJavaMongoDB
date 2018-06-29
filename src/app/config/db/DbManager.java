package app.config.db;

import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Indexes.descending;
import java.util.logging.Logger;
import org.bson.Document;

public class DbManager {
    
    private static final Logger LOGGER = Logger.getLogger("Control");
    private MongoClient mongoClient;
    private MongoDatabase mongoDb;
    private MongoCollection<Document> mongoCollection;
    public static DbManager dbManager;

    private DbManager() {
        mongoClient = new MongoClient("localhost", 27017);
        mongoDb = mongoClient.getDatabase("servtecmongodb");
    }
    
    public static synchronized DbManager getDbManager() {
        if ( dbManager == null ) {
            dbManager = new DbManager();
        }
        return dbManager;
    }

    /**
     * @return the mongoClient
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    /**
     * @return the mongoDb
     */
    public MongoDatabase getMongoDb() {
        return mongoDb;
    }

    /**
     * @return the mongoCollection
     */
    public MongoCollection<Document> getMongoCollection() {
        return mongoCollection;
    }

    public Integer getMaxId(String collection) {
        mongoCollection = mongoDb.getCollection(collection);
        Integer id = 0;
        MongoCursor<Document> docs = mongoCollection.find().sort(descending("id")).iterator();
        //docs.next().getInteger("id");
        while (docs.hasNext()) {
            id = docs.next().getInteger("id");
            break;
        }
        docs.close();
        return id + 1;
    }
}
