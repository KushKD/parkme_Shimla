package Model;

import java.io.Serializable;

/**
 * Created by kuush on 5/27/2016.
 */
public class My_Marker_Pojo implements Serializable {


    public String getThrashholdValue() {
        return ThrashholdValue;
    }

    public void setThrashholdValue(String thrashholdValue) {
        ThrashholdValue = thrashholdValue;
    }

    public String getSutedFor() {
        return SutedFor;
    }

    public void setSutedFor(String sutedFor) {
        SutedFor = sutedFor;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getParkingPlace() {
        return ParkingPlace;
    }

    public void setParkingPlace(String parkingPlace) {
        ParkingPlace = parkingPlace;
    }

    public String getParkingFullTag() {
        return ParkingFullTag;
    }

    public void setParkingFullTag(String parkingFullTag) {
        ParkingFullTag = parkingFullTag;
    }

    public String getParkingArea() {
        return ParkingArea;
    }

    public void setParkingArea(String parkingArea) {
        ParkingArea = parkingArea;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getImage2() {
        return Image2;
    }

    public void setImage2(String image2) {
        Image2 = image2;
    }

    public String getImage1() {
        return Image1;
    }

    public void setImage1(String image1) {
        Image1 = image1;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public String getContactPerson3() {
        return ContactPerson3;
    }

    public void setContactPerson3(String contactPerson3) {
        ContactPerson3 = contactPerson3;
    }

    public String getContactPerson2() {
        return ContactPerson2;
    }

    public void setContactPerson2(String contactPerson2) {
        ContactPerson2 = contactPerson2;
    }

    public String getContactPerson1() {
        return ContactPerson1;
    }

    public void setContactPerson1(String contactPerson1) {
        ContactPerson1 = contactPerson1;
    }

    public String getContactNumber3() {
        return ContactNumber3;
    }

    public void setContactNumber3(String contactNumber3) {
        ContactNumber3 = contactNumber3;
    }

    public String getContactNumber2() {
        return ContactNumber2;
    }

    public void setContactNumber2(String contactNumber2) {
        ContactNumber2 = contactNumber2;
    }

    public String getContactNumber1() {
        return ContactNumber1;
    }

    public void setContactNumber1(String contactNumber1) {
        ContactNumber1 = contactNumber1;
    }

    public String getCapacity() {
        return Capacity;
    }

    public void setCapacity(String capacity) {
        Capacity = capacity;
    }

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
    public String ParkingArea;
    public String ParkingFullTag;
    public String ParkingPlace;
    public String Remarks;
    public String SutedFor;
    public String ThrashholdValue;
    public String MinimumParkingFeeSmallCar;
    public String MinimumParkingFeebigCar;
    public String MinimumParkingTime;
    public String ParkingID;
    public String Availability;
    public String percentage;

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String Rating = "1.0";

    public String getParkingID() {
        return ParkingID;
    }

    public void setParkingID(String parkingID) {
        ParkingID = parkingID;
    }

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

    public String getMinimumParkingTime() {
        return MinimumParkingTime;
    }

    public void setMinimumParkingTime(String minimumParkingTime) {
        MinimumParkingTime = minimumParkingTime;
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


    public My_Marker_Pojo(){

    }


    public My_Marker_Pojo(String Capacity,
                          String ContactNumber1, String ContactNumber2, String ContactNumber3,
                          String ContactPerson1, String ContactPerson2, String ContactPerson3,
                          String Identifier,
                          String Image, String Image1, String Image2,
                          Double Latitude, Double Longitude,
                          String ParkingArea, String ParkingFullTag, String ParkingPlace,
                          String Remarks, String SutedFor, String ThrashholdValue,
                          String MinimumParkingFeeSmallCar, String MinimumParkingFeebigCar, String MinimumParkingTime,
                          String percentage, String Availability,
                          String ParkingID, String Rating)
    {
        this.Capacity = Capacity;
        this.ContactNumber1 = ContactNumber1;
        this.ContactNumber2 = ContactNumber2;
        this.ContactNumber3 = ContactNumber3;
        this.ContactPerson1 = ContactPerson1;
        this.ContactPerson2 = ContactPerson2;
        this.ContactPerson3 = ContactPerson3;
        this.Identifier = Identifier;
        this.Image = Image;
        this.Image1 = Image1;
        this.Image2 = Image2;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.ParkingArea = ParkingArea;
        this.ParkingFullTag = ParkingFullTag;
        this.ParkingPlace = ParkingPlace;
        this.Remarks = Remarks;
        this.SutedFor = SutedFor;
        this.ThrashholdValue = ThrashholdValue;
        this.MinimumParkingTime = MinimumParkingTime;
        this.MinimumParkingFeebigCar = MinimumParkingFeebigCar;
        this.MinimumParkingFeeSmallCar = MinimumParkingFeeSmallCar;
        this.percentage = percentage;
        this.Availability = Availability;
        this.ParkingID = ParkingID;

        if(Rating==null) {
            this.Rating = "1.0";
        }else{
            this.Rating = Rating;
        }


    }


}
