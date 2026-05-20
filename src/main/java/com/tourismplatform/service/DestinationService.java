package com.tourismplatform.service;

import com.tourismplatform.dao.DestinationDAO;
import com.tourismplatform.model.Destination;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinationService {

    private final DestinationDAO destinationDAO;

    public DestinationService(DestinationDAO destinationDAO) {
        this.destinationDAO = destinationDAO;
    }

    public int saveDestination(Destination destination) {
        return destinationDAO.saveDestination(destination);
    }

    public List<Destination> getAllActiveDestinations() {
        return destinationDAO.getAllActiveDestinations();
    }

    public Destination getDestinationById(int destinationId) {
        return destinationDAO.getDestinationById(destinationId);
    }

    public int updateDestination(Destination destination) {
        return destinationDAO.updateDestination(destination);
    }

    public int deactivateDestination(int destinationId) {
        return destinationDAO.deactivateDestination(destinationId);
    }
}

