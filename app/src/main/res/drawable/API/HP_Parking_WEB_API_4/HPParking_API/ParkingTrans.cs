using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class ParkingTrans
    {

        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string TypeofCar { get; set; }

        [DataMember]
        public string VehicleNo { get; set; }

        [DataMember]
        public string DriverName { get; set; }

        [DataMember]
        public string PhoneNumber { get; set; }

        [DataMember]
        public string EstimatedParkingtime { get; set; }

        [DataMember]
        public string EstimatedFee { get; set; }

        [DataMember]
        public string InTime { get; set; }

        [DataMember]
        public string OutTime { get; set; }

        [DataMember]
        public string ActualFee { get; set; }


    }
}