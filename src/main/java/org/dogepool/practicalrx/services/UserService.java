package org.dogepool.practicalrx.services;

import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;

import java.util.ArrayList;
import java.util.List;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.QueryResult;
import com.couchbase.client.java.query.QueryRow;
import com.couchbase.client.java.query.Select;
import com.couchbase.client.java.query.Statement;
import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.error.DogePoolException;
import org.dogepool.practicalrx.error.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service to get user information.
 */
@Service
public class UserService {

    @Autowired
    private Bucket couchbaseBucket;

    @Value("${store.enableFindAll:false}")
    private boolean useCouchbaseForFindAll;

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
        if (useCouchbaseForFindAll) {
            try {
                Statement statement = Select.select("avatarId", "bio", "displayName", "id", "nickname").from(x("default"))
                        .where(x("type").eq(s("user"))).groupBy(x("displayName"));
                QueryResult queryResult = couchbaseBucket.query(statement);
                List<User> users = new ArrayList<User>();
                for (QueryRow qr : queryResult) {
                    users.add(User.fromJsonObject(qr.value()));
                }
                return Observable.from(users);
            } catch (Exception e) {
                return Observable.error(new DogePoolException("Error while getting list of users from database",
                        Error.DATABASE, HttpStatus.INTERNAL_SERVER_ERROR, e));
            }
        } else {
            return Observable.just(User.USER, User.OTHERUSER);
        }
    }
}
