package cool.muyucloud.housing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Entity
@Table(schema = "favourite")
@IdClass(Favourite.FavouriteId.class)
public class Favourite {
    private Integer uid;
    private Long aid;

    public Favourite(Integer uid, Long aid) {
        this.uid = uid;
        this.aid = aid;
    }

    @Id
    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Id
    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public static class FavouriteId implements Serializable {
        @Autowired
        private Integer uid;
        @Autowired
        private Long aid;

        public Integer getUid() {
            return uid;
        }

        public void setUid(Integer uid) {
            this.uid = uid;
        }

        public Long getAid() {
            return aid;
        }

        public void setAid(Long aid) {
            this.aid = aid;
        }
    }
}
