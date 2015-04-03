package org.dogepool.practicalrx.services;

import java.util.ArrayList;
import java.util.List;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.*;
import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import static com.couchbase.client.java.query.dsl.Expression.*;

/**
 * Service to get user information.
 */
@Service
public class UserService {

    @Autowired
    Bucket couchbaseBucket;

    public Observable<User> getUser(long id) {
        return findAll()
                .filter(u -> u.id == id)
                .last();
    }

    public Observable<User> getUserByLogin(String login) {
        return findAll()
                .filter(u -> login.equals(u.nickname))
                .last();
    }

    public Observable<User> findAll() {
        Statement statement = Select.select("avatarId", "bio", "displayName", "id", "nickname").from(x("default"))
                .where(x("type").eq(s("user"))).groupBy(x("displayName"));
        QueryResult queryResult = couchbaseBucket.query(statement);
        List<User> users = new ArrayList<User>();
        for (QueryRow qr : queryResult ) {
            users.add(User.fromJsonObject(qr.value()));
        }
        return Observable.from(users);
    }
}
