package funjava.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class TransaksiZakat
 * Merepresentasikan satu entitas transaksi penerimaan dana ZISWAF
 * (Zakat Mal, Zakat Profesi, Zakat Fitrah, Infaq, Shadaqah).
 * 
 * Menerapkan prinsip Relasi Objek (Agregasi) dengan Class Muzakki
 * dan enkapsulasi ketat.
 */
public class TransaksiZakat {
    private String idZiswaf;         // Kode unik misal: ZSW-20260926-0001
    private Muzakki muzakki;         // Objek muzakki pembayar dana
    private String jenisDana;        // Zakat Mal, Zakat Fitrah (Uang), Zakat Fitrah (Beras), Infaq Gedung, Sedekah
    private String kategori;         // ZAKAT, INFAQ, atau SHADAQAH
    private long nominalRupiah;      // Nominal uang tunai / transfer (Rp)
    private double berasKg;          // Bobot beras (Kg), khusus zakat fitrah beras
    private int jumlahJiwa;          // Jumlah jiwa tanggungan zakat fitrah
    private String keterangan;       // Catatan peruntukan / haul
    private String doaAkad;          // Teks doa akad serah terima
    private String waktuTransaksi;   // Waktu pencatatan (yyyy-MM-dd HH:mm:ss)

    // Formatter tanggal default
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Constructor Default
    public TransaksiZakat() {
        this.idZiswaf = generateDefaultId();
        this.muzakki = new Muzakki();
        this.jenisDana = "Zakat Fitrah (Uang)";
        this.kategori = "ZAKAT";
        this.nominalRupiah = 45000;
        this.berasKg = 0.0;
        this.jumlahJiwa = 1;
        this.keterangan = "Zakat Fitrah 1 Jiwa";
        this.doaAkad = "Ajarakallahu fi ma a'thaita...";
        this.waktuTransaksi = LocalDateTime.now().format(FORMATTER);
    }

    // Parameterized Constructor
    public TransaksiZakat(String idZiswaf, Muzakki muzakki, String jenisDana,
                          long nominalRupiah, double berasKg, int jumlahJiwa,
                          String keterangan, String doaAkad, String waktuTransaksi) {
        setIdZiswaf(idZiswaf);
        setMuzakki(muzakki);
        setJenisDana(jenisDana);
        setNominalRupiah(nominalRupiah);
        setBerasKg(berasKg);
        setJumlahJiwa(jumlahJiwa);
        setKeterangan(keterangan);
        setDoaAkad(doaAkad);
        setWaktuTransaksi(waktuTransaksi != null ? waktuTransaksi : LocalDateTime.now().format(FORMATTER));
    }

    private String generateDefaultId() {
        return "ZSW-" + System.currentTimeMillis() % 1000000;
    }

    // --- LOGIKA KLASIFIKASI KATEGORI (ZAKAT / INFAQ / SHADAQAH) ---
    private void updateKategori() {
        if (this.jenisDana == null) {
            this.kategori = "LAINNYA";
            return;
        }
        String lower = this.jenisDana.toLowerCase();
        if (lower.contains("zakat")) {
            this.kategori = "ZAKAT";
        } else if (lower.contains("infaq")) {
            this.kategori = "INFAQ";
        } else if (lower.contains("sedekah") || lower.contains("shadaqah")) {
            this.kategori = "SHADAQAH";
        } else {
            this.kategori = "ZAKAT";
        }
    }

    // --- GETTER & SETTER DENGAN ENKAPSULASI ---

    public String getIdZiswaf() {
        return idZiswaf;
    }

    public void setIdZiswaf(String idZiswaf) {
        this.idZiswaf = (idZiswaf == null || idZiswaf.trim().isEmpty()) ? generateDefaultId() : idZiswaf.trim();
    }

    public Muzakki getMuzakki() {
        return muzakki;
    }

    public void setMuzakki(Muzakki muzakki) {
        this.muzakki = (muzakki != null) ? muzakki : new Muzakki();
    }

    public String getJenisDana() {
        return jenisDana;
    }

    public void setJenisDana(String jenisDana) {
        this.jenisDana = (jenisDana != null) ? jenisDana : "Zakat Mal";
        updateKategori();
    }

    public String getKategori() {
        return kategori;
    }

    public long getNominalRupiah() {
        return nominalRupiah;
    }

    public void setNominalRupiah(long nominalRupiah) {
        // Mencegah nilai negatif
        this.nominalRupiah = Math.max(0, nominalRupiah);
    }

    public double getBerasKg() {
        return berasKg;
    }

    public void setBerasKg(double berasKg) {
        // Mencegah nilai negatif
        this.berasKg = Math.max(0.0, berasKg);
    }

    public int getJumlahJiwa() {
        return jumlahJiwa;
    }

    public void setJumlahJiwa(int jumlahJiwa) {
        this.jumlahJiwa = Math.max(1, jumlahJiwa);
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = (keterangan == null || keterangan.trim().isEmpty()) ? "-" : keterangan.trim();
    }

    public String getDoaAkad() {
        return doaAkad;
    }

    public void setDoaAkad(String doaAkad) {
        this.doaAkad = (doaAkad == null) ? "" : doaAkad.trim();
    }

    public String getWaktuTransaksi() {
        return waktuTransaksi;
    }

    public void setWaktuTransaksi(String waktuTransaksi) {
        this.waktuTransaksi = (waktuTransaksi == null || waktuTransaksi.trim().isEmpty())
                ? LocalDateTime.now().format(FORMATTER) : waktuTransaksi.trim();
    }

    /**
     * Konversi ke baris catatan penyimpanan buku kas (delimiter pipe |).
     * Format: ID|WAKTU|NIK|NAMA|NOHP|ALAMAT|KATEGORI_MUZAKKI|JENIS_DANA|KATEGORI|NOMINAL|BERAS|JIWA|KETERANGAN
     */
    public String toRecordString() {
        Muzakki m = getMuzakki();
        return String.join("|",
                idZiswaf,
                waktuTransaksi,
                m.getNikNip().replace("|", "/"),
                m.getNamaLengkap().replace("|", "/"),
                m.getNoHp().replace("|", "/"),
                m.getAlamat().replace("|", "/"),
                m.getKategori().replace("|", "/"),
                jenisDana.replace("|", "/"),
                kategori,
                String.valueOf(nominalRupiah),
                String.valueOf(berasKg),
                String.valueOf(jumlahJiwa),
                keterangan.replace("|", "/")
        );
    }

    /**
     * Parse baris dari berkas buku kas kembali menjadi Objek TransaksiZakat.
     */
    public static TransaksiZakat fromRecordString(String line) {
        if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
            return null;
        }
        String[] parts = line.split("\\|", -1);
        if (parts.length < 13) {
            return null;
        }
        try {
            String id = parts[0];
            String waktu = parts[1];
            String nik = parts[2];
            String nama = parts[3];
            String noHp = parts[4];
            String alamat = parts[5];
            String katMuzakki = parts[6];
            String jenisDana = parts[7];
            // parts[8] is kategori (auto updated by jenisDana)
            long nominal = Long.parseLong(parts[9]);
            double beras = Double.parseDouble(parts[10]);
            int jiwa = Integer.parseInt(parts[11]);
            String ket = parts[12];

            Muzakki m = new Muzakki(nik, nama, noHp, alamat, katMuzakki);
            return new TransaksiZakat(id, m, jenisDana, nominal, beras, jiwa, ket,
                    "Ajarakallahu fi ma a'thaita...", waktu);
        } catch (Exception e) {
            return null;
        }
    }
}
