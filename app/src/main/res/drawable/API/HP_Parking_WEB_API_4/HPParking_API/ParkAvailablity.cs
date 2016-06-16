using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class ParkAvailablity
    {
        [DataMember]
        public string Availability { get; set; }

        [DataMember]
        public string percentage { get; set; }
    }
}