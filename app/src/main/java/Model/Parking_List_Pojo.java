package Model;

import java.io.Serializable;

/**
 * Created by kuush on 9/14/2016.
 */
public class Parking_List_Pojo implements Serializable {

    public String getParkingPlace() {
        return ParkingPlace;
    }

    public void setParkingPlace(String parkingPlace) {
        ParkingPlace = parkingPlace;
    }

    public String getParkingArea() {
        return ParkingArea;
    }

    public void setParkingArea(String parkingArea) {
        ParkingArea = parkingArea;
    }

    public String getParkingFullTag() {
        return ParkingFullTag;
    }

    public void setParkingFullTag(String parkingFullTag) {
        ParkingFullTag = parkingFullTag;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getSutedFor() {
        return SutedFor;
    }

    public void setSutedFor(String sutedFor) {
        SutedFor = sutedFor;
    }

    public String getThrashholdValue() {
        return ThrashholdValue;
    }

    public void setThrashholdValue(String thrashholdValue) {
        ThrashholdValue = thrashholdValue;
    }

    public String getMinimumParkingFeeSmallCar() {
        return MinimumParkingFeeSmallCar;
    }

    public void setMinimumParkingFeeSmallCar(String minimumParkingFeeSmallCar) {
        MinimumParkingFeeSmallCar = minimumParkingFeeSmallCar;
    }

    public String getMinimumParkingFeebigCar() {
        return MinimumParkingFeebigCar;
    }

    public void setMinimumParkingFeebigCar(String minimumParkingFeebigCar) {
        MinimumParkingFeebigCar = minimumParkingFeebigCar;
    }

    public String getMinimumParkingTime() {
        return MinimumParkingTime;
    }

    public void setMinimumParkingTime(String minimumParkingTime) {
        MinimumParkingTime = minimumParkingTime;
    }

    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

    public String getContactNumber1() {
        return ContactNumber1;
    }

    public void setContactNumber1(String contactNumber1) {
        ContactNumber1 = contactNumber1;
    }

    public String getContactNumber2() {
        return ContactNumber2;
    }

    public void setContactNumber2(String contactNumber2) {
        ContactNumber2 = contactNumber2;
    }

    public String getContactNumber3() {
        return ContactNumber3;
    }

    public void setContactNumber3(String contactNumber3) {
        ContactNumber3 = contactNumber3;
    }

    public String getContactPerson1() {
        return ContactPerson1;
    }

    public void setContactPerson1(String contactPerson1) {
        ContactPerson1 = contactPerson1;
    }

    public String getContactPerson2() {
        return ContactPerson2;
    }

    public void setContactPerson2(String contactPerson2) {
        ContactPerson2 = contactPerson2;
    }

    public String getContactPerson3() {
        return ContactPerson3;
    }

    public void setContactPerson3(String contactPerson3) {
        ContactPerson3 = contactPerson3;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude_my_Location() {
        return Latitude_my_Location;
    }

    public void setLatitude_my_Location(Double latitude_my_Location) {
        Latitude_my_Location = latitude_my_Location;
    }

    public Double getLongitude_my_Location() {
        return Longitude_my_Location;
    }

    public void setLongitude_my_Location(Double longitude_my_Location) {
        Longitude_my_Location = longitude_my_Location;
    }

    public String getParkingId() {
        return ParkingId;
    }

    public void setParkingId(String parkingId) {
        ParkingId = parkingId;
    }

    public String Remarks;
    public String SutedFor;
    public String ThrashholdValue;
    public String MinimumParkingFeeSmallCar;
    public String MinimumParkingFeebigCar;
    public String MinimumParkingTime;
    public String Capacity;
    public String ContactNumber1;
    public String ContactNumber2;
    public String ContactNumber3;
    public String ContactPerson1;
    public String ContactPerson2;
    public String ContactPerson3;
    public String Identifier;
    public String Image;
    public String Image1;
    public String Image2;
    public Double Latitude;
    public Double Longitude;
    public Double Latitude_my_Location;
    public Double Longitude_my_Location;
    public String ParkingPlace;
    public String ParkingArea;
    public String ParkingFullTag;

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String Availability;
    public String percentage;
    public String ParkingId;

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String Rating;

}
