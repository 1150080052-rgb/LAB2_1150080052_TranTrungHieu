package week_02;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class frm_KetNoiSQL extends JFrame {

    private final JTextField txt_server = new JTextField("127.0.0.1", 26);
    private final JTextField txt_port = new JTextField("14333", 10);
    private final JTextField txt_database = new JTextField("LAB2_MaHoaHienDai", 26);
    private final JTextField txt_user = new JTextField("lab2_user", 26);
    private final JPasswordField txt_password = new JPasswordField(26);
    private final JButton btn_test = new JButton("Test Connection");
    private final JButton btn_history = new JButton("Xem lịch sử");
    private final JLabel lbl_status = new JLabel("Chưa kết nối.");

    public frm_KetNoiSQL() {
        setTitle("Kết nối Microsoft SQL Server - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUi();
        btn_test.addActionListener(e -> testConnection());
        btn_history.addActionListener(e -> new frm_LichSu().setVisible(true));
        pack();
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("KẾT NỐI MICROSOFT SQL SERVER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        p.add(title, c);
        c.gridwidth = 1;

        addRow(p, c, 1, "Server:", txt_server);
        addRow(p, c, 2, "Port:", txt_port);
        addRow(p, c, 3, "Database:", txt_database);
        addRow(p, c, 4, "User:", txt_user);
        addRow(p, c, 5, "Password:", txt_password);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(btn_test);
        buttons.add(btn_history);
        c.gridx = 1;
        c.gridy = 6;
        p.add(buttons, c);

        lbl_status.setVerticalAlignment(SwingConstants.TOP);
        lbl_status.setPreferredSize(new Dimension(450, 50));
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 2;
        p.add(lbl_status, c);

        setContentPane(p);
    }

    private void addRow(JPanel p, GridBagConstraints c, int y, String text, JComponent field) {
        c.gridx = 0;
        c.gridy = y;
        p.add(new JLabel(text), c);
        c.gridx = 1;
        p.add(field, c);
    }

    private void testConnection() {
        try {
            int port = Integer.parseInt(txt_port.getText().trim());
            DbConfig.set(txt_server.getText().trim(), port, txt_database.getText().trim(),
                    txt_user.getText().trim(), txt_password.getPassword());
            lbl_status.setText("<html>" + DbConfig.test() + "</html>");
        } catch (Exception ex) {
            DbConfig.clear();
            lbl_status.setText("<html>Kết nối thất bại: " + ex.getMessage() + "</html>");
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Không kết nối được SQL Server", JOptionPane.ERROR_MESSAGE);
        }
    }
        public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new frm_KetNoiSQL().setVisible(true));
    }
}
