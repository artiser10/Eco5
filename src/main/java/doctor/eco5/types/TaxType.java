package doctor.eco5.types;

import doctor.eco5.obj.eco.Tax;

import java.util.HashMap;

public enum TaxType {
    Salary,
    Spend;

    public static final HashMap<TaxType, String> codec = new HashMap<>();

    static {
        codec.put(Salary, "Salary");
        codec.put(Spend, "Spend");
    }

    @Override
    public String toString() {
        return codec.get(this);
    }

    public TaxType fromString() {
        for (TaxType tax : codec.keySet()) {
            if (tax == this) {
                return tax;
            }
        }
        return null;
    }

    public float amount() {
        return 1 + Tax.codec.get(this)/100F;
    }
}