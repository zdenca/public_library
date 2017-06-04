package cz.klimesova.public_library.dao;

/**
 * Created by Zdenca on 5/30/2017.
 */
public class ConnectionProperties {
    private final String connection;
    private final String user;
    private final String password;

    public ConnectionProperties(String connection, String user, String password) {
        this.connection = connection;
        this.user = user;
        this.password = password;
    }

    public String getConnection() {
        return connection;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "DbProperties{" +
                "connection='" + connection + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
