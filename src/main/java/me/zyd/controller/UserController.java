package me.zyd.controller;

import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import me.zyd.dao.UserDao;
import me.zyd.dao.impl.UserDaoImpl;
import me.zyd.result.ErrorCode;
import org.bson.Document;
import org.bson.types.ObjectId;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Date;

/**
 * Created by zhaoyudong on 2018/1/31.
 */
public class UserController {

    private UserDao userDao;
    private Gson gson = new Gson();

    public UserController(MongoDatabase mongoDatabase) {
        this.userDao = new UserDaoImpl(mongoDatabase);
    }

    public Route createUser = (Request request, Response response) -> {
        String body = request.body();
        Document document = Document.parse(body);
        if (document.getString("name") == null || document.getString("name").equalsIgnoreCase(""))
            throw new IllegalArgumentException();
        document.put("_id", ObjectId.get());
        document.put("created", new Date());
        document.put("updated", new Date());
        userDao.save(document);
        return document;
    };

    public Route deleteUser = (Request request, Response response) -> {
        String userId = request.params(":id");
        if (!ObjectId.isValid(userId))
            throw new IllegalArgumentException();
        ObjectId objectId = new ObjectId(userId);
        userDao.deleteOne(objectId);
        return ErrorCode.SUCCESS;
    };

    public Route findUser = (Request request, Response response) -> {
        String userId = request.params(":id");
        if (!ObjectId.isValid(userId))
            throw new IllegalArgumentException();
        ObjectId objectId = new ObjectId(userId);
        return userDao.findOne(objectId);
    };

    public Route updateUser = (Request request, Response response) -> {
        String userId = request.params(":id");
        if (!ObjectId.isValid(userId))
            throw new IllegalArgumentException();
        ObjectId objectId = new ObjectId(userId);
        String body = request.body();
        Document document = Document.parse(body);
        if (document.getString("name") == null || document.getString("name").equalsIgnoreCase(""))
            throw new IllegalArgumentException();
        document.put("_id", objectId);
        return userDao.update(document);
    };
}
