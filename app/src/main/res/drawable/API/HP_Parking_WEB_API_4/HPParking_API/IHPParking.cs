using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.ServiceModel.Web;

namespace HP_Parking_WEB_API
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IHPParking" in both code and config file together.
    [ServiceContract]
    public interface IHPParking
    {

       
        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getxml/{id}")]
        string XMLData(string id);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getjson/{id}")]
        string JSONData(string id);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getparkingLocation_XML/{Townid}")]
        IEnumerable<ParkingLocations> getParking_xml(string Townid);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getparkingLocation_JSON/{Townid}")]
        IEnumerable<ParkingLocations> getParking_JSON(string Townid);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getparkingLocationCoordinates_XML")]
        IEnumerable<ParkingLocations> getParkingGEOLocation_xml(LocationDetails details);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getparkingLocationCoordinates_JSON")]
        IEnumerable<ParkingLocations> getParkingGEOLocation_JSON(LocationDetails details);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingFeeLocationCoordinates_JSON")]
        IEnumerable<fee> getParkingLocFee_JSON(LocationDetails details);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingFeeLocationCoordinates_XML")]
        IEnumerable<fee> getParkingLocFee_xml(LocationDetails details);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingFeeParkingId_XML/{ParkingId}/{CarTypes}")]
        IEnumerable<fee> getParkingFee_xml(string ParkingId, string CarTypes);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingFeeParkingId_JSON/{ParkingId}/{CarTypes}")]
        IEnumerable<fee> getParkingFee_JSON(string ParkingId, string CarTypes);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getVehicleRegistration_JSON")]
        string getVehicelRegister_JSON(Registration VehicleDetails);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getVehicleRegistration_XML")]
        string getVehicelRegister_xml(Registration VehicleDetails);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getComplaintList_JSON")]
        IEnumerable<getNatureofComplaints> getComplaintList_JSON();

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getComplaintList_XML")]
        IEnumerable<getNatureofComplaints> getComplaintList_XML();

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getComplaintTyoeList_JSON/{Id}")]
        IEnumerable<getComplaintType> getComplaintTypeList_JSON(string id);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getComplaintTypeList_XML/{Id}")]
        IEnumerable<getComplaintType> getComplaintTypeList_XML(string id);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getIssueFeedback_JSON")]
        string getIssueFeedback_JSON(IssuesFeedBack IssuesFeedback);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getIssueFeedback_XML")]
        string getIssueFeedback_XML(IssuesFeedBack IssuesFeedback);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingTransaction_JSON")]
        string getParkingTransaction_JSON(ParkingTrans ParkTrans);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingTransactions_XML")]
        string getParkingTransaction_XML(ParkingTrans ParkTrans);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkedVehiclelist_JSON/{ParkingId}")]
        IEnumerable<ParkedVehicles> getParkedVehiclelist_JSON(string ParkingId);
        
        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkedVehiclelist_XML/{ParkingId}")]
        IEnumerable<ParkedVehicles> getParkedVehiclelist_XML(string ParkingId);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingOut_JSON")]
        string getParkingOut_JSON(VehicleOut VehicleOuts);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingOut_XML")]
        string getParkingOut_XML(VehicleOut VehicleOuts);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getConfirmPayment_JSON")]
        string getConfirmPayment_JSON(VehicleOut VehicleOuts);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getConfirmPayment_XML")]
        string getConfirmPayment_XML(VehicleOut VehicleOuts);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getStates_JSON")]
        IEnumerable<States> getStates_JSON();

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getStates_XML")]
        IEnumerable<States> getStates_XML();

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getStates_JSON/{StateId}")]
        IEnumerable<Districts> getDistrict_JSON(string StateId);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getStates_XML/{StateId}")]
        IEnumerable<Districts> getDistrict_XML(string StateId);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getAllParkReqest_JSON/{ParkingId}")]
        IEnumerable<ParkinRequest> getAllParkReqest_JSON(string ParkingId);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getAllParkReqest_XML/{ParkingId}")]
        IEnumerable<ParkinRequest> getAllParkReqest_XML(string ParkingId);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getAllParkOutReqest_JSON/{ParkingId}")]
        IEnumerable<ParkinRequest> getAllParkOutReqest_JSON(string ParkingId);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getAllParkOutReqest_XML/{ParkingId}")]
        IEnumerable<ParkinRequest> getAllParkOutReqest_XML(string ParkingId);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getConfirmParkinStatus_JSON")]
        string getConfirmParkinStatus_JSON(ParkinRequest ParkInRequst);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getConfirmParkinStatus_XML")]
        string getConfirmParkinStatus_XML(ParkinRequest ParkInRequst);


        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getConfirmParkOutStatus_JSON")]
        string getConfirmParkOutStatus_JSON(ParkinRequest ParkInRequst);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getConfirmParkOutStatus_XML")]
        string getConfirmParkOutStatus_XML(ParkinRequest ParkInRequst);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkMeRequest_JSON")]
        string getParkMeRequest_JSON(ParkinRequest ParkMeRequst);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkMeRequest_XML")]
        string getParkMeRequest_XML(ParkinRequest ParkMeRequst);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkOutRequest_JSON")]
        string getParkOutRequest_JSON(ParkinRequest ParkOutRequst);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkOutRequest_XML")]
        string getParkOutRequest_XML(ParkinRequest ParkOutRequst);


        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingAvailblity_JSON/{ParkingId}")]
        ParkAvailablity getParkingAvailblity_JSON(string ParkingId);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getParkingAvailblity_XML/{ParkingId}")]
        ParkAvailablity getParkingAvailblity_XML(string ParkingId);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getRating_JSON")]
        string getRating_JSON(Rating getRating);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getRating_XML")]
        string getRating_XML(Rating getRating);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getDailyReport_JSON")]
        IEnumerable<CollectionReport> getDailyReport_JSON(ReportDate fDate);

        [OperationContract]
        [WebInvoke(Method = "POST", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getDailyReport_XML")]
        IEnumerable<CollectionReport> getDailyReport_XML(ReportDate fDate);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getOTP_JSON/{AadhaarNo}")]
        string getOTP_JSON(string AadhaarNo);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getOTP_XML/{AadhaarNo}")]
        string getOTP_XML(string AadhaarNo);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getValidateOTP_JSON/{AadhaarNo}/{OTP}")]
        EmployeeDetails getValidateOTP_JSON(string AadhaarNo, string OTP);

        [OperationContract]
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Xml, BodyStyle = WebMessageBodyStyle.Wrapped, UriTemplate = "getValidateOTP_XML/{AadhaarNo}/{OTP}")]
        EmployeeDetails getValidateOTP_XML(string AadhaarNo,string OTP);
    }
}
