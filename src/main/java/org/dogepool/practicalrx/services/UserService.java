package org.dogepool.practicalrx.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.*;
import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import static com.couchbase.client.java.query.dsl.Expression.*;

/**
 * Service to get user information.
 */
@Service
public class UserService {

    @Autowired
    private Bucket couchbaseBucket;

    @Value("${store.enableFindAll:false}")
    private boolean useCouchbaseForFindAll;

    public User getUser(long id) {
        for (User user : findAll()) {
            if (user.id == id) {
                return user;
            }
        }

        return null; //TODO any better way of doing this in Java 8?
    }

    public User getUserByLogin(String login) {
        for (User user : findAll()) {
            if (login.equals(user.nickname)) {
                return user;
            }
        }

        return null; //TODO any better way of doing this in Java 8?
    }

    public List<User> findAll() {
        if (useCouchbaseForFindAll) {
            Statement statement = Select.select("avatarId", "bio", "displayName", "id", "nickname").from(x("default"))
                                        .where(x("type").eq(s("user"))).groupBy(x("displayName"));
            QueryResult queryResult = couchbaseBucket.query(statement);
            List<User> users = new ArrayList<User>();
            for (QueryRow qr : queryResult) {
                users.add(User.fromJsonObject(qr.value()));
            }
            return users;
        } else {
            return Arrays.asList(User.USER, User.OTHERUSER);
        }
    }
}
