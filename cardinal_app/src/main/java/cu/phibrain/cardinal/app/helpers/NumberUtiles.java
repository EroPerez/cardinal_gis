package cu.phibrain.cardinal.app.helpers;

public class NumberUtiles {

    public static Double parseStringToDouble(String value, double defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : Double.parseDouble(value);
    }

    public static Long parseStringToLong(String value, Long defaultValue) {
        try {
            return value == null || value.trim().isEmpty() ? defaultValue : Long.parseLong("0" + value);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        Long o = null;
        return o;
    }
}
