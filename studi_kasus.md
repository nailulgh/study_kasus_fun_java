## 🕌 KATEGORI 7: LAYANAN SOSIAL, FILANTROPI & KEISLAMAN

*(Nilai jual istimewa yang mencerminkan kekhasan kampus Islami UIN Maliki Malang)*

### 21. "ZISWAF Digital Counter" — Kalkulator & Kasir Zakat, Infaq, Shadaqah

- **Latar Belakang:** Lembaga amil zakat masjid kampus membutuhkan aplikasi yang menghitung nisab zakat secara otomatis sesuai syariat dan mencatat muzakki/mustahik secara transparan.
- **Rancangan OOP:**
    - Class `Muzakki`: atribut `nikNip`, `namaLengkap`, `noHp`, `alamat`.
    - Class `TransaksiZakat`: atribut `idZiswaf`, `jenisDana` (Zakat Mal / Zakat Fitrah / Infaq Gedung / Sedekah), `nominalRupiah`, `berasKg`, `doaAkad`.
- **Antarmuka GUI:**
    - Kalkulator Zakat Mal otomatis (input saldo tabungan/emas $\times 2.5\%$).
    - Pilihan Zakat Fitrah (pembayaran uang tunai Rp 45.000 atau beras 2.5 kg per jiwa).
    - Tombol visual "Tampilkan Doa Amil Zakat" saat transaksi disimpan.
- **Data Persistence:** File `buku_kas_ziswaf.txt` yang memisahkan rekapitulasi dana Zakat, Infaq, dan Shadaqah secara transparan.
- **WOW Factor Demo Expo:** Memperagakan penghitungan zakat profesi secara otomatis di hadapan dosen penguji dan mencetak Bukti Setor ZISWAF resmi bergaya lembaga filantropi modern.