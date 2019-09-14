package service.common;

import nativelot.LotInterface;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@Model
public class CommonService {

    @Inject
    LotInterface lotInterface;

    public List<Airport> getAirports() throws IOException {
        return lotInterface.getAirports();
    }
}
