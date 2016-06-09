using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class Districts
    {
        [DataMember]
        public string DistrictCode { get; set; }

        [DataMember]
        public string DistrictName { get; set; }
    }
}