using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.ServiceModel.Web;
using System.Data.SqlClient;
using System.Data;

namespace HP_Parking_WEB_API
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "HPParking" in code, svc and config file together.
    public class HPParking : IHPParking
    {

        SqlConnection dbConnection;
        List<ParkingLocations> Parking_List = null;
        ParkingLocations objPark = null;
        #region Townwise
        private IEnumerable<ParkingLocations> GetAllParking(string id)
        {
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            } 

            DataSet dt = new DataSet();
            SqlCommand cmd = new SqlCommand("[getParkingLocation]", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@TownId", id);


            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);

                if (dt.Tables[0].Rows.Count > 0)
                {
                    Parking_List = new List<ParkingLocations>();
                    for (int i = 0; i < dt.Tables[0].Rows.Count; i++)
                    {
                        objPark = new ParkingLocations();
                        objPark.ParkingPlace = dt.Tables[0].Rows[i]["ParkingPlace"].ToString();
                        objPark.Identifier = dt.Tables[0].Rows[i]["Identifier"].ToString();
                        objPark.Longitude = dt.Tables[0].Rows[i]["Longitude"].ToString();
                        objPark.Latitude = dt.Tables[0].Rows[i]["Latitude"].ToString();
                        objPark.Image = dt.Tables[0].Rows[i]["Image"].ToString();
                        objPark.Image1 = dt.Tables[0].Rows[i]["Image1"].ToString();
                        objPark.Image2 = dt.Tables[0].Rows[i]["Image2"].ToString();
                        objPark.Capacity = dt.Tables[0].Rows[i]["Capacity"].ToString();
                        objPark.ContactNumber1 = dt.Tables[0].Rows[i]["ContactNumber1"].ToString();
                        objPark.ContactNumber2 = dt.Tables[0].Rows[i]["ContactNumber2"].ToString();
                        objPark.ContactNumber3 = dt.Tables[0].Rows[i]["ContactNumber3"].ToString();
                        objPark.ContactPerson1 = dt.Tables[0].Rows[i]["ContactPerson1"].ToString();
                        objPark.ContactPerson2 = dt.Tables[0].Rows[i]["ContactPerson2"].ToString();
                        objPark.ContactPerson3 = dt.Tables[0].Rows[i]["ContactPerson3"].ToString();
                        objPark.ParkingArea = dt.Tables[0].Rows[i]["ParkingArea"].ToString();
                        objPark.SutedFor = dt.Tables[0].Rows[i]["SutedFor"].ToString();
                        objPark.ThrashholdValue = dt.Tables[0].Rows[i]["ThrashholdValue"].ToString();
                        objPark.ParkingFullTag = dt.Tables[0].Rows[i]["ParkingFullTag"].ToString();
                        objPark.Remarks = "";
                        Parking_List.Add(objPark);
                    }
                }
                else
                {
                    Parking_List = new List<ParkingLocations>();
                    objPark = new ParkingLocations();
                    objPark.Remarks = "No Record Found";
                    Parking_List.Add(objPark);

                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally
            {
                adp.Dispose();
            }
            return Parking_List;
        }

        public IEnumerable<ParkingLocations> getParking_xml(string id)
        {
            return GetAllParking(id);

        }

        public IEnumerable<ParkingLocations> getParking_JSON(string id)
        {
            return GetAllParking(id);
        }
        #endregion


        #region GeoLocation

        private IEnumerable<ParkingLocations> GetAllParkingGoeLocation(LocationDetails details)
        {
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataSet dt = new DataSet();
            SqlCommand cmd = new SqlCommand("getParkingLocationGeo", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@Latitude", String.Format("{0:0.000}", details.Latitude));
            cmd.Parameters.AddWithValue("@Longitude", String.Format("{0:0.000}",details.Longitude));


            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);


                Parking_List = new List<ParkingLocations>();
                for (int i = 0; i < dt.Tables[0].Rows.Count; i++)
                {
                    objPark = new ParkingLocations();
                    objPark.ParkingPlace = dt.Tables[0].Rows[i]["ParkingPlace"].ToString();
                    objPark.Identifier = dt.Tables[0].Rows[i]["Identifier"].ToString();
                    objPark.Longitude = dt.Tables[0].Rows[i]["Longitude"].ToString();
                    objPark.Latitude = dt.Tables[0].Rows[i]["Latitude"].ToString();
                    objPark.Image = dt.Tables[0].Rows[i]["Image"].ToString();
                    objPark.Image1 = dt.Tables[0].Rows[i]["Image1"].ToString();
                    objPark.Image2 = dt.Tables[0].Rows[i]["Image2"].ToString();
                    objPark.Capacity = dt.Tables[0].Rows[i]["Capacity"].ToString();
                    objPark.ContactNumber1 = dt.Tables[0].Rows[i]["ContactNumber1"].ToString();
                    objPark.ContactNumber2 = dt.Tables[0].Rows[i]["ContactNumber2"].ToString();
                    objPark.ContactNumber3 = dt.Tables[0].Rows[i]["ContactNumber3"].ToString();
                    objPark.ContactPerson1 = dt.Tables[0].Rows[i]["ContactPerson1"].ToString();
                    objPark.ContactPerson2 = dt.Tables[0].Rows[i]["ContactPerson2"].ToString();
                    objPark.ContactPerson3 = dt.Tables[0].Rows[i]["ContactPerson3"].ToString();
                    objPark.ParkingArea = dt.Tables[0].Rows[i]["ParkingArea"].ToString();
                    objPark.SutedFor = dt.Tables[0].Rows[i]["SutedFor"].ToString();
                    objPark.ThrashholdValue = dt.Tables[0].Rows[i]["ThrashholdValue"].ToString();
                    objPark.ParkingFullTag = dt.Tables[0].Rows[i]["ParkingFullTag"].ToString();
                    Parking_List.Add(objPark);
                }
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally
            {
                adp.Dispose();
            }
            return Parking_List;
        }

        public IEnumerable<ParkingLocations> getParkingGEOLocation_xml(LocationDetails details)
        {
            return GetAllParkingGoeLocation(details);

        }

        public IEnumerable<ParkingLocations> getParkingGEOLocation_JSON(LocationDetails details)
        {
            return GetAllParkingGoeLocation( details);
        }

        #endregion

        #region Dummy Functions to check weather the API is Working Or Not

        public string XMLData(string id)
        {
            return id;
        }

        public string JSONData(string id)
        {
            return id;
        }

        #endregion




      
    }
}
