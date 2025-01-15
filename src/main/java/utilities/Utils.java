package utilities;

public class Utils {

    public static void closeables(AutoCloseable ... closeables) {

        if (closeables != null) {
            try {

                for (AutoCloseable autoCloseable : closeables) {

                    autoCloseable.close();

                }

            }catch (Exception ignored) {

            }

        }



    }


}
