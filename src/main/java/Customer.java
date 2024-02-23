import java.sql.Date;

public class Customer {
    private Integer Id;
    private String name;
    private String email;
    private String address;

    public Customer(Integer id, String name, String email, String address) {
        Id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Customer(String name, String email, String address) {

        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
