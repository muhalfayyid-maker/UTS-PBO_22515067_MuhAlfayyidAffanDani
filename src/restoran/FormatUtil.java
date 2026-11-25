package restoran;

import java.util.Locale;

/**
 * Class bantu kecil untuk memformat angka ke tampilan rupiah (Rp10.000).
 */
public class FormatUtil {

    private FormatUtil() {
        // class ini hanya berisi method static, jadi tidak perlu dibuat objeknya
    }

    public static String rupiah(double nilai) {
        return String.format(Locale.forLanguageTag("id-ID"), "Rp%,.0f", nilai);
    }
}
