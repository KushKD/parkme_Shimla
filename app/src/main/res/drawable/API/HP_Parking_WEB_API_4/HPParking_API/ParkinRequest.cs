using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class ParkinRequest
    {
        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string RegisterId { get; set; }

        [DataMember]
        public string VehicleNo { get; set; }

        [DataMember]
        public string VehicleType { get; set; }

        [DataMember]
        public string EstimatedTime { get; set; }

        [DataMember]
        public string InTime { get; set; }


        [DataMember]
        public string PhoneNumber { get; set; }

        [DataMember]
        public string RequestTime { get; set; }

        [DataMember]
        public string RequestStatus { get; set; }
    }
}