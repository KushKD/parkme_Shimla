package findparking.hp.dit.himachal.com.shimlaparking;

import java.io.Serializable;

/**
 * Created by kuush on 5/30/2016.
 */
public class Rates_POJO implements Serializable {



    public String CarType ;

    public String getFeeAmount() {
        return FeeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        FeeAmount = feeAmount;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getCarType() {
        return CarType;
    }

    public void setCarType(String carType) {
        CarType = carType;
    }

    public String Duration;
      public String FeeAmount;




}
