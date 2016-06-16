using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class ParkedVehicles
    {
        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string VehicleNo { get; set; }

        [DataMember]
        public string DriverName { get; set; }

        [DataMember]
        public string PhoneNumber { get; set; }

        [DataMember]
        public string ParkInTime { get; set; }

        [DataMember]
        public string ParkOutTime { get; set; }
    }
}