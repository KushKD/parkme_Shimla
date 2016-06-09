using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace HP_Parking_WEB_API
{
 [Serializable, DataContract(Name = "LocationDetails")]
    public class LocationDetails
    {

       //Send Latitude and Longitude
     [DataMember]
     public String Latitude { get; set; }

     [DataMember]
     public String Longitude { get; set; } 
    }
}
