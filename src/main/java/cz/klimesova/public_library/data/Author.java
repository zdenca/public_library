package cz.klimesova.public_library.data;

/**
 * Created by Zdenca on 5/29/2017.
 */
public class Author {
    private int id;
    private Name name;

    public Author( int id, Name name) {
        this.id = id;
        this.name = name;

    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
