package com.tourismplatform.service;

import com.tourismplatform.dao.TourPackageDAO;
import com.tourismplatform.model.TourPackage;
import org.springframework.stereotype.Service;
import com.tourismplatform.model.Destination;
import java.util.List;

@Service
public class TourPackageService {

    private final TourPackageDAO tourPackageDAO;

    public TourPackageService(TourPackageDAO tourPackageDAO) {
        this.tourPackageDAO = tourPackageDAO;
    }

    public int saveTourPackage(TourPackage tourPackage) {
        return tourPackageDAO.saveTourPackage(tourPackage);
    }

    public List<TourPackage> getAllActivePackages() {
        return tourPackageDAO.getAllActivePackages();
    }

    public TourPackage getTourPackageById(int packageId) {
        return tourPackageDAO.getTourPackageById(packageId);
    }

    public int updateTourPackage(TourPackage tourPackage) {
        return tourPackageDAO.updateTourPackage(tourPackage);
    }

    public int deactivateTourPackage(int packageId) {
        return tourPackageDAO.deactivateTourPackage(packageId);
    }

    public List<Integer> getAssignedDestinationIds(int packageId) {
        return tourPackageDAO.getAssignedDestinationIds(packageId);
    }

    public void updatePackageDestinations(int packageId, List<Integer> destinationIds) {
        tourPackageDAO.removeDestinationsFromPackage(packageId);

        if (destinationIds != null) {
            for (Integer destinationId : destinationIds) {
                tourPackageDAO.assignDestinationToPackage(packageId, destinationId);
            }
        }
    }

    public List<Destination> getDestinationsByPackageId(int packageId) {
        return tourPackageDAO.getDestinationsByPackageId(packageId);
    }
}
