using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using System.Data.SqlClient;
namespace ValidateAadhaar1
{
    public class ValidateAadhaarNo
    {


        // The multiplication table
        static int[,] d = new int[,]
        {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 
            {1, 2, 3, 4, 0, 6, 7, 8, 9, 5}, 
            {2, 3, 4, 0, 1, 7, 8, 9, 5, 6}, 
            {3, 4, 0, 1, 2, 8, 9, 5, 6, 7}, 
            {4, 0, 1, 2, 3, 9, 5, 6, 7, 8}, 
            {5, 9, 8, 7, 6, 0, 4, 3, 2, 1}, 
            {6, 5, 9, 8, 7, 1, 0, 4, 3, 2}, 
            {7, 6, 5, 9, 8, 2, 1, 0, 4, 3}, 
            {8, 7, 6, 5, 9, 3, 2, 1, 0, 4}, 
            {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
        };

        // The permutation table
        static int[,] p = new int[,]
        {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 
            {1, 5, 7, 6, 2, 8, 3, 0, 9, 4}, 
            {5, 8, 0, 3, 7, 9, 6, 1, 4, 2}, 
            {8, 9, 1, 6, 0, 4, 3, 5, 2, 7}, 
            {9, 4, 5, 3, 1, 2, 6, 8, 7, 0}, 
            {4, 2, 8, 6, 5, 7, 3, 9, 0, 1}, 
            {2, 7, 9, 3, 8, 0, 6, 4, 1, 5}, 
            {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
        };

        // The inverse table
        static int[] inv = { 0, 4, 3, 2, 1, 5, 6, 7, 8, 9 };


        /// <summary>
        /// Validates that an entered number is Verhoeff compliant.
        /// NB: Make sure the check digit is the last one!
        /// </summary>
        /// <param name="num"></param>
        /// <returns>True if Verhoeff compliant, otherwise false</returns>
        public bool validateVerhoeff(string num)
        {
            int c = 0;
            try
            {
                int[] myArray = StringToReversedIntArray(num);

                for (int i = 0; i < myArray.Length; i++)
                {
                    c = d[c, p[(i % 8), myArray[i]]];
                }
            }
            catch { }
            return c == 0;

        }

        /// <summary>
        /// For a given number generates a Verhoeff digit
        /// Append this check digit to num
        /// </summary>
        /// <param name="num"></param>
        /// <returns>Verhoeff check digit as string</returns>
        public string generateVerhoeff(string num)
        {
            int c = 0;
            int[] myArray = StringToReversedIntArray(num);

            for (int i = 0; i < myArray.Length; i++)
            {
                c = d[c, p[((i + 1) % 8), myArray[i]]];
            }

            return inv[c].ToString();
        }


        /// <summary>
        /// Converts a string to a reversed integer array.
        /// </summary>
        /// <param name="num"></param>
        /// <returns>Reversed integer array</returns>
        private int[] StringToReversedIntArray(string num)
        {
            int[] myArray = new int[num.Length];

            for (int i = 0; i < num.Length; i++)
            {
                myArray[i] = int.Parse(num.Substring(i, 1));
            }

            Array.Reverse(myArray);

            return myArray;

        }

        public bool ValidateInfo(String Name, String SpouseName, String Aadhaar)
        {
            Boolean valid = false;
            String CmpName = ""; String CmpSpouseName = "";
            if (Name.Length > 0 && SpouseName.Length > 0)
            {
                if (Name.Contains(' '))
                {
                    CmpName = Name.Substring(0, Name.IndexOf(' '));
                }
                else
                {
                    CmpName = Name;
                }
                if (SpouseName.Contains(' '))
                {
                    CmpSpouseName = SpouseName.Substring(0, SpouseName.IndexOf(' '));
                }
                else
                {
                    CmpSpouseName = SpouseName;
                }
                SqlDataAdapter adp = new SqlDataAdapter("Select top 1 uid,name,addr_careof From EID_UID_MAPPING where uid='" + Aadhaar + "' and name like '" + CmpName + "%' --and rtrim((ltrim(substring(addr_careof,5,LEN(addr_careof))))) like '" + CmpSpouseName + "%'",
                    "Server=10.241.8.36,1433;database=AADHAR_KYR;uid=aadhar;PWD=User@123; timeout=100000");
                DataTable dt = new DataTable();
                try
                {
                    adp.Fill(dt);
                    if (dt.Rows.Count > 0)
                    {
                        valid = true;
                    }


                }
                catch (Exception ex)
                {
                    throw ex;
                }
                finally
                { adp.Dispose(); dt.Dispose(); }
            }
            return valid;

        }
        
        public DataTable GetDetails(String Uid)
        {
            DataTable dt = new DataTable();
            SqlDataAdapter adp = new SqlDataAdapter("Select name [Name],substring(addr_careof,4,len(addr_careof)) [Spouse] from EID_UID_MAPPING where uid='" + Uid + "'", "Server=10.241.8.36,1433;database=AADHAR_KYR;uid=aadhar;PWD=User@123; timeout=100000");
            try
            {
                adp.Fill(dt);
            }
            catch { }
            return dt;
        }

        public DataTable GetDetailsForEID(String EID, String Name)
        {
            String CmpName = "";
            if (Name.Contains(' '))
            {
                CmpName = Name.Substring(0, Name.IndexOf(' '));
            }
            else
            {
                CmpName = Name;
            }
            DataTable dt = new DataTable();
            //SqlDataAdapter adp = new SqlDataAdapter("Select uid from EID_UID_MAPPING where Substring(CAST(eid as varchar(30)),1,14)=dbo.GetNumericValue('" + EID + "') and name like '" + CmpName + "%'", "Server=10.241.8.36,1433;database=AADHAR_KYR;uid=aadhar;PWD=User@123; timeout=100000");
            SqlDataAdapter adp = new SqlDataAdapter("Select uid from EID_UID_MAPPING where mobile='" + EID + "' and name like '" + CmpName + "%'", "Server=10.241.8.36,1433;database=AADHAR_KYR;uid=aadhar;PWD=User@123; timeout=100000");
            try
            {
                adp.Fill(dt);
            }
            catch { }
            return dt;
        }

        public bool CheckDBTStatus(String Aadhaar)
        {
            Boolean valid = false;
            SqlDataAdapter adp = new SqlDataAdapter("Select Beneficiary_Aadhaar_Number from Total_DBT_Done where Beneficiary_Aadhaar_Number='"+Aadhaar+"'",
                   "Server=.;database=DBT;uid=sa;PWD=User@123; timeout=100000");
            DataTable dt = new DataTable();
            try
            {
                adp.Fill(dt);
                if (dt.Rows.Count > 0)
                {
                    valid = true;
                }


            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally
            { adp.Dispose(); dt.Dispose(); }

            return valid;

        }
    }


    
}
