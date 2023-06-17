package cool.muyucloud.housing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "address_id")
public class AddressId {
    /**
     * This is an Address ID resolve database
     * <p>
     *     aid: an ID code used for identify a geolocation, made of 8-byte long integer, each byte represents a code for an division code.
     * </p>
     * <p>
     *     the byte reference follows this sequence: province, prefecture, county, township, village, building#, unit, room#
     * </p>
     * <p>
     *     For example, if 0x01 refers to Anhui Province, 0x01 refers to He-fei prefecture,
     *     the aid is 0x0101000000000000; and this code refers to every position in Anhui, He-fei.
     * </p>
     */
    @Id
    private Long aid;
    private String division;    // 区划名称

    public AddressId(long aid, String division) {
        this.aid = aid;
        this.division = division;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
