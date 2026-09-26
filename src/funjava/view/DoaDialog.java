package funjava.view;

import funjava.util.ReceiptUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * DoaDialog
 * Dialog visual Islami yang menampilkan Lafaz Doa Amil Zakat
 * serta Doa Niat Muzakki saat transaksi disimpan / disetorkan.
 */
public class DoaDialog extends JDialog {

    private final Color primaryOrange = new Color(229, 127, 62);
    private final Color darkCoffee    = new Color(31, 16, 8);
    private final Color creamBg       = new Color(253, 243, 231);
    private final Color islamicGreen  = new Color(27, 94, 32);

    public DoaDialog(Frame parent, String namaMuzakki, String jenisDana) {
        super(parent, "Doa Serah Terima & Akad ZISWAF", true);
        initUI(namaMuzakki, jenisDana);
    }

    private void initUI(String namaMuzakki, String jenisDana) {
        setSize(580, 520);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(darkCoffee);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("DOA PENERIMAAN AMIL ZAKAT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(primaryOrange);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Akad Penyerahan " + (jenisDana != null ? jenisDana : "ZISWAF") 
                + " atas nama " + (namaMuzakki != null ? namaMuzakki : "Hamba Allah"));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(Color.WHITE);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnlHeader.add(lblTitle);
        pnlHeader.add(Box.createVerticalStrut(5));
        pnlHeader.add(lblSub);

        add(pnlHeader, BorderLayout.NORTH);

        // Content Panel
        JPanel pnlContent = new JPanel();
        pnlContent.setBackground(creamBg);
        pnlContent.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));

        // Card Doa Amil
        JPanel cardAmil = new JPanel();
        cardAmil.setBackground(Color.WHITE);
        cardAmil.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(208, 196, 184), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        cardAmil.setLayout(new BoxLayout(cardAmil, BoxLayout.Y_AXIS));

        JLabel lblAmilTag = new JLabel("Lafaz Doa Amil (Bagi Muzakki):");
        lblAmilTag.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblAmilTag.setForeground(islamicGreen);
        lblAmilTag.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Teks Arab Berharakat
        JLabel lblArab = new JLabel(ReceiptUtil.DOA_AMIL_ARAB);
        lblArab.setFont(new Font("Traditional Arabic", Font.BOLD, 22));
        lblArab.setForeground(darkCoffee);
        lblArab.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblLatin = new JLabel("<html><center><i>" + ReceiptUtil.DOA_AMIL_LATIN + "</i></center></html>");
        lblLatin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLatin.setForeground(new Color(60, 60, 60));
        lblLatin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblArti = new JLabel("<html><center style='color:#555555; padding-top:6px;'>" 
                + "<b>Artinya:</b> " + ReceiptUtil.DOA_AMIL_ARTI + "</center></html>");
        lblArti.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblArti.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardAmil.add(lblAmilTag);
        cardAmil.add(Box.createVerticalStrut(10));
        cardAmil.add(lblArab);
        cardAmil.add(Box.createVerticalStrut(10));
        cardAmil.add(lblLatin);
        cardAmil.add(Box.createVerticalStrut(8));
        cardAmil.add(lblArti);

        pnlContent.add(cardAmil);
        pnlContent.add(Box.createVerticalStrut(15));

        // Card Dalil & Hikmah
        JPanel cardHikmah = new JPanel();
        cardHikmah.setBackground(new Color(245, 240, 235));
        cardHikmah.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 210, 200), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        cardHikmah.setLayout(new BorderLayout());

        JLabel lblDalil = new JLabel("<html><center><b>Dasar Syariat QS. At-Taubah [9]: 103</b><br>"
                + "<i>\"Ambillah zakat dari sebagian harta mereka, dengan zakat itu kamu membersihkan dan mensucikan mereka dan mendoalah untuk mereka. Sesungguhnya doa kamu itu (menjadi) ketenteraman jiwa bagi mereka.\"</i></center></html>");
        lblDalil.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDalil.setForeground(new Color(80, 50, 20));
        cardHikmah.add(lblDalil, BorderLayout.CENTER);

        pnlContent.add(cardHikmah);

        add(pnlContent, BorderLayout.CENTER);

        // Footer Button Panel
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnClose = new JButton("🤲 Aamiin Yaa Rabbal 'Alamin (Tutup)");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setBackground(primaryOrange);
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnClose.addActionListener(e -> dispose());

        pnlFooter.add(btnClose);
        add(pnlFooter, BorderLayout.SOUTH);
    }
}
