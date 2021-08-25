package cu.phibrain.cardinal.app.helpers;

public class NumberUtiles {

    public static double METERS_PER_PIXELS = 3779.5280352161F;

    public static Double parseStringToDouble(String value, double defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : Double.parseDouble(value);
    }

    public static Long parseStringToLong(String value, Long defaultValue) {
        try {
            if (value == null || value.trim().isEmpty())
                return defaultValue;
            else
                return Long.parseLong(value);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return null;
    }


    public static int roundUp(double n) {

        return Integer.parseInt(Long.toString(Math.round(n + 0.5f)));
    }

    public static double toPixels(double meters){
        return meters * METERS_PER_PIXELS;
    }

}
