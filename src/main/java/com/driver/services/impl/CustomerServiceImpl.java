package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CabRepository;
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
	@Autowired
	private CabRepository cabRepository;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
        Customer customer = customerRepository2.findById(customerId).get();
		customerRepository2.delete(customer);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> driverList =driverRepository2.findAllByOrderByDriverId();
//		System.out.println(driverList);
		Driver availableDriver = null;
        for(Driver driver : driverList){
			Cab driverCab = driver.getCab();
			if (driverCab.getAvailable()){
				availableDriver = driver;
				break;
			}
		}
//		System.out.println(availableDriver);
		if(availableDriver==null){
			throw new NoCabAvailableException("No cab available!");
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
//		making the driver's cab unavailable
		Cab driverCab = availableDriver.getCab();
		driverCab.setAvailable(false);
		availableDriver.setCab(driverCab);
		driverCab.setDriver(availableDriver);
//		if i save the driver then it will automaticallu save the cab and tripbooking
//		driverRepository2.save(availableDriver);
		tripBookingRepository2.save(tripBooking);
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

		tripBookingRepository2.save(tripBooking);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
         TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		 tripBooking.setStatus(TripStatus.COMPLETED);

		 Driver driver = tripBooking.getDriver();
		 driver.getCab().setAvailable(true);

		 tripBookingRepository2.save(tripBooking);

	}
}
