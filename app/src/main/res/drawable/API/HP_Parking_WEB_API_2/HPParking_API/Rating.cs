using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class Rating
    {
        [DataMember]
        public string RegId { get; set; }

        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string Stars { get; set; }

        [DataMember]
        public string Remarks { get; set; }
    }
}