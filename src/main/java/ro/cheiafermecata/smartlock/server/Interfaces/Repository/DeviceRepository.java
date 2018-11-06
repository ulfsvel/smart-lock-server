package ro.cheiafermecata.smartlock.server.Interfaces.Repository;

import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.Device;

import java.util.List;

@Repository
public interface DeviceRepository {

    List<Device> getByUserId(Long id);

}
