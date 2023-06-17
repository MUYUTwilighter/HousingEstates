package cool.muyucloud.housing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {
    public static final byte TYPE_BANNED = 0x00;
    public static final byte TYPE_USER = 0x01;
    public static final byte TYPE_ESTATE_ADMIN = 0x02;
    public static final byte TYPE_USER_ADMIN = 0x03;
    public static final byte TYPE_DATABASE_ADMIN = 0x04;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private Byte type = TYPE_USER;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
