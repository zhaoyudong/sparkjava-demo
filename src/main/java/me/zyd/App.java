package me.zyd;

import com.google.gson.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import me.zyd.controller.UserController;
import me.zyd.result.ErrorCode;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

import static spark.Spark.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        MongoCredential mongoCredential = MongoCredential.createMongoCRCredential("test1", "test", "test1".toCharArray());
        MongoClient mongoClient = new MongoClient(new ServerAddress("192.168.10.68"), mongoCredential, MongoClientOptions.builder().build());
        UserController userController = new UserController(mongoClient.getDatabase("test"));
        Gson gson = new GsonBuilder().registerTypeAdapter(ObjectId.class, new JsonSerializer<ObjectId>() {
            @Override
            public JsonElement serialize(ObjectId id, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(id.toHexString());
            }
        }).create();

        port(8088);
        get("users/:id", userController.findUser, gson::toJson);
        post("users", userController.createUser, gson::toJson);
        put("users/:id", userController.updateUser, gson::toJson);
        delete("users/:id", userController.deleteUser, gson::toJson);

        exception(IllegalArgumentException.class, (exception, request, response) -> {
            response.body(gson.toJson(ErrorCode.ARGUMENT_ERR));
        });

        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });
    }
}
