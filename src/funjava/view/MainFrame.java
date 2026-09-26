package funjava.view;

import funjava.model.Muzakki;
import funjava.model.TransaksiZakat;
import funjava.storage.TransactionStorage;
import funjava.util.ReceiptUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * MainFrame - ZISWAF Digital Counter
 * Antarmuka GUI Utama Aplikasi Kasir & Kalkulator Syariat Zakat, Infaq, Shadaqah.
 * Komunitas Fun Java - IT Incubation 2026, UIN Maulana Malik Ibrahim Malang.
 */
public class MainFrame extends JFrame {

    // Palet Warna Resmi Komunitas Fun Java
    private final Color primaryOrange  = new Color(229, 127, 62);
    private final Color darkCoffee     = new Color(31, 16, 8);
    private final Color creamBg        = new Color(253, 243, 231);
    private final Color cleanWhite     = Color.WHITE;
    private final Color cardBorder     = new Color(208, 196, 184);
    private final Color islamicGreen   = new Color(34, 139, 34);
    private final Color deepGreen      = new Color(27, 94, 32);

    // Komponen Form Muzakki
    private JTextField txtNikNip;
    private JTextField txtNamaLengkap;
    private JTextField txtNoHp;
    private JTextField txtAlamat;
    private JComboBox<String> cbKategoriMuzakki;

    // Komponen Jenis ZISWAF
    private JComboBox<String> cbJenisDana;
    private JPanel pnlDynamicInput;
    private CardLayout cardLayoutDynamic;

    // Komponen Input Zakat Fitrah
    private JRadioButton rbFitrahUang;
    private JRadioButton rbFitrahBeras;
    private JSpinner spJumlahJiwa;
    private JLabel lblFitrahSubtotal;

    // Komponen Input Zakat Mal
    private JTextField txtMalHarta;
    private JTextField txtMalHargaEmas;
    private JLabel lblMalNisab;
    private JLabel lblMalStatus;
    private JLabel lblMalHasil;
    private long malHasilNominal = 0;

    // Komponen Input Zakat Profesi (Demo Expo WOW Factor)
    private JTextField txtProfesiPenghasilan;
    private JTextField txtProfesiPengeluaran;
    private JLabel lblProfesiNisab;
    private JLabel lblProfesiStatus;
    private JLabel lblProfesiHasil;
    private long profesiHasilNominal = 0;

    // Komponen Input Infaq & Shadaqah
    private JTextField txtInfaqNominal;
    private JComboBox<String> cbInfaqPeruntukan;
    private JTextField txtShadaqahNominal;
    private JComboBox<String> cbShadaqahPeruntukan;

    // Keterangan & Doa
    private JTextField txtKeterangan;
    private JLabel lblRingkasanTotal;

    // Komponen Tab Buku Kas & Riwayat
    private JTable tblRiwayat;
    private DefaultTableModel tableModel;
    private JTextField txtPencarian;
    private JComboBox<String> cbFilterKategori;
    private List<TransaksiZakat> listSemuaTransaksi = new ArrayList<>();

    // Label Metric Cards Rekapitulasi
    private JLabel lblMetricZakat;
    private JLabel lblMetricInfaq;
    private JLabel lblMetricShadaqah;
    private JLabel lblMetricGrandTotal;

    // Jam Digital Header
    private JLabel lblJamDigital;

    // Transaksi terakhir untuk cetak ulang cepat
    private TransaksiZakat transaksiTerakhir = null;

