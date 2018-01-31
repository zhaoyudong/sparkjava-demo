package me.zyd.dao.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import me.zyd.dao.UserDao;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.ReturnDocument.AFTER;
import static com.mongodb.client.model.Updates.*;

/**
 * Created by zhaoyudong on 2018/1/31.
 */
public class UserDaoImpl implements UserDao {

    private MongoCollection<Document> mongoCollection;

    public UserDaoImpl(MongoDatabase mongoDatabase) {
        this.mongoCollection = mongoDatabase.getCollection("users");
    }

    public void save(Document user) {
        this.mongoCollection.insertOne(user);
    }

    public Document findOne(ObjectId id) {
        return this.mongoCollection.find(eq("_id", id)).first();
    }

    public Document update(Document user) {
        return this.mongoCollection.findOneAndUpdate(eq("_id", user.get("_id")), combine(set("name", user.get("name")),
                currentDate("updated")), new FindOneAndUpdateOptions().returnDocument(AFTER));
    }

    public long deleteOne(ObjectId id) {
        return this.mongoCollection.deleteOne(eq("_id", id)).getDeletedCount();
    }
}
