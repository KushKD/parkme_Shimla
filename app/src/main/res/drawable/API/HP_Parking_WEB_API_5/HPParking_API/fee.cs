using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class fee
    {
        [DataMember]
        public string Duration { get; set; }

        [DataMember]
        public string Amount { get; set; }

    
      
      

        
    }
}