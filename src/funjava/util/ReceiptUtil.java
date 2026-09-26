package funjava.util;

import funjava.model.Muzakki;
import funjava.model.TransaksiZakat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility Class ReceiptUtil
 * Menyediakan utilitas pemformatan mata uang Rupiah, bobot beras,
 * teks doa amil dan niat muzakki syariah, serta generator cetak
 * Bukti Setor ZISWAF resmi LAZIS Masjid Tarbiyah UIN Malang.
 */
public class ReceiptUtil {

    /**
     * Memformat angka menjadi format mata uang Rupiah standar Indonesia.
     * Contoh: 1500000 -> Rp 1.500.000
     */
    public static String formatRupiah(long nominal) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("id-ID"));
        symbols.setCurrencySymbol("Rp ");
        symbols.setMonetaryDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("Rp #,##0", symbols);
        return df.format(nominal);
    }

    /**
     * Memformat bobot beras ke satuan kilogram (Kg).
     * Contoh: 2.5 -> 2,50 Kg
     */
    public static String formatBeras(double kg) {
        if (kg <= 0) return "-";
        return String.format(Locale.GERMAN, "%.2f Kg", kg);
    }

    // --- DOA SYARIAT RESMI ---

    public static final String DOA_AMIL_ARAB = 
            "آجَرَكَ اللهُ فِيمَا أَعْطَيْتَ، وَبَارَكَ لَكَ فِيمَا أَبْقَيْتَ، وَجَعَلَهُ لَكَ طَهُورًا";

    public static final String DOA_AMIL_LATIN = 
            "\"Ajarakallahu fi ma a'thaita, wa baraka laka fi ma abqaita, waj'alhu laka thahura.\"";

    public static final String DOA_AMIL_ARTI = 
            "Semoga Allah memberikan pahala atas apa yang telah engkau berikan, "
            + "memberkahi apa yang engkau sisakan, dan menjadikannya pembersih (harta dan jiwa) bagimu.";

    public static final String DOA_NIAT_FITRAH = 
            "نَوَيْتُ أَنْ أُخْرِجَ زَكَاةَ الْفِطْرِ عَنْ نَفْسِي فَرْضًا لِلَّهِ تَعَالَى\n"
            + "Nawaitu an ukhrija zakaatal fithri 'an nafsii fardhan lillaahi ta'aalaa.\n"
            + "(Aku berniat mengeluarkan zakat fitrah untuk diriku sendiri, fardhu karena Allah Ta'ala.)";

    public static final String DOA_NIAT_MAL = 
            "نَوَيْتُ أَنْ أُخْرِجَ زَكَاةَ مَالِي فَرْضًا لِلَّهِ تَعَالَى\n"
            + "Nawaitu an ukhrija zakaata maalii fardhan lillaahi ta'aalaa.\n"
            + "(Aku berniat mengeluarkan zakat hartaku, fardhu karena Allah Ta'ala.)";

    public static final String DOA_NIAT_INFAQ = 
            "نَوَيْتُ التَّقَرُّبَ إِلَى اللهِ تَعَالَى بِهَذِهِ الصَّدَقَةِ\n"
            + "Nawaitut taqarruba ilallaahi ta'aalaa bi haadzihis shadaqah.\n"
            + "(Aku berniat mendekatkan diri kepada Allah Ta'ala melalui sedekah/infaq ini.)";

    /**
     * Menghasilkan Bukti Setor ZISWAF Resmi Elektronik
     * Bergaya lembaga filantropi modern (BAZNAS / LAZIS UIN Maliki Malang).
     */
    public static String generateReceipt(TransaksiZakat trx) {
        if (trx == null) return "Data transaksi kosong.";

        Muzakki m = trx.getMuzakki();
        StringBuilder sb = new StringBuilder();
        String lineSeparator = "================================================================\n";
        String dashSeparator = "----------------------------------------------------------------\n";

        sb.append(lineSeparator);
        sb.append("      LEMBAGA AMIL ZAKAT, INFAQ & SHADAQAH (LAZIS) KAMPUS       \n");
        sb.append("             MASJID TARBIYAH UIN MALIKI MALANG                  \n");
        sb.append("   Jl. Gajayana No. 50, Lowokwaru, Dinoyo, Kota Malang 65144   \n");
        sb.append("        Telepon / Call Center Amil: (0341) 551354               \n");
        sb.append(lineSeparator);
        sb.append("               BUKTI SETOR ELEKTRONIK ZISWAF                    \n");
        sb.append("          (Tanda Terima Sah Berdasarkan Syariat Islam)          \n");
        sb.append(dashSeparator);
        sb.append(String.format("No. Transaksi    : %s\n", trx.getIdZiswaf()));
        sb.append(String.format("Waktu Penyetoran : %s WIB\n", trx.getWaktuTransaksi()));
        sb.append(String.format("Kategori Kas     : [ %s ]\n", trx.getKategori()));
        sb.append(dashSeparator);
        sb.append("DATA MUZAKKI / MUNFIQ:\n");
        sb.append(String.format("  Nama Lengkap   : %s\n", m.getNamaLengkap()));
        sb.append(String.format("  NIK / NIP / NIM: %s\n", m.getNikNip()));
        sb.append(String.format("  Status Sivitas : %s\n", m.getKategori()));
        sb.append(String.format("  No. WhatsApp   : %s\n", m.getNoHp()));
        sb.append(String.format("  Alamat / Unit  : %s\n", m.getAlamat()));
        sb.append(dashSeparator);
        sb.append("RINCIAN AKAD PENYETORAN:\n");
        sb.append(String.format("  Akad / Program : %s\n", trx.getJenisDana()));
        if (trx.getNominalRupiah() > 0) {
            sb.append(String.format("  Nominal Tunai  : %s\n", formatRupiah(trx.getNominalRupiah())));
        }
        if (trx.getBerasKg() > 0) {
            sb.append(String.format("  Bobot Beras    : %s\n", formatBeras(trx.getBerasKg())));
        }
        if (trx.getJumlahJiwa() > 1) {
            sb.append(String.format("  Tanggungan     : %d Jiwa\n", trx.getJumlahJiwa()));
        }
        sb.append(String.format("  Keterangan     : %s\n", trx.getKeterangan()));
        sb.append(dashSeparator);
        sb.append("DOA SERAH TERIMA AMIL ZAKAT:\n");
        sb.append("  ").append(DOA_AMIL_LATIN).append("\n");
        sb.append("  Artinya: \"").append(DOA_AMIL_ARTI).append("\"\n");
        sb.append(dashSeparator);
        sb.append("STATUS VERIFIKASI SISTEM:\n");
        sb.append(String.format("  Kode Otorisasi : [VERIFIED-UIN-%08X]\n", trx.getIdZiswaf().hashCode() & 0xFFFFFFFFL));
        sb.append("  Tercatat di    : Berkas Buku Kas Digital buku_kas_ziswaf.txt\n");
        sb.append("  Petugas Amil   : Sistem Kasir ZISWAF IT Incubation 2026\n");
        sb.append(lineSeparator);
        sb.append("  \"Jazakumullahu Khairan Katsiran atas kedermawanan Anda.\"       \n");
        sb.append("  Dana disalurkan secara amanah & transparan kepada Mustahik.   \n");
        sb.append(lineSeparator);

        return sb.toString();
    }
}
