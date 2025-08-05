/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rekammedis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import laporan.DlgBerkasRawat;
import laporan.DlgDiagnosaPenyakit;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Base64;
import org.apache.hc.core5.http.io.entity.StringEntity;


/**
 *
 * @author perpustakaan
 */
public final class RMDataResumePasienRanap extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2;
    private int i=0;    
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    private String kodekamar="",namakamar="",tglkeluar="",jamkeluar="",finger="",json;
    private ObjectMapper mapper= new ObjectMapper();
    private JsonNode root;
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public RMDataResumePasienRanap(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        WindowURLSertisign.setSize(570,100);
        WindowPhrase.setSize(320,100);
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No.Rawat","No.RM","Nama Pasien","Kode Dokter","Dokter Penanggung Jawab","Kode Pengirim","Dokter Pegirim",
            "Kode Kamar","Kamar/Ruang/Bangsal","Tgl.Masuk","Jam Masuk","Tgl.Keluar","Jam Keluar","Diagnosa Awal Masuk","Alasan Masuk Dirawat",
            "Keluhan Utama Riwayat Penyakit","Pemeriksaan Fisik","Jalannya Penyakit Selama Perawatan","Pemeriksaan Penunjang Rad Terpenting",
            "Pemeriksaan Penunjang Lab Terpenting","Tindakan/Operasi Selama Perawatan","Obat-obatan Selama Perawatan","Diagnosa Utama",
            "ICD10 Utama","Diagnosa Sekunder 1","ICD10 Sek 1","Diagnosa Sekunder 2","ICD10 Sek 2","Diagnosa Sekunder 3","ICD10 Sek 3",
            "Diagnosa Sekunder 4","ICD10 Sek 4","Prosedur Utama","ICD9 Utama","Prosedur Sekunder 1","ICD9 Sek1","Prosedur Sekunder 2",
            "ICD9 Sek2","Prosedur Sekunder 3","ICD9 Sek3","Alergi Obat","Diet","Hasil Lab Yang Belum Selesai (Pending)",
            "Instruksi/Anjuran Dan Edukasi (Follow Up)","Keadaan Pulang","Ket.Keadaan Pulang","Cara Keluar","Ket.Cara Keluar","Dilanjutkan",
            "Ket.Dilanjutkan","Kontrol Kembali","Obat Pulang","Kode Bayar","Cara Bayar"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 54; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(75);
            }else if(i==2){
                column.setPreferredWidth(150);
            }else if(i==3){
                column.setPreferredWidth(80);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(80);
            }else if(i==6){
                column.setPreferredWidth(150);
            }else if(i==7){
                column.setPreferredWidth(70);
            }else if(i==8){
                column.setPreferredWidth(150);
            }else if(i==9){
                column.setPreferredWidth(65);
            }else if(i==10){
                column.setPreferredWidth(65);
            }else if(i==11){
                column.setPreferredWidth(65);
            }else if(i==12){
                column.setPreferredWidth(65);
            }else if(i==13){
                column.setPreferredWidth(150);
            }else if(i==14){
                column.setPreferredWidth(150);
            }else if(i==15){
                column.setPreferredWidth(200);
            }else if(i==16){
                column.setPreferredWidth(250);
            }else if(i==17){
                column.setPreferredWidth(250);
            }else if(i==18){
                column.setPreferredWidth(250);
            }else if(i==19){
                column.setPreferredWidth(250);
            }else if(i==20){
                column.setPreferredWidth(250);
            }else if(i==21){
                column.setPreferredWidth(250);
            }else if(i==22){
                column.setPreferredWidth(150);
            }else if(i==23){
                column.setPreferredWidth(75);
            }else if(i==24){
                column.setPreferredWidth(150);
            }else if(i==25){
                column.setPreferredWidth(75);
            }else if(i==26){
                column.setPreferredWidth(150);
            }else if(i==27){
                column.setPreferredWidth(75);
            }else if(i==28){
                column.setPreferredWidth(150);
            }else if(i==29){
                column.setPreferredWidth(75);
            }else if(i==30){
                column.setPreferredWidth(150);
            }else if(i==31){
                column.setPreferredWidth(75);
            }else if(i==32){
                column.setPreferredWidth(150);
            }else if(i==33){
                column.setPreferredWidth(75);
            }else if(i==34){
                column.setPreferredWidth(150);
            }else if(i==35){
                column.setPreferredWidth(75);
            }else if(i==36){
                column.setPreferredWidth(150);
            }else if(i==37){
                column.setPreferredWidth(75);
            }else if(i==38){
                column.setPreferredWidth(150);
            }else if(i==39){
                column.setPreferredWidth(75);
            }else if(i==40){
                column.setPreferredWidth(150);
            }else if(i==41){
                column.setPreferredWidth(250);
            }else if(i==42){
                column.setPreferredWidth(250);
            }else if(i==43){
                column.setPreferredWidth(250);
            }else if(i==44){
                column.setPreferredWidth(90);
            }else if(i==45){
                column.setPreferredWidth(120);
            }else if(i==46){
                column.setPreferredWidth(100);
            }else if(i==47){
                column.setPreferredWidth(120);
            }else if(i==48){
                column.setPreferredWidth(90);
            }else if(i==49){
                column.setPreferredWidth(120);
            }else if(i==50){
                column.setPreferredWidth(120);
            }else if(i==51){
                column.setPreferredWidth(250);
            }else{
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        DiagnosaAwal.setDocument(new batasInput((int)70).getKata(DiagnosaAwal));
        Alasan.setDocument(new batasInput((int)70).getKata(Alasan));
        KeluhanUtama.setDocument(new batasInput((int)2000).getKata(KeluhanUtama));
        PemeriksaanFisik.setDocument(new batasInput((int)2000).getKata(PemeriksaanFisik));
        JalannyaPenyakit.setDocument(new batasInput((int)2000).getKata(JalannyaPenyakit));
        PemeriksaanRad.setDocument(new batasInput((int)2000).getKata(PemeriksaanRad));
        HasilLaborat.setDocument(new batasInput((int)2000).getKata(HasilLaborat));
        TindakanSelamaDiRS.setDocument(new batasInput((int)2000).getKata(TindakanSelamaDiRS));
        ObatSelamaDiRS.setDocument(new batasInput((int)2000).getKata(ObatSelamaDiRS));
        DiagnosaUtama.setDocument(new batasInput((int)80).getKata(DiagnosaUtama));
        DiagnosaSekunder1.setDocument(new batasInput((int)80).getKata(DiagnosaSekunder1));
        DiagnosaSekunder2.setDocument(new batasInput((int)80).getKata(DiagnosaSekunder2));
        DiagnosaSekunder3.setDocument(new batasInput((int)80).getKata(DiagnosaSekunder3));
        DiagnosaSekunder4.setDocument(new batasInput((int)80).getKata(DiagnosaSekunder4));
        ProsedurUtama.setDocument(new batasInput((int)80).getKata(ProsedurUtama));
        ProsedurSekunder1.setDocument(new batasInput((int)80).getKata(ProsedurSekunder1));
        ProsedurSekunder2.setDocument(new batasInput((int)80).getKata(ProsedurSekunder2));
        ProsedurSekunder3.setDocument(new batasInput((int)80).getKata(ProsedurSekunder3));
        KodeDiagnosaUtama.setDocument(new batasInput((int)10).getKata(KodeDiagnosaUtama));
        KodeDiagnosaSekunder1.setDocument(new batasInput((int)10).getKata(KodeDiagnosaSekunder1));
        KodeDiagnosaSekunder2.setDocument(new batasInput((int)10).getKata(KodeDiagnosaSekunder2));
        KodeDiagnosaSekunder3.setDocument(new batasInput((int)10).getKata(KodeDiagnosaSekunder3));
        KodeDiagnosaSekunder4.setDocument(new batasInput((int)10).getKata(KodeDiagnosaSekunder4));
        KodeProsedurUtama.setDocument(new batasInput((int)8).getKata(KodeProsedurUtama));
        KodeProsedurSekunder1.setDocument(new batasInput((int)8).getKata(KodeProsedurSekunder1));
        KodeProsedurSekunder2.setDocument(new batasInput((int)8).getKata(KodeProsedurSekunder2));
        KodeProsedurSekunder3.setDocument(new batasInput((int)8).getKata(KodeProsedurSekunder3));
        Alergi.setDocument(new batasInput((int)100).getKata(Alergi));
        Diet.setDocument(new batasInput((int)2000).getKata(Diet));
        LabBelum.setDocument(new batasInput((int)2000).getKata(LabBelum));
        Edukasi.setDocument(new batasInput((int)2000).getKata(Edukasi));
        KetKeadaanPulang.setDocument(new batasInput((int)50).getKata(KetKeadaanPulang));
        KetKeluar.setDocument(new batasInput((int)50).getKata(KetKeluar));
        KetDilanjutkan.setDocument(new batasInput((int)50).getKata(KetDilanjutkan));
        ObatPulang.setDocument(new batasInput((int)2000).getKata(ObatPulang));
        
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){
                    KodeDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NamaDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                    KodeDokter.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        ChkInput.setSelected(false);
        isForm();
      
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnLaporanResume = new javax.swing.JMenuItem();
        MnLaporanResumeESign = new javax.swing.JMenuItem();
        MnLaporanResumeSertisign = new javax.swing.JMenuItem();
        MnInputDiagnosa = new javax.swing.JMenuItem();
        ppBerkasDigital = new javax.swing.JMenuItem();
        WindowURLSertisign = new javax.swing.JDialog();
        WindowPhrase = new javax.swing.JDialog();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnLaporanResume.setBackground(new java.awt.Color(255, 255, 254));
        MnLaporanResume.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnLaporanResume.setForeground(new java.awt.Color(50, 50, 50));
        MnLaporanResume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnLaporanResume.setText("Laporan Resume Pasien");
        MnLaporanResume.setName("MnLaporanResume"); // NOI18N
        MnLaporanResume.setPreferredSize(new java.awt.Dimension(250, 26));
        MnLaporanResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnLaporanResumeActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnLaporanResume);

        MnLaporanResumeESign.setBackground(new java.awt.Color(255, 255, 254));
        MnLaporanResumeESign.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnLaporanResumeESign.setForeground(new java.awt.Color(50, 50, 50));
        MnLaporanResumeESign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnLaporanResumeESign.setText("Laporan Resume Pasien PDF E-Sign");
        MnLaporanResumeESign.setName("MnLaporanResumeESign"); // NOI18N
        MnLaporanResumeESign.setPreferredSize(new java.awt.Dimension(250, 26));
        MnLaporanResumeESign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnLaporanResumeESignActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnLaporanResumeESign);

        MnLaporanResumeSertisign.setBackground(new java.awt.Color(255, 255, 254));
        MnLaporanResumeSertisign.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnLaporanResumeSertisign.setForeground(new java.awt.Color(50, 50, 50));
        MnLaporanResumeSertisign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnLaporanResumeSertisign.setText("Laporan Resume Pasien PDF Sertisign");
        MnLaporanResumeSertisign.setName("MnLaporanResumeSertisign"); // NOI18N
        MnLaporanResumeSertisign.setPreferredSize(new java.awt.Dimension(250, 26));
        MnLaporanResumeSertisign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnLaporanResumeSertisignActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnLaporanResumeSertisign);

        MnInputDiagnosa.setBackground(new java.awt.Color(255, 255, 254));
        MnInputDiagnosa.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnInputDiagnosa.setForeground(new java.awt.Color(50, 50, 50));
        MnInputDiagnosa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnInputDiagnosa.setText("Input Diagnosa Pasien");
        MnInputDiagnosa.setName("MnInputDiagnosa"); // NOI18N
        MnInputDiagnosa.setPreferredSize(new java.awt.Dimension(250, 26));
        MnInputDiagnosa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnInputDiagnosaActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnInputDiagnosa);

        ppBerkasDigital.setBackground(new java.awt.Color(255, 255, 254));
        ppBerkasDigital.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBerkasDigital.setForeground(new java.awt.Color(50, 50, 50));
        ppBerkasDigital.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBerkasDigital.setText("Berkas Digital Perawatan");
        ppBerkasDigital.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBerkasDigital.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBerkasDigital.setName("ppBerkasDigital"); // NOI18N
        ppBerkasDigital.setPreferredSize(new java.awt.Dimension(250, 26));
        ppBerkasDigital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBerkasDigitalBtnPrintActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppBerkasDigital);

        WindowURLSertisign.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowURLSertisign.setModal(true);
        WindowURLSertisign.setName("WindowURLSertisign"); // NOI18N
        WindowURLSertisign.setUndecorated(true);
        WindowURLSertisign.setResizable(false);

        WindowPhrase.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowPhrase.setModal(true);
        WindowPhrase.setName("WindowPhrase"); // NOI18N
        WindowPhrase.setUndecorated(true);
        WindowPhrase.setResizable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
        }else{            
            Valid.pindah(evt,TCari,BtnDokter);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KodeDokter.getText().equals("")||NamaDokter.getText().equals("")){
            Valid.textKosong(BtnDokter,"Dokter Penanggung Jawab");
        }else if(KeluhanUtama.getText().equals("")){
            Valid.textKosong(KeluhanUtama,"Keluhan utama riwayat penyakit yang postif");
        }else if(JalannyaPenyakit.getText().equals("")){
            Valid.textKosong(JalannyaPenyakit,"Jalannya penyakit selama perawatan");
        }else if(DiagnosaUtama.getText().equals("")){
            Valid.textKosong(DiagnosaUtama,"Diagnosa Utama");
        }else{
            if(Sequel.menyimpantf("resume_pasien_ranap","?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?","No.Rawat",41,new String[]{
                    TNoRw.getText(),KodeDokter.getText(),DiagnosaAwal.getText(),Alasan.getText(),KeluhanUtama.getText(),PemeriksaanFisik.getText(),JalannyaPenyakit.getText(),
                    PemeriksaanRad.getText(),HasilLaborat.getText(),TindakanSelamaDiRS.getText(),ObatSelamaDiRS.getText(),DiagnosaUtama.getText(),KodeDiagnosaUtama.getText(),
                    DiagnosaSekunder1.getText(),KodeDiagnosaSekunder1.getText(),DiagnosaSekunder2.getText(),KodeDiagnosaSekunder2.getText(),DiagnosaSekunder3.getText(),
                    KodeDiagnosaSekunder3.getText(),DiagnosaSekunder4.getText(),KodeDiagnosaSekunder4.getText(),ProsedurUtama.getText(),KodeProsedurUtama.getText(),
                    ProsedurSekunder1.getText(),KodeProsedurSekunder1.getText(),ProsedurSekunder2.getText(),KodeProsedurSekunder2.getText(),ProsedurSekunder3.getText(), 
                    KodeProsedurSekunder3.getText(),Alergi.getText(),Diet.getText(),LabBelum.getText(),Edukasi.getText(),CaraKeluar.getSelectedItem().toString(),KetKeluar.getText(),
                    Keadaan.getSelectedItem().toString(),KetKeadaanPulang.getText(),DIlanjutkan.getSelectedItem().toString(),KetDilanjutkan.getText(),
                    Valid.SetTgl(Kontrol.getSelectedItem()+"")+" "+Kontrol.getSelectedItem().toString().substring(11,19),ObatPulang.getText()
                })==true){
                    tabMode.addRow(new Object[]{
                        TNoRw.getText(),TNoRM.getText(),TPasien.getText(),KodeDokter.getText(),NamaDokter.getText(),KodeDokterPengirim.getText(),NamaDokterPengirim.getText(),
                        KdRuang.getText(),NmRuang.getText(),Masuk.getText(),JamMasuk.getText(),Keluar.getText(),JamKeluar.getText(),DiagnosaAwal.getText(),Alasan.getText(),
                        KeluhanUtama.getText(),PemeriksaanFisik.getText(),JalannyaPenyakit.getText(),PemeriksaanRad.getText(),HasilLaborat.getText(),TindakanSelamaDiRS.getText(),
                        ObatSelamaDiRS.getText(),DiagnosaUtama.getText(),KodeDiagnosaUtama.getText(),DiagnosaSekunder1.getText(),KodeDiagnosaSekunder1.getText(),DiagnosaSekunder2.getText(),
                        KodeDiagnosaSekunder2.getText(),DiagnosaSekunder3.getText(),KodeDiagnosaSekunder3.getText(),DiagnosaSekunder4.getText(),KodeDiagnosaSekunder4.getText(),
                        ProsedurUtama.getText(),KodeProsedurUtama.getText(),ProsedurSekunder1.getText(),KodeProsedurSekunder1.getText(),ProsedurSekunder2.getText(),KodeProsedurSekunder2.getText(),
                        ProsedurSekunder3.getText(),KodeProsedurSekunder3.getText(),Alergi.getText(),Diet.getText(),LabBelum.getText(),Edukasi.getText(),Keadaan.getSelectedItem().toString(),
                        KetKeadaanPulang.getText(),CaraKeluar.getSelectedItem().toString(),KetKeluar.getText(),DIlanjutkan.getSelectedItem().toString(),KetDilanjutkan.getText(),
                        Valid.SetTgl(Kontrol.getSelectedItem()+"")+" "+Kontrol.getSelectedItem().toString().substring(11,19),ObatPulang.getText(),KdPj.getText(),CaraBayar.getText()
                    });
                    emptTeks();
                    LCount.setText(""+tabMode.getRowCount());
            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,ObatPulang,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        emptTeks();
        ChkInput.setSelected(true);
        isForm(); 
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObat.getSelectedRow()>-1){
            if(akses.getkode().equals("Admin Utama")){
                hapus();
            }else{
                if(KodeDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString())){
                    hapus();
                }else{
                    JOptionPane.showMessageDialog(null,"Hanya bisa dihapus oleh dokter yang bersangkutan..!!");
                }
            }
        }else{
            JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }            
            
}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KodeDokter.getText().equals("")||NamaDokter.getText().equals("")){
            Valid.textKosong(BtnDokter,"Dokter Penanggung Jawab");
        }else if(KeluhanUtama.getText().equals("")){
            Valid.textKosong(KeluhanUtama,"Keluhan utama riwayat penyakit yang postif");
        }else if(JalannyaPenyakit.getText().equals("")){
            Valid.textKosong(JalannyaPenyakit,"Jalannya penyakit selama perawatan");
        }else if(DiagnosaUtama.getText().equals("")){
            Valid.textKosong(DiagnosaUtama,"Diagnosa Utama");
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(KodeDokter.getText().equals(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString())){
                        ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Hanya bisa diganti oleh dokter yang bersangkutan..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dokter.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnEdit,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(! TCari.getText().trim().equals("")){
            BtnCariActionPerformed(evt);
        }
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
            Map<String, Object> param = new HashMap<>(); 
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            Valid.MyReportqry("rptDataResumePasienRanap.jasper","report","::[ Data Resume Pasien ]::",
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,resume_pasien_ranap.kd_dokter,dokter.nm_dokter,reg_periksa.kd_dokter as kodepengirim,pengirim.nm_dokter as pengirim,"+
                    "reg_periksa.tgl_registrasi,reg_periksa.jam_reg,resume_pasien_ranap.diagnosa_awal,resume_pasien_ranap.alasan,resume_pasien_ranap.keluhan_utama,resume_pasien_ranap.pemeriksaan_fisik,"+
                    "resume_pasien_ranap.jalannya_penyakit,resume_pasien_ranap.pemeriksaan_penunjang,resume_pasien_ranap.hasil_laborat,resume_pasien_ranap.tindakan_dan_operasi,resume_pasien_ranap.obat_di_rs,"+
                    "resume_pasien_ranap.diagnosa_utama,resume_pasien_ranap.kd_diagnosa_utama,resume_pasien_ranap.diagnosa_sekunder,resume_pasien_ranap.kd_diagnosa_sekunder,resume_pasien_ranap.diagnosa_sekunder2,"+
                    "resume_pasien_ranap.kd_diagnosa_sekunder2,resume_pasien_ranap.diagnosa_sekunder3,resume_pasien_ranap.kd_diagnosa_sekunder3,resume_pasien_ranap.diagnosa_sekunder4,"+
                    "resume_pasien_ranap.kd_diagnosa_sekunder4,resume_pasien_ranap.prosedur_utama,resume_pasien_ranap.kd_prosedur_utama,resume_pasien_ranap.prosedur_sekunder,resume_pasien_ranap.kd_prosedur_sekunder,"+
                    "resume_pasien_ranap.prosedur_sekunder2,resume_pasien_ranap.kd_prosedur_sekunder2,resume_pasien_ranap.prosedur_sekunder3,resume_pasien_ranap.kd_prosedur_sekunder3,resume_pasien_ranap.alergi,"+
                    "resume_pasien_ranap.diet,resume_pasien_ranap.lab_belum,resume_pasien_ranap.edukasi,resume_pasien_ranap.cara_keluar,resume_pasien_ranap.ket_keluar,resume_pasien_ranap.keadaan,"+
                    "resume_pasien_ranap.ket_keadaan,resume_pasien_ranap.dilanjutkan,resume_pasien_ranap.ket_dilanjutkan,resume_pasien_ranap.kontrol,resume_pasien_ranap.obat_pulang "+
                    "from resume_pasien_ranap inner join reg_periksa on resume_pasien_ranap.no_rawat=reg_periksa.no_rawat inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on resume_pasien_ranap.kd_dokter=dokter.kd_dokter inner join dokter as pengirim on reg_periksa.kd_dokter=pengirim.kd_dokter "+
                    "where reg_periksa.tgl_registrasi between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' "+
                    (TCari.getText().trim().equals("")?"":"and (reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                    "resume_pasien_ranap.kd_dokter like '%"+TCari.getText().trim()+"%' or dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or resume_pasien_ranap.keadaan like '%"+TCari.getText().trim()+"%' or "+
                    "resume_pasien_ranap.kd_diagnosa_utama like '%"+TCari.getText().trim()+"%' or resume_pasien_ranap.diagnosa_utama like '%"+TCari.getText().trim()+"%' or "+
                    "resume_pasien_ranap.prosedur_utama like '%"+TCari.getText().trim()+"%' or reg_periksa.no_rawat like '%"+TCari.getText().trim()+"%' or "+
                    "resume_pasien_ranap.kd_prosedur_utama like '%"+TCari.getText().trim()+"%')")+"order by reg_periksa.tgl_registrasi,reg_periksa.status_lanjut",param);
        }
        this.setCursor(Cursor.getDefaultCursor());
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnEdit, BtnKeluar);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    ChkInput.setSelected(true);
                    isForm(); 
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void DiagnosaSekunder2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaSekunder2KeyPressed
        Valid.pindah(evt,KodeDiagnosaSekunder1,KodeDiagnosaSekunder2);
    }//GEN-LAST:event_DiagnosaSekunder2KeyPressed

    private void DiagnosaUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaUtamaKeyPressed
       Valid.pindah(evt,ObatSelamaDiRS,KodeDiagnosaUtama);
    }//GEN-LAST:event_DiagnosaUtamaKeyPressed

    private void DiagnosaSekunder3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaSekunder3KeyPressed
        Valid.pindah(evt,KodeDiagnosaSekunder2,KodeDiagnosaSekunder3);
    }//GEN-LAST:event_DiagnosaSekunder3KeyPressed

    private void DiagnosaSekunder4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaSekunder4KeyPressed
        Valid.pindah(evt,KodeDiagnosaSekunder3,KodeDiagnosaSekunder4);
    }//GEN-LAST:event_DiagnosaSekunder4KeyPressed

    private void DiagnosaSekunder1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaSekunder1KeyPressed
        Valid.pindah(evt,KodeDiagnosaUtama,KodeDiagnosaSekunder1);
    }//GEN-LAST:event_DiagnosaSekunder1KeyPressed

    private void KodeDiagnosaUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDiagnosaUtamaKeyPressed
        Valid.pindah(evt,DiagnosaUtama,DiagnosaSekunder1);
    }//GEN-LAST:event_KodeDiagnosaUtamaKeyPressed

    private void KodeDiagnosaSekunder1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDiagnosaSekunder1KeyPressed
        Valid.pindah(evt,DiagnosaSekunder1,DiagnosaSekunder2);
    }//GEN-LAST:event_KodeDiagnosaSekunder1KeyPressed

    private void KodeDiagnosaSekunder2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDiagnosaSekunder2KeyPressed
        Valid.pindah(evt,DiagnosaSekunder2,DiagnosaSekunder3);
    }//GEN-LAST:event_KodeDiagnosaSekunder2KeyPressed

    private void KodeDiagnosaSekunder3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDiagnosaSekunder3KeyPressed
        Valid.pindah(evt,DiagnosaSekunder3,DiagnosaSekunder4);
    }//GEN-LAST:event_KodeDiagnosaSekunder3KeyPressed

    private void KodeDiagnosaSekunder4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDiagnosaSekunder4KeyPressed
        Valid.pindah(evt,DiagnosaSekunder4,ProsedurUtama);
    }//GEN-LAST:event_KodeDiagnosaSekunder4KeyPressed

    private void ProsedurUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsedurUtamaKeyPressed
        Valid.pindah(evt,KodeDiagnosaSekunder4,KodeProsedurUtama);
    }//GEN-LAST:event_ProsedurUtamaKeyPressed

    private void KodeProsedurUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeProsedurUtamaKeyPressed
        Valid.pindah(evt,ProsedurUtama,ProsedurSekunder1);
    }//GEN-LAST:event_KodeProsedurUtamaKeyPressed

    private void ProsedurSekunder1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsedurSekunder1KeyPressed
        Valid.pindah(evt,KodeProsedurUtama,KodeProsedurSekunder1);
    }//GEN-LAST:event_ProsedurSekunder1KeyPressed

    private void KodeProsedurSekunder1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeProsedurSekunder1KeyPressed
        Valid.pindah(evt,ProsedurSekunder1,ProsedurSekunder2);
    }//GEN-LAST:event_KodeProsedurSekunder1KeyPressed

    private void ProsedurSekunder2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsedurSekunder2KeyPressed
        Valid.pindah(evt,KodeProsedurSekunder1,KodeProsedurSekunder2);
    }//GEN-LAST:event_ProsedurSekunder2KeyPressed

    private void KodeProsedurSekunder2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeProsedurSekunder2KeyPressed
        Valid.pindah(evt,ProsedurSekunder2,ProsedurSekunder3);
    }//GEN-LAST:event_KodeProsedurSekunder2KeyPressed

    private void KodeProsedurSekunder3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeProsedurSekunder3KeyPressed
        Valid.pindah(evt,ProsedurSekunder3,Alergi);
    }//GEN-LAST:event_KodeProsedurSekunder3KeyPressed

    private void ProsedurSekunder3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProsedurSekunder3KeyPressed
        Valid.pindah(evt,KodeProsedurSekunder2,KodeProsedurSekunder3);
    }//GEN-LAST:event_ProsedurSekunder3KeyPressed

    private void KodeDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDokterKeyPressed
        Valid.pindah(evt,TCari,CaraKeluar);
    }//GEN-LAST:event_KodeDokterKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(internalFrame1);
        dokter.setVisible(true);
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
       Valid.pindah(evt,TCari,CaraKeluar);
    }//GEN-LAST:event_BtnDokterKeyPressed

    private void KeluhanUtamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeluhanUtamaKeyPressed
        Valid.pindah2(evt,Alasan,PemeriksaanFisik);
    }//GEN-LAST:event_KeluhanUtamaKeyPressed

    private void PemeriksaanFisikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemeriksaanFisikKeyPressed
        Valid.pindah2(evt,KeluhanUtama,JalannyaPenyakit);
    }//GEN-LAST:event_PemeriksaanFisikKeyPressed

    private void PemeriksaanRadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PemeriksaanRadKeyPressed
        Valid.pindah2(evt,JalannyaPenyakit,HasilLaborat);
    }//GEN-LAST:event_PemeriksaanRadKeyPressed

    private void HasilLaboratKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_HasilLaboratKeyPressed
        Valid.pindah2(evt,PemeriksaanRad,TindakanSelamaDiRS);
    }//GEN-LAST:event_HasilLaboratKeyPressed

    private void MnLaporanResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnLaporanResumeActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();    
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("norawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),3).toString());
            param.put("finger","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+tbObat.getValueAt(tbObat.getSelectedRow(),4).toString()+"\nID "+(finger.equals("")?tbObat.getValueAt(tbObat.getSelectedRow(),3).toString():finger)+"\n"+Valid.SetTgl3(Keluar.getText())); 
            try {
                ps=koneksi.prepareStatement("select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=? and dpjp_ranap.kd_dokter<>?");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    ps.setString(2,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
                    rs=ps.executeQuery();
                    i=2;
                    while(rs.next()){
                       if(i==2){
                           finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                           param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                           param.put("namadokter2",rs.getString("nm_dokter")); 
                       }
                       if(i==3){
                           finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                           param.put("finger3","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                           param.put("namadokter3",rs.getString("nm_dokter")); 
                       }
                       i++;
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }
            param.put("ruang",KdRuang.getText()+" "+NmRuang.getText());
            param.put("tanggalkeluar",Valid.SetTgl3(Keluar.getText()));
            param.put("jamkeluar",JamKeluar.getText());
            Valid.MyReport("rptLaporanResumeRanap.jasper","report","::[ Laporan Resume Pasien ]::",param);
        }
    }//GEN-LAST:event_MnLaporanResumeActionPerformed

    private void BtnDokter1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter1ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariKeluhan carikeluhan=new RMCariKeluhan(null,false);
            carikeluhan.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {
                    if(carikeluhan.getTable().getSelectedRow()!= -1){
                        KeluhanUtama.append(carikeluhan.getTable().getValueAt(carikeluhan.getTable().getSelectedRow(),2).toString()+", ");
                        KeluhanUtama.requestFocus();
                    }
                }
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            carikeluhan.setNoRawat(TNoRw.getText());
            carikeluhan.tampil();
            carikeluhan.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            carikeluhan.setLocationRelativeTo(internalFrame1);
            carikeluhan.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokter1ActionPerformed

    private void BtnDokter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter2ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariHasilRadiologi cariradiologi=new RMCariHasilRadiologi(null,false);
            cariradiologi.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {
                    if(cariradiologi.getTable().getSelectedRow()!= -1){
                        PemeriksaanRad.append(cariradiologi.getTable().getValueAt(cariradiologi.getTable().getSelectedRow(),2).toString()+", ");
                        PemeriksaanRad.requestFocus();
                    }
                }
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            cariradiologi.setNoRawat(TNoRw.getText());
            cariradiologi.tampil();
            cariradiologi.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            cariradiologi.setLocationRelativeTo(internalFrame1);
            cariradiologi.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokter2ActionPerformed

    private void BtnDokter3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter3ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariHasilLaborat carilaborat=new RMCariHasilLaborat(null,false);
            carilaborat.getTable().addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {}
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_SPACE){
                        if(carilaborat.getTable().getSelectedRow()!= -1){
                            HasilLaborat.append(carilaborat.getTable().getValueAt(carilaborat.getTable().getSelectedRow(),3).toString()+", ");
                            HasilLaborat.requestFocus();
                        }
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {}
            }); 

            carilaborat.BtnKeluar.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (i= 0; i < carilaborat.getTable().getRowCount(); i++) {
                        if(carilaborat.getTable().getValueAt(i,0).toString().equals("true")){
                            HasilLaborat.append(carilaborat.getTable().getValueAt(i,3).toString()+", ");
                        }
                    }
                    HasilLaborat.requestFocus();
                }
            });
            carilaborat.setNoRawat(TNoRw.getText());
            carilaborat.tampil();
            carilaborat.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            carilaborat.setLocationRelativeTo(internalFrame1);
            carilaborat.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokter3ActionPerformed

    private void MnInputDiagnosaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnInputDiagnosaActionPerformed
        if(TNoRw.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu pasien...!!!");
            TCari.requestFocus();
        }else{
            DlgDiagnosaPenyakit penyakit=new DlgDiagnosaPenyakit(null,false);
            penyakit.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {
                    tampil();
                }
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            penyakit.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            penyakit.setLocationRelativeTo(internalFrame1);
            penyakit.isCek();
            penyakit.setNoRm(TNoRw.getText(),DTPCari1.getDate(),DTPCari2.getDate(),Sequel.cariIsi("select reg_periksa.status_lanjut from reg_periksa where reg_periksa.no_rawat=?",TNoRw.getText()));
            penyakit.panelDiagnosa1.tampil();
            penyakit.setVisible(true);
        }
    }//GEN-LAST:event_MnInputDiagnosaActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void ppBerkasDigitalBtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBerkasDigitalBtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(!tbObat.getValueAt(tbObat.getSelectedRow(),0).toString().equals("")){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    DlgBerkasRawat berkas=new DlgBerkasRawat(null,true);
                    berkas.setJudul("::[ Berkas Digital Perawatan ]::","berkasrawat/pages");
                    try {
                        if(akses.gethapus_berkas_digital_perawatan()==true){
                            berkas.loadURL("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/"+"berkasrawat/login2.php?act=login&usere="+koneksiDB.USERHYBRIDWEB()+"&passwordte="+koneksiDB.PASHYBRIDWEB()+"&no_rawat="+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                        }else{
                            berkas.loadURL("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/"+"berkasrawat/login2nonhapus.php?act=login&usere="+koneksiDB.USERHYBRIDWEB()+"&passwordte="+koneksiDB.PASHYBRIDWEB()+"&no_rawat="+tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                        }   
                    } catch (Exception ex) {
                        System.out.println("Notifikasi : "+ex);
                    }

                    berkas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                    berkas.setLocationRelativeTo(internalFrame1);
                    berkas.setVisible(true);
                    this.setCursor(Cursor.getDefaultCursor());
                }
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_ppBerkasDigitalBtnPrintActionPerformed

    private void CaraKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CaraKeluarKeyPressed
        Valid.pindah(evt, KetKeadaanPulang,KetKeluar);
    }//GEN-LAST:event_CaraKeluarKeyPressed

    private void KeadaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeadaanKeyPressed
        Valid.pindah(evt,Edukasi,KetKeadaanPulang);
    }//GEN-LAST:event_KeadaanKeyPressed

    private void BtnDokter5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter5ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariPemeriksaan caripemeriksaan=new RMCariPemeriksaan(null,false);
            caripemeriksaan.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {
                    if(caripemeriksaan.getTable().getSelectedRow()!= -1){
                        PemeriksaanFisik.append(caripemeriksaan.getTable().getValueAt(caripemeriksaan.getTable().getSelectedRow(),2).toString()+", ");
                        PemeriksaanFisik.requestFocus();
                    }
                }
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            caripemeriksaan.setNoRawat(TNoRw.getText());
            caripemeriksaan.tampil();
            caripemeriksaan.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            caripemeriksaan.setLocationRelativeTo(internalFrame1);
            caripemeriksaan.setVisible(true);
        } 
    }//GEN-LAST:event_BtnDokter5ActionPerformed

    private void KodeDokterPengirimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDokterPengirimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KodeDokterPengirimKeyPressed

    private void BtnDokter16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter16ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariTindakan caritindakan=new RMCariTindakan(null,false);
            caritindakan.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {
                    if(caritindakan.getTable().getSelectedRow()!= -1){
                        TindakanSelamaDiRS.append(caritindakan.getTable().getValueAt(caritindakan.getTable().getSelectedRow(),2).toString()+", ");
                        TindakanSelamaDiRS.requestFocus();
                    }
                }
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            caritindakan.setNoRawat(TNoRw.getText());
            caritindakan.tampil();
            caritindakan.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            caritindakan.setLocationRelativeTo(internalFrame1);
            caritindakan.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokter16ActionPerformed

    private void TindakanSelamaDiRSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TindakanSelamaDiRSKeyPressed
        Valid.pindah2(evt,HasilLaborat,ObatSelamaDiRS);
    }//GEN-LAST:event_TindakanSelamaDiRSKeyPressed

    private void BtnDokter17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter17ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariLabPending carilabpending=new RMCariLabPending(null,false);
            carilabpending.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {
                    if(carilabpending.getTable().getSelectedRow()!= -1){
                        LabBelum.append(carilabpending.getTable().getValueAt(carilabpending.getTable().getSelectedRow(),2).toString()+", ");
                        LabBelum.requestFocus();
                    }
                }
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            carilabpending.setNoRawat(TNoRw.getText());
            carilabpending.tampil();
            carilabpending.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            carilabpending.setLocationRelativeTo(internalFrame1);
            carilabpending.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokter17ActionPerformed

    private void DietKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DietKeyPressed
        Valid.pindah2(evt,Alergi,LabBelum);
    }//GEN-LAST:event_DietKeyPressed

    private void EdukasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EdukasiKeyPressed
        Valid.pindah2(evt,LabBelum,Keadaan);
    }//GEN-LAST:event_EdukasiKeyPressed

    private void DIlanjutkanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DIlanjutkanKeyPressed
        Valid.pindah(evt,KetKeluar,KetDilanjutkan);
    }//GEN-LAST:event_DIlanjutkanKeyPressed

    private void KontrolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KontrolKeyPressed
        Valid.pindah2(evt,KetDilanjutkan,ObatPulang);
    }//GEN-LAST:event_KontrolKeyPressed

    private void BtnDokter18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter18ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariDiet caridiet=new RMCariDiet(null,false);
            caridiet.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {
                    if(caridiet.getTable().getSelectedRow()!= -1){
                        Diet.append(caridiet.getTable().getValueAt(caridiet.getTable().getSelectedRow(),2).toString()+", ");
                        Diet.requestFocus();
                    }
                }
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            caridiet.setNoRawat(TNoRw.getText());
            caridiet.tampil();
            caridiet.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            caridiet.setLocationRelativeTo(internalFrame1);
            caridiet.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokter18ActionPerformed

    private void LabBelumKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LabBelumKeyPressed
        Valid.pindah2(evt,Diet,Edukasi);
    }//GEN-LAST:event_LabBelumKeyPressed

    private void JalannyaPenyakitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JalannyaPenyakitKeyPressed
        Valid.pindah2(evt,PemeriksaanFisik,PemeriksaanRad);
    }//GEN-LAST:event_JalannyaPenyakitKeyPressed

    private void ObatPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ObatPulangKeyPressed
        Valid.pindah2(evt,Kontrol,BtnSimpan);
    }//GEN-LAST:event_ObatPulangKeyPressed

    private void BtnDokter19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter19ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariObatPulang cariobatpulang=new RMCariObatPulang(null,false);
            cariobatpulang.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {}
                @Override
                public void windowClosing(WindowEvent e) {}
                @Override
                public void windowClosed(WindowEvent e) {
                    if(cariobatpulang.getTable().getSelectedRow()!= -1){
                        ObatPulang.append(cariobatpulang.getTable().getValueAt(cariobatpulang.getTable().getSelectedRow(),2).toString()+"\n");
                        ObatPulang.requestFocus();
                    }
                }
                @Override
                public void windowIconified(WindowEvent e) {}
                @Override
                public void windowDeiconified(WindowEvent e) {}
                @Override
                public void windowActivated(WindowEvent e) {}
                @Override
                public void windowDeactivated(WindowEvent e) {}
            });
            cariobatpulang.setNoRawat(TNoRw.getText());
            cariobatpulang.tampil();
            cariobatpulang.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            cariobatpulang.setLocationRelativeTo(internalFrame1);
            cariobatpulang.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokter19ActionPerformed

    private void DiagnosaAwalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DiagnosaAwalKeyPressed
        Valid.pindah(evt,TCari,Alasan);
    }//GEN-LAST:event_DiagnosaAwalKeyPressed

    private void AlasanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlasanKeyPressed
        Valid.pindah(evt,DiagnosaAwal,KeluhanUtama);
    }//GEN-LAST:event_AlasanKeyPressed

    private void AlergiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AlergiKeyPressed
        Valid.pindah(evt,KodeProsedurSekunder3,Diet);
    }//GEN-LAST:event_AlergiKeyPressed

    private void KetKeadaanPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetKeadaanPulangKeyPressed
        Valid.pindah(evt,Keadaan,CaraKeluar);
    }//GEN-LAST:event_KetKeadaanPulangKeyPressed

    private void KetKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetKeluarKeyPressed
        Valid.pindah(evt,CaraKeluar,DIlanjutkan);
    }//GEN-LAST:event_KetKeluarKeyPressed

    private void KetDilanjutkanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KetDilanjutkanKeyPressed
        Valid.pindah(evt,DIlanjutkan,Kontrol);
    }//GEN-LAST:event_KetDilanjutkanKeyPressed

    private void BtnDokter20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter20ActionPerformed
        if(TNoRw.getText().equals("")&&TNoRM.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Pasien masih kosong...!!!");
        }else{
            RMCariJumlahObat cariobat=new RMCariJumlahObat(null,false);
            cariobat.getTable().addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {}
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_SPACE){
                        if(cariobat.getTable().getSelectedRow()!= -1){
                            ObatSelamaDiRS.append(cariobat.getTable().getValueAt(cariobat.getTable().getSelectedRow(),3).toString()+", ");
                            ObatSelamaDiRS.requestFocus();
                        }
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {}
            }); 

            cariobat.BtnKeluar.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (i= 0; i < cariobat.getTable().getRowCount(); i++) {
                        if(cariobat.getTable().getValueAt(i,0).toString().equals("true")){
                            ObatSelamaDiRS.append(cariobat.getTable().getValueAt(i,3).toString()+", ");
                        }
                    }
                    ObatSelamaDiRS.requestFocus();
                }
            });
            cariobat.setNoRawat(TNoRw.getText());
            cariobat.tampil();
            cariobat.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            cariobat.setLocationRelativeTo(internalFrame1);
            cariobat.setVisible(true);
        }
    }//GEN-LAST:event_BtnDokter20ActionPerformed

    private void ObatSelamaDiRSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ObatSelamaDiRSKeyPressed
        Valid.pindah2(evt,TindakanSelamaDiRS,DiagnosaUtama);
    }//GEN-LAST:event_ObatSelamaDiRSKeyPressed

    private void BtnDokter6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokter6ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        RMRiwayatPerawatan resume=new RMRiwayatPerawatan(null,true);
        resume.setNoRm(TNoRM.getText(),TPasien.getText());
        resume.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
        resume.setLocationRelativeTo(internalFrame1);
        resume.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnDokter6ActionPerformed

    private void MnLaporanResumeESignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnLaporanResumeESignActionPerformed
        WindowPhrase.setAlwaysOnTop(true);
        WindowPhrase.setLocationRelativeTo(internalFrame1);
        WindowPhrase.setVisible(true);
    }//GEN-LAST:event_MnLaporanResumeESignActionPerformed

    private void MnLaporanResumeSertisignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnLaporanResumeSertisignActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();    
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
            param.put("norawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
            param.put("finger","#1A"); 
            try {
                ps=koneksi.prepareStatement("select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=? and dpjp_ranap.kd_dokter<>?");
                try {
                    ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                    ps.setString(2,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
                    rs=ps.executeQuery();
                    i=2;
                    while(rs.next()){
                       if(i==2){
                           finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                           param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                           param.put("namadokter2",rs.getString("nm_dokter")); 
                       }
                       if(i==3){
                           finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                           param.put("finger3","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                           param.put("namadokter3",rs.getString("nm_dokter")); 
                       }
                       i++;
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(ps!=null){
                        ps.close();
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            }
            param.put("ruang",KdRuang.getText()+" "+NmRuang.getText());
            param.put("tanggalkeluar",Valid.SetTgl3(Keluar.getText()));
            param.put("jamkeluar",JamKeluar.getText());
            Valid.MyReportPDF2("rptLaporanResumeRanap2.jasper","report","::[ Laporan Resume Pasien ]::",param);
            File f = new File("./report/rptLaporanResumeRanap2.pdf");  
            try {
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost post = new HttpPost(koneksiDB.URLAPISERTISIGN());
                post.addHeader("apikey",koneksiDB.APIKEYSERTISIGN());
                post.addHeader("Accept","application/json");
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                    .addBinaryBody("document", f, ContentType.APPLICATION_PDF, f.getName())
                    .addTextBody("signer",Sequel.cariIsi("select dokter.email from dokter where dokter.kd_dokter=?", akses.getkode()))
                    .addTextBody("keyword","#1A")
                    .addTextBody("qrValue","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+NamaDokter.getText()+"\nID "+Sequel.cariIsi("select pegawai.no_ktp from pegawai where pegawai.nik=?", akses.getkode())+"\n"+Valid.SetTgl3(Keluar.getText()))
                    .addTextBody("w", "40")
                    .addTextBody("h", "40");
                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                System.out.println("URL Kirim File : "+koneksiDB.URLAPISERTISIGN());
                try (CloseableHttpResponse response = httpClient.execute(post)) {
                    if (response.getCode() == 200) {
                        json=EntityUtils.toString(response.getEntity());
                        System.out.println("Respon Kirim File : " + json);
                        root = mapper.readTree(json);
                        System.out.println("Id File : " +root.path("data").path("transaction_id").asText());
                        System.out.println("URL Callback File : "+koneksiDB.URLDOKUMENSERTISIGN()+"/"+root.path("data").path("transaction_id").asText()+".pdf");
                        URLSertisign.setText(koneksiDB.URLDOKUMENSERTISIGN()+"/"+root.path("data").path("transaction_id").asText()+".pdf");
                        WindowURLSertisign.setAlwaysOnTop(true);
                        WindowURLSertisign.setLocationRelativeTo(internalFrame1);
                        WindowURLSertisign.setVisible(true);
                    } else {
                        System.out.println("Notifikasi : " + EntityUtils.toString(response.getEntity()));
                    }
                } catch (Exception a) {
                    System.out.println("Notifikasi : " + a);
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            }
        }
    }//GEN-LAST:event_MnLaporanResumeSertisignActionPerformed

    private void BtnCloseUrlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseUrlActionPerformed
        URLSertisign.setText("");
        WindowURLSertisign.dispose();
    }//GEN-LAST:event_BtnCloseUrlActionPerformed

    private void BtnBukaURLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBukaURLActionPerformed
        try {
            Desktop.getDesktop().browse(new URI(URLSertisign.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnBukaURLActionPerformed

    private void BtnDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadFileActionPerformed

    private void BtnDownloadBukaFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDownloadBukaFileActionPerformed
        try {
            URL url = new URL(URLSertisign.getText());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf");
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            readableByteChannel.close();
            System.out.println("Download Selesai : " + "Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf");
            Desktop.getDesktop().browse(new File("Resume"+TNoRw.getText().trim().replaceAll("/","")+".pdf").toURI());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(rootPane,"File belum tersedia, silahkan tunggu & ulangi beberapa saat lagi..!!");
            System.out.println("Notifikasi : " + e);
        }
    }//GEN-LAST:event_BtnDownloadBukaFileActionPerformed

    private void BtnClosePhraseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnClosePhraseActionPerformed
        Phrase.setText("");
        WindowPhrase.dispose();
    }//GEN-LAST:event_BtnClosePhraseActionPerformed

    private void BtnSimpanTandaTanganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanTandaTanganActionPerformed
        if(Phrase.getText().equals("")){
            Valid.textKosong(Phrase,"Phrase");
        }else{
            if(tbObat.getSelectedRow()>-1){
                Map<String, Object> param = new HashMap<>();    
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                param.put("norawat",tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                param.put("finger","#"); 
                try {
                    ps=koneksi.prepareStatement("select dpjp_ranap.kd_dokter,dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat=? and dpjp_ranap.kd_dokter<>?");
                    try {
                        ps.setString(1,tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());
                        ps.setString(2,tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
                        rs=ps.executeQuery();
                        i=2;
                        while(rs.next()){
                           if(i==2){
                               finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                               param.put("finger2","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                               param.put("namadokter2",rs.getString("nm_dokter")); 
                           }
                           if(i==3){
                               finger=Sequel.cariIsi("select sha1(sidikjari.sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",rs.getString("kd_dokter"));
                               param.put("finger3","Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+"\nDitandatangani secara elektronik oleh "+rs.getString("nm_dokter")+"\nID "+(finger.equals("")?rs.getString("kd_dokter"):finger)+"\n"+Valid.SetTgl3(Keluar.getText()));
                               param.put("namadokter3",rs.getString("nm_dokter")); 
                           }
                           i++;
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : "+e);
                    } finally{
                        if(rs!=null){
                            rs.close();
                        }
                        if(ps!=null){
                            ps.close();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif : "+e);
                }
                param.put("ruang",KdRuang.getText()+" "+NmRuang.getText());
                param.put("tanggalkeluar",Valid.SetTgl3(Keluar.getText()));
                param.put("jamkeluar",JamKeluar.getText());
                Valid.MyReportPDF2("rptLaporanResumeRanap2.jasper","report","::[ Laporan Resume Pasien ]::",param);
                File f = new File("./report/rptLaporanResumeRanap2.pdf");  
                try {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpPost post = new HttpPost(koneksiDB.URLAKSESFILEESIGN());
                    post.setHeader("Content-Type", "application/json");
                    post.addHeader("username", koneksiDB.USERNAMEAPIESIGN());
                    post.addHeader("password", koneksiDB.PASSAPIESIGN());
                    post.addHeader("url", koneksiDB.URLAPIESIGN());
                    
                    byte[] fileContent = Files.readAllBytes(f.toPath());
                    
                    json="{" +
                             "\"file\":\""+Base64.getEncoder().encodeToString(fileContent)+"\"," +
                             "\"nik\":\""+Sequel.cariIsi("select pegawai.no_ktp from pegawai where pegawai.nik=?", akses.getkode())+"\"," +
                             "\"passphrase\":\""+Phrase.getText()+"\"," +
                             "\"tampilan\":\"visible\"," +
                             "\"image\":\"false\"," +
                             "\"linkQR\":\"Dikeluarkan di "+akses.getnamars()+", Kabupaten/Kota "+akses.getkabupatenrs()+" dan ditandatangani secara elektronik oleh "+NamaDokter.getText()+" ID "+KodeDokter.getText()+" Tanggal "+Valid.SetTgl3(Keluar.getText())+"\"," +
                             "\"width\":\"55\"," +
                             "\"height\":\"55\"," +
                             "\"tag_koordinat\":\"#\"" +
                          "}";
                    
                    System.out.println("URL Akses file :"+koneksiDB.URLAKSESFILEESIGN());
                    System.out.println("JSON Dikirim :"+json);
                    post.setEntity(new StringEntity(json));
                    try (CloseableHttpResponse response = httpClient.execute(post)) {
                        System.out.println("Response Status : " + response.getCode());
                        json=EntityUtils.toString(response.getEntity());
                        root = mapper.readTree(json);
                        if (response.getCode() == 200) {
                            try (FileOutputStream fos = new FileOutputStream(new File("Resume"+TNoRw.getText().trim().replaceAll("/","")+"_"+TNoRM.getText().trim().replaceAll(" ","")+"_"+TPasien.getText().trim().replaceAll(" ","")+".pdf"))) {
                                byte[] fileBytes = Base64.getDecoder().decode(root.path("response").asText());
                                fos.write(fileBytes);
                                WindowPhrase.dispose();
                                JOptionPane.showMessageDialog(null,"Proses tanda tangan berhasil...");
                                Desktop.getDesktop().browse(new File("Resume"+TNoRw.getText().trim().replaceAll("/","")+"_"+TNoRM.getText().trim().replaceAll(" ","")+"_"+TPasien.getText().trim().replaceAll(" ","")+".pdf").toURI());
                            } catch (Exception e) {
                                WindowPhrase.dispose();
                                JOptionPane.showMessageDialog(null,"Gagal mengkonversi base64 ke file...");
                                System.out.println("Notif : " +e);
                            }
                        } else {
                            WindowPhrase.dispose();
                            JOptionPane.showMessageDialog(null,"Code : "+root.path("metadata").path("code").asText()+" Pesan : "+root.path("metadata").path("message").asText());
                        }
                    } catch (IOException a) {
                        System.out.println("Notifikasi : " + a);
                        WindowPhrase.dispose();
                        JOptionPane.showMessageDialog(null,""+a);
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi : " + e);
                    WindowPhrase.dispose();
                    JOptionPane.showMessageDialog(null,""+e);
                }
            }
        }
    }//GEN-LAST:event_BtnSimpanTandaTanganActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMDataResumePasienRanap dialog = new RMDataResumePasienRanap(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MnInputDiagnosa;
    private javax.swing.JMenuItem MnLaporanResume;
    private javax.swing.JMenuItem MnLaporanResumeESign;
    private javax.swing.JMenuItem MnLaporanResumeSertisign;
    private javax.swing.JDialog WindowPhrase;
    private javax.swing.JDialog WindowURLSertisign;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JMenuItem ppBerkasDigital;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,resume_pasien_ranap.kd_dokter,dokter.nm_dokter,reg_periksa.kd_dokter as kodepengirim,pengirim.nm_dokter as pengirim,"+
                    "reg_periksa.tgl_registrasi,reg_periksa.jam_reg,resume_pasien_ranap.diagnosa_awal,resume_pasien_ranap.alasan,resume_pasien_ranap.keluhan_utama,resume_pasien_ranap.pemeriksaan_fisik,"+
                    "resume_pasien_ranap.jalannya_penyakit,resume_pasien_ranap.pemeriksaan_penunjang,resume_pasien_ranap.hasil_laborat,resume_pasien_ranap.tindakan_dan_operasi,resume_pasien_ranap.obat_di_rs,"+
                    "resume_pasien_ranap.diagnosa_utama,resume_pasien_ranap.kd_diagnosa_utama,resume_pasien_ranap.diagnosa_sekunder,resume_pasien_ranap.kd_diagnosa_sekunder,resume_pasien_ranap.diagnosa_sekunder2,"+
                    "resume_pasien_ranap.kd_diagnosa_sekunder2,resume_pasien_ranap.diagnosa_sekunder3,resume_pasien_ranap.kd_diagnosa_sekunder3,resume_pasien_ranap.diagnosa_sekunder4,"+
                    "resume_pasien_ranap.kd_diagnosa_sekunder4,resume_pasien_ranap.prosedur_utama,resume_pasien_ranap.kd_prosedur_utama,resume_pasien_ranap.prosedur_sekunder,resume_pasien_ranap.kd_prosedur_sekunder,"+
                    "resume_pasien_ranap.prosedur_sekunder2,resume_pasien_ranap.kd_prosedur_sekunder2,resume_pasien_ranap.prosedur_sekunder3,resume_pasien_ranap.kd_prosedur_sekunder3,resume_pasien_ranap.alergi,"+
                    "resume_pasien_ranap.diet,resume_pasien_ranap.lab_belum,resume_pasien_ranap.edukasi,resume_pasien_ranap.cara_keluar,resume_pasien_ranap.ket_keluar,resume_pasien_ranap.keadaan,"+
                    "resume_pasien_ranap.ket_keadaan,resume_pasien_ranap.dilanjutkan,resume_pasien_ranap.ket_dilanjutkan,resume_pasien_ranap.kontrol,resume_pasien_ranap.obat_pulang,reg_periksa.kd_pj,penjab.png_jawab "+
                    "from resume_pasien_ranap inner join reg_periksa on resume_pasien_ranap.no_rawat=reg_periksa.no_rawat inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                    "inner join dokter on resume_pasien_ranap.kd_dokter=dokter.kd_dokter inner join dokter as pengirim on reg_periksa.kd_dokter=pengirim.kd_dokter "+
                    "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj where reg_periksa.tgl_registrasi between ? and ? "+
                    (TCari.getText().trim().equals("")?"":"and (reg_periksa.no_rkm_medis like ? or pasien.nm_pasien like ? or resume_pasien_ranap.kd_dokter like ? or "+
                    "dokter.nm_dokter like ? or resume_pasien_ranap.keadaan like ? or resume_pasien_ranap.kd_diagnosa_utama like ? or resume_pasien_ranap.diagnosa_utama like ? or "+
                    "resume_pasien_ranap.prosedur_utama like ? or reg_periksa.no_rawat like ? or resume_pasien_ranap.kd_prosedur_utama like ?)")+
                    "order by reg_periksa.tgl_registrasi,reg_periksa.status_lanjut");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                if(!TCari.getText().trim().equals("")){
                    ps.setString(3,"%"+TCari.getText()+"%");
                    ps.setString(4,"%"+TCari.getText()+"%");
                    ps.setString(5,"%"+TCari.getText()+"%");
                    ps.setString(6,"%"+TCari.getText()+"%");
                    ps.setString(7,"%"+TCari.getText()+"%");
                    ps.setString(8,"%"+TCari.getText()+"%");
                    ps.setString(9,"%"+TCari.getText()+"%");
                    ps.setString(10,"%"+TCari.getText()+"%");
                    ps.setString(11,"%"+TCari.getText()+"%");
                    ps.setString(12,"%"+TCari.getText()+"%");
                }

                rs=ps.executeQuery();
                while(rs.next()){
                    kodekamar="";namakamar="";tglkeluar="";jamkeluar="";
                    ps2=koneksi.prepareStatement(
                        "select if(kamar_inap.tgl_keluar='0000-00-00',current_date(),kamar_inap.tgl_keluar) as tgl_keluar,"+
                        "if(kamar_inap.jam_keluar='00:00:00',current_time(),kamar_inap.jam_keluar) as jam_keluar,kamar_inap.kd_kamar,bangsal.nm_bangsal "+
                        "from kamar_inap inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                        "where kamar_inap.no_rawat=? order by kamar_inap.tgl_keluar desc,kamar_inap.jam_keluar desc limit 1");
                    try {
                        ps2.setString(1,rs.getString("no_rawat"));
                        rs2=ps2.executeQuery();
                        if(rs2.next()){
                            kodekamar=rs2.getString("kd_kamar");
                            namakamar=rs2.getString("nm_bangsal");
                            tglkeluar=rs2.getString("tgl_keluar");
                            jamkeluar=rs2.getString("jam_keluar");
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                        if(ps2!=null){
                            ps2.close();
                        }
                    }
                    
                    tabMode.addRow(new Object[]{
                        rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),
                        rs.getString("kodepengirim"),rs.getString("pengirim"),kodekamar,namakamar,rs.getString("tgl_registrasi"),rs.getString("jam_reg"),tglkeluar,
                        jamkeluar,rs.getString("diagnosa_awal"),rs.getString("alasan"),rs.getString("keluhan_utama"),rs.getString("pemeriksaan_fisik"),
                        rs.getString("jalannya_penyakit"),rs.getString("pemeriksaan_penunjang"),rs.getString("hasil_laborat"),rs.getString("tindakan_dan_operasi"),
                        rs.getString("obat_di_rs"),rs.getString("diagnosa_utama"),rs.getString("kd_diagnosa_utama"),rs.getString("diagnosa_sekunder"),
                        rs.getString("kd_diagnosa_sekunder"),rs.getString("diagnosa_sekunder2"),rs.getString("kd_diagnosa_sekunder2"),rs.getString("diagnosa_sekunder3"),
                        rs.getString("kd_diagnosa_sekunder3"),rs.getString("diagnosa_sekunder4"),rs.getString("kd_diagnosa_sekunder4"),rs.getString("prosedur_utama"),
                        rs.getString("kd_prosedur_utama"),rs.getString("prosedur_sekunder"),rs.getString("kd_prosedur_sekunder"),rs.getString("prosedur_sekunder2"),
                        rs.getString("kd_prosedur_sekunder2"),rs.getString("prosedur_sekunder3"),rs.getString("kd_prosedur_sekunder3"),rs.getString("alergi"),
                        rs.getString("diet"),rs.getString("lab_belum"),rs.getString("edukasi"),rs.getString("keadaan"),rs.getString("ket_keadaan"),
                        rs.getString("cara_keluar"),rs.getString("ket_keluar"),rs.getString("dilanjutkan"),rs.getString("ket_dilanjutkan"),rs.getString("kontrol"),
                        rs.getString("obat_pulang"),rs.getString("kd_pj"),rs.getString("png_jawab")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        LCount.setText(""+tabMode.getRowCount());
    }

    public void emptTeks() {
        Alasan.setText("");
        KeluhanUtama.setText("");
        PemeriksaanFisik.setText("");
        JalannyaPenyakit.setText("");
        PemeriksaanRad.setText("");
        HasilLaborat.setText("");
        TindakanSelamaDiRS.setText("");
        ObatSelamaDiRS.setText("");
        DiagnosaUtama.setText("");
        DiagnosaSekunder1.setText("");
        DiagnosaSekunder2.setText("");
        DiagnosaSekunder3.setText("");
        DiagnosaSekunder4.setText("");
        ProsedurUtama.setText("");
        ProsedurSekunder1.setText("");
        ProsedurSekunder2.setText("");
        ProsedurSekunder3.setText("");
        KodeDiagnosaUtama.setText("");
        KodeDiagnosaSekunder1.setText("");
        KodeDiagnosaSekunder2.setText("");
        KodeDiagnosaSekunder3.setText("");
        KodeDiagnosaSekunder4.setText("");
        KodeProsedurUtama.setText("");
        KodeProsedurSekunder1.setText("");
        KodeProsedurSekunder2.setText("");
        KodeProsedurSekunder3.setText("");
        Alergi.setText("");
        Diet.setText("");
        LabBelum.setText("");
        Edukasi.setText("");
        KetKeadaanPulang.setText("");
        KetKeluar.setText("");
        KetDilanjutkan.setText("");
        ObatPulang.setText("");
        Keadaan.setSelectedIndex(0);
        CaraKeluar.setSelectedIndex(0);
        DIlanjutkan.setSelectedIndex(0);
        Kontrol.setDate(new Date());
        DiagnosaAwal.requestFocus();
    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString());  
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());  
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());  
            KodeDokterPengirim.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());  
            NamaDokterPengirim.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());  
            KdRuang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());  
            NmRuang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());  
            Masuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString());  
            JamMasuk.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());  
            Keluar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());  
            JamKeluar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());  
            DiagnosaAwal.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());  
            Alasan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());  
            KeluhanUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());  
            PemeriksaanFisik.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());  
            JalannyaPenyakit.setText(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());  
            PemeriksaanRad.setText(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString());  
            HasilLaborat.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());  
            TindakanSelamaDiRS.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());  
            ObatSelamaDiRS.setText(tbObat.getValueAt(tbObat.getSelectedRow(),21).toString());  
            DiagnosaUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),22).toString());  
            KodeDiagnosaUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),23).toString());  
            DiagnosaSekunder1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),24).toString());  
            KodeDiagnosaSekunder1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),25).toString());  
            DiagnosaSekunder2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),26).toString());  
            KodeDiagnosaSekunder2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),27).toString());  
            DiagnosaSekunder3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),28).toString());  
            KodeDiagnosaSekunder3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),29).toString());  
            DiagnosaSekunder4.setText(tbObat.getValueAt(tbObat.getSelectedRow(),30).toString());  
            KodeDiagnosaSekunder4.setText(tbObat.getValueAt(tbObat.getSelectedRow(),31).toString());  
            ProsedurUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),32).toString());  
            KodeProsedurUtama.setText(tbObat.getValueAt(tbObat.getSelectedRow(),33).toString());  
            ProsedurSekunder1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),34).toString());  
            KodeProsedurSekunder1.setText(tbObat.getValueAt(tbObat.getSelectedRow(),35).toString());  
            ProsedurSekunder2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),36).toString());  
            KodeProsedurSekunder2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),37).toString());  
            ProsedurSekunder3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),38).toString());  
            KodeProsedurSekunder3.setText(tbObat.getValueAt(tbObat.getSelectedRow(),39).toString());  
            Alergi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),40).toString());  
            Diet.setText(tbObat.getValueAt(tbObat.getSelectedRow(),41).toString());  
            LabBelum.setText(tbObat.getValueAt(tbObat.getSelectedRow(),42).toString());  
            Edukasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),43).toString());  
            Keadaan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),44).toString());  
            KetKeadaanPulang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),45).toString()); 
            CaraKeluar.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),46).toString());  
            KetKeluar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),47).toString());  
            DIlanjutkan.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),48).toString());  
            KetDilanjutkan.setText(tbObat.getValueAt(tbObat.getSelectedRow(),49).toString());   
            ObatPulang.setText(tbObat.getValueAt(tbObat.getSelectedRow(),51).toString());     
            KdPj.setText(tbObat.getValueAt(tbObat.getSelectedRow(),52).toString());     
            CaraBayar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),53).toString());  
            Valid.SetTgl2(Kontrol,tbObat.getValueAt(tbObat.getSelectedRow(),50).toString());
        }
    }
    
    private void isRawat() {
        try {
            ps=koneksi.prepareStatement(
                    "select reg_periksa.no_rkm_medis,concat(pasien.nm_pasien,' (',reg_periksa.umurdaftar,' ',reg_periksa.sttsumur,')') as nm_pasien,"+
                    "reg_periksa.tgl_registrasi,reg_periksa.jam_reg,reg_periksa.kd_dokter,dokter.nm_dokter,reg_periksa.kd_pj,penjab.png_jawab,"+
                    "if(kamar_inap.tgl_keluar='0000-00-00',current_date(),kamar_inap.tgl_keluar) as tgl_keluar,"+
                    "if(kamar_inap.jam_keluar='00:00:00',current_time(),kamar_inap.jam_keluar) as jam_keluar,"+
                    "kamar_inap.diagnosa_awal,kamar_inap.kd_kamar,bangsal.nm_bangsal from reg_periksa "+
                    "inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis "+
                    "inner join dokter on dokter.kd_dokter=reg_periksa.kd_dokter "+
                    "inner join penjab on penjab.kd_pj=reg_periksa.kd_pj "+
                    "inner join kamar_inap on kamar_inap.no_rawat=reg_periksa.no_rawat "+
                    "inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar "+
                    "inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal "+
                    "where reg_periksa.no_rawat=? order by kamar_inap.tgl_keluar desc,kamar_inap.jam_keluar desc limit 1");
            try {
                ps.setString(1,TNoRw.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    DTPCari1.setDate(rs.getDate("tgl_registrasi"));
                    TNoRM.setText(rs.getString("no_rkm_medis"));
                    TPasien.setText(rs.getString("nm_pasien"));
                    Masuk.setText(rs.getString("tgl_registrasi"));
                    JamMasuk.setText(rs.getString("jam_reg"));
                    Keluar.setText(rs.getString("tgl_keluar"));
                    JamKeluar.setText(rs.getString("jam_keluar"));
                    DiagnosaAwal.setText(rs.getString("diagnosa_awal"));
                    KdPj.setText(rs.getString("kd_pj"));
                    CaraBayar.setText(rs.getString("png_jawab"));
                    KdRuang.setText(rs.getString("kd_kamar"));
                    NmRuang.setText(rs.getString("nm_bangsal"));
                    KodeDokterPengirim.setText(rs.getString("kd_dokter"));
                    NamaDokterPengirim.setText(rs.getString("nm_dokter"));
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        DTPCari2.setDate(tgl2);    
        isRawat();              
        ChkInput.setSelected(true);
        isForm();
        CaraKeluar.requestFocus();
        try {
            ps=koneksi.prepareStatement(
                    "select diagnosa_pasien.kd_penyakit,penyakit.nm_penyakit,diagnosa_pasien.prioritas "+
                    "from diagnosa_pasien inner join penyakit on diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit "+
                    "where diagnosa_pasien.no_rawat=? and diagnosa_pasien.status='Ranap' order by diagnosa_pasien.prioritas ");
            try {
                ps.setString(1,norwt);
                rs=ps.executeQuery();
                while(rs.next()){
                    if(rs.getInt("prioritas")==1){
                        KodeDiagnosaUtama.setText(rs.getString("kd_penyakit"));
                        DiagnosaUtama.setText(rs.getString("nm_penyakit"));
                    }
                    
                    if(rs.getInt("prioritas")==2){
                        KodeDiagnosaSekunder1.setText(rs.getString("kd_penyakit"));
                        DiagnosaSekunder1.setText(rs.getString("nm_penyakit"));
                    }
                    
                    if(rs.getInt("prioritas")==3){
                        KodeDiagnosaSekunder2.setText(rs.getString("kd_penyakit"));
                        DiagnosaSekunder2.setText(rs.getString("nm_penyakit"));
                    }
                    
                    if(rs.getInt("prioritas")==4){
                        KodeDiagnosaSekunder3.setText(rs.getString("kd_penyakit"));
                        DiagnosaSekunder3.setText(rs.getString("nm_penyakit"));
                    }
                    
                    if(rs.getInt("prioritas")==5){
                        KodeDiagnosaSekunder4.setText(rs.getString("kd_penyakit"));
                        DiagnosaSekunder4.setText(rs.getString("nm_penyakit"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        } 
        
        try {
            ps=koneksi.prepareStatement(
                    "select prosedur_pasien.kode,icd9.deskripsi_panjang, prosedur_pasien.prioritas "+
                    "from prosedur_pasien inner join icd9 on prosedur_pasien.kode=icd9.kode "+
                    "where prosedur_pasien.no_rawat=? order by prosedur_pasien.prioritas ");
            try {
                ps.setString(1,norwt);
                rs=ps.executeQuery();
                while(rs.next()){
                    if(rs.getInt("prioritas")==1){
                        KodeProsedurUtama.setText(rs.getString("kode"));
                        ProsedurUtama.setText(rs.getString("deskripsi_panjang"));
                    }
                    
                    if(rs.getInt("prioritas")==2){
                        KodeProsedurSekunder1.setText(rs.getString("kode"));
                        ProsedurSekunder1.setText(rs.getString("deskripsi_panjang"));
                    }
                    
                    if(rs.getInt("prioritas")==3){
                        KodeProsedurSekunder2.setText(rs.getString("kode"));
                        ProsedurSekunder2.setText(rs.getString("deskripsi_panjang"));
                    }
                    
                    if(rs.getInt("prioritas")==4){
                        KodeProsedurSekunder3.setText(rs.getString("kode"));
                        ProsedurSekunder3.setText(rs.getString("deskripsi_panjang"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        } 
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,this.getHeight()-122));
            scrollInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            scrollInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getdata_resume_pasien());
        BtnHapus.setEnabled(akses.getdata_resume_pasien());
        BtnEdit.setEnabled(akses.getdata_resume_pasien());
        BtnPrint.setEnabled(akses.getdata_resume_pasien()); 
        MnInputDiagnosa.setEnabled(akses.getdiagnosa_pasien());   
        ppBerkasDigital.setEnabled(akses.getberkas_digital_perawatan());    
        if(akses.getjml2()>=1){
            KodeDokter.setEditable(false);
            BtnDokter.setEnabled(false);
            KodeDokter.setText(akses.getkode());
            NamaDokter.setText(dokter.tampil3(KodeDokter.getText()));
            if(NamaDokter.getText().equals("")){
                KodeDokter.setText("");
                JOptionPane.showMessageDialog(null,"User login bukan dokter...!!");
            }
        }            
    }

    private void ganti() {
        if(Sequel.mengedittf("resume_pasien_ranap","no_rawat=?","no_rawat=?,kd_dokter=?,diagnosa_awal=?,alasan=?,keluhan_utama=?,pemeriksaan_fisik=?,jalannya_penyakit=?,pemeriksaan_penunjang=?,"+
                "hasil_laborat=?,tindakan_dan_operasi=?,obat_di_rs=?,diagnosa_utama=?,kd_diagnosa_utama=?,diagnosa_sekunder=?,kd_diagnosa_sekunder=?,diagnosa_sekunder2=?,kd_diagnosa_sekunder2=?,"+
                "diagnosa_sekunder3=?,kd_diagnosa_sekunder3=?,diagnosa_sekunder4=?,kd_diagnosa_sekunder4=?,prosedur_utama=?,kd_prosedur_utama=?,prosedur_sekunder=?,kd_prosedur_sekunder=?,"+
                "prosedur_sekunder2=?,kd_prosedur_sekunder2=?,prosedur_sekunder3=?,kd_prosedur_sekunder3=?,alergi=?,diet=?,lab_belum=?,edukasi=?,cara_keluar=?,ket_keluar=?,keadaan=?,"+
                "ket_keadaan=?,dilanjutkan=?,ket_dilanjutkan=?,kontrol=?,obat_pulang=?",42,new String[]{
                TNoRw.getText(),KodeDokter.getText(),DiagnosaAwal.getText(),Alasan.getText(),KeluhanUtama.getText(),PemeriksaanFisik.getText(),JalannyaPenyakit.getText(),
                PemeriksaanRad.getText(),HasilLaborat.getText(),TindakanSelamaDiRS.getText(),ObatSelamaDiRS.getText(),DiagnosaUtama.getText(),KodeDiagnosaUtama.getText(),
                DiagnosaSekunder1.getText(),KodeDiagnosaSekunder1.getText(),DiagnosaSekunder2.getText(),KodeDiagnosaSekunder2.getText(),DiagnosaSekunder3.getText(),
                KodeDiagnosaSekunder3.getText(),DiagnosaSekunder4.getText(),KodeDiagnosaSekunder4.getText(),ProsedurUtama.getText(),KodeProsedurUtama.getText(),
                ProsedurSekunder1.getText(),KodeProsedurSekunder1.getText(),ProsedurSekunder2.getText(),KodeProsedurSekunder2.getText(),ProsedurSekunder3.getText(), 
                KodeProsedurSekunder3.getText(),Alergi.getText(),Diet.getText(),LabBelum.getText(),Edukasi.getText(),CaraKeluar.getSelectedItem().toString(),KetKeluar.getText(),
                Keadaan.getSelectedItem().toString(),KetKeadaanPulang.getText(),DIlanjutkan.getSelectedItem().toString(),KetDilanjutkan.getText(),
                Valid.SetTgl(Kontrol.getSelectedItem()+"")+" "+Kontrol.getSelectedItem().toString().substring(11,19),ObatPulang.getText(),
                tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
                })==true){
                   tbObat.setValueAt(TNoRw.getText(),tbObat.getSelectedRow(),0);
                   tbObat.setValueAt(TNoRM.getText(),tbObat.getSelectedRow(),1);
                   tbObat.setValueAt(TPasien.getText(),tbObat.getSelectedRow(),2);
                   tbObat.setValueAt(KodeDokter.getText(),tbObat.getSelectedRow(),3);
                   tbObat.setValueAt(NamaDokter.getText(),tbObat.getSelectedRow(),4);
                   tbObat.setValueAt(KodeDokterPengirim.getText(),tbObat.getSelectedRow(),5);
                   tbObat.setValueAt(NamaDokterPengirim.getText(),tbObat.getSelectedRow(),6);
                   tbObat.setValueAt(KdRuang.getText(),tbObat.getSelectedRow(),7);
                   tbObat.setValueAt(NmRuang.getText(),tbObat.getSelectedRow(),8);
                   tbObat.setValueAt(Masuk.getText(),tbObat.getSelectedRow(),9);
                   tbObat.setValueAt(JamMasuk.getText(),tbObat.getSelectedRow(),10);
                   tbObat.setValueAt(Keluar.getText(),tbObat.getSelectedRow(),11);
                   tbObat.setValueAt(JamKeluar.getText(),tbObat.getSelectedRow(),12);
                   tbObat.setValueAt(DiagnosaAwal.getText(),tbObat.getSelectedRow(),13);
                   tbObat.setValueAt(Alasan.getText(),tbObat.getSelectedRow(),14);
                   tbObat.setValueAt(KeluhanUtama.getText(),tbObat.getSelectedRow(),15);
                   tbObat.setValueAt(PemeriksaanFisik.getText(),tbObat.getSelectedRow(),16);
                   tbObat.setValueAt(JalannyaPenyakit.getText(),tbObat.getSelectedRow(),17);
                   tbObat.setValueAt(PemeriksaanRad.getText(),tbObat.getSelectedRow(),18);
                   tbObat.setValueAt(HasilLaborat.getText(),tbObat.getSelectedRow(),19);
                   tbObat.setValueAt(TindakanSelamaDiRS.getText(),tbObat.getSelectedRow(),20);
                   tbObat.setValueAt(ObatSelamaDiRS.getText(),tbObat.getSelectedRow(),21);
                   tbObat.setValueAt(DiagnosaUtama.getText(),tbObat.getSelectedRow(),22);
                   tbObat.setValueAt(KodeDiagnosaUtama.getText(),tbObat.getSelectedRow(),23);
                   tbObat.setValueAt(DiagnosaSekunder1.getText(),tbObat.getSelectedRow(),24);
                   tbObat.setValueAt(KodeDiagnosaSekunder1.getText(),tbObat.getSelectedRow(),25);
                   tbObat.setValueAt(DiagnosaSekunder2.getText(),tbObat.getSelectedRow(),26);
                   tbObat.setValueAt(KodeDiagnosaSekunder2.getText(),tbObat.getSelectedRow(),27);
                   tbObat.setValueAt(DiagnosaSekunder3.getText(),tbObat.getSelectedRow(),28);
                   tbObat.setValueAt(KodeDiagnosaSekunder3.getText(),tbObat.getSelectedRow(),29);
                   tbObat.setValueAt(DiagnosaSekunder4.getText(),tbObat.getSelectedRow(),30);
                   tbObat.setValueAt(KodeDiagnosaSekunder4.getText(),tbObat.getSelectedRow(),31);
                   tbObat.setValueAt(ProsedurUtama.getText(),tbObat.getSelectedRow(),32);
                   tbObat.setValueAt(KodeProsedurUtama.getText(),tbObat.getSelectedRow(),33);
                   tbObat.setValueAt(ProsedurSekunder1.getText(),tbObat.getSelectedRow(),34);
                   tbObat.setValueAt(KodeProsedurSekunder1.getText(),tbObat.getSelectedRow(),35);
                   tbObat.setValueAt(ProsedurSekunder2.getText(),tbObat.getSelectedRow(),36);
                   tbObat.setValueAt(KodeProsedurSekunder2.getText(),tbObat.getSelectedRow(),37);
                   tbObat.setValueAt(ProsedurSekunder3.getText(),tbObat.getSelectedRow(),38);
                   tbObat.setValueAt(KodeProsedurSekunder3.getText(),tbObat.getSelectedRow(),39);
                   tbObat.setValueAt(Alergi.getText(),tbObat.getSelectedRow(),40);
                   tbObat.setValueAt(Diet.getText(),tbObat.getSelectedRow(),41);
                   tbObat.setValueAt(LabBelum.getText(),tbObat.getSelectedRow(),42);
                   tbObat.setValueAt(Edukasi.getText(),tbObat.getSelectedRow(),43);
                   tbObat.setValueAt(Keadaan.getSelectedItem().toString(),tbObat.getSelectedRow(),44);
                   tbObat.setValueAt(KetKeadaanPulang.getText(),tbObat.getSelectedRow(),45);
                   tbObat.setValueAt(CaraKeluar.getSelectedItem().toString(),tbObat.getSelectedRow(),46);
                   tbObat.setValueAt(KetKeluar.getText(),tbObat.getSelectedRow(),47);
                   tbObat.setValueAt(DIlanjutkan.getSelectedItem().toString(),tbObat.getSelectedRow(),48);
                   tbObat.setValueAt(KetDilanjutkan.getText(),tbObat.getSelectedRow(),49);
                   tbObat.setValueAt(Valid.SetTgl(Kontrol.getSelectedItem()+"")+" "+Kontrol.getSelectedItem().toString().substring(11,19),tbObat.getSelectedRow(),50);
                   tbObat.setValueAt(ObatPulang.getText(),tbObat.getSelectedRow(),51);
                   tbObat.setValueAt(KdPj.getText(),tbObat.getSelectedRow(),52);
                   tbObat.setValueAt(CaraBayar.getText(),tbObat.getSelectedRow(),53);
                   emptTeks();
            }
    }

    private void hapus() {
        if(Sequel.queryu2tf("delete from resume_pasien_ranap where no_rawat=?",1,new String[]{
            tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
        })==true){
            tabMode.removeRow(tbObat.getSelectedRow());
            LCount.setText(""+tabMode.getRowCount());
            emptTeks();
        }else{
            JOptionPane.showMessageDialog(null,"Gagal menghapus..!!");
        }
    }

    
}
