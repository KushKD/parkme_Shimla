using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using System.ServiceModel.Web;
using System.Data.SqlClient;
using System.Data;
using System.Net;
using System.IO;
using System.Web;
using System.Security.Cryptography;
using System.Net.Mail;

namespace HP_Parking_WEB_API
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "HPParking" in code, svc and config file together.
    public class HPParking : IHPParking
    {
        fee objfee = new fee();
        
        getNatureofComplaints objComp = new getNatureofComplaints();
        
        SqlConnection dbConnection;
        
        List<ParkingLocations> Parking_List = null;
        
        List<getNatureofComplaints> ComplaintList = null;
        
        List<fee> fee_List = null;
        
        ParkingLocations objPark = null;

        #region Townwise

        private DataTable AvailabilityParking(string ParkingId)
        {
            
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("sp_GetParkingAvailability", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@ParkId", ParkingId);
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt); dbConnection.Close(); dbConnection.Dispose();
            }
            catch { dbConnection.Close(); dbConnection.Dispose(); }
            finally
            {
                try
                {
                    cmd.Dispose();
                    adp.Dispose(); dbConnection.Close(); dbConnection.Dispose();
                }
                catch { }
            }
            return dt;
        }
        private string ParkingTransaction(string ParkingId)
        {
            string Transaction = "0";
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("SP_CheckParkingTransactions", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@ParkingId", ParkingId);
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt); dbConnection.Close(); dbConnection.Dispose();
                if (dt.Rows.Count > 0)
                {
                    Transaction = dt.Rows[0]["Transaction"].ToString().Trim();
                }
            }
            catch { dbConnection.Close(); dbConnection.Dispose(); }
            finally
            {
                try
                {
                    cmd.Dispose();
                    adp.Dispose(); dbConnection.Close(); dbConnection.Dispose();
                }
                catch { }
            }
            return Transaction;
        }
        private DataTable GetParkingFee(string id, string Cartype)
        {
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("sp_GetParkingFee", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@ParkingId", id);
            cmd.Parameters.AddWithValue("@CarType", Cartype);

            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                dbConnection.Close(); dbConnection.Dispose();
            }
            catch { dbConnection.Close(); dbConnection.Dispose(); }
            finally
            {
                try
                {
                    cmd.Dispose();
                    adp.Dispose(); dbConnection.Close(); dbConnection.Dispose();
                }
                catch { }
            }
            return dt;
        }
        private IEnumerable<ParkingLocations> GetAllParking(string id)
        {
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataSet dt = new DataSet();
            SqlCommand cmd = new SqlCommand("getParkingLocation", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@TownId", id);


            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                dbConnection.Close(); dbConnection.Dispose();
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
                        objPark.ParkingId = dt.Tables[0].Rows[i]["ParkingLocationId"].ToString();
                        objPark.Town = dt.Tables[0].Rows[i]["Town"].ToString();
                        objPark.Remarks = "";
                        DataTable dtFee = GetParkingFee(dt.Tables[0].Rows[i]["ParkingLocationId"].ToString().Trim(), "1");
                        DataTable dtFee1 = GetParkingFee(dt.Tables[0].Rows[i]["ParkingLocationId"].ToString().Trim(), "2");
                        string Transactions = ParkingTransaction(dt.Tables[0].Rows[i]["ParkingLocationId"].ToString().Trim());
                        DataTable dtGetAvailability = AvailabilityParking(dt.Tables[0].Rows[i]["ParkingLocationId"].ToString().Trim());
                        if (Convert.ToInt32(Transactions) > 0)
                        {
                            if (dtGetAvailability.Rows.Count > 0)
                            {
                                if (Convert.ToInt32(dtGetAvailability.Rows[0]["Availability"].ToString().Trim()) > 0)
                                {
                                    objPark.Availability = dtGetAvailability.Rows[0]["Availability"].ToString().Trim() + "/" + dtGetAvailability.Rows[0]["Capacity"].ToString().Trim();
                                    objPark.percentage = string.Format("{0:0}", ((Convert.ToDouble(dtGetAvailability.Rows[0]["Availability"].ToString().Trim()) / Convert.ToDouble(dtGetAvailability.Rows[0]["Capacity"].ToString().Trim())) * 100));
                                }
                                else
                                {
                                    objPark.Availability = "0"; //dtGetAvailability.Rows[0]["Availability"].ToString().Trim() + "/" + dtGetAvailability.Rows[0]["Capacity"].ToString().Trim();
                                    objPark.percentage = "0";//string.Format("{0:0}", ((Convert.ToDouble(dtGetAvailability.Rows[0]["Availability"].ToString().Trim()) / Convert.ToDouble(dtGetAvailability.Rows[0]["Capacity"].ToString().Trim())) * 100));

                                }
                            }
                            else
                            {
                                objPark.Availability = "Not Known"; objPark.percentage = "";
                            }
                        }
                        else
                        {
                            objPark.Availability = "Not Known"; objPark.percentage = "";
                        }
                        if (dtFee.Rows.Count > 0)
                        {
                            objPark.MinimumParkingFeeSmallCar = "0";
                            for (int t = 0; t < dtFee.Rows.Count; t++)
                            {
                                if (dtFee.Rows[t]["VehicalType"].ToString().Trim().ToUpper() == "Small Car".ToUpper())
                                {
                                    for (int j = 3; j < dtFee.Columns.Count; j++)
                                    {
                                        if (dtFee.Rows[t][j].ToString().Trim() != "0")
                                        {
                                            
                                            string PriviousFee = objPark.MinimumParkingFeeSmallCar;
                                            string NewFee = dtFee.Rows[t][j].ToString();
                                            if (j == 3)
                                            {
                                                objPark.MinimumParkingFeeSmallCar = NewFee;
                                            }
                                            if (PriviousFee != null)
                                            {
                                                if (NewFee == PriviousFee)
                                                {
                                                    objPark.MinimumParkingFeeSmallCar = NewFee;
                                                    objPark.MinimumParkingTime = dtFee.Columns[j].ColumnName.ToString();
                                                    continue;

                                                }
                                                else
                                                {
                                                   
                                                    continue;
                                                }
                                            }
                                            else
                                            {
                                                continue;
                                            }
                                            
                                            break;
                                        }
                                    }

                                }

                            }
                            if (dtFee1.Rows.Count > 0)
                            {
                                for (int t = 0; t < dtFee1.Rows.Count; t++)
                                {
                                    if (dtFee1.Rows[t]["VehicalType"].ToString().Trim().ToUpper() == "Big Car".ToUpper())
                                    {
                                        for (int j = 3; j < dtFee1.Columns.Count; j++)
                                        {
                                            if (dtFee1.Rows[t][j].ToString().Trim() != "0")
                                            {
                                                objPark.MinimumParkingFeebigCar = dtFee1.Rows[t][j].ToString();

                                                break;
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        else
                        {
                            objPark.MinimumParkingFeeSmallCar = "";
                            objPark.MinimumParkingTime = "";
                            objPark.MinimumParkingFeebigCar = "";
                        }
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
                dbConnection.Close(); dbConnection.Dispose();
            }
            finally
            {
                try
                {
                    adp.Dispose(); dbConnection.Close(); dbConnection.Dispose(); cmd.Dispose();
                }
                catch { }
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
            cmd.Parameters.AddWithValue("@Longitude", String.Format("{0:0.000}", details.Longitude));


            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                dbConnection.Close(); dbConnection.Dispose();

                Parking_List = new List<ParkingLocations>();
                if (dt.Tables[0].Rows.Count > 0)
                {
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
                        objPark.ParkingId = dt.Tables[0].Rows[i]["ParkingLocationId"].ToString();
                        objPark.Town = dt.Tables[0].Rows[i]["Town"].ToString();
                        objPark.Remarks = "";
                        DataTable dtGetAvailability = AvailabilityParking(dt.Tables[0].Rows[i]["ParkingLocationId"].ToString().Trim());
                        DataTable dtFee = GetParkingFee(dt.Tables[0].Rows[i]["ParkingLocationId"].ToString().Trim(), "1");
                        DataTable dtFee1 = GetParkingFee(dt.Tables[0].Rows[i]["ParkingLocationId"].ToString().Trim(), "2");
                        if (dtGetAvailability.Rows.Count > 0)
                        {
                            if (Convert.ToInt32(dtGetAvailability.Rows[0]["Availability"].ToString().Trim()) > 0)
                            {
                                objPark.Availability = dtGetAvailability.Rows[0]["Availability"].ToString().Trim() + "/" + dtGetAvailability.Rows[0]["Capacity"].ToString().Trim();
                                objPark.percentage = string.Format("{0:0}", ((Convert.ToDouble(dtGetAvailability.Rows[0]["Availability"].ToString().Trim()) / Convert.ToDouble(dtGetAvailability.Rows[0]["Capacity"].ToString().Trim())) * 100));
                            }
                            else
                            {
                                objPark.Availability = "0"; //dtGetAvailability.Rows[0]["Availability"].ToString().Trim() + "/" + dtGetAvailability.Rows[0]["Capacity"].ToString().Trim();
                                objPark.percentage = "0";//string.Format("{0:0}", ((Convert.ToDouble(dtGetAvailability.Rows[0]["Availability"].ToString().Trim()) / Convert.ToDouble(dtGetAvailability.Rows[0]["Capacity"].ToString().Trim())) * 100));

                            }
                        }
                        else
                        {
                            objPark.Availability = "Not Known"; objPark.percentage = "";
                        }
                        if (dtFee.Rows.Count > 0)
                        {
                            objPark.MinimumParkingFeeSmallCar = "0";
                            for (int t = 0; t < dtFee.Rows.Count; t++)
                            {
                                if (dtFee.Rows[t]["VehicalType"].ToString().Trim().ToUpper() == "Small Car".ToUpper())
                                {
                                    for (int j = 3; j < dtFee.Columns.Count; j++)
                                    {
                                        if (dtFee.Rows[t][j].ToString().Trim() != "0")
                                        {

                                            string PriviousFee = objPark.MinimumParkingFeeSmallCar;
                                            string NewFee = dtFee.Rows[t][j].ToString();
                                            if (j == 3)
                                            {
                                                objPark.MinimumParkingFeeSmallCar = NewFee;
                                            }
                                            if (PriviousFee != null)
                                            {
                                                if (NewFee == PriviousFee)
                                                {
                                                    objPark.MinimumParkingFeeSmallCar = NewFee;
                                                    objPark.MinimumParkingTime = dtFee.Columns[j].ColumnName.ToString();
                                                    continue;

                                                }
                                                else
                                                {

                                                    continue;
                                                }
                                            }
                                            else
                                            {
                                                continue;
                                            }

                                            break;
                                        }
                                    }

                                }

                            }
                            if (dtFee1.Rows.Count > 0)
                            {
                                for (int t = 0; t < dtFee1.Rows.Count; t++)
                                {
                                    if (dtFee1.Rows[t]["VehicalType"].ToString().Trim().ToUpper() == "Big Car".ToUpper())
                                    {
                                        for (int j = 3; j < dtFee1.Columns.Count; j++)
                                        {
                                            if (dtFee1.Rows[t][j].ToString().Trim() != "0")
                                            {
                                                objPark.MinimumParkingFeebigCar = dtFee1.Rows[t][j].ToString();

                                                break;
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        else
                        {
                            objPark.MinimumParkingFeeSmallCar = "";
                            objPark.MinimumParkingTime = "";
                            objPark.MinimumParkingFeebigCar = "";
                        }

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
                dbConnection.Close(); dbConnection.Dispose();
            }
            finally
            {
                adp.Dispose(); cmd.Dispose(); dbConnection.Close(); dbConnection.Dispose();
            }
            return Parking_List;
        }

        public IEnumerable<ParkingLocations> getParkingGEOLocation_xml(LocationDetails details)
        {
            return GetAllParkingGoeLocation(details);

        }

        public IEnumerable<ParkingLocations> getParkingGEOLocation_JSON(LocationDetails details)
        {
            return GetAllParkingGoeLocation(details);
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

        #region Fee
        private IEnumerable<fee> GetAllParkingFee(LocationDetails details)
        {
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataSet dt = new DataSet();
            SqlCommand cmd = new SqlCommand("sp_GetParkingFeeGeo", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@Latitude", String.Format("{0:0.000}", details.Latitude));
            cmd.Parameters.AddWithValue("@Longitude", String.Format("{0:0.000}", details.Longitude));


            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                dbConnection.Close(); dbConnection.Dispose();

                fee_List = new List<fee>();
                if (dt.Tables[0].Rows.Count > 0)
                {
                    for (int i = 0; i < dt.Tables[0].Rows.Count; i++)
                    {

                        for (int j = 3; j < dt.Tables[0].Columns.Count; j++)
                        {
                            if (dt.Tables[0].Rows[i][2].ToString().ToUpper() == "SMALL CAR")
                            {
                                objfee = new fee();
                                objfee.Duration = dt.Tables[0].Columns[j].ColumnName.ToString();
                                objfee.Amount = dt.Tables[0].Rows[i][j].ToString();


                                fee_List.Add(objfee);
                            }
                            else
                            {
                                objfee = new fee();
                                objfee.Duration = dt.Tables[0].Columns[j].ColumnName.ToString();
                                objfee.Amount = dt.Tables[0].Rows[i][j].ToString();

                                fee_List.Add(objfee);
                            }
                        }
                    }
                }
                else
                {
                    //Parking_List = new List<ParkingLocations>();
                    //objPark = new ParkingLocations();
                    //objPark.Remarks = "No Record Found";
                    //Parking_List.Add(objPark);
                }
            }
            catch (Exception ex)
            {
                dbConnection.Close(); dbConnection.Dispose();
            }
            finally
            {
                adp.Dispose(); cmd.Dispose(); dbConnection.Close(); dbConnection.Dispose();
            }
            return fee_List;
        }

        private IEnumerable<fee> GetAllParkingFee(string ParkingId, string CarType)
        {
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataSet dt = new DataSet();
            SqlCommand cmd = new SqlCommand("sp_GetParkingFee", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@ParkingId", ParkingId);
            cmd.Parameters.AddWithValue("@CarType", CarType);



            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                dbConnection.Close(); dbConnection.Dispose();

                fee_List = new List<fee>();
                if (dt.Tables[0].Rows.Count > 0)
                {
                    for (int i = 0; i < dt.Tables[0].Rows.Count; i++)
                    {

                        for (int j = 3; j < dt.Tables[0].Columns.Count; j++)
                        {

                            objfee = new fee();
                            objfee.Duration = dt.Tables[0].Columns[j].ColumnName.ToString();
                            objfee.Amount = dt.Tables[0].Rows[i][j].ToString();


                            fee_List.Add(objfee);

                        }
                    }
                }
                else
                {
                    //Parking_List = new List<ParkingLocations>();
                    //objPark = new ParkingLocations();
                    //objPark.Remarks = "No Record Found";
                    //Parking_List.Add(objPark);
                }
            }
            catch (Exception ex)
            {
                dbConnection.Close(); dbConnection.Dispose();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close(); dbConnection.Dispose();
                }
                catch { }
            }
            return fee_List;
        }

        public IEnumerable<fee> getParkingLocFee_xml(LocationDetails details)
        {
            return GetAllParkingFee(details);
        }

        IEnumerable<fee> IHPParking.getParkingLocFee_JSON(LocationDetails details)
        {
            return GetAllParkingFee(details);
        }


        IEnumerable<fee> IHPParking.getParkingFee_xml(string ParkingId, string CarTypes)
        {
            return GetAllParkingFee(ParkingId, CarTypes);
        }

        IEnumerable<fee> IHPParking.getParkingFee_JSON(string ParkingId, string CarTypes)
        {
            return GetAllParkingFee(ParkingId, CarTypes);
        }
        #endregion

        #region VehicelRegistration

        private string VehicelRegistration(Registration objRegister)
        {
            string message = "";
            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("INSERT INTO [dbo].[tbl_VehicelRegistration]([VehicelNumber],[Name],[ContactNumber],Email,[IMI],[Createdate],[Createdby]) VALUES(@Vehicel,@Name,@mobile,@Email,@IMI,@CDate,@Createdby)", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                cmd.Parameters.AddWithValue("@Vehicel", objRegister.vehicelNumber.ToString().Replace(" ", "").Trim());
                cmd.Parameters.AddWithValue("@Name", objRegister.Name.ToString().Trim());
                cmd.Parameters.AddWithValue("@mobile", objRegister.MobileNumber.ToString().Trim());
                cmd.Parameters.AddWithValue("@Email", objRegister.EMail.ToString().Trim());
                cmd.Parameters.AddWithValue("@IMI", objRegister.IMI.ToString().Trim());
                cmd.Parameters.AddWithValue("@CDate", DateTime.Now.ToString());
                cmd.Parameters.AddWithValue("@Createdby", "Mobile App");
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }


                try
                {
                    cmd.ExecuteNonQuery(); message = "Registation completed";
                    dbConnection.Close(); dbConnection.Dispose();
                }
                catch (Exception ex)
                {
                    message = "Registation failed, please try again" + ex.Message.ToString();
                    dbConnection.Close(); dbConnection.Dispose();
                }
            }
            catch (Exception ex)
            {
                message = "Error" + ex.Message.ToString();
                dbConnection.Close(); dbConnection.Dispose();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); dbConnection.Dispose(); cmd.Dispose();
                }
                catch { }
            }

            return message;
        }


        string IHPParking.getVehicelRegister_JSON(Registration VehicleDetails)
        {
            return VehicelRegistration(VehicleDetails);
        }

        string IHPParking.getVehicelRegister_xml(Registration VehicleDetails)
        {
            return VehicelRegistration(VehicleDetails);
        }

        #endregion

        #region Issues&Feedback
        getComplaintType objComplaintType = null;
        List<getComplaintType> objComplaintType_List = new List<getComplaintType>();
        private IEnumerable<getNatureofComplaints> GetNaturOfcomplaionts()
        {
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataSet dt = new DataSet();
            SqlCommand cmd = new SqlCommand("getComplaints", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                dbConnection.Close(); dbConnection.Dispose();

                ComplaintList = new List<getNatureofComplaints>();
                if (dt.Tables[0].Rows.Count > 0)
                {
                    for (int i = 0; i < dt.Tables[0].Rows.Count; i++)
                    {
                        objComplaintType = new getComplaintType();
                        objComp.Id = dt.Tables[0].Rows[i][0].ToString().Trim();
                        objComp.Type = dt.Tables[0].Rows[i][1].ToString().Trim();
                        ComplaintList.Add(objComp);
                    }
                }
            }
            catch { dbConnection.Close(); dbConnection.Dispose(); }
            finally
            {
                try
                {
                    cmd.Dispose(); adp.Dispose(); dbConnection.Close();
                }
                catch
                { }
            }
            return ComplaintList;
        }

        private IEnumerable<getComplaintType> GetNaturOfcomplaiontsType(string id)
        {
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataSet dt = new DataSet();
            SqlCommand cmd = new SqlCommand("getComplainttype", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@id", id);
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                dbConnection.Close(); dbConnection.Dispose();


                if (dt.Tables[0].Rows.Count > 0)
                {
                    for (int i = 0; i < dt.Tables[0].Rows.Count; i++)
                    {
                        objComplaintType = new getComplaintType();
                        objComplaintType.Id = dt.Tables[0].Rows[i][0].ToString().Trim();
                        objComplaintType.ComplaintType = dt.Tables[0].Rows[i][2].ToString().Trim();
                        objComplaintType_List.Add(objComplaintType);
                    }

                }
            }
            catch { dbConnection.Close(); dbConnection.Dispose(); cmd.Dispose(); }
            finally
            {
                try
                {
                    dbConnection.Close(); dbConnection.Dispose(); cmd.Dispose();
                }
                catch { }
            }
            return objComplaintType_List;
        }

        public IEnumerable<getNatureofComplaints> getComplaintList_JSON()
        {
            return GetNaturOfcomplaionts();
        }

        public IEnumerable<getNatureofComplaints> getComplaintList_XML()
        {
            return GetNaturOfcomplaionts();
        }

        public IEnumerable<getComplaintType> getComplaintTypeList_JSON(string id)
        {
            return GetNaturOfcomplaiontsType(id);
        }

        public IEnumerable<getComplaintType> getComplaintTypeList_XML(string id)
        {
            return GetNaturOfcomplaiontsType(id);
        }

        private string SaveIssuesFeedback(IssuesFeedBack objIssue)
        {
            string message = "";

            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand(" INSERT INTO [tbl_IssueFeedback]([ComplaintCategory],[ComplaintType],[SubmiteAsAnomious],[ParkingId],[Name],[Email],[Phone],[IMEI],[IssueDiscription],[Latitude],[Longitude],[CeateDate],[Createdby]) " +
                                            " VALUES(@CCat,@Comptype,@Anomious,@ParkingId,@Name,@Email,@Phone,@IMEI,@Discription,@Lat,@Long,@CDate,@Createdby) ", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                cmd.Parameters.AddWithValue("@CCat", objIssue.NatureofComplaint.ToString().Trim());
                cmd.Parameters.AddWithValue("@Comptype", objIssue.TypeofComplaint.ToString().Trim());
                cmd.Parameters.AddWithValue("@ParkingId", objIssue.ParkingId.ToString().Trim());
                cmd.Parameters.AddWithValue("@Anomious", objIssue.Anoanonymous.ToString().Trim());
                cmd.Parameters.AddWithValue("@Name", objIssue.Name.ToString().Trim());
                cmd.Parameters.AddWithValue("@Email", objIssue.Email.ToString().Trim());
                cmd.Parameters.AddWithValue("@Phone", objIssue.Mobile.ToString().Trim());
                cmd.Parameters.AddWithValue("@IMEI", objIssue.IMEI.ToString().Trim());
                cmd.Parameters.AddWithValue("@Discription", objIssue.Discription.ToString().Trim());
                cmd.Parameters.AddWithValue("@Lat", objIssue.Latitude.ToString().Trim());
                cmd.Parameters.AddWithValue("@Long", objIssue.Longitude.ToString().Trim());
                cmd.Parameters.AddWithValue("@CDate", DateTime.Now.ToString());
                cmd.Parameters.AddWithValue("@Createdby", "Mobile App User");
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }


                try
                {
                    cmd.ExecuteNonQuery();
                    dbConnection.Close(); dbConnection.Dispose(); cmd.Dispose();
                    message = "Your feedback/ Complaint has been registered successfully, your reference number is :" + DateTime.Now.Year.ToString() + DateTime.Now.Month.ToString() + "-" + GetTranIdforComplaint(objIssue.Mobile.ToString().Trim(), objIssue.NatureofComplaint.ToString().Trim(), objIssue.TypeofComplaint.ToString().Trim(), objIssue.IMEI.ToString().Trim(), DateTime.Now.ToString("dd/MM/yyyy"));
                }
                catch (Exception ex)
                {
                    message = "Registation failed, please try again" + ex.Message.ToString();
                    dbConnection.Close(); dbConnection.Dispose(); cmd.Dispose();
                }
            }
            catch (Exception ex)
            {
                message = "Error" + ex.Message.ToString();
                dbConnection.Close(); dbConnection.Dispose(); cmd.Dispose();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); dbConnection.Dispose(); cmd.Dispose();
                }
                catch { }
            }

            return message;
        }

        private string GetTranIdforComplaint(string mobile, string complainttype, string complaint, string IMEI, string datetime)
        {
            string message = "";
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }

            DataSet dt = new DataSet();
            SqlCommand cmd = new SqlCommand("sp_GetTransId", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.AddWithValue("@mobile", mobile);
            cmd.Parameters.AddWithValue("@compttype", complainttype);
            cmd.Parameters.AddWithValue("@complaint", complaint);
            cmd.Parameters.AddWithValue("@IMEI", IMEI);
            cmd.Parameters.AddWithValue("@datetime", datetime);


            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                cmd.Dispose(); dbConnection.Close(); adp.Dispose();
                if (dt.Tables[0].Rows.Count > 0)
                {
                    message = dt.Tables[0].Rows[0][0].ToString();
                }
            }
            catch
            { }
            finally
            {
                try
                {
                    cmd.Dispose(); dbConnection.Close(); adp.Dispose();
                }
                catch { }
            }
            return message;
        }


        public string getIssueFeedback_JSON(IssuesFeedBack IssuesFeedback)
        {
            return SaveIssuesFeedback(IssuesFeedback);
        }

        public string getIssueFeedback_XML(IssuesFeedBack IssuesFeedback)
        {
            return SaveIssuesFeedback(IssuesFeedback);
        }
        #endregion

        #region ParkingTrans

        private string GetparkingFee(string ParkingId, string Time, string VehicleType)
        {
            string fee = "0.00";
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("sp_GetFee", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@ParkingId", ParkingId);
            cmd.Parameters.AddWithValue("@Time", Time);
            cmd.Parameters.AddWithValue("@VehicleType", VehicleType);

            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose();
                dbConnection.Close();
                if (dt.Rows.Count > 0)
                {
                    fee = dt.Rows[0][0].ToString().Trim();
                }

            }
            catch
            { }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose();
                    dbConnection.Close();
                }
                catch { }
            }

            return fee;
        }

        private string GetparkingName(string ParkingId)
        {
            string ParkingName = "";
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("Sp_GetParkingName", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@ParkingId", ParkingId);


            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose();
                dbConnection.Close();
                if (dt.Rows.Count > 0)
                {
                    ParkingName = dt.Rows[0][0].ToString().Trim();
                }

            }
            catch
            {
                adp.Dispose(); cmd.Dispose();
                dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose();
                    dbConnection.Close();
                }
                catch { }
            }

            return ParkingName;
        }

        private void SendMobile(string Massege, string MobNo)
        {
            try
            {

                Stream dataStream;
                HttpWebRequest request = (HttpWebRequest)WebRequest.Create("http://msdgweb.mgov.gov.in/esms/sendsmsrequest");
                request.ProtocolVersion = HttpVersion.Version10;
                ((HttpWebRequest)request).UserAgent = "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)";
                request.Method = "POST";
                try
                {
                    String smsservicetype = "bulkmsg";
                    String query = "username=" + HttpUtility.UrlEncode("hpgovt") +
                        "&password=" + HttpUtility.UrlEncode("hpdit@1234") +
                        "&smsservicetype=" + HttpUtility.UrlEncode(smsservicetype) +
                        "&content=" + HttpUtility.UrlEncode(Massege) +
                        "&bulkmobno=" + HttpUtility.UrlEncode(MobNo) +
                        "&senderid=" + HttpUtility.UrlEncode("hpgovt");
                    byte[] byteArray = Encoding.ASCII.GetBytes(query);
                    request.ContentType = "application/x-www-form-urlencoded";
                    request.ContentLength = byteArray.Length;
                    dataStream = request.GetRequestStream();
                    dataStream.Write(byteArray, 0, byteArray.Length);

                    dataStream.Close();
                    WebResponse response = request.GetResponse();
                    String Status = ((HttpWebResponse)response).StatusDescription;
                    dataStream = response.GetResponseStream();
                    StreamReader reader = new StreamReader(dataStream);
                    string responseFromServer = reader.ReadToEnd();
                    reader.Close();
                    dataStream.Close();
                }
                catch (Exception er)
                {
                    SendMobile(Massege,  MobNo);
                }

            }
            catch (Exception er)
            {
                SendMobile(Massege, MobNo);
            }
        }

        private string InsertParkingTrans(ParkingTrans objParkingTrans)
        {
            string message = "";

            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand(" INSERT INTO [dbo].[tbl_parkingtrans]([ParkingId],[TypeofCar],[VehicleNo],[DriverName],[PhoneNumber],[EstimatedParkingtime],[EstimatedFee],[Intime],[Outtime],[ActualFee],[CreateDate]) " +
                                            " VALUES (@Parkid,@TypeofCar,@VehicleNo,@DriverName,@PhoneNumber,@EstimatedParkingtime,@EstimatedFee,@Intime,@Outtime,@ActualFee,@CreateDate)", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                string time = "0.00";
                switch (objParkingTrans.EstimatedParkingtime.ToString())
                {
                    case "0":
                        time = "2";
                        break;
                    case "1":
                        time = "3";
                        break;
                    case "2":
                        time = "4";
                        break;
                    case "3":
                        time = "6";
                        break;
                    case "4":
                        time = "8";
                        break;
                    case "5":
                        time = "10";
                        break;
                    case "6":
                        time = "12";
                        break;
                    case "7":
                        time = "24";
                        break;
                    case "8":
                        time = "720";
                        break;
                    case "9":
                        time = "1";
                        break;
                }
                string Fee = GetparkingFee(objParkingTrans.ParkingId.ToString().Trim(), time, objParkingTrans.TypeofCar.ToString().Trim());
                cmd.Parameters.AddWithValue("@Parkid", objParkingTrans.ParkingId.ToString().Trim());
                cmd.Parameters.AddWithValue("@TypeofCar", objParkingTrans.TypeofCar.ToString().Trim());
                cmd.Parameters.AddWithValue("@VehicleNo", objParkingTrans.VehicleNo.ToString().Trim());
                cmd.Parameters.AddWithValue("@DriverName", objParkingTrans.DriverName.ToString().Trim());
                cmd.Parameters.AddWithValue("@PhoneNumber", objParkingTrans.PhoneNumber.ToString().Trim());
                cmd.Parameters.AddWithValue("@EstimatedParkingtime", time);
                cmd.Parameters.AddWithValue("@EstimatedFee", Math.Round(Convert.ToDouble(Fee)).ToString());
                cmd.Parameters.AddWithValue("@Intime", Convert.ToDateTime(objParkingTrans.InTime.ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Parameters.AddWithValue("@Outtime", "");
                cmd.Parameters.AddWithValue("@ActualFee", objParkingTrans.ActualFee.ToString().Trim());
                cmd.Parameters.AddWithValue("@CreateDate", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }
                try
                {
                    cmd.ExecuteNonQuery();
                    dbConnection.Close(); cmd.Dispose();
                    message = "Your vehicle " + objParkingTrans.VehicleNo.ToString().Trim() + " is parked in " + GetparkingName(objParkingTrans.ParkingId.ToString().Trim()) + ". Estimated parking fee is Rs." + Math.Round(Convert.ToDouble(Fee)).ToString() + " and out time is before " + Convert.ToDateTime(Convert.ToDateTime(objParkingTrans.InTime.ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss")).AddHours(Convert.ToDouble(time)).ToString("yyyy-MM-dd HH:mm:ss");
                    if (objParkingTrans.PhoneNumber.ToString().Trim() != "")
                    {
                        SendMobile(message, objParkingTrans.PhoneNumber.ToString().Trim());
                    }
                }
                catch (Exception ex)
                {
                    dbConnection.Close(); cmd.Dispose();
                    message = "Trandaction failed, please try again" + ex.Message.ToString();
                }
            }
            catch (Exception ex)
            {
                dbConnection.Close(); cmd.Dispose();
                message = "Error" + ex.Message.ToString();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); cmd.Dispose();
                }
                catch
                { }
            }

            return message;
        }

        public string getParkingTransaction_JSON(ParkingTrans ParkTrans)
        {
            return InsertParkingTrans(ParkTrans);
        }

        public string getParkingTransaction_XML(ParkingTrans ParkTrans)
        {
            return InsertParkingTrans(ParkTrans);
        }
        #endregion

        #region Parked Vehicle List

        private IEnumerable<ParkedVehicles> GetAllParkedVehicles(string ParkingId)
        {
            List<ParkedVehicles> Vehicle_List = null;//new List<ParkedVehicles>();
            ParkedVehicles objVehicle = null;//new ParkedVehicles();
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("SP_GetVehicelList", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@ParkingId", ParkingId);
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                Vehicle_List = new List<ParkedVehicles>();
                if (dt.Rows.Count > 0)
                {

                    for (int i = 0; i < dt.Rows.Count; i++)
                    {
                        objVehicle = new ParkedVehicles();
                        objVehicle.VehicleNo = dt.Rows[i]["VehicleNo"].ToString().Trim();
                        objVehicle.DriverName = dt.Rows[i]["DriverName"].ToString().Trim();
                        objVehicle.PhoneNumber = dt.Rows[i]["PhoneNumber"].ToString().Trim();
                        objVehicle.ParkingId = dt.Rows[i]["ParkingId"].ToString().Trim();
                        objVehicle.ParkInTime =Convert.ToDateTime(dt.Rows[i]["ParkInTime"].ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss");
                        objVehicle.ParkOutTime = Convert.ToDateTime(Convert.ToDateTime(dt.Rows[i]["ParkInTime"].ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss")).AddHours(Convert.ToDouble(dt.Rows[i]["ParkOutTime"].ToString().Trim())).ToString("yyyy-MM-dd HH:mm:ss");
                        Vehicle_List.Add(objVehicle);
                    }
                }
                else
                {
                    Vehicle_List.Clear();
                }
            }
            catch
            {
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                }
                catch { }
            }
            return Vehicle_List;
        }

        public IEnumerable<ParkedVehicles> getParkedVehiclelist_JSON(string ParkingId)
        {
            return GetAllParkedVehicles(ParkingId.Trim());
        }

        public IEnumerable<ParkedVehicles> getParkedVehiclelist_XML(string ParkingId)
        {
            return GetAllParkedVehicles(ParkingId.Trim());
        }

        #endregion

        #region VehicleOut
        private string GetFinalFee(string parkingId, string VehicleNumber, string OutTime)
        {
            //Sp_GetOutFee

            string fee = "0.00";
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("Sp_GetOutFee", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@ParkingId", parkingId.Trim());
            cmd.Parameters.AddWithValue("@VehicleNo", VehicleNumber.Trim());
            cmd.Parameters.AddWithValue("@OutTime", OutTime.Trim());

            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose();
                dbConnection.Close();
                if (dt.Rows.Count > 0)
                {
                    fee = dt.Rows[0][0].ToString().Trim();
                }

            }
            catch
            {
                adp.Dispose(); cmd.Dispose();
                dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose();
                    dbConnection.Close();
                }
                catch { }
            }

            return fee;
        }

        private string VehicleOut(VehicleOut objVehicleOuts)
        {
            string message = "";
            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("UPDATE [dbo].[tbl_parkingtrans] SET [Outtime] = @Outtime , [ActualFee] = @fee WHERE ParkingId=@ParkingId and VehicleNo=@Vehicle and PaymentReceived='false'", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                string Fee = GetFinalFee(objVehicleOuts.ParkingId.Trim(), objVehicleOuts.VehicleNo.Trim(), Convert.ToDateTime(objVehicleOuts.OutTime.Trim()).ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Parameters.AddWithValue("@Outtime", Convert.ToDateTime(objVehicleOuts.OutTime.Trim()).ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Parameters.AddWithValue("@fee", Math.Round(Convert.ToDouble(Fee)).ToString());
                cmd.Parameters.AddWithValue("@ParkingId", objVehicleOuts.ParkingId.Trim());
                cmd.Parameters.AddWithValue("@Vehicle", objVehicleOuts.VehicleNo.Trim());
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }
                try
                {
                    cmd.ExecuteNonQuery();
                    dbConnection.Close(); cmd.Dispose();
                    message = "Your vehicle " + objVehicleOuts.VehicleNo.Trim() + " is out from   " + GetparkingName(objVehicleOuts.ParkingId.Trim()) + ". Check out time is " + Convert.ToDateTime(objVehicleOuts.OutTime.Trim()).ToString("yyyy-MM-dd HH:mm:ss") + " Total parking fee is Rs." + Math.Round(Convert.ToDouble(Fee)).ToString() + ".";
                    if (objVehicleOuts.PhoneNumber.Trim() != "")
                    {
                        SendMobile(message, objVehicleOuts.PhoneNumber.Trim());
                    }
                }
                catch (Exception ex)
                {
                    dbConnection.Close(); cmd.Dispose();
                    message = "Trandaction failed, please try again" + ex.Message.ToString();
                }
            }
            catch (Exception ex)
            {
                dbConnection.Close(); cmd.Dispose();
                message = "Error" + ex.Message.ToString();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); cmd.Dispose();
                }
                catch { }
            }

            return message;


        }


        public string getParkingOut_JSON(VehicleOut VehicleOuts)
        {
            return VehicleOut(VehicleOuts);
        }

        public string getParkingOut_XML(VehicleOut VehicleOuts)
        {
            return VehicleOut(VehicleOuts);
        }
        #endregion

        #region Confirm Payment

        private string VehicleOutConfirm(VehicleOut objVehicleOuts)
        {
            string message = "";
            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("UPDATE [dbo].[tbl_parkingtrans] SET PaymentReceived='true'  WHERE ParkingId=@ParkingId and VehicleNo=@Vehicle and [Outtime] = @Outtime ", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                //string Fee = GetFinalFee(objVehicleOuts.ParkingId.Trim(), objVehicleOuts.VehicleNo.Trim(), objVehicleOuts.OutTime.Trim());
                cmd.Parameters.AddWithValue("@Outtime", Convert.ToDateTime(objVehicleOuts.OutTime.Trim()).ToString("yyyy-MM-dd HH:mm:ss"));

                cmd.Parameters.AddWithValue("@ParkingId", objVehicleOuts.ParkingId.Trim());
                cmd.Parameters.AddWithValue("@Vehicle", objVehicleOuts.VehicleNo.Trim());
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }
                try
                {
                    cmd.ExecuteNonQuery();
                    dbConnection.Close(); cmd.Dispose();
                    message = "Saved successfully";

                }
                catch (Exception ex)
                {
                    dbConnection.Close(); cmd.Dispose();
                    message = "Trandaction failed, please try again" + ex.Message.ToString();
                }
            }
            catch (Exception ex)
            {
                dbConnection.Close(); cmd.Dispose();
                message = "Error" + ex.Message.ToString();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); cmd.Dispose();
                }
                catch { }
            }

            return message;


        }
        public string getConfirmPayment_JSON(VehicleOut VehicleOuts)
        {
            return VehicleOutConfirm(VehicleOuts);
        }

        public string getConfirmPayment_XML(VehicleOut VehicleOuts)
        {
            return VehicleOutConfirm(VehicleOuts);
        }

        #endregion

        #region Return AllStates
        public IEnumerable<States> getStates_JSON()
        {
            return GetAllStates();
        }

        public IEnumerable<States> getStates_XML()
        {
            return GetAllStates();
        }

        private IEnumerable<States> GetAllStates()
        {
            List<States> State_List = null;//new List<ParkedVehicles>();
            States objState = null;//new ParkedVehicles();
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("sp_GetAllDistrict", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();

            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                State_List = new List<States>();
                if (dt.Rows.Count > 0)
                {

                    for (int i = 0; i < dt.Rows.Count; i++)
                    {
                        objState = new States();
                        objState.StateCode = dt.Rows[i]["statecd"].ToString().Trim();
                        objState.StateName = dt.Rows[i]["statenm"].ToString().Trim();
                        State_List.Add(objState);
                    }
                }
                else
                {
                    State_List.Clear();
                }
            }
            catch
            {
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                }
                catch { }
            }
            return State_List;
        }
        #endregion

        #region Return All Districts according to State
        public IEnumerable<Districts> getDistrict_JSON(string StateId)
        {
            return GetAllDistrict(StateId);
        }

        public IEnumerable<Districts> getDistrict_XML(string StateId)
        {
            return GetAllDistrict(StateId);
        }

        private IEnumerable<Districts> GetAllDistrict(string StateId)
        {
            List<Districts> District_List = null;
            Districts objDistrict = null;
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("sp_GetAllDistrict", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@StateCode", StateId);
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                District_List = new List<Districts>();
                if (dt.Rows.Count > 0)
                {

                    for (int i = 0; i < dt.Rows.Count; i++)
                    {
                        objDistrict = new Districts();
                        objDistrict.DistrictCode = dt.Rows[i]["districtcd"].ToString().Trim();
                        objDistrict.DistrictName = dt.Rows[i]["districtnm"].ToString().Trim();
                        District_List.Add(objDistrict);
                    }
                }
                else
                {
                    District_List.Clear();
                }
            }
            catch
            {
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                }
                catch { }
            }
            return District_List;
        }

        #endregion

        #region GetParkingInRequest
        private IEnumerable<ParkinRequest> getAllParkReqest(string ParkingId)
        {
            List<ParkinRequest> ParkingRequest_List = null;
            ParkinRequest objParkInRequest = null;
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("Sp_ReturnAllParkingRequest", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@ParkingId", ParkingId);
            cmd.Parameters.AddWithValue("@date", DateTime.Now.ToString("yyyy-MM-dd"));
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                ParkingRequest_List = new List<ParkinRequest>();
                if (dt.Rows.Count > 0)
                {

                    for (int i = 0; i < dt.Rows.Count; i++)
                    {
                        objParkInRequest = new ParkinRequest();
                        objParkInRequest.ParkingId = dt.Rows[i]["ParkingId"].ToString().Trim();
                        objParkInRequest.RegisterId = dt.Rows[i]["RegistrationId"].ToString().Trim();
                        objParkInRequest.VehicleNo = dt.Rows[i]["VehichleNumber"].ToString().Trim();
                        objParkInRequest.PhoneNumber = dt.Rows[i]["MobileNumber"].ToString().Trim();
                        objParkInRequest.EstimatedTime = dt.Rows[i]["EstimatedParkTime"].ToString().Trim();
                        objParkInRequest.VehicleType = dt.Rows[i]["VehichleType"].ToString().Trim();
                        objParkInRequest.RequestTime = Convert.ToDateTime(dt.Rows[i]["RequestDatetime"].ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss");
                        objParkInRequest.RequestStatus = dt.Rows[i]["RequestStatus"].ToString().Trim();
                        objParkInRequest.InTime = "";
                        ParkingRequest_List.Add(objParkInRequest);
                    }
                }
                else
                {
                    ParkingRequest_List.Clear();
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                }

            }
            catch
            {
                ParkingRequest_List.Clear();
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                }
                catch { }
            }
            return ParkingRequest_List;

        }

        public IEnumerable<ParkinRequest> getAllParkReqest_JSON(string ParkingId)
        {
            return getAllParkReqest(ParkingId);
        }

        public IEnumerable<ParkinRequest> getAllParkReqest_XML(string ParkingId)
        {
            return getAllParkReqest(ParkingId);
        }

        #endregion

        #region GetParkinConfirmStatus
        private string ParkingConfirm(ParkinRequest ParkIn)
        {
            string message = "";
            if (InsertParkingReqsTrans(ParkIn) == true)
            {

                dbConnection = DBConnect.getConnection();

                DataTable dt = new DataTable();
                //--and [RequestDatetime]=@Date 
                SqlCommand cmd = new SqlCommand("UPDATE [dbo].[tbl_ParkInRequest] SET [RequestStatus] = @RequestStatus WHERE [ParkingId]=@ParkId and [RegistrationId]=@RegId and [VehichleNumber]=@VehichleNo and [MobileNumber]=@MobNo ", dbConnection);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.Clear();
                try
                {
                   // string time = "0.00";
                    //switch (ParkIn.EstimatedTime.ToString())
                    //{
                    //    case "0":
                    //        time = "2";
                    //        break;
                    //    case "1":
                    //        time = "3";
                    //        break;
                    //    case "2":
                    //        time = "4";
                    //        break;
                    //    case "3":
                    //        time = "6";
                    //        break;
                    //    case "4":
                    //        time = "8";
                    //        break;
                    //    case "5":
                    //        time = "10";
                    //        break;
                    //    case "6":
                    //        time = "12";
                    //        break;
                    //    case "7":
                    //        time = "24";
                    //        break;
                    //    case "8":
                    //        time = "720";
                    //        break;
                    //    case "9":
                    //        time = "1";
                    //        break;
                    //}
                    string Fee = string.Format("{0:0.00}", GetparkingFee(ParkIn.ParkingId.ToString().Trim(), ParkIn.EstimatedTime.ToString(), ParkIn.VehicleType.ToString().Trim()));
                    cmd.Parameters.AddWithValue("@ParkId", ParkIn.ParkingId.ToString().Trim());
                    cmd.Parameters.AddWithValue("@VehichleNo", ParkIn.VehicleNo.ToString().Trim());
                    cmd.Parameters.AddWithValue("@RegId", ParkIn.RegisterId.ToString().Trim());
                    cmd.Parameters.AddWithValue("@MobNo", ParkIn.PhoneNumber.ToString().Trim());
                    cmd.Parameters.AddWithValue("@RequestStatus", ParkIn.RequestStatus.ToString().Trim());
                   // cmd.Parameters.AddWithValue("@Date", Convert.ToDateTime(ParkIn.InTime.ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss"));
                    cmd.Connection = dbConnection;
                    if (dbConnection.State.ToString() == "Closed")
                    {
                        dbConnection.Open();
                    }
                    try
                    {
                        cmd.ExecuteNonQuery();
                        dbConnection.Close(); cmd.Dispose();
                        message = "Your parking request for vehicle number " + ParkIn.VehicleNo.ToString().Trim() + " at " + GetparkingName(ParkIn.ParkingId.ToString().Trim()) + " is confirmed. Estimated parking fee is Rs." + Math.Round(Convert.ToDouble(Fee)).ToString() + " and out time is before " + Convert.ToDateTime(ParkIn.InTime.ToString().Trim()).AddHours(Convert.ToDouble(ParkIn.EstimatedTime.ToString().Trim())).ToString("yyyy-MM-dd HH:mm:ss");
                        if (ParkIn.PhoneNumber.ToString().Trim() != "")
                        {
                            SendMobile(message, ParkIn.PhoneNumber.ToString().Trim());
                        }


                    }
                    catch (Exception ex)
                    {
                        message = "Trandaction failed, please try again" + ex.Message.ToString();
                        dbConnection.Close(); cmd.Dispose();
                    }
                }
                catch (Exception ex)
                {
                    message = "Error" + ex.Message.ToString();
                    dbConnection.Close(); cmd.Dispose();
                }
                finally
                {
                    try
                    {
                        dbConnection.Close(); cmd.Dispose();
                    }
                    catch { }
                }
 
            }
            return message;

        }


        private bool InsertParkingReqsTrans(ParkinRequest ParkIn)
        {
            bool sucess = false;

            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand(" INSERT INTO [dbo].[tbl_parkingtrans]([ParkingId],[TypeofCar],[VehicleNo],[DriverName],[PhoneNumber],[EstimatedParkingtime],[EstimatedFee],[Intime],[Outtime],[ActualFee],[CreateDate]) " +
                                            " VALUES (@Parkid,@TypeofCar,@VehicleNo,@DriverName,@PhoneNumber,@EstimatedParkingtime,@EstimatedFee,@Intime,@Outtime,@ActualFee,@CreateDate)", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                //string time = "0.00";
                //switch (ParkIn.EstimatedTime.ToString())
                //{
                //    case "0":
                //        time = "2";
                //        break;
                //    case "1":
                //        time = "3";
                //        break;
                //    case "2":
                //        time = "4";
                //        break;
                //    case "3":
                //        time = "6";
                //        break;
                //    case "4":
                //        time = "8";
                //        break;
                //    case "5":
                //        time = "10";
                //        break;
                //    case "6":
                //        time = "12";
                //        break;
                //    case "7":
                //        time = "24";
                //        break;
                //    case "8":
                //        time = "720";
                //        break;
                //    case "9":
                //        time = "1";
                //        break;
                //}

                string Fee = string.Format("{0:0.00}", GetparkingFee(ParkIn.ParkingId.ToString().Trim(), ParkIn.EstimatedTime.ToString(), ParkIn.VehicleType.ToString().Trim()));
                cmd.Parameters.AddWithValue("@Parkid", ParkIn.ParkingId.ToString().Trim());
                cmd.Parameters.AddWithValue("@TypeofCar", ParkIn.VehicleType.ToString().Trim());
                cmd.Parameters.AddWithValue("@VehicleNo", ParkIn.VehicleNo.ToString().Trim());
                cmd.Parameters.AddWithValue("@DriverName", "");
                cmd.Parameters.AddWithValue("@PhoneNumber", ParkIn.PhoneNumber.ToString().Trim());
                cmd.Parameters.AddWithValue("@EstimatedParkingtime", ParkIn.EstimatedTime.ToString());
                cmd.Parameters.AddWithValue("@EstimatedFee", Math.Round(Convert.ToDouble(Fee)).ToString());
                cmd.Parameters.AddWithValue("@Intime", Convert.ToDateTime(ParkIn.InTime.ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Parameters.AddWithValue("@Outtime", "");
                cmd.Parameters.AddWithValue("@ActualFee", "");
                cmd.Parameters.AddWithValue("@CreateDate", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }
                try
                {
                    cmd.ExecuteNonQuery();
                    dbConnection.Close(); cmd.Dispose();
                    sucess = true;
                }
                catch (Exception ex)
                {
                    dbConnection.Close(); cmd.Dispose();
                    //  message = "Trandaction failed, please try again" + ex.Message.ToString();
                    sucess = false;
                }
            }
            catch (Exception ex)
            {
                //message = "Error" + ex.Message.ToString();
                dbConnection.Close(); cmd.Dispose();
                sucess = false;
            }
            finally
            {
                try
                {
                    dbConnection.Close(); cmd.Dispose();
                }
                catch { }
            }

            return sucess;
        }
        public string getConfirmParkinStatus_JSON(ParkinRequest ParkInRequst)
        {
           return ParkingConfirm(ParkInRequst);
        }

        public string getConfirmParkinStatus_XML(ParkinRequest ParkInRequst)
        {
            return ParkingConfirm(ParkInRequst);
        }

        #endregion

        #region Parking Out Request

        private IEnumerable<ParkinRequest> getAllParkOutReqest(string ParkingId)
        {
            List<ParkinRequest> ParkingRequest_List = null;
            ParkinRequest objParkInRequest = null;
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("Sp_ReturnAllParkingOutRequest", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@ParkingId", ParkingId);
            cmd.Parameters.AddWithValue("@date", DateTime.Now.ToString("yyyy-MM-dd"));
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                ParkingRequest_List = new List<ParkinRequest>();
                if (dt.Rows.Count > 0)
                {

                    for (int i = 0; i < dt.Rows.Count; i++)
                    {
                        objParkInRequest = new ParkinRequest();
                        objParkInRequest.ParkingId = dt.Rows[i]["ParkingId"].ToString().Trim();
                        objParkInRequest.RegisterId = dt.Rows[i]["RegistrationId"].ToString().Trim();
                        objParkInRequest.VehicleNo = dt.Rows[i]["VehichleNumber"].ToString().Trim();
                        objParkInRequest.PhoneNumber = dt.Rows[i]["MobileNumber"].ToString().Trim();
                        objParkInRequest.EstimatedTime = dt.Rows[i]["EstimatedParkTime"].ToString().Trim();
                        objParkInRequest.VehicleType = dt.Rows[i]["VehichleType"].ToString().Trim();
                        objParkInRequest.RequestTime = Convert.ToDateTime(dt.Rows[i]["RequestDatetime"].ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss");
                        objParkInRequest.RequestStatus = dt.Rows[i]["RequestStatus"].ToString().Trim();
                        objParkInRequest.InTime = "";
                        ParkingRequest_List.Add(objParkInRequest);
                    }
                }
                else
                {
                    ParkingRequest_List.Clear();

                }

            }
            catch
            {
                ParkingRequest_List.Clear();
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                }
                catch { }
            }
            return ParkingRequest_List;

        }

        public IEnumerable<ParkinRequest> getAllParkOutReqest_JSON(string ParkingId)
        {
            return getAllParkOutReqest(ParkingId);
        }

        public IEnumerable<ParkinRequest> getAllParkOutReqest_XML(string ParkingId)
        {
            return getAllParkOutReqest(ParkingId);
        }
        #endregion

        #region ConfirmOutParking

        private string ParkingOutConfirm(ParkinRequest ParkIn)
        {
            string message = "";
            if (VehicleOutRequest(ParkIn) == true)
            {

                dbConnection = DBConnect.getConnection();

                DataTable dt = new DataTable();
                //--and [RequestDatetime]=@Date 
                SqlCommand cmd = new SqlCommand("UPDATE [dbo].[tbl_ParkOutRequest] SET [RequestStatus] = @RequestStatus WHERE [ParkingId]=@ParkId and [RegistrationId]=@RegId and [VehichleNumber]=@VehichleNo and [MobileNumber]=@MobNo ", dbConnection);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.Clear();
                try
                {
                    string Fee = string.Format("{0:0.00}", GetparkingFee(ParkIn.ParkingId.ToString().Trim(), ParkIn.EstimatedTime.ToString().Trim(), ParkIn.VehicleType.ToString().Trim()));
                    cmd.Parameters.AddWithValue("@ParkId", ParkIn.ParkingId.ToString().Trim());
                    cmd.Parameters.AddWithValue("@VehichleNo", ParkIn.VehicleNo.ToString().Trim());
                    cmd.Parameters.AddWithValue("@RegId", ParkIn.RegisterId.ToString().Trim());
                    cmd.Parameters.AddWithValue("@MobNo", ParkIn.PhoneNumber.ToString().Trim());
                    cmd.Parameters.AddWithValue("@RequestStatus", ParkIn.RequestStatus.ToString().Trim());
                    // cmd.Parameters.AddWithValue("@Date", Convert.ToDateTime(ParkIn.InTime.ToString().Trim()).ToString("yyyy-MM-dd HH:mm:ss"));
                    cmd.Connection = dbConnection;
                    if (dbConnection.State.ToString() == "Closed")
                    {
                        dbConnection.Open();
                    }
                    try
                    {
                        cmd.ExecuteNonQuery();
                        dbConnection.Close(); cmd.Dispose();
                        message = "Your vehicle " + ParkIn.VehicleNo.Trim() + " is out from   " + GetparkingName(ParkIn.ParkingId.Trim()) + ". Check out time is " + Convert.ToDateTime(ParkIn.RequestTime.Trim()).ToString("dd/MM/yyyy HH:mm:ss") + " Total parking fee is Rs." + Math.Round(Convert.ToDouble(Fee)).ToString() + ".";
                        if (ParkIn.PhoneNumber.Trim() != "")
                        {
                            SendMobile(message, ParkIn.PhoneNumber.Trim());
                        }


                    }
                    catch (Exception ex)
                    {
                        message = "Trandaction failed, please try again" + ex.Message.ToString();
                        dbConnection.Close(); cmd.Dispose();
                    }
                }
                catch (Exception ex)
                {
                    message = "Error" + ex.Message.ToString();
                    dbConnection.Close(); cmd.Dispose();
                }
                finally
                {
                    try
                    {
                        dbConnection.Close(); cmd.Dispose();
                    }
                    catch { }
                }

            }
            return message;

        }

        private bool VehicleOutRequest(ParkinRequest ParkIn)
        {
            bool message = false;
            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("UPDATE [dbo].[tbl_parkingtrans] SET [Outtime] = @Outtime , [ActualFee] = @fee, [PaymentReceived]='true'   WHERE ParkingId=@ParkingId and VehicleNo=@Vehicle and ActualFee='' and PaymentReceived='false' ", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                string Fee = GetFinalFee(ParkIn.ParkingId.Trim(), ParkIn.VehicleNo.Trim(), ParkIn.RequestTime.Trim());
                cmd.Parameters.AddWithValue("@Outtime", Convert.ToDateTime(ParkIn.RequestTime.Trim()).ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Parameters.AddWithValue("@fee", Math.Round(Convert.ToDouble(Fee)).ToString());
                cmd.Parameters.AddWithValue("@ParkingId", ParkIn.ParkingId);
                cmd.Parameters.AddWithValue("@Vehicle", ParkIn.VehicleNo);
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }
                try
                {
                    cmd.ExecuteNonQuery();
                    dbConnection.Close(); cmd.Dispose();
                    message = true;
                }
                catch (Exception ex)
                {
                    dbConnection.Close(); cmd.Dispose();
                }
            }
            catch (Exception ex)
            {
                dbConnection.Close(); cmd.Dispose();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); cmd.Dispose();
                }
                catch { }
            }

            return message;


        }

        public string getConfirmParkOutStatus_JSON(ParkinRequest ParkInRequst)
        {
            return ParkingOutConfirm(ParkInRequst);
        }

        public string getConfirmParkOutStatus_XML(ParkinRequest ParkInRequst)
        {
            return ParkingOutConfirm(ParkInRequst);
        }

        #endregion
        
        #region getParkMeRequest

        private bool DeletePriviousRequst(ParkinRequest ParkMeRequest)
        {
            bool success=false;
            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand(" Delete from [dbo].[tbl_ParkInRequest] where [ParkingId]=@ParkingId and [VehichleNumber]=@VehichleNumber and [VehichleType]=@VehichleType and [MobileNumber]=@MobileNumber and RequestStatus='Pending'", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                cmd.Parameters.AddWithValue("@ParkingId", ParkMeRequest.ParkingId.ToString().Trim());
                cmd.Parameters.AddWithValue("@VehichleNumber", ParkMeRequest.VehicleNo.ToString().Trim());
                cmd.Parameters.AddWithValue("@VehichleType", ParkMeRequest.VehicleType.ToString().Trim());
                cmd.Parameters.AddWithValue("@MobileNumber", ParkMeRequest.PhoneNumber.ToString().Trim());
                
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }


                try
                {
                    cmd.ExecuteNonQuery(); success = true;
                    dbConnection.Close(); cmd.Dispose();
                }
                catch (Exception ex)
                {
                    success = false;
                    dbConnection.Close(); cmd.Dispose();
                }
            }
            catch (Exception ex)
            {
                success = false;
                dbConnection.Close(); cmd.Dispose();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); cmd.Dispose();
                }
                catch { }
            }
            return success;
        }
        private string getParkMeRequest(ParkinRequest ParkMeRequest)
        {
            string message = "";
            if (DeletePriviousRequst(ParkMeRequest) == true)
            {
                
                dbConnection = DBConnect.getConnection();

                DataTable dt = new DataTable();
                SqlCommand cmd = new SqlCommand(" INSERT INTO [dbo].[tbl_ParkInRequest]([ParkingId],[RegistrationId],[VehichleNumber],[VehichleType],[EstimatedParkTime],[MobileNumber],[RequestDatetime],[RequestStatus],[CreateDate]) " +
                                                " VALUES (@ParkingId,@RegistrationId,@VehichleNumber,@VehichleType,@EstimatedParkTime,@MobileNumber,@RequestDatetime,@RequestStatus,@CreateDate)", dbConnection);
                cmd.CommandType = CommandType.Text;
                cmd.Parameters.Clear();
                try
                {
                    string time = "0.00";
                    switch (ParkMeRequest.EstimatedTime.ToString().Trim())
                    {
                        case "0":
                            time = "2";
                            break;
                        case "1":
                            time = "3";
                            break;
                        case "2":
                            time = "4";
                            break;
                        case "3":
                            time = "6";
                            break;
                        case "4":
                            time = "8";
                            break;
                        case "5":
                            time = "10";
                            break;
                        case "6":
                            time = "12";
                            break;
                        case "7":
                            time = "24";
                            break;
                        case "8":
                            time = "720";
                            break;
                        case "9":
                            time = "1";
                            break;
                    }
                    cmd.Parameters.AddWithValue("@ParkingId", ParkMeRequest.ParkingId.ToString().Trim());
                    cmd.Parameters.AddWithValue("@RegistrationId", ParkMeRequest.RegisterId.ToString().Trim());
                    cmd.Parameters.AddWithValue("@VehichleNumber", ParkMeRequest.VehicleNo.ToString().Trim());
                    cmd.Parameters.AddWithValue("@VehichleType", ParkMeRequest.VehicleType.ToString().Trim());
                    cmd.Parameters.AddWithValue("@MobileNumber", ParkMeRequest.PhoneNumber.ToString().Trim());
                    cmd.Parameters.AddWithValue("@EstimatedParkTime", time);
                    cmd.Parameters.AddWithValue("@RequestDatetime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                    cmd.Parameters.AddWithValue("@RequestStatus", "Pending");
                    cmd.Parameters.AddWithValue("@CreateDate", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                    cmd.Connection = dbConnection;
                    if (dbConnection.State.ToString() == "Closed")
                    {
                        dbConnection.Open();
                    }


                    try
                    {
                        cmd.ExecuteNonQuery(); message = "Your request has been received";
                        dbConnection.Close(); cmd.Dispose();
                    }
                    catch (Exception ex)
                    {
                        message = "Registation failed, please try again" + ex.Message.ToString();
                        dbConnection.Close(); cmd.Dispose();
                    }
                }
                catch (Exception ex)
                {
                    message = "Error" + ex.Message.ToString();
                    dbConnection.Close(); cmd.Dispose();
                }
                finally
                {
                    try
                    {
                        dbConnection.Close(); cmd.Dispose();
                    }
                    catch { }
                }
            }
            else
            {
                message = "Registation failed, please try again";
            }

            return message;
        }

        public string getParkMeRequest_JSON(ParkinRequest ParkMeRequst)
        {
            return getParkMeRequest(ParkMeRequst);
        }

        public string getParkMeRequest_XML(ParkinRequest ParkMeRequst)
        {
            return getParkMeRequest(ParkMeRequst); 
        }
        #endregion

        #region getParkOutRequest

        private bool CheckParkingStatus(string ParkingId, string Vehiclenumber)
        {
            bool Parked = false;
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("sp_GetAllDistrict", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@ParkId", ParkingId);
            cmd.Parameters.AddWithValue("@Vehicle", Vehiclenumber);
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
               
                if (dt.Rows.Count > 0)
                {

               
                }
                else
                {
                    
                }
            }
            catch
            {
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                }
                catch { }
            }
            return Parked;

        }
        private string getParkOutRequest(ParkinRequest ParkOutRequest)
        {
            string message = "";
            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand(" INSERT INTO [dbo].[tbl_ParkOutRequest]([ParkingId],[RegistrationId],[VehichleNumber],[VehichleType],[EstimatedParkTime],[MobileNumber],[RequestDatetime],[RequestStatus],[CreateDate]) " +
                                            " VALUES (@ParkingId,@RegistrationId,@VehichleNumber,@VehichleType,@EstimatedParkTime,@MobileNumber,@RequestDatetime,@RequestStatus,@CreateDate)", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                string time = "0.00";
                switch (ParkOutRequest.EstimatedTime.ToString().Trim())
                {
                    case "0":
                        time = "2";
                        break;
                    case "1":
                        time = "3";
                        break;
                    case "2":
                        time = "4";
                        break;
                    case "3":
                        time = "6";
                        break;
                    case "4":
                        time = "8";
                        break;
                    case "5":
                        time = "10";
                        break;
                    case "6":
                        time = "12";
                        break;
                    case "7":
                        time = "24";
                        break;
                    case "8":
                        time = "720";
                        break;
                    case "9":
                        time = "1";
                        break;
                }

                cmd.Parameters.AddWithValue("@ParkingId", ParkOutRequest.ParkingId.ToString().Trim());
                cmd.Parameters.AddWithValue("@RegistrationId", ParkOutRequest.RegisterId.ToString().Trim());
                cmd.Parameters.AddWithValue("@VehichleNumber", ParkOutRequest.VehicleNo.ToString().Trim());
                cmd.Parameters.AddWithValue("@VehichleType", ParkOutRequest.VehicleType.ToString().Trim());
                cmd.Parameters.AddWithValue("@EstimatedParkTime", time);
                cmd.Parameters.AddWithValue("@MobileNumber", ParkOutRequest.PhoneNumber.ToString().Trim());
                cmd.Parameters.AddWithValue("@RequestDatetime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Parameters.AddWithValue("@RequestStatus", "Pending");
                cmd.Parameters.AddWithValue("@CreateDate", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }


                try
                {
                    cmd.ExecuteNonQuery(); message = "Your park out request received";
                    dbConnection.Close(); cmd.Dispose();
                }
                catch (Exception ex)
                {
                    message = "Registation failed, please try again" + ex.Message.ToString();
                    dbConnection.Close(); cmd.Dispose();
                }
            }
            catch (Exception ex)
            {
                message = "Error" + ex.Message.ToString();
                dbConnection.Close(); cmd.Dispose();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); cmd.Dispose();
                }
                catch { }
            }

            return message;
        }

        public string getParkOutRequest_JSON(ParkinRequest ParkOutRequst)
        {
            return getParkOutRequest(ParkOutRequst);
        }

        public string getParkOutRequest_XML(ParkinRequest ParkOutRequst)
        {
            return getParkOutRequest(ParkOutRequst);
        }
        #endregion

        #region GetParkingAvailablity
        private ParkAvailablity RetrunParkingAvailblity(string ParkingId)
        {
            
            ParkAvailablity objParkAvail = new ParkAvailablity();
          try
          {
            DataTable dt=  AvailabilityParking(ParkingId);
            
            if (dt.Rows.Count > 0)
            {
                if (Convert.ToInt32(dt.Rows[0]["Availability"].ToString().Trim()) > 0)
                {
                    objParkAvail.Availability = dt.Rows[0]["Availability"].ToString().Trim() + "/" + dt.Rows[0]["Capacity"].ToString().Trim();
                    objParkAvail.percentage = string.Format("{0:0}", ((Convert.ToDouble(dt.Rows[0]["Availability"].ToString().Trim()) / Convert.ToDouble(dt.Rows[0]["Capacity"].ToString().Trim())) * 100));
                }
                else
                {
                    objParkAvail.Availability = "0"; //dtGetAvailability.Rows[0]["Availability"].ToString().Trim() + "/" + dtGetAvailability.Rows[0]["Capacity"].ToString().Trim();
                    objParkAvail.percentage = "0";//string.Format("{0:0}", ((Convert.ToDouble(dtGetAvailability.Rows[0]["Availability"].ToString().Trim()) / Convert.ToDouble(dtGetAvailability.Rows[0]["Capacity"].ToString().Trim())) * 100));

                }

            }
            else
            {
                objParkAvail.Availability = "Not Known"; //dtGetAvailability.Rows[0]["Availability"].ToString().Trim() + "/" + dtGetAvailability.Rows[0]["Capacity"].ToString().Trim();
                objParkAvail.percentage = "0";
            }
          }
          catch
          {
          
          }
          return objParkAvail;
        
        }
        public ParkAvailablity getParkingAvailblity_JSON(string ParkingId)
        {
         return RetrunParkingAvailblity(ParkingId)   ;
        }

        public ParkAvailablity getParkingAvailblity_XML(string ParkingId)
        {
            return RetrunParkingAvailblity(ParkingId);
        }
        #endregion

        #region GetRating

        private string getParkingRating(Rating ParkRating)
        {
            string message = "";
            dbConnection = DBConnect.getConnection();

            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand(" INSERT INTO [dbo].[tbl_Rating]([RegId],[ParkingId],[Rating],[Remarks],[CreatedDate]) VALUES(@RegId,@ParkingId,@Rating,@Remarks, @CreatedDate) ", dbConnection);
            cmd.CommandType = CommandType.Text;
            cmd.Parameters.Clear();
            try
            {
                cmd.Parameters.AddWithValue("@RegId", ParkRating.RegId.ToString().Trim());
                cmd.Parameters.AddWithValue("@ParkingId", ParkRating.ParkingId.ToString().Trim());
                cmd.Parameters.AddWithValue("@Rating", ParkRating.Stars.ToString().Trim());
                cmd.Parameters.AddWithValue("@Remarks", ParkRating.Remarks.ToString().Trim());
                cmd.Parameters.AddWithValue("@CreatedDate", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Connection = dbConnection;
                if (dbConnection.State.ToString() == "Closed")
                {
                    dbConnection.Open();
                }


                try
                {
                    cmd.ExecuteNonQuery(); message = "Your rating for parking has been revceived, thank you.";
                    dbConnection.Close(); cmd.Dispose();
                }
                catch (Exception ex)
                {
                    message = " failed, please try again" + ex.Message.ToString();
                    dbConnection.Close(); cmd.Dispose();
                }
            }
            catch (Exception ex)
            {
                message = "Error" + ex.Message.ToString();
                dbConnection.Close(); cmd.Dispose();
            }
            finally
            {
                try
                {
                    dbConnection.Close(); cmd.Dispose();
                }
                catch { }
            }

            return message;
        }
        public string getRating_JSON(Rating getRating)
        {
            return getParkingRating(getRating);
        }

        public string getRating_XML(Rating getRating)
        {
            return getParkingRating(getRating);
        }
        #endregion

        #region GetDaily Collection Report
        private IEnumerable<CollectionReport> GetAllDistrict(ReportDate fDate)
        {
            List<CollectionReport> Collection_List = null;
            CollectionReport objCollection = null;
            dbConnection = DBConnect.getConnection();

            if (dbConnection.State.ToString() == "Closed")
            {
                dbConnection.Open();
            }
            //  double time=Convert.ToDouble(Convert.ToDateTime(Time).ToString("HH:mm"));
            DataTable dt = new DataTable();
            SqlCommand cmd = new SqlCommand("sp_GenrateCollectionRepor", dbConnection);
            cmd.CommandType = CommandType.StoredProcedure;
            cmd.Parameters.Clear();
            cmd.Parameters.AddWithValue("@ParkId", fDate.ParkingId);
            cmd.Parameters.AddWithValue("@fromDate", fDate.FromDate);
            cmd.Parameters.AddWithValue("@toDate", fDate.LastDate);
            SqlDataAdapter adp = new SqlDataAdapter(cmd);
            try
            {
                adp.Fill(dt);
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                Collection_List = new List<CollectionReport>();
                if (dt.Rows.Count > 0)
                {

                    for (int i = 0; i < dt.Rows.Count; i++)
                    {
                        objCollection = new CollectionReport();
                        objCollection.TotalVehicles = dt.Rows[i]["Total Vehicle"].ToString().Trim();
                        objCollection.TotalCollection = dt.Rows[i]["Total Collection"].ToString().Trim();
                        Collection_List.Add(objCollection);
                    }
                }
                else
                {
                    Collection_List.Clear();
                }
            }
            catch
            {
                adp.Dispose(); cmd.Dispose(); dbConnection.Close();
            }
            finally
            {
                try
                {
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                }
                catch { }
            }
            return Collection_List;
        }


        public IEnumerable<CollectionReport> getDailyReport_JSON(ReportDate fDate)
        {
            return GetAllDistrict(fDate);
        }

        public IEnumerable<CollectionReport> getDailyReport_XML(ReportDate fDate)
        {
            return GetAllDistrict(fDate);
        }

        #endregion

        #region GenerateOTP

        private bool ValidateOTP(string Mobile, string OTP)
        {

            bool valid = false;
            if (Mobile != "0")
            {
                dbConnection = DBConnect.getConnection();
                SqlCommand cmd = new SqlCommand("Select transID from tbl_OTP where MobileNo=@mob and OTP=@otp and convert(date,[Datetime],101)=@date and IsActive='true'");
                try
                {
                    cmd.Parameters.Clear();
                    cmd.Parameters.AddWithValue("@mob", Mobile);
                    cmd.Parameters.AddWithValue("@otp", OTP);
                    cmd.Parameters.AddWithValue("@date", DateTime.Now.ToString("yyyy-MM-dd"));
                    cmd.Connection = dbConnection;
                    cmd.CommandType = CommandType.Text;
                    if (dbConnection.State == ConnectionState.Closed)
                    {
                        dbConnection.Open();
                    }
                    DataTable dt = new DataTable();
                    using (SqlDataReader dr = cmd.ExecuteReader())
                    {

                        dt.Load(dr);

                    }
                    if (dt.Rows.Count > 0)
                    {
                        valid = true;
                        cmd = new SqlCommand("update tbl_OTP set IsActive='false' where transID=@id");
                        cmd.Parameters.Clear();
                        cmd.Parameters.AddWithValue("@id", dt.Rows[0]["transID"].ToString());
                        cmd.Connection = dbConnection;
                        cmd.CommandType = CommandType.Text;
                        if (dbConnection.State == ConnectionState.Closed)
                        {
                            dbConnection.Open();
                        }
                        cmd.ExecuteNonQuery();
                        cmd.Dispose(); dbConnection.Close();

                    }
                }
                catch { }
                finally
                { try { cmd.Dispose(); dbConnection.Close(); } catch { } }

            }
            return valid;
        }

        private string GeneratOTP()
        {

            var chars = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
            var random = new Random();
            var result = new string(
                Enumerable.Repeat(chars, 6)
                          .Select(s => s[random.Next(s.Length)])
                          .ToArray());
            return result.ToString();


        }
        string OTP = "0";
        private void OTPbyEmail(string Email)
        {
            if (OTP == "0")
            {
                OTP = GeneratOTP();

            }
            if (SaveOTPInfo(Email, OTP) == true)
            {
                SendMail(Email, OTP);
            }
        }

        private void SendMail(string emailid, string otp)
        {
            try
            {
                // String Email = dt.Rows[0]["UName"].ToString();
                if (emailid != "")
                {
                    System.Net.Mail.MailMessage mail = new System.Net.Mail.MailMessage();
                    mail.From = new MailAddress("hpuid@hp.gov.in");
                    mail.To.Add(emailid);
                    String Body = "Your OTP for HPSSSB application is: " + otp.ToString() + ". Valid for 2 hours only.";
                    mail.Subject = "OTP for HPSSSB application.";
                    mail.Body = Body;
                    mail.Priority = MailPriority.High;
                    SmtpClient smtp = new SmtpClient("10.241.8.51", 25);
                    smtp.Credentials = new NetworkCredential("hpuid", "test@123");
                    try
                    {
                        smtp.Send(mail);
                    }
                    catch
                    { }
                }
            }

            catch (Exception er)
            {
                //ScriptManager.RegisterStartupScript(this, GetType(), "SSSB", "alert('" + er.Message.ToString() + "');", true);
            }
        }

        private bool SaveOTPInfo(string Mobile, string OTP)
        {
            bool com = false;
            dbConnection = DBConnect.getConnection();
            SqlCommand cmd = new SqlCommand("Insert into tbl_OTP (MobileNo,OTP,Enc_OTP,IsActive,Datetime) values(@Mobile,@otp,@EOTP,@Active,@Date)");
            try
            {
                cmd.Parameters.Clear();
                cmd.Parameters.AddWithValue("@Mobile", Mobile);
                cmd.Parameters.AddWithValue("@otp", OTP);
                cmd.Parameters.AddWithValue("@EOTP", MD5Hash(OTP));
                cmd.Parameters.AddWithValue("@Active", "true");
                cmd.Parameters.AddWithValue("@date", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                cmd.Connection = dbConnection;
                cmd.CommandType = CommandType.Text;
                if (dbConnection.State == ConnectionState.Closed)
                {
                    dbConnection.Open();
                }
                //DataTable dt = new DataTable();
                cmd.ExecuteNonQuery(); com = true;
                cmd.Dispose(); dbConnection.Close();
            }
            catch
            { }
            return com;



        }

        private string MD5Hash(string OTP)
        {
            MD5 md5 = new MD5CryptoServiceProvider();
            //compute hash from the bytes of text
            md5.ComputeHash(ASCIIEncoding.ASCII.GetBytes(OTP));
            //get hash result after compute it
            byte[] result = md5.Hash;
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < result.Length; i++)
            {
                //change it into 2 hexadecimal digits
                //for each byte
                strBuilder.Append(result[i].ToString("x2"));
            }

            return strBuilder.ToString();
        }

        private string getUserOTP(string Aadhaarno)
        {
            string message = "";
            ValidateAadhaar1.ValidateAadhaarNo objAadhaar = new ValidateAadhaar1.ValidateAadhaarNo();
            if (objAadhaar.validateVerhoeff(Aadhaarno) == true)
            {
                DataTable dt = new DataTable();
                dbConnection = DBConnect.getConnection();
                SqlCommand cmd = new SqlCommand("sp_IndividualDetails", dbConnection);
                cmd.CommandType = CommandType.StoredProcedure;
                cmd.Parameters.Clear();
                cmd.Parameters.AddWithValue("@AadhaarNo", Aadhaarno);

                SqlDataAdapter adp = new SqlDataAdapter(cmd);
                try
                {
                    adp.Fill(dt);
                    adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                    if (dt.Rows.Count > 0)
                    {
                        string OTP = GeneratOTP();
                        if (dt.Rows[0]["MobileNo"].ToString().Trim() != "" && dt.Rows[0]["MobileNo"].ToString().Trim().Length == 10)
                        {
                            if (SaveOTPInfo(dt.Rows[0]["MobileNo"].ToString().Trim(), OTP) == true)
                            {
                                SendMobile("Your one time password for HP Parking Manager App is: " + OTP, dt.Rows[0]["MobileNo"].ToString().Trim());
                                if (dt.Rows[0]["AlternateMobileNo"].ToString().Trim() != "" && dt.Rows[0]["AlternateMobileNo"].ToString().Trim().Length == 10)
                                {
                                    SendMobile("Your one time password for HP Parking Manager App is: " + OTP, dt.Rows[0]["AlternateMobileNo"].ToString().Trim());
                                }
                                message = "Your one time password for HP Parking Manager App has been sent on registered mobile number XXXXXX" + dt.Rows[0]["MobileNo"].ToString().Trim().Substring(6, 4);
                            }
                            else
                            {
                                message = "Valid mobile number is not registered. Please contact your Parking Manager.";
                            }
                        }
                    }
                    else
                    {
                        message = "Your Aadhaar number is not registered. Please contact your Parking Manager.";
                    }

                }

                catch (Exception ex)
                { message = "Server error occured: " + ex.Message.ToString().Trim(); }

                finally { }
            }
            else
            {
                message = "Invalid Aadhaar number entered.";
            }
            return message;

 
        }

        public string getOTP_JSON(string AadhaarNo)
        {
            return getUserOTP(AadhaarNo);
        }

        public string getOTP_XML(string AadhaarNo)
        {
            return getUserOTP(AadhaarNo);
        }

        private string GetMobileNo(string Aadhaarno)
        {
            string mobile="0";
             ValidateAadhaar1.ValidateAadhaarNo objAadhaar = new ValidateAadhaar1.ValidateAadhaarNo();
             if (objAadhaar.validateVerhoeff(Aadhaarno) == true)
             {
                 DataTable dt = new DataTable();
                 dbConnection = DBConnect.getConnection();
                 SqlCommand cmd = new SqlCommand("sp_IndividualDetails", dbConnection);
                 cmd.CommandType = CommandType.StoredProcedure;
                 cmd.Parameters.Clear();
                 cmd.Parameters.AddWithValue("@AadhaarNo", Aadhaarno);

                 SqlDataAdapter adp = new SqlDataAdapter(cmd);
                 try
                 {
                     adp.Fill(dt);
                     adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                     if (dt.Rows.Count > 0)
                     {
                         mobile = dt.Rows[0]["MobileNo"].ToString().Trim();
                     }
                    
                 }
                 catch { }
             }


                return mobile;
 
        }

        private EmployeeDetails ValidateUserOTP(string Aadhaar, string OTP)
        {
            EmployeeDetails objEmployee=new EmployeeDetails();
            if (ValidateOTP(GetMobileNo(Aadhaar.Trim()), OTP.Trim()) == true)
            {
                 DataTable dt = new DataTable();
                 dbConnection = DBConnect.getConnection();
                 SqlCommand cmd = new SqlCommand("sp_IndividualDetailsFull", dbConnection);
                 cmd.CommandType = CommandType.StoredProcedure;
                 cmd.Parameters.Clear();
                 cmd.Parameters.AddWithValue("@AadhaarNo", Aadhaar);

                 SqlDataAdapter adp = new SqlDataAdapter(cmd);
                 try
                 {
                     adp.Fill(dt);
                     adp.Dispose(); cmd.Dispose(); dbConnection.Close();
                     if (dt.Rows.Count > 0)
                     {
                         objEmployee.OperatorName = dt.Rows[0]["Name"].ToString();
                         objEmployee.ParkingId = dt.Rows[0]["ParkinLocationID"].ToString();
                         objEmployee.MobileNumber = dt.Rows[0]["MobileNo"].ToString();
                         objEmployee.ParkingLandmark = dt.Rows[0]["Identifier"].ToString();
                         objEmployee.ParkingLocation = dt.Rows[0]["ParkingPlace"].ToString();
                         objEmployee.AlternateMobileNumber = dt.Rows[0]["AlternateMobileNo"].ToString();
                         objEmployee.Email = dt.Rows[0]["EmailID"].ToString();
                         objEmployee.OperatorAadhaarNo = dt.Rows[0]["AadharNo"].ToString();
                     }
                 }
                 catch
                 {
                     objEmployee.OperatorName = "Inactive user, contact your Parking Manager.";
                     objEmployee.ParkingId = "0";
                     objEmployee.MobileNumber = "0";
                     objEmployee.ParkingLandmark = "";
                     objEmployee.ParkingLocation = "";
                     objEmployee.AlternateMobileNumber ="0";
                     objEmployee.Email ="";
                     objEmployee.OperatorAadhaarNo = Aadhaar;
                 }
            }
            
            else
            {
                objEmployee.OperatorName = "OTP did not match. Please try again.";
                objEmployee.ParkingId = "0";
                objEmployee.MobileNumber = "0";
                objEmployee.ParkingLandmark = "";
                objEmployee.ParkingLocation = "";
                objEmployee.AlternateMobileNumber = "0";
                objEmployee.Email = "";
                objEmployee.OperatorAadhaarNo = Aadhaar;
            }
            return objEmployee;
        }

        public EmployeeDetails getValidateOTP_JSON(string AadhaarNo, string OTP)
        {
            return ValidateUserOTP(AadhaarNo, OTP);
        }

        public EmployeeDetails getValidateOTP_XML(string AadhaarNo, string OTP)
        {
            return ValidateUserOTP(AadhaarNo, OTP);
        }
        #endregion
    }
}
