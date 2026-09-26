package funjava.model;

/**
 * Class Muzakki
 * Merepresentasikan data donatur / pembayar zakat, infaq, dan shadaqah
 * di lingkungan kampus UIN Maulana Malik Ibrahim Malang.
 * 
 * Menerapkan prinsip Enkapsulasi OOP penuh (private fields,
 * parameterized constructor, getter-setter tervalidasi).
 */
public class Muzakki {
    private String nikNip;        // NIK (KTP), NIP (Dosen/Tendik), atau NIM (Mahasiswa)
    private String namaLengkap;   // Nama lengkap muzakki
    private String noHp;          // Nomor WhatsApp / HP aktif
    private String alamat;        // Alamat domisili atau fakultas/unit kerja
    private String kategori;      // Dosen/Tendik, Mahasiswa UIN, Masyarakat Umum/Jamaah

    // Constructor Default
    public Muzakki() {
        this.nikNip = "-";
        this.namaLengkap = "Hamba Allah";
        this.noHp = "-";
        this.alamat = "-";
        this.kategori = "Masyarakat Umum";
    }

    // Parameterized Constructor
    public Muzakki(String nikNip, String namaLengkap, String noHp, String alamat, String kategori) {
        setNikNip(nikNip);
        setNamaLengkap(namaLengkap);
        setNoHp(noHp);
        setAlamat(alamat);
        setKategori(kategori);
    }

    // --- GETTER & SETTER DENGAN VALIDASI ENKAPSULASI ---

    public String getNikNip() {
        return nikNip;
    }

    public void setNikNip(String nikNip) {
        if (nikNip == null || nikNip.trim().isEmpty()) {
            this.nikNip = "TIDAK-ADA-NIK";
        } else {
            this.nikNip = nikNip.trim();
        }
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        if (namaLengkap == null || namaLengkap.trim().isEmpty()) {
            this.namaLengkap = "Hamba Allah";
        } else {
            this.namaLengkap = namaLengkap.trim();
        }
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        if (noHp == null || noHp.trim().isEmpty()) {
            this.noHp = "-";
        } else {
            this.noHp = noHp.trim();
        }
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        if (alamat == null || alamat.trim().isEmpty()) {
            this.alamat = "Malang";
        } else {
            this.alamat = alamat.trim();
        }
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        if (kategori == null || kategori.trim().isEmpty()) {
            this.kategori = "Masyarakat Umum";
        } else {
            this.kategori = kategori.trim();
        }
    }

    @Override
    public String toString() {
        return namaLengkap + " (" + nikNip + ") - " + kategori;
    }
}
