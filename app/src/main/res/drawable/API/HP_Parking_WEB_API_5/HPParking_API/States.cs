﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace HP_Parking_WEB_API
{
    public class States
    {
        [DataMember]
        public string StateCode { get; set; }

        [DataMember]
        public string StateName { get; set; }
    }
}