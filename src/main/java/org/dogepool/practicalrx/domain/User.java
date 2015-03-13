package org.dogepool.practicalrx.domain;

public class User {

    public static final User USER = new User(0L, "user0", "Test User", "Story of my life.\nEnd of Story.", "12434");
    public static final User OTHERUSER = new User(1L, "richUser", "Richie Rich", "I'm rich I have dogecoin", "45678");

    public final long id;
    public final String nickname;
    public final String displayName;
    public final String bio;
    public final String avatarId;

    public User(long id, String nickname, String displayName, String bio, String avatarId) {
        this.id = id;
        this.nickname = nickname;
        this.displayName = displayName;
        this.bio = bio;
        this.avatarId = avatarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (id != user.id) {
            return false;
        }
        if (!nickname.equals(user.nickname)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + nickname.hashCode();
        return result;
    }
}
