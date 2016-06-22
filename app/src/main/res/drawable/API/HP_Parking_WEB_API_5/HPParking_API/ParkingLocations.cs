using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace HP_Parking_WEB_API
{
    [Serializable, DataContract(Name = "ParkingLocations")]
   public class ParkingLocations
    {
        [DataMember]
        public string ParkingPlace { get; set; }

        [DataMember]
        public string ParkingId { get; set; }

        [DataMember]
        public string Identifier { get; set; }

        [DataMember]
        public string Town { get; set; }

        [DataMember]
        public string Longitude { get; set; }

        [DataMember]
        public string Latitude { get; set; }

        [DataMember]
        public string Image { get; set; }

        [DataMember]
        public string Image1 { get; set; }

        [DataMember]
        public string Image2 { get; set; }

        [DataMember]
        public string Capacity { get; set; }

        [DataMember]
        public string ContactNumber1 { get; set; }

        [DataMember]
        public string ContactNumber2 { get; set; }

        [DataMember]
        public string ContactNumber3 { get; set; }

        [DataMember]
        public string ContactPerson1 { get; set; }

        [DataMember]
        public string ContactPerson2 { get; set; }

        [DataMember]
        public string ContactPerson3 { get; set; }

        [DataMember]
        public string ParkingArea { get; set; }

        [DataMember]
        public string SutedFor { get; set; }

        [DataMember]
        public string ThrashholdValue { get; set; }

        [DataMember]
        public string ParkingFullTag { get; set; }

        [DataMember]
        public string MinimumParkingTime { get; set; }

        [DataMember]
        public string MinimumParkingFeeSmallCar { get; set; }

        [DataMember]
        public string MinimumParkingFeebigCar { get; set; }

        [DataMember]
        public string Availability { get; set; }

        [DataMember]
        public string percentage { get; set; }
        [DataMember]
        public string Rating { get; set; }

        [DataMember]
        public string Remarks { get; set; }
    }
}
