using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class getNatureofComplaints
    {
        [DataMember]
        public string Id { get; set; }

        [DataMember]
        public string Type { get; set; }
    }
}