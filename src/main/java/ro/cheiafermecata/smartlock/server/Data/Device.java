package ro.cheiafermecata.smartlock.server.Data;

import javax.persistence.Entity;

@Entity
public class Device {

    private Long id;

    private Long usersId;

    public Device(Long usersId, String name) {
        this.usersId = usersId;
        this.name = name;
    }

    public Device(Long id, Long usersId, String name) {
        this.id = id;
        this.usersId = usersId;
        this.name = name;
    }

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
