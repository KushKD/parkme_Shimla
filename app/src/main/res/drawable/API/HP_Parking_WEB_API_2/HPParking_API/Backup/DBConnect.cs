using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;
using System.Configuration;

namespace HP_Parking_WEB_API
{
    public class DBConnect
    {
        private static SqlConnection NewCon;
        private static string conStr = ConfigurationManager.ConnectionStrings["Parking"].ConnectionString;

        public static SqlConnection getConnection()
        {
            NewCon = new SqlConnection(conStr);
            return NewCon;

        }
        public DBConnect()
        {

        }
    }
}