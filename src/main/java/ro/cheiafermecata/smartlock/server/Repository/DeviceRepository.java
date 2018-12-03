package ro.cheiafermecata.smartlock.server.Repository;

import org.springframework.stereotype.Repository;
import ro.cheiafermecata.smartlock.server.Data.Device;

import java.util.List;

@Repository
public interface DeviceRepository {

    /**
     * Gets all the Devices that belong to the user (user_id = {id})
     * @param id the id of the user
     * @return a List of Device
     */
    List<Device> getByUserId(Long id);

    /**
     * Gets the device with id = {id}
     * @param id the id of the device
     * @return an Device
     */
    Device getById(Long id);

    /**
     * Persists the Device and returns its {id} (can be used for modifying and creating Devices)
     * @param device the device to persist
     * @return the {id} of th device
     */
    Long save(Device device);

}
