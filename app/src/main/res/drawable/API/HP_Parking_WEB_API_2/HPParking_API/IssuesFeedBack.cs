using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{ 
    public class IssuesFeedBack
    {
        [DataMember]
        public string NatureofComplaint { get; set; }

        [DataMember]
        public string TypeofComplaint { get; set; }

        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string Anoanonymous { get; set; }

        [DataMember]
        public string Name { get; set; }

        [DataMember]
        public string Mobile { get; set; }

        [DataMember]
        public string Email { get; set; }

        [DataMember]
        public string IMEI { get; set; }

        [DataMember]
        public string Discription { get; set; }

        [DataMember]
        public string Latitude { get; set; }

        [DataMember]
        public string Longitude { get; set; }
        
    }
}