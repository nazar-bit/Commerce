package pjv.hello.vasylnaz.windfarmbackend;

public class Details {

    private final int id;
    private String name;

    public Details(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
