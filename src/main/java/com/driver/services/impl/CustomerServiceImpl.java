package com.driver.services.impl;

import com.driver.model.*;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.delete(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> driverList =driverRepository2.findAllByOrderByDriverId();
		Driver availableDriver = null;
        for(Driver driver : driverList){
			Cab driverCab = driver.getCab();
			if (driverCab.getAvailable()){
				availableDriver = driver;
				break;
			}
		}
		if(availableDriver==null){
			throw new Exception("No cab available!");
		}
		Customer customer = customerRepository2.findById(customerId).get();
//		List<TripBooking> tripBookingListByCustomer = customer.getTripBookingList();
		TripBooking tripBooking = new TripBooking();
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setBill(distanceInKm*availableDriver.getCab().getPerKmRate());
		tripBooking.setDistanceInKm(distanceInKm);
		tripBooking.setStatus(TripStatus.CONFIRMED);

		tripBooking.setCustomer(customer);
		customer.getTripBookingList().add(tripBooking);

		tripBooking.setDriver(availableDriver);
		availableDriver.getTripBookingList().add(tripBooking);
//		it will also save tripBooking
		customerRepository2.save(customer);
		driverRepository2.save(availableDriver);
		return tripBooking;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.CANCELED);
//      making that cab available
		Driver driver = tripBooking.getDriver();
		driver.getCab().setAvailable(true);

		driverRepository2.save(driver);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
         TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		 tripBooking.setStatus(TripStatus.COMPLETED);

		 Driver driver = tripBooking.getDriver();
		 driver.getCab().setAvailable(true);

		 driverRepository2.save(driver);

	}
}
