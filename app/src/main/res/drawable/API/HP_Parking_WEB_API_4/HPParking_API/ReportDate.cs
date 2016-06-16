using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class ReportDate
    {
        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string FromDate { get; set; }

        [DataMember]
        public string LastDate { get; set; }
    }
}