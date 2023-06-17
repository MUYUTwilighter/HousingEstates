package cool.muyucloud.housing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estate")
public class Estate {
    @Id
    private Long aid;
    private Integer owner;
    private Double square;
    private Double price;

    public Estate(long aid, int uid, double price, double square) {
        this.aid = aid;
        this.owner = uid;
        this.price = price;
        this.square = square;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Double getSquare() {
        return square;
    }

    public void setSquare(Double square) {
        this.square = square;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
