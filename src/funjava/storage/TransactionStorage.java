package funjava.storage;

import funjava.model.Muzakki;
import funjava.model.TransaksiZakat;
import funjava.util.ReceiptUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class TransactionStorage
 * Menangani persistensi data fisik ke berkas non-volatil 'buku_kas_ziswaf.txt'.
 * 
 * Memastikan pemisahan rekapitulasi dana Zakat, Infaq, dan Shadaqah
 * secara transparan, akuntabel, dan bebas bug I/O.
 */
public class TransactionStorage {
    public static final String FILE_NAME = "buku_kas_ziswaf.txt";

    /**
     * Memastikan file penyimpanan tersedia. Jika belum ada,
     * membuat file baru dengan header resmi dan beberapa data awal sivitas kampus UIN.
     */
    public static synchronized void initStorage() {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                pw.println("# ========================================================================");
                pw.println("# BUKU KAS BESAR ZISWAF — LAZIS MASJID TARBIYAH UIN MAULANA MALIK IBRAHIM");
                pw.println("# Rekapitulasi Terpadu: Zakat Mal, Zakat Fitrah, Infaq Gedung & Shadaqah");
                pw.println("# Format Record: ID|WAKTU|NIK|NAMA|NOHP|ALAMAT|KATEGORI_MUZAKKI|JENIS_DANA|KATEGORI|NOMINAL|BERAS|JIWA|KETERANGAN");
                pw.println("# ========================================================================");
                
                // Tambahkan data sample awal sivitas kampus UIN Malang
                pw.println("ZSW-20260901-001|2026-09-01 09:15:00|198005152006041002|Prof. Dr. H. Ahmad Fauzi, M.Kom|081234567891|Fakultas Sains dan Teknologi|Dosen / Tendik|Zakat Profesi (Penghasilan)|ZAKAT|350000|0.0|1|Zakat Profesi Dosen FST");
                pw.println("ZSW-20260905-002|2026-09-05 13:40:00|240605110025|Muhammad Rizky Pratama|085712345678|Mabna Ibnu Sina Kamar 204|Mahasiswa UIN|Infaq Gedung & Sarana|INFAQ|50000|0.0|1|Infaq Pembangunan Sound Masjid");
                pw.println("ZSW-20260910-003|2026-09-10 16:20:00|3573010508820003|Hj. Siti Aminah, S.Ag|081398765432|Jl. Sunan Kalijaga No. 12 Malang|Masyarakat Umum|Zakat Mal (Tabungan & Emas)|ZAKAT|3000000|0.0|1|Zakat Mal Tabungan Emas Haul 1 Th");
                pw.println("ZSW-20260915-004|2026-09-15 08:30:00|230605110088|Aisyah Nur Sabrina|089611223344|Mabna Khadijah Kamar 112|Mahasiswa UIN|Sedekah Subuh & Sosial|SHADAQAH|25000|0.0|1|Sedekah Subuh Peduli Dhuafa");
                pw.println("ZSW-20260920-005|2026-09-20 10:05:00|3573021903750001|Drs. Bambang Sudarsono, M.Si|081223344556|Gajayana Residence No. B-4|Dosen / Tendik|Zakat Fitrah (Uang)|ZAKAT|180000|0.0|4|Zakat Fitrah 4 Jiwa Keluarga");
                pw.println("ZSW-20260922-006|2026-09-22 14:15:00|3573032512900005|Keluarga Bpk. Santoso|082155667788|Dinoyo Gg. 6 Malang|Masyarakat Umum|Zakat Fitrah (Beras)|ZAKAT|0|7.5|3|Zakat Fitrah Beras 3 Jiwa");
            } catch (IOException e) {
                System.err.println("[Storage Error] Gagal inisialisasi file buku kas: " + e.getMessage());
            }
        }
    }

    /**
     * Menyimpan satu transaksi baru ke dalam buku kas (Mode Append non-volatil).
     */
    public static synchronized boolean appendTransaction(TransaksiZakat trx) {
        if (trx == null) return false;
        initStorage(); // pastikan file ada

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME, true), StandardCharsets.UTF_8))) {
            pw.println(trx.toRecordString());
            return true;
        } catch (IOException e) {
            System.err.println("[Storage Error] Gagal menulis transaksi: " + e.getMessage());
            return false;
        }
    }

    /**
     * Membaca seluruh data transaksi dari file 'buku_kas_ziswaf.txt' ke memori RAM (List).
     */
    public static synchronized List<TransaksiZakat> loadAllTransactions() {
        initStorage();
        List<TransaksiZakat> list = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return list;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                TransaksiZakat trx = TransaksiZakat.fromRecordString(line);
                if (trx != null) {
                    list.add(trx);
                }
            }
        } catch (IOException e) {
            System.err.println("[Storage Error] Gagal membaca buku kas: " + e.getMessage());
        }
        return list;
    }

    /**
     * Objek penampung kalkulasi ringkasan dana terpisah
     */
    public static class RekapData {
        public long totalZakatNominal = 0;
        public double totalZakatBerasKg = 0.0;
        public long totalInfaqNominal = 0;
        public long totalShadaqahNominal = 0;
        public int countZakat = 0;
        public int countInfaq = 0;
        public int countShadaqah = 0;
        public int countTotal = 0;

        public long getGrandTotalNominal() {
            return totalZakatNominal + totalInfaqNominal + totalShadaqahNominal;
        }
    }

    /**
     * Mengalkulasi rekapitulasi dana Zakat, Infaq, dan Shadaqah secara terpisah.
     */
    public static RekapData calculateRekapitulasi(List<TransaksiZakat> list) {
        RekapData rekap = new RekapData();
        if (list == null) return rekap;

        for (TransaksiZakat t : list) {
            rekap.countTotal++;
            String kat = t.getKategori();
            if ("ZAKAT".equalsIgnoreCase(kat)) {
                rekap.countZakat++;
                rekap.totalZakatNominal += t.getNominalRupiah();
                rekap.totalZakatBerasKg += t.getBerasKg();
            } else if ("INFAQ".equalsIgnoreCase(kat)) {
                rekap.countInfaq++;
                rekap.totalInfaqNominal += t.getNominalRupiah();
            } else if ("SHADAQAH".equalsIgnoreCase(kat)) {
                rekap.countShadaqah++;
                rekap.totalShadaqahNominal += t.getNominalRupiah();
            }
        }
        return rekap;
    }

    /**
     * Menghasilkan teks laporan rekapitulasi kas resmi yang memisahkan Zakat, Infaq, Shadaqah.
     */
    public static String generateLaporanRekapitulasiText(List<TransaksiZakat> list) {
        RekapData r = calculateRekapitulasi(list);
        StringBuilder sb = new StringBuilder();
        String eq = "==============================================================================\n";
        String dash = "------------------------------------------------------------------------------\n";

        sb.append(eq);
        sb.append("         LAPORAN REKAPITULASI BUKU KAS DANA ZISWAF TERPADU                    \n");
        sb.append("                 MASJID TARBIYAH UIN MALIKI MALANG                            \n");
        sb.append("       Transparansi Pengelolaan Zakat, Infaq, Shadaqah Sivitas Kampus         \n");
        sb.append(eq);
        sb.append(String.format("Waktu Audit Laporan : %s WIB\n", java.time.LocalDateTime.now().toString().replace("T", " ")));
        sb.append(String.format("Total Donatur / Trx : %d Transaksi Tercatat\n", r.countTotal));
        sb.append(dash);
        sb.append("1. REKAPITULASI DANA ZAKAT (Mal, Profesi & Fitrah):\n");
        sb.append(String.format("   - Jumlah Transaksi Zakat : %d transaksi\n", r.countZakat));
        sb.append(String.format("   - Total Zakat Uang Tunai : %s\n", ReceiptUtil.formatRupiah(r.totalZakatNominal)));
        sb.append(String.format("   - Total Zakat Beras      : %s\n", ReceiptUtil.formatBeras(r.totalZakatBerasKg)));
        sb.append(dash);
        sb.append("2. REKAPITULASI DANA INFAQ (Pembangunan & Fasilitas Kampus):\n");
        sb.append(String.format("   - Jumlah Transaksi Infaq : %d transaksi\n", r.countInfaq));
        sb.append(String.format("   - Total Dana Infaq       : %s\n", ReceiptUtil.formatRupiah(r.totalInfaqNominal)));
        sb.append(dash);
        sb.append("3. REKAPITULASI DANA SHADAQAH (Sosial, Dhuafa & Bea Mahasiswa):\n");
        sb.append(String.format("   - Jumlah Transaksi Sedekah : %d transaksi\n", r.countShadaqah));
        sb.append(String.format("   - Total Dana Sedekah       : %s\n", ReceiptUtil.formatRupiah(r.totalShadaqahNominal)));
        sb.append(eq);
        sb.append(String.format("GRAND TOTAL SALDO KAS TERKUMPUL : %s\n", ReceiptUtil.formatRupiah(r.getGrandTotalNominal())));
        sb.append(String.format("TOTAL BERAS ZAKAT FITRAH        : %s\n", ReceiptUtil.formatBeras(r.totalZakatBerasKg)));
        sb.append(eq);
        sb.append("Penyaluran dana ZISWAF diawasi oleh Dewan Pengawas Syariah UIN Maliki Malang.\n");
        sb.append("Laporan ini sah, akuntabel, dan dapat dipertanggungjawabkan di hadapan publik.\n");
        sb.append(eq);

        return sb.toString();
    }
}