    public MainFrame() {
        setTitle("ZISWAF Digital Counter — LAZIS Masjid Tarbiyah UIN Maliki Malang");
        setSize(1100, 750);
        setMinimumSize(new Dimension(980, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inisialisasi Berkas Persistensi
        TransactionStorage.initStorage();

        initUI();
        startDigitalClock();
        muatDataTabel();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(creamBg);

        // 1. Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Tabbed Pane Utama
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(cleanWhite);
        tabbedPane.setForeground(darkCoffee);

        tabbedPane.addTab("  🕌 Kasir & Penyetoran ZISWAF  ", createTabKasir());
        tabbedPane.addTab("  📊 Buku Kas & Rekapitulasi Transparan  ", createTabBukuKas());
        tabbedPane.addTab("  🧮 Kalkulator Simulasi Syariah (Expo Live)  ", createTabKalkulatorSyariah());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // =========================================================================
    // 1. HEADER PANEL DENGAN BRANDING FUN JAVA & JAM DIGITAL
    // =========================================================================
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(darkCoffee);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        // Brand Info Kiri
        JPanel pnlBrand = new JPanel(new GridLayout(2, 1, 0, 2));
        pnlBrand.setOpaque(false);

        JLabel lblTitle = new JLabel("🕌 ZISWAF DIGITAL COUNTER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(primaryOrange);

        JLabel lblSub = new JLabel("Pusat Pengelolaan Zakat, Infaq & Shadaqah • LAZIS Masjid Tarbiyah UIN Maulana Malik Ibrahim Malang");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(230, 230, 230));

        pnlBrand.add(lblTitle);
        pnlBrand.add(lblSub);

        // Info Kanan (Jam & Badge)
        JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        pnlRight.setOpaque(false);

        JLabel lblBadge = new JLabel("  IT INCUBATION 2026 • FUN JAVA  ");
        lblBadge.setOpaque(true);
        lblBadge.setBackground(deepGreen);
        lblBadge.setForeground(Color.WHITE);
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblBadge.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        lblJamDigital = new JLabel("2026-09-26 00:00:00 WIB");
        lblJamDigital.setFont(new Font("Consolas", Font.BOLD, 13));
        lblJamDigital.setForeground(creamBg);

        pnlRight.add(lblBadge);
        pnlRight.add(lblJamDigital);

        header.add(pnlBrand, BorderLayout.WEST);
        header.add(pnlRight, BorderLayout.EAST);

        return header;
    }

    private void startDigitalClock() {
        Timer timer = new Timer(1000, e -> {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            lblJamDigital.setText(LocalDateTime.now().format(dtf) + " WIB");
        });
        timer.start();
    }

    // =========================================================================
    // 2. TAB 1: KASIR & PENYETORAN ZISWAF
    // =========================================================================
    private JPanel createTabKasir() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(creamBg);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel Kiri: Identitas Muzakki
        JPanel pnlMuzakki = new JPanel(new GridBagLayout());
        pnlMuzakki.setBackground(cleanWhite);
        pnlMuzakki.setPreferredSize(new Dimension(360, 0));
        pnlMuzakki.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1),
                new EmptyBorder(12, 14, 12, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Header Card Muzakki
        JLabel lblHeaderMuzakki = new JLabel("👤 Identitas Muzakki / Donatur");
        lblHeaderMuzakki.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblHeaderMuzakki.setForeground(darkCoffee);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        pnlMuzakki.add(lblHeaderMuzakki, gbc);

        JSeparator sepM = new JSeparator();
        gbc.gridy = 1;
        pnlMuzakki.add(sepM, gbc);

        gbc.gridwidth = 1;

        // NIK / NIP / NIM
        gbc.gridy = 2; gbc.gridx = 0;
        pnlMuzakki.add(new JLabel("NIK / NIP / NIM:"), gbc);
        txtNikNip = new JTextField();
        txtNikNip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        pnlMuzakki.add(txtNikNip, gbc);

        // Nama Lengkap
        gbc.gridy = 3; gbc.gridx = 0;
        pnlMuzakki.add(new JLabel("Nama Lengkap: *"), gbc);
        txtNamaLengkap = new JTextField();
        txtNamaLengkap.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        pnlMuzakki.add(txtNamaLengkap, gbc);

        // Kategori Sivitas
        gbc.gridy = 4; gbc.gridx = 0;
        pnlMuzakki.add(new JLabel("Status Sivitas:"), gbc);
        cbKategoriMuzakki = new JComboBox<>(new String[]{
                "Dosen / Tendik", "Mahasiswa UIN", "Masyarakat Umum / Jamaah"
        });
        cbKategoriMuzakki.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        pnlMuzakki.add(cbKategoriMuzakki, gbc);

        // No. WhatsApp / HP
        gbc.gridy = 5; gbc.gridx = 0;
        pnlMuzakki.add(new JLabel("No. WhatsApp/HP:"), gbc);
        txtNoHp = new JTextField();
        txtNoHp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        pnlMuzakki.add(txtNoHp, gbc);

        // Alamat / Unit Kerja
        gbc.gridy = 6; gbc.gridx = 0;
        pnlMuzakki.add(new JLabel("Alamat / Gedung:"), gbc);
        txtAlamat = new JTextField();
        txtAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridx = 1;
        pnlMuzakki.add(txtAlamat, gbc);

        // Tombol Auto-Fill Demo untuk Expo (Meringankan saat demo di hadapan juri)
        JButton btnQuickDemo = new JButton("⚡ Isi Data Cepat Sivitas UIN");
        btnQuickDemo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        btnQuickDemo.setBackground(new Color(240, 240, 240));
        btnQuickDemo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnQuickDemo.addActionListener(e -> isiDataMuzakkiDemo());
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 4, 4, 4);
        pnlMuzakki.add(btnQuickDemo, gbc);

        // Spacer pendorong ke atas
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        pnlMuzakki.add(new JLabel(), gbc);

        // Panel Kanan: Rincian Akad & Jenis ZISWAF
        JPanel pnlAkad = new JPanel(new BorderLayout(10, 10));
        pnlAkad.setBackground(cleanWhite);
        pnlAkad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1),
                new EmptyBorder(12, 16, 12, 16)
        ));

        // Pilihan Jenis Dana Dropdown
        JPanel pnlPilihanJenis = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlPilihanJenis.setOpaque(false);
        JLabel lblPilih = new JLabel("Pilih Jenis Akad ZISWAF:");
        lblPilih.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPilih.setForeground(darkCoffee);

        cbJenisDana = new JComboBox<>(new String[]{
                "Zakat Fitrah",
                "Zakat Mal (Tabungan & Emas)",
                "Zakat Profesi (Penghasilan)",
                "Infaq Gedung & Sarana Masjid",
                "Sedekah Subuh & Santunan Mahasiswa"
        });
        cbJenisDana.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cbJenisDana.setBackground(cleanWhite);
        cbJenisDana.addActionListener(e -> onJenisDanaChanged());

        pnlPilihanJenis.add(lblPilih);
        pnlPilihanJenis.add(cbJenisDana);

        pnlAkad.add(pnlPilihanJenis, BorderLayout.NORTH);

        // Dynamic Card Panel untuk masing-masing jenis
        cardLayoutDynamic = new CardLayout();
        pnlDynamicInput = new JPanel(cardLayoutDynamic);
        pnlDynamicInput.setOpaque(false);

        pnlDynamicInput.add(createCardZakatFitrah(), "Zakat Fitrah");
        pnlDynamicInput.add(createCardZakatMal(), "Zakat Mal (Tabungan & Emas)");
        pnlDynamicInput.add(createCardZakatProfesi(), "Zakat Profesi (Penghasilan)");
        pnlDynamicInput.add(createCardInfaqGedung(), "Infaq Gedung & Sarana Masjid");
        pnlDynamicInput.add(createCardShadaqah(), "Sedekah Subuh & Santunan Mahasiswa");

        pnlAkad.add(pnlDynamicInput, BorderLayout.CENTER);

        // Bottom Action Panel: Catatan, Ringkasan, & Tombol Simpan
        JPanel pnlBottomAction = new JPanel(new BorderLayout(8, 8));
        pnlBottomAction.setOpaque(false);
        pnlBottomAction.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel pnlKet = new JPanel(new BorderLayout(5, 5));
        pnlKet.setOpaque(false);
        pnlKet.add(new JLabel("Catatan / Keterangan Khusus:"), BorderLayout.WEST);
        txtKeterangan = new JTextField("ZISWAF Berkah Sivitas UIN Maliki Malang");
        txtKeterangan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnlKet.add(txtKeterangan, BorderLayout.CENTER);

        // Ringkasan Setoran Bar
        JPanel pnlRingkasanBar = new JPanel(new BorderLayout());
        pnlRingkasanBar.setBackground(creamBg);
        pnlRingkasanBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        JLabel lblRingkasanTitle = new JLabel("TOTAL SETORAN TERHITUNG:");
        lblRingkasanTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRingkasanTitle.setForeground(darkCoffee);

        lblRingkasanTotal = new JLabel("Rp 45.000 (1 Jiwa)");
        lblRingkasanTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRingkasanTotal.setForeground(primaryOrange);

        pnlRingkasanBar.add(lblRingkasanTitle, BorderLayout.WEST);
        pnlRingkasanBar.add(lblRingkasanTotal, BorderLayout.EAST);

        // Buttons
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
        pnlButtons.setOpaque(false);

        JButton btnDoa = new JButton("🤲 Tampilkan Doa Amil Zakat");
        btnDoa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDoa.setBackground(deepGreen);
        btnDoa.setForeground(Color.WHITE);
        btnDoa.setFocusPainted(false);
        btnDoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDoa.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        btnDoa.addActionListener(e -> bukaDoaAmilModal());

        JButton btnReset = new JButton("🔄 Reset Form");
        btnReset.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnReset.setBackground(new Color(230, 230, 230));
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> resetFormTransaksi());

        JButton btnSimpan = new JButton("💾 Simpan Transaksi & Cetak Bukti Setor");
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSimpan.setBackground(primaryOrange);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSimpan.addActionListener(e -> simpanTransaksi());

        pnlButtons.add(btnDoa);
        pnlButtons.add(btnReset);
        pnlButtons.add(btnSimpan);

        pnlBottomAction.add(pnlKet, BorderLayout.NORTH);
        pnlBottomAction.add(pnlRingkasanBar, BorderLayout.CENTER);
        pnlBottomAction.add(pnlButtons, BorderLayout.SOUTH);

        pnlAkad.add(pnlBottomAction, BorderLayout.SOUTH);

        // Satukan Kiri & Kanan
        panel.add(pnlMuzakki, BorderLayout.WEST);
        panel.add(pnlAkad, BorderLayout.CENTER);

        return panel;
    }

    // --- CARD SUB-PANELS UNTUK MASING-MASING JENIS ZISWAF ---

    // 1. Card Zakat Fitrah
    private JPanel createCardZakatFitrah() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryOrange, 1),
                "Konfigurasi Zakat Fitrah (Standar Syariat Ramadan)",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), primaryOrange
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        card.add(new JLabel("Pilihan Bentuk Pembayaran:"), g);

        rbFitrahUang = new JRadioButton("Uang Tunai (Rp 45.000 / jiwa)", true);
        rbFitrahBeras = new JRadioButton("Beras (2.5 Kg / jiwa)");
        ButtonGroup bgFitrah = new ButtonGroup();
        bgFitrah.add(rbFitrahUang);
        bgFitrah.add(rbFitrahBeras);

        rbFitrahUang.setOpaque(false);
        rbFitrahBeras.setOpaque(false);
        rbFitrahUang.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rbFitrahBeras.setFont(new Font("Segoe UI", Font.BOLD, 12));

        rbFitrahUang.addActionListener(e -> updateFitrahCalculation());
        rbFitrahBeras.addActionListener(e -> updateFitrahCalculation());

        JPanel pnlRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        pnlRadio.setOpaque(false);
        pnlRadio.add(rbFitrahUang);
        pnlRadio.add(rbFitrahBeras);

        g.gridx = 1;
        card.add(pnlRadio, g);

        g.gridx = 0; g.gridy = 1;
        card.add(new JLabel("Jumlah Jiwa Tanggungan:"), g);

        spJumlahJiwa = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        spJumlahJiwa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        spJumlahJiwa.addChangeListener(e -> updateFitrahCalculation());
        g.gridx = 1;
        card.add(spJumlahJiwa, g);

        g.gridx = 0; g.gridy = 2;
        card.add(new JLabel("Kalkulasi Otomatis:"), g);

        lblFitrahSubtotal = new JLabel("Rp 45.000 (1 Jiwa)");
        lblFitrahSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFitrahSubtotal.setForeground(islamicGreen);
        g.gridx = 1;
        card.add(lblFitrahSubtotal, g);

        // Keterangan Syariat
        JLabel lblNote = new JLabel("<html><i>Ketentuan SK BAZNAS & Dewan Syariah UIN Malang: 2.5 kg beras premium atau Rp 45.000/jiwa.</i></html>");
        lblNote.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblNote.setForeground(Color.GRAY);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        card.add(lblNote, g);

        return card;
    }

    private void updateFitrahCalculation() {
        int jiwa = (int) spJumlahJiwa.getValue();
        if (rbFitrahUang.isSelected()) {
            long total = (long) jiwa * 45000;
            lblFitrahSubtotal.setText(ReceiptUtil.formatRupiah(total) + " (" + jiwa + " Jiwa)");
            lblRingkasanTotal.setText(ReceiptUtil.formatRupiah(total) + " (" + jiwa + " Jiwa)");
        } else {
            double totalBeras = jiwa * 2.5;
            lblFitrahSubtotal.setText(ReceiptUtil.formatBeras(totalBeras) + " (" + jiwa + " Jiwa)");
            lblRingkasanTotal.setText(ReceiptUtil.formatBeras(totalBeras) + " (" + jiwa + " Jiwa)");
        }
    }

    // 2. Card Zakat Mal (Tabungan, Investasi, Emas)
    private JPanel createCardZakatMal() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryOrange, 1),
                "Kalkulator Zakat Mal Otomatis (Saldo Tabungan / Emas × 2.5%)",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), primaryOrange
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        card.add(new JLabel("Total Harta / Saldo (Haul 1 Th):"), g);
        txtMalHarta = new JTextField("125000000");
        txtMalHarta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g.gridx = 1;
        card.add(txtMalHarta, g);

        g.gridx = 0; g.gridy = 1;
        card.add(new JLabel("Harga Emas per Gram Saat Ini:"), g);
        txtMalHargaEmas = new JTextField("1400000");
        txtMalHargaEmas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = 1;
        card.add(txtMalHargaEmas, g);

        g.gridx = 0; g.gridy = 2;
        card.add(new JLabel("Nisab Syariat (85 Gram Emas):"), g);
        lblMalNisab = new JLabel("Rp 119.000.000");
        lblMalNisab.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMalNisab.setForeground(darkCoffee);
        g.gridx = 1;
        card.add(lblMalNisab, g);

        g.gridx = 0; g.gridy = 3;
        card.add(new JLabel("Status Kewajiban:"), g);
        lblMalStatus = new JLabel("✅ Memenuhi Nisab (Wajib Zakat)");
        lblMalStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMalStatus.setForeground(islamicGreen);
        g.gridx = 1;
        card.add(lblMalStatus, g);

        g.gridx = 0; g.gridy = 4;
        card.add(new JLabel("Zakat Mal Terhitung (2.5%):"), g);
        lblMalHasil = new JLabel("Rp 3.125.000");
        lblMalHasil.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMalHasil.setForeground(primaryOrange);
        g.gridx = 1;
        card.add(lblMalHasil, g);

        // Tombol Hitung Ulang
        JButton btnHitungMal = new JButton("🧮 Hitung Zakat Mal Otomatis");
        btnHitungMal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHitungMal.setBackground(primaryOrange);
        btnHitungMal.setForeground(Color.WHITE);
        btnHitungMal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHitungMal.addActionListener(e -> hitungZakatMal());
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        card.add(btnHitungMal, g);

        // Listener Enter
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungZakatMal();
            }
        };
        txtMalHarta.addKeyListener(enterListener);
        txtMalHargaEmas.addKeyListener(enterListener);

        return card;
    }

    private void hitungZakatMal() {
        try {
            String sHarta = txtMalHarta.getText().replaceAll("[^0-9]", "");
            String sEmas = txtMalHargaEmas.getText().replaceAll("[^0-9]", "");

            long harta = sHarta.isEmpty() ? 0 : Long.parseLong(sHarta);
            long hargaEmas = sEmas.isEmpty() ? 1400000 : Long.parseLong(sEmas);

            long nisab = 85 * hargaEmas;
            lblMalNisab.setText(ReceiptUtil.formatRupiah(nisab) + " (85 gr emas)");

            if (harta >= nisab) {
                malHasilNominal = Math.round(harta * 0.025);
                lblMalStatus.setText("✅ Memenuhi Nisab (Wajib Zakat Mal 2.5%)");
                lblMalStatus.setForeground(islamicGreen);
                lblMalHasil.setText(ReceiptUtil.formatRupiah(malHasilNominal));
                lblRingkasanTotal.setText(ReceiptUtil.formatRupiah(malHasilNominal));
            } else {
                malHasilNominal = 0;
                lblMalStatus.setText("ℹ️ Belum Mencapai Nisab (Dianjurkan Infaq/Sedekah)");
                lblMalStatus.setForeground(new Color(180, 100, 20));
                lblMalHasil.setText("Rp 0 (Belum Wajib Zakat Mal)");
                lblRingkasanTotal.setText("Rp 0 (Disarankan Infaq)");
            }
        } catch (Exception ex) {
            lblMalHasil.setText("Format angka tidak valid!");
        }
    }

    // 3. Card Zakat Profesi (Demo Expo WOW Factor)
    private JPanel createCardZakatProfesi() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryOrange, 1),
                "Simulasi Zakat Profesi Dosen/Karyawan UIN (Demo Expo WOW Factor)",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), primaryOrange
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 10, 6, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        card.add(new JLabel("Penghasilan Bruto per Bulan:"), g);
        txtProfesiPenghasilan = new JTextField("12000000");
        txtProfesiPenghasilan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g.gridx = 1;
        card.add(txtProfesiPenghasilan, g);

        g.gridx = 0; g.gridy = 1;
        card.add(new JLabel("Kebutuhan Pokok Bulanan:"), g);
        txtProfesiPengeluaran = new JTextField("4000000");
        txtProfesiPengeluaran.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = 1;
        card.add(txtProfesiPengeluaran, g);

        g.gridx = 0; g.gridy = 2;
        card.add(new JLabel("Nisab Bulanan (MUI/BAZNAS):"), g);
        lblProfesiNisab = new JLabel("Rp 7.000.000 / bulan (524 kg beras)");
        lblProfesiNisab.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProfesiNisab.setForeground(darkCoffee);
        g.gridx = 1;
        card.add(lblProfesiNisab, g);

        g.gridx = 0; g.gridy = 3;
        card.add(new JLabel("Status Kewajiban:"), g);
        lblProfesiStatus = new JLabel("✅ Penghasilan Netto Memenuhi Nisab");
        lblProfesiStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProfesiStatus.setForeground(islamicGreen);
        g.gridx = 1;
        card.add(lblProfesiStatus, g);

        g.gridx = 0; g.gridy = 4;
        card.add(new JLabel("Zakat Profesi (2.5% Netto):"), g);
        lblProfesiHasil = new JLabel("Rp 200.000 / bln");
        lblProfesiHasil.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblProfesiHasil.setForeground(primaryOrange);
        g.gridx = 1;
        card.add(lblProfesiHasil, g);

        JButton btnHitungProfesi = new JButton("🧮 Hitung Zakat Profesi Otomatis");
        btnHitungProfesi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHitungProfesi.setBackground(deepGreen);
        btnHitungProfesi.setForeground(Color.WHITE);
        btnHitungProfesi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHitungProfesi.addActionListener(e -> hitungZakatProfesi());
        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        card.add(btnHitungProfesi, g);

        KeyAdapter pListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungZakatProfesi();
            }
        };
        txtProfesiPenghasilan.addKeyListener(pListener);
        txtProfesiPengeluaran.addKeyListener(pListener);

        return card;
    }

    private void hitungZakatProfesi() {
        try {
            String sIn = txtProfesiPenghasilan.getText().replaceAll("[^0-9]", "");
            String sOut = txtProfesiPengeluaran.getText().replaceAll("[^0-9]", "");

            long bruto = sIn.isEmpty() ? 0 : Long.parseLong(sIn);
            long kebutuhan = sOut.isEmpty() ? 0 : Long.parseLong(sOut);

            long netto = Math.max(0, bruto - kebutuhan);
            long nisabBulanan = 7000000; // Nisab profesi bulanan standar

            if (netto >= nisabBulanan) {
                profesiHasilNominal = Math.round(netto * 0.025);
                lblProfesiStatus.setText("✅ Netto " + ReceiptUtil.formatRupiah(netto) + " >= Nisab (Wajib Zakat 2.5%)");
                lblProfesiStatus.setForeground(islamicGreen);
                lblProfesiHasil.setText(ReceiptUtil.formatRupiah(profesiHasilNominal) + " / bulan");
                lblRingkasanTotal.setText(ReceiptUtil.formatRupiah(profesiHasilNominal));
            } else {
                profesiHasilNominal = 0;
                lblProfesiStatus.setText("ℹ️ Netto " + ReceiptUtil.formatRupiah(netto) + " < Nisab (Dianjurkan Infaq/Sedekah)");
                lblProfesiStatus.setForeground(new Color(180, 100, 20));
                lblProfesiHasil.setText("Rp 0 (Belum Wajib Zakat)");
                lblRingkasanTotal.setText("Rp 0 (Disarankan Infaq)");
            }
        } catch (Exception ex) {
            lblProfesiHasil.setText("Format angka salah!");
        }
    }

    // 4. Card Infaq Gedung
    private JPanel createCardInfaqGedung() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryOrange, 1),
                "Penyaluran Infaq Gedung & Sarana Masjid Kampus",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), primaryOrange
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        card.add(new JLabel("Nominal Infaq (Rp):"), g);
        txtInfaqNominal = new JTextField("100000");
        txtInfaqNominal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g.gridx = 1;
        card.add(txtInfaqNominal, g);

        g.gridx = 0; g.gridy = 1;
        card.add(new JLabel("Peruntukan Program Infaq:"), g);
        cbInfaqPeruntukan = new JComboBox<>(new String[]{
                "Pembangunan Gedung & Menara Masjid",
                "Renovasi Fasilitas Wudhu & Sound System",
                "Pengadaan Mushaf Al-Qur'an & Kitab",
                "Operasional Kajian & Tahfidz Ma'had"
        });
        cbInfaqPeruntukan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = 1;
        card.add(cbInfaqPeruntukan, g);

        txtInfaqNominal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateInfaqSummary();
            }
        });

        return card;
    }

    private void updateInfaqSummary() {
        try {
            String s = txtInfaqNominal.getText().replaceAll("[^0-9]", "");
            long nom = s.isEmpty() ? 0 : Long.parseLong(s);
            lblRingkasanTotal.setText(ReceiptUtil.formatRupiah(nom));
        } catch (Exception ignored) {}
    }

    // 5. Card Shadaqah
    private JPanel createCardShadaqah() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryOrange, 1),
                "Sedekah Subuh & Santunan Kesejahteraan Sosial Sivitas",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), primaryOrange
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 10, 8, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        card.add(new JLabel("Nominal Sedekah (Rp):"), g);
        txtShadaqahNominal = new JTextField("50000");
        txtShadaqahNominal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g.gridx = 1;
        card.add(txtShadaqahNominal, g);

        g.gridx = 0; g.gridy = 1;
        card.add(new JLabel("Kategori Santunan / Sedekah:"), g);
        cbShadaqahPeruntukan = new JComboBox<>(new String[]{
                "Bantuan UKT Mahasiswa Dhuafa UIN",
                "Santunan Yatim Piatu Sekitar Kampus",
                "Jumat Berkah Makan Siang Gratis Jamaah",
                "Dana Tanggap Bencana & Kemanusiaan"
        });
        cbShadaqahPeruntukan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = 1;
        card.add(cbShadaqahPeruntukan, g);

        txtShadaqahNominal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateShadaqahSummary();
            }
        });

        return card;
    }

    private void updateShadaqahSummary() {
        try {
            String s = txtShadaqahNominal.getText().replaceAll("[^0-9]", "");
            long nom = s.isEmpty() ? 0 : Long.parseLong(s);
            lblRingkasanTotal.setText(ReceiptUtil.formatRupiah(nom));
        } catch (Exception ignored) {}
    }

    private void onJenisDanaChanged() {
        String selected = (String) cbJenisDana.getSelectedItem();
        cardLayoutDynamic.show(pnlDynamicInput, selected);

        if ("Zakat Fitrah".equals(selected)) {
            updateFitrahCalculation();
        } else if ("Zakat Mal (Tabungan & Emas)".equals(selected)) {
            hitungZakatMal();
        } else if ("Zakat Profesi (Penghasilan)".equals(selected)) {
            hitungZakatProfesi();
        } else if ("Infaq Gedung & Sarana Masjid".equals(selected)) {
            updateInfaqSummary();
        } else if ("Sedekah Subuh & Santunan Mahasiswa".equals(selected)) {
            updateShadaqahSummary();
        }
    }

    private void isiDataMuzakkiDemo() {
        // Rotasi dummy data realistis sivitas UIN Malang
        String[][] dummies = {
                {"198503122010121003", "Dr. H. Muhammad Ridwan, M.T.", "081234889900", "Fakultas Sains dan Teknologi UIN Malang", "Dosen / Tendik"},
                {"240605110115", "Fathimah Az-Zahra", "085645678901", "Mabna Asma' Binti Abi Bakar Kamar 305", "Mahasiswa UIN"},
                {"3573021104800002", "H. Abdullah Syukri", "081333445566", "Jl. Sumbersari Gg. 5 No. 17 Malang", "Masyarakat Umum / Jamaah"}
        };
        int pick = (int) (System.currentTimeMillis() % dummies.length);
        txtNikNip.setText(dummies[pick][0]);
        txtNamaLengkap.setText(dummies[pick][1]);
        txtNoHp.setText(dummies[pick][2]);
        txtAlamat.setText(dummies[pick][3]);
        cbKategoriMuzakki.setSelectedItem(dummies[pick][4]);
    }

    private void resetFormTransaksi() {
        txtNikNip.setText("");
        txtNamaLengkap.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        txtKeterangan.setText("ZISWAF Berkah Sivitas UIN Maliki Malang");
        cbKategoriMuzakki.setSelectedIndex(0);
        cbJenisDana.setSelectedIndex(0);
        spJumlahJiwa.setValue(1);
        rbFitrahUang.setSelected(true);
        updateFitrahCalculation();
    }

    private void bukaDoaAmilModal() {
        String nama = txtNamaLengkap.getText().trim();
        if (nama.isEmpty()) nama = "Hamba Allah";
        String jenis = (String) cbJenisDana.getSelectedItem();
        DoaDialog dialog = new DoaDialog(this, nama, jenis);
        dialog.setVisible(true);
    }

    // =========================================================================
    // 3. LOGIKA SIMPAN TRANSAKSI KE PERSISTENSI STORAGE
    // =========================================================================
    private void simpanTransaksi() {
        String nama = txtNamaLengkap.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama Lengkap Muzakki / Donatur wajib diisi!",
                    "Validasi Input", JOptionPane.WARNING_MESSAGE);
            txtNamaLengkap.requestFocus();
            return;
        }

        String nik = txtNikNip.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String katMuzakki = (String) cbKategoriMuzakki.getSelectedItem();
        Muzakki muzakki = new Muzakki(nik, nama, noHp, alamat, katMuzakki);

        String jenisDana = (String) cbJenisDana.getSelectedItem();
        long nominal = 0;
        double beras = 0.0;
        int jiwa = 1;
        String keterangan = txtKeterangan.getText().trim();

        if ("Zakat Fitrah".equals(jenisDana)) {
            jiwa = (int) spJumlahJiwa.getValue();
            if (rbFitrahUang.isSelected()) {
                nominal = (long) jiwa * 45000;
                keterangan = "Zakat Fitrah Tunai (" + jiwa + " Jiwa) - " + keterangan;
            } else {
                beras = jiwa * 2.5;
                keterangan = "Zakat Fitrah Beras (" + jiwa + " Jiwa) - " + keterangan;
            }
        } else if ("Zakat Mal (Tabungan & Emas)".equals(jenisDana)) {
            hitungZakatMal();
            if (malHasilNominal <= 0) {
                int res = JOptionPane.showConfirmDialog(this,
                        "Harta belum mencapai nisab 85 gr emas.\nApakah ingin tetap melanjutkan sebagai Infaq/Sedekah Sukarela?",
                        "Konfirmasi Syariat", JOptionPane.YES_NO_OPTION);
                if (res != JOptionPane.YES_OPTION) return;
                nominal = 50000; // default donasi
                jenisDana = "Infaq Pengganti Zakat Mal";
            } else {
                nominal = malHasilNominal;
            }
        } else if ("Zakat Profesi (Penghasilan)".equals(jenisDana)) {
            hitungZakatProfesi();
            if (profesiHasilNominal <= 0) {
                int res = JOptionPane.showConfirmDialog(this,
                        "Penghasilan belum mencapai nisab profesi bulanan.\nApakah ingin menyalurkan sebagai Shadaqah Sukarela?",
                        "Konfirmasi Syariat", JOptionPane.YES_NO_OPTION);
                if (res != JOptionPane.YES_OPTION) return;
                nominal = 50000;
                jenisDana = "Sedekah Profesi";
            } else {
                nominal = profesiHasilNominal;
            }
        } else if ("Infaq Gedung & Sarana Masjid".equals(jenisDana)) {
            try {
                String s = txtInfaqNominal.getText().replaceAll("[^0-9]", "");
                nominal = s.isEmpty() ? 0 : Long.parseLong(s);
            } catch (Exception e) {
                nominal = 0;
            }
            if (nominal <= 0) {
                JOptionPane.showMessageDialog(this, "Nominal Infaq harus lebih dari 0!", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            keterangan = "[" + cbInfaqPeruntukan.getSelectedItem() + "] " + keterangan;
        } else if ("Sedekah Subuh & Santunan Mahasiswa".equals(jenisDana)) {
            try {
                String s = txtShadaqahNominal.getText().replaceAll("[^0-9]", "");
                nominal = s.isEmpty() ? 0 : Long.parseLong(s);
            } catch (Exception e) {
                nominal = 0;
            }
            if (nominal <= 0) {
                JOptionPane.showMessageDialog(this, "Nominal Sedekah harus lebih dari 0!", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            keterangan = "[" + cbShadaqahPeruntukan.getSelectedItem() + "] " + keterangan;
        }

        // Generate ID Transaksi
        String idTrx = "ZSW-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        TransaksiZakat trxBaru = new TransaksiZakat(
                idTrx, muzakki, jenisDana, nominal, beras, jiwa,
                keterangan, ReceiptUtil.DOA_AMIL_LATIN, waktu
        );

        // Simpan Permanen ke Storage buku_kas_ziswaf.txt
        boolean sukses = TransactionStorage.appendTransaction(trxBaru);

        if (sukses) {
            transaksiTerakhir = trxBaru;
            // Muat ulang data tabel dan statistik
            muatDataTabel();

            // Tampilkan pop-up dialog Doa Amil Zakat sesuai panduan studi kasus
            DoaDialog doaDlg = new DoaDialog(this, muzakki.getNamaLengkap(), jenisDana);
            doaDlg.setVisible(true);

            // Tampilkan Bukti Setor ZISWAF Resmi
            tampilkanPreviewKuitansi(trxBaru);

            // Bersihkan form
            resetFormTransaksi();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Gagal menulis ke berkas storage buku_kas_ziswaf.txt!",
                    "Storage Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanPreviewKuitansi(TransaksiZakat trx) {
        String receiptText = ReceiptUtil.generateReceipt(trx);

        JTextArea area = new JTextArea(receiptText);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setEditable(false);
        area.setBackground(new Color(250, 250, 250));

        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(580, 480));

        JPanel pnlDlg = new JPanel(new BorderLayout(5, 5));
        pnlDlg.add(sp, BorderLayout.CENTER);

        JButton btnEksporNota = new JButton("💾 Simpan Struk ke File Teks (.txt)");
        btnEksporNota.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEksporNota.setBackground(deepGreen);
        btnEksporNota.setForeground(Color.WHITE);
        btnEksporNota.addActionListener(e -> {
            try {
                String namaFile = "Bukti_Setor_" + trx.getIdZiswaf() + ".txt";
                try (FileWriter fw = new FileWriter(namaFile)) {
                    fw.write(receiptText);
                }
                JOptionPane.showMessageDialog(this,
                        "Bukti Setor berhasil disimpan sebagai berkas:\n" + new File(namaFile).getAbsolutePath(),
                        "Ekspor Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan berkas struk!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        pnlDlg.add(btnEksporNota, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, pnlDlg, "BUKTI SETOR ELEKTRONIK ZISWAF RESMI", JOptionPane.PLAIN_MESSAGE);
    }

    // =========================================================================
    // 4. TAB 2: BUKU KAS & REKAPITULASI TRANSPARAN (DATA PERSISTENCE)
    // =========================================================================
    private JPanel createTabBukuKas() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(creamBg);
        panel.setBorder(new EmptyBorder(12, 15, 12, 15));

        // Metric Cards Panel (Ringkasan Kas Terpisah)
        JPanel pnlMetrics = new JPanel(new GridLayout(1, 4, 12, 0));
        pnlMetrics.setOpaque(false);
        pnlMetrics.setPreferredSize(new Dimension(0, 95));

        lblMetricZakat = new JLabel("Rp 0");
        lblMetricInfaq = new JLabel("Rp 0");
        lblMetricShadaqah = new JLabel("Rp 0");
        lblMetricGrandTotal = new JLabel("Rp 0");

        pnlMetrics.add(createMetricCard("💰 DANA ZAKAT (Mal & Fitrah)", lblMetricZakat, primaryOrange));
        pnlMetrics.add(createMetricCard("🏢 DANA INFAQ GEDUNG", lblMetricInfaq, deepGreen));
        pnlMetrics.add(createMetricCard("🤝 DANA SHADAQAH SOSIAL", lblMetricShadaqah, new Color(30, 136, 229)));
        pnlMetrics.add(createMetricCard("📈 GRAND TOTAL KAS", lblMetricGrandTotal, darkCoffee));

        panel.add(pnlMetrics, BorderLayout.NORTH);

        // Center Panel: Toolbar + JTable
        JPanel pnlCenter = new JPanel(new BorderLayout(8, 8));
        pnlCenter.setBackground(cleanWhite);
        pnlCenter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        // Toolbar Filter & Search
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlToolbar.setOpaque(false);

        pnlToolbar.add(new JLabel("Filter Kategori:"));
        cbFilterKategori = new JComboBox<>(new String[]{
                "Semua Kategori", "ZAKAT", "INFAQ", "SHADAQAH"
        });
        cbFilterKategori.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cbFilterKategori.addActionListener(e -> filterDataTabel());
        pnlToolbar.add(cbFilterKategori);

        pnlToolbar.add(Box.createHorizontalStrut(15));
        pnlToolbar.add(new JLabel("Cari Nama / ID:"));
        txtPencarian = new JTextField(15);
        txtPencarian.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPencarian.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterDataTabel();
            }
        });
        pnlToolbar.add(txtPencarian);

        JButton btnRefresh = new JButton("🔄 Refresh Data");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRefresh.setBackground(new Color(240, 240, 240));
        btnRefresh.addActionListener(e -> muatDataTabel());
        pnlToolbar.add(btnRefresh);

        JButton btnCetakUlang = new JButton("🖨️ Cetak Ulang Bukti Setor");
        btnCetakUlang.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCetakUlang.setBackground(primaryOrange);
        btnCetakUlang.setForeground(Color.WHITE);
        btnCetakUlang.addActionListener(e -> cetakUlangDariTabel());
        pnlToolbar.add(btnCetakUlang);

        JButton btnEksporAudit = new JButton("📑 Laporan Rekap Audit");
        btnEksporAudit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEksporAudit.setBackground(deepGreen);
        btnEksporAudit.setForeground(Color.WHITE);
        btnEksporAudit.addActionListener(e -> eksporLaporanAudit());
        pnlToolbar.add(btnEksporAudit);

        pnlCenter.add(pnlToolbar, BorderLayout.NORTH);

        // Inisialisasi JTable
        String[] columns = {
                "No", "ID ZISWAF", "Waktu Setor", "Nama Muzakki",
                "Kategori", "Akad / Jenis Dana", "Nominal (Rp)", "Beras (Kg)", "Peruntukan / Catatan"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only table
            }
        };

        tblRiwayat = new JTable(tableModel);
        tblRiwayat.setRowHeight(28);
        tblRiwayat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblRiwayat.setSelectionBackground(creamBg);
        tblRiwayat.setSelectionForeground(darkCoffee);
        tblRiwayat.setGridColor(new Color(230, 230, 230));

        // Header Styling
        JTableHeader th = tblRiwayat.getTableHeader();
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));
        th.setBackground(darkCoffee);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 32));

        // Column Alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblRiwayat.getColumnModel().getColumn(0).setPreferredWidth(35);
        tblRiwayat.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblRiwayat.getColumnModel().getColumn(1).setPreferredWidth(130);
        tblRiwayat.getColumnModel().getColumn(2).setPreferredWidth(125);
        tblRiwayat.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tblRiwayat.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        tblRiwayat.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);

        JScrollPane spTable = new JScrollPane(tblRiwayat);
        pnlCenter.add(spTable, BorderLayout.CENTER);

        panel.add(pnlCenter, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMetricCard(String title, JLabel lblValue, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(cleanWhite);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(cardBorder, 1),
                        new EmptyBorder(8, 12, 8, 12)
                )
        ));

        JLabel lblT = new JLabel(title);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblT.setForeground(new Color(100, 100, 100));

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblValue.setForeground(accentColor);

        card.add(lblT, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private void muatDataTabel() {
        listSemuaTransaksi = TransactionStorage.loadAllTransactions();
        filterDataTabel();
        updateRekapDashboard();
    }

    private void filterDataTabel() {
        tableModel.setRowCount(0);
        String katFilter = (String) cbFilterKategori.getSelectedItem();
        String keyword = txtPencarian.getText().trim().toLowerCase();

        int no = 1;
        for (TransaksiZakat t : listSemuaTransaksi) {
            boolean passKat = "Semua Kategori".equals(katFilter) || t.getKategori().equalsIgnoreCase(katFilter);
            boolean passSearch = keyword.isEmpty() 
                    || t.getMuzakki().getNamaLengkap().toLowerCase().contains(keyword)
                    || t.getIdZiswaf().toLowerCase().contains(keyword)
                    || t.getJenisDana().toLowerCase().contains(keyword);

            if (passKat && passSearch) {
                tableModel.addRow(new Object[]{
                        no++,
                        t.getIdZiswaf(),
                        t.getWaktuTransaksi(),
                        t.getMuzakki().getNamaLengkap(),
                        t.getKategori(),
                        t.getJenisDana(),
                        ReceiptUtil.formatRupiah(t.getNominalRupiah()),
                        ReceiptUtil.formatBeras(t.getBerasKg()),
                        t.getKeterangan()
                });
            }
        }
    }

    private void updateRekapDashboard() {
        TransactionStorage.RekapData r = TransactionStorage.calculateRekapitulasi(listSemuaTransaksi);
        String txtZakat = ReceiptUtil.formatRupiah(r.totalZakatNominal);
        if (r.totalZakatBerasKg > 0) {
            txtZakat += " + " + ReceiptUtil.formatBeras(r.totalZakatBerasKg);
        }
        lblMetricZakat.setText(txtZakat);
        lblMetricInfaq.setText(ReceiptUtil.formatRupiah(r.totalInfaqNominal));
        lblMetricShadaqah.setText(ReceiptUtil.formatRupiah(r.totalShadaqahNominal));
        lblMetricGrandTotal.setText(ReceiptUtil.formatRupiah(r.getGrandTotalNominal()) + " (" + r.countTotal + " Trx)");
    }

    private void cetakUlangDariTabel() {
        int selectedRow = tblRiwayat.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih salah satu baris transaksi pada tabel terlebih dahulu!",
                    "Pilih Transaksi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String idZiswaf = (String) tblRiwayat.getValueAt(selectedRow, 1);
        for (TransaksiZakat t : listSemuaTransaksi) {
            if (t.getIdZiswaf().equals(idZiswaf)) {
                tampilkanPreviewKuitansi(t);
                return;
            }
        }
    }

    private void eksporLaporanAudit() {
        String laporan = TransactionStorage.generateLaporanRekapitulasiText(listSemuaTransaksi);
        JTextArea area = new JTextArea(laporan);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(650, 420));

        int res = JOptionPane.showConfirmDialog(this, sp,
                "Rekapitulasi Buku Kas Transparan (Zakat, Infaq & Shadaqah)",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            try {
                String namaFile = "Laporan_Audit_BukuKas_ZISWAF.txt";
                try (FileWriter fw = new FileWriter(namaFile)) {
                    fw.write(laporan);
                }
                JOptionPane.showMessageDialog(this,
                        "Laporan Audit berhasil diekspor ke berkas:\n" + new File(namaFile).getAbsolutePath(),
                        "Ekspor Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Gagal menulis file audit: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================================================================
    // 5. TAB 3: KALKULATOR SIMULASI SYARIAH (EXPO WOW FACTOR)
    // =========================================================================
    private JPanel createTabKalkulatorSyariah() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(creamBg);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Header Tab 3
        JPanel pnlTitle = new JPanel(new GridLayout(2, 1, 0, 4));
        pnlTitle.setOpaque(false);
        JLabel lblT3 = new JLabel("🧮 Interactive Syariah Calculator — Expo Demo Station");
        lblT3.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblT3.setForeground(darkCoffee);
        JLabel lblS3 = new JLabel("Peragakan kalkulasi Zakat Profesi & Zakat Mal instan di hadapan Dosen Penguji & Pengunjung Expo.");
        lblS3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblS3.setForeground(Color.DARK_GRAY);
        pnlTitle.add(lblT3);
        pnlTitle.add(lblS3);
        panel.add(pnlTitle, BorderLayout.NORTH);

        // Body: 2 Kolom (Kiri: Form Simulasi, Kanan: Panduan Dalil & Fiqih)
        JPanel pnlBody = new JPanel(new GridLayout(1, 2, 20, 0));
        pnlBody.setOpaque(false);

        // Kolom Kiri: Live Simulation Card
        JPanel cardSim = new JPanel(new GridBagLayout());
        cardSim.setBackground(cleanWhite);
        cardSim.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblJudulSim = new JLabel("Simulasi Zakat Profesi Pengunjung");
        lblJudulSim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblJudulSim.setForeground(primaryOrange);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        cardSim.add(lblJudulSim, g);
        g.gridwidth = 1;

        g.gridx = 0; g.gridy = 1;
        cardSim.add(new JLabel("Gaji Pokok / Bulan (Rp):"), g);
        JTextField txtSimGaji = new JTextField("10000000");
        txtSimGaji.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g.gridx = 1;
        cardSim.add(txtSimGaji, g);

        g.gridx = 0; g.gridy = 2;
        cardSim.add(new JLabel("Tunjangan / Bonus (Rp):"), g);
        JTextField txtSimBonus = new JTextField("2500000");
        txtSimBonus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = 1;
        cardSim.add(txtSimBonus, g);

        g.gridx = 0; g.gridy = 3;
        cardSim.add(new JLabel("Kebutuhan Pokok Bulanan (Rp):"), g);
        JTextField txtSimPengeluaran = new JTextField("4500000");
        txtSimPengeluaran.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g.gridx = 1;
        cardSim.add(txtSimPengeluaran, g);

        g.gridx = 0; g.gridy = 4;
        cardSim.add(new JLabel("Nisab Bulanan Acuan:"), g);
        JLabel lblSimNisab = new JLabel("Rp 7.000.000 (Setara 524 Kg Beras)");
        lblSimNisab.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSimNisab.setForeground(darkCoffee);
        g.gridx = 1;
        cardSim.add(lblSimNisab, g);

        g.gridx = 0; g.gridy = 5;
        cardSim.add(new JLabel("Penghasilan Bersih (Netto):"), g);
        JLabel lblSimNetto = new JLabel("Rp 8.000.000");
        lblSimNetto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSimNetto.setForeground(deepGreen);
        g.gridx = 1;
        cardSim.add(lblSimNetto, g);

        g.gridx = 0; g.gridy = 6;
        cardSim.add(new JLabel("Kewajiban Zakat (2.5%):"), g);
        JLabel lblSimZakatHasil = new JLabel("Rp 200.000 / Bulan");
        lblSimZakatHasil.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSimZakatHasil.setForeground(primaryOrange);
        g.gridx = 1;
        cardSim.add(lblSimZakatHasil, g);

        Runnable hitungSim = () -> {
            try {
                long gaji = Long.parseLong(txtSimGaji.getText().replaceAll("[^0-9]", ""));
                long bonus = Long.parseLong(txtSimBonus.getText().replaceAll("[^0-9]", ""));
                long beban = Long.parseLong(txtSimPengeluaran.getText().replaceAll("[^0-9]", ""));
                long netto = Math.max(0, (gaji + bonus) - beban);
                lblSimNetto.setText(ReceiptUtil.formatRupiah(netto));

                if (netto >= 7000000) {
                    long z = Math.round(netto * 0.025);
                    lblSimZakatHasil.setText(ReceiptUtil.formatRupiah(z) + " / bln");
                    lblSimZakatHasil.setForeground(primaryOrange);
                } else {
                    lblSimZakatHasil.setText("Rp 0 (Belum Nisab)");
                    lblSimZakatHasil.setForeground(new Color(150, 150, 150));
                }
            } catch (Exception ignored) {}
        };

        KeyAdapter ka = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungSim.run();
            }
        };
        txtSimGaji.addKeyListener(ka);
        txtSimBonus.addKeyListener(ka);
        txtSimPengeluaran.addKeyListener(ka);

        JButton btnTerapkanKeKasir = new JButton("➡️ Terapkan Nilai ini ke Tab Kasir");
        btnTerapkanKeKasir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnTerapkanKeKasir.setBackground(primaryOrange);
        btnTerapkanKeKasir.setForeground(Color.WHITE);
        btnTerapkanKeKasir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTerapkanKeKasir.addActionListener(e -> {
            cbJenisDana.setSelectedItem("Zakat Profesi (Penghasilan)");
            txtProfesiPenghasilan.setText(String.valueOf(
                    Long.parseLong(txtSimGaji.getText().replaceAll("[^0-9]", ""))
                    + Long.parseLong(txtSimBonus.getText().replaceAll("[^0-9]", ""))
            ));
            txtProfesiPengeluaran.setText(txtSimPengeluaran.getText());
            hitungZakatProfesi();
            JOptionPane.showMessageDialog(this,
                    "Hasil simulasi zakat profesi telah disalin ke Form Kasir ZISWAF!",
                    "Data Tersalin", JOptionPane.INFORMATION_MESSAGE);
        });
        g.gridx = 0; g.gridy = 7; g.gridwidth = 2;
        cardSim.add(btnTerapkanKeKasir, g);

        pnlBody.add(cardSim);

        // Kolom Kanan: Landasan Fiqih & Falsafah Filantropi UIN Malang
        JPanel cardFiqih = new JPanel(new BorderLayout(10, 10));
        cardFiqih.setBackground(cleanWhite);
        cardFiqih.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cardBorder, 1),
                new EmptyBorder(15, 18, 15, 18)
        ));

        JLabel lblFiqihHeader = new JLabel("📖 Landasan Fiqih ZISWAF Sivitas Akademika");
        lblFiqihHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFiqihHeader.setForeground(deepGreen);
        cardFiqih.add(lblFiqihHeader, BorderLayout.NORTH);

        JEditorPane txtFiqih = new JEditorPane();
        txtFiqih.setContentType("text/html");
        txtFiqih.setEditable(false);
        txtFiqih.setText("<html><body style='font-family:Segoe UI, sans-serif; font-size:11px; color:#333333; line-height:1.4;'>"
                + "<p><b>1. Zakat Profesi (Al-Mal Al-Mustafad):</b><br>"
                + "Berdasarkan <i>Fatwa MUI No. 3 Tahun 2003</i>, zakat penghasilan dikeluarkan saat diperoleh dengan nisab setara 524 kg beras per tahun/bulan dan kadar zakat <b>2,5%</b>.</p>"
                + "<p><b>2. Zakat Mal (Tabungan & Emas):</b><br>"
                + "Nisab adalah 85 gram emas murni. Haul berlaku selama kepemilikan 1 tahun hijriyah/masehi dengan tarif <b>2,5%</b>.</p>"
                + "<p><b>3. Zakat Fitrah:</b><br>"
                + "Kewajiban perorangan yang ditunaikan sebelum sholat Idul Fitri sebesar <b>2,5 kg</b> atau 3,5 liter beras, atau uang tunai senilai makanan pokok tersebut (Rp 45.000).</p>"
                + "<p><b>4. Infaq & Shadaqah:</b><br>"
                + "Penyaluran sukarela yang sangat dianjurkan untuk mendukung beasiswa mahasiswa dhuafa, operasional dakwah masjid kampus, dan kemakmuran umat.</p>"
                + "<hr>"
                + "<p style='color:#1B5E20;'><i>\"Perumpamaan orang yang menafkahkan hartanya di jalan Allah adalah serupa dengan sebutir benih yang menumbuhkan tujuh bulir, pada tiap-tiap bulir seratus biji...\"</i><br>(QS. Al-Baqarah: 261)</p>"
                + "</body></html>");
        cardFiqih.add(new JScrollPane(txtFiqih), BorderLayout.CENTER);

        pnlBody.add(cardFiqih);

        panel.add(pnlBody, BorderLayout.CENTER);

        return panel;
    }
}
