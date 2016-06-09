﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class VehicleOut
    {
        [DataMember]
        public string VehicleNo { get; set; }

        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string DriverName { get; set; }

        [DataMember]
        public string PhoneNumber { get; set; }

        [DataMember]
        public string OutTime { get; set; }
    }
}