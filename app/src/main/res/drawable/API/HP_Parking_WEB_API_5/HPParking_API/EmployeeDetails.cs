using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class EmployeeDetails
    {

        [DataMember]
        public string OperatorName { get; set; }

        [DataMember]
        public string OperatorAadhaarNo { get; set; }

        [DataMember]
        public string MobileNumber { get; set; }

        [DataMember]
        public string AlternateMobileNumber { get; set; }
        [DataMember]
        public string Email { get; set; }

        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string ParkingLocation { get; set; }

        [DataMember]
        public string ParkingLandmark { get; set; }
    }
}