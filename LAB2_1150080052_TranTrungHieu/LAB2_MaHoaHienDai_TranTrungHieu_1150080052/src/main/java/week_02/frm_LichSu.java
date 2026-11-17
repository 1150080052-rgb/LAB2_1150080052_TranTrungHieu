package week_02;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class frm_LichSu extends JFrame {

    private final JTable tbl_history = new JTable(HistoryDao.emptyModel());
    private final JButton btn_refresh = new JButton("Tải lại");
    private final JLabel lbl_status = new JLabel("Đang tải...");

    public frm_LichSu() {
        setTitle("Lịch sử thao tác (SQL Server) - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("LỊCH SỬ THAO TÁC - BẢNG DBO.CRYPTO_HISTORY");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        p.add(title, BorderLayout.NORTH);

        JScrollPane sp = new JScrollPane(tbl_history);
        sp.setPreferredSize(new Dimension(760, 260));
        p.add(sp, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        south.add(btn_refresh);
        south.add(lbl_status);
        p.add(south, BorderLayout.SOUTH);

        setContentPane(p);
        btn_refresh.addActionListener(e -> loadData());
        pack();
        setLocationRelativeTo(null);
        loadData();
    }

    private void loadData() {
        try {
            tbl_history.setModel(HistoryDao.loadLatest(50));
            lbl_status.setText("Đã tải " + tbl_history.getRowCount() + " dòng mới nhất từ dbo.crypto_history.");
        } catch (java.sql.SQLException ex) {
            lbl_status.setText("Không đọc được lịch sử: " + ex.getMessage());
        }
    }
        public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new frm_LichSu().setVisible(true));
    }
}