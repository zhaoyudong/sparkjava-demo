package me.zyd.dao;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by zhaoyudong on 2018/1/31.
 */
public interface UserDao {
    void save(Document user);

    Document findOne(ObjectId id);

    Document update(Document user);

    long deleteOne(ObjectId id);
}
