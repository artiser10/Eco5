package doctor.eco5.obj.eco;

import doctor.eco5.types.TaxType;

import java.util.HashMap;

public class Tax {
    public static final HashMap<TaxType, Integer> codec = new HashMap<>();

    static { // в процентах
        codec.put(TaxType.Salary, 13); // на выплату зп для компании (комнания платит 100 * x, пользователь получает 100)
        codec.put(TaxType.Spend, 13); // на прибыль (мгновенные продажи и т.п.)

    }
}