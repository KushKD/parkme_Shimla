using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    [Serializable, DataContract(Name = "Registration")]
    public class Registration
    {
        [DataMember]
        public string vehicelNumber { get; set; }
        [DataMember]
        public string MobileNumber { get; set; }
        [DataMember]
        public string Name { get; set; }
        [DataMember]
        public string EMail { get; set; }
        [DataMember]
        public string IMI { get; set; }
    }
}