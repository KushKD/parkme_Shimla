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
    }
}
