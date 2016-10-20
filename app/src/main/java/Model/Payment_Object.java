package Model;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by kuush on 10/19/2016.
 */

public class Payment_Object implements Serializable {


    public String EstimatedTime_Server;
    public String VehicleType_Server;
    public String ParkingId_Server;
    public String Phone_Number;
    public String Car_Number;


    public String getEstimatedTime_Server() {
        return EstimatedTime_Server;
    }

    public void setEstimatedTime_Server(String estimatedTime_Server) {
        EstimatedTime_Server = estimatedTime_Server;
    }

    public String getVehicleType_Server() {
        return VehicleType_Server;
    }

    public void setVehicleType_Server(String vehicleType_Server) {
        VehicleType_Server = vehicleType_Server;
    }

    public String getParkingId_Server() {
        return ParkingId_Server;
    }

    public void setParkingId_Server(String parkingId_Server) {
        ParkingId_Server = parkingId_Server;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getCar_Number() {
        return Car_Number;
    }

    public void setCar_Number(String car_Number) {
        Car_Number = car_Number;
    }





}
