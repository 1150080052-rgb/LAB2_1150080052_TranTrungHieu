package week_02;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class frm_AES extends JFrame {

    /** Tên người dùng đã Login thành công; null = chưa đăng nhập. */
    private String currentUser = null;

    private final JTextField txt_username = new JTextField("sinhvien01", 22);
    private final JPasswordField txt_password = new JPasswordField(22);
    private final JButton btn_sql = new JButton("Kết nối SQL Server");
    private final JButton btn_register = new JButton("Register");
    private final JButton btn_login = new JButton("Login");
    private final JTextField txt_ticket = new JTextField(36);
    private final JTextArea txt_plaintext = new JTextArea(3, 50);
    private final JTextField txt_key = new JTextField(26);
    private final JTextArea txt_ciphertext = new JTextArea(3, 50);
    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JButton btn_save = new JButton("Save File");
    private final JButton btn_open = new JButton("Open File");
    private final JLabel lbl_status = new JLabel("Sẵn sàng.");

    public frm_AES() {
        setTitle("AES Cipher - Register / Login - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        txt_ticket.setEditable(false);
        buildUi();
        btn_sql.addActionListener(e -> new frm_KetNoiSQL().setVisible(true));
        btn_register.addActionListener(e -> doRegister());
        btn_login.addActionListener(e -> doLogin());
        btn_encrypt.addActionListener(e -> doEncrypt());
        btn_decrypt.addActionListener(e -> doDecrypt());
        btn_save.addActionListener(e -> doSave());
        btn_open.addActionListener(e -> doOpen());
        pack();
        setLocationRelativeTo(null);
    }

    private void buildUi() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.NORTHWEST;

        JLabel title = new JLabel("AES-GCM - ĐĂNG KÝ / ĐĂNG NHẬP (SQL SERVER)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        p.add(title, c);
        c.gridwidth = 1;

        txt_plaintext.setLineWrap(true);
        txt_ciphertext.setLineWrap(true);

        c.gridx = 0;
        c.gridy = 1;
        p.add(new JLabel("Username:"), c);
        c.gridx = 1;
        p.add(txt_username, c);

        JPanel pwRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pwRow.add(txt_password);
        pwRow.add(new JLabel("(ít nhất 8 ký tự)"));
        c.gridx = 0;
        c.gridy = 2;
        p.add(new JLabel("Password:"), c);
        c.gridx = 1;
        p.add(pwRow, c);

        JPanel accRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        accRow.add(btn_sql);
        accRow.add(btn_register);
        accRow.add(btn_login);
        c.gridx = 1;
        c.gridy = 3;
        p.add(accRow, c);

        c.gridx = 0;
        c.gridy = 4;
        p.add(new JLabel("Ticket:"), c);
        c.gridx = 1;
        p.add(txt_ticket, c);

        c.gridx = 0;
        c.gridy = 5;
        p.add(new JLabel("Plaintext:"), c);
        c.gridx = 1;
        p.add(new JScrollPane(txt_plaintext), c);

        JPanel keyRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        keyRow.add(txt_key);
        keyRow.add(new JLabel("Mật khẩu để tạo khóa AES-256"));
        c.gridx = 0;
        c.gridy = 6;
        p.add(new JLabel("Key AES:"), c);
        c.gridx = 1;
        p.add(keyRow, c);

        c.gridx = 0;
        c.gridy = 7;
        p.add(new JLabel("Ciphertext:"), c);
        c.gridx = 1;
        p.add(new JScrollPane(txt_ciphertext), c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttons.add(btn_encrypt);
        buttons.add(btn_decrypt);
        buttons.add(btn_save);
        buttons.add(btn_open);
        c.gridx = 1;
        c.gridy = 8;
        p.add(buttons, c);

        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 2;
        p.add(lbl_status, c);

        setContentPane(p);
    }

    private void doRegister() {
        try {
            String user = txt_username.getText().trim();
            UserDao.register(user, txt_password.getPassword());
            HistoryDao.tryInsert("AES", "REGISTER", user, 0, 0);
            lbl_status.setText("Register thành công: " + user + ". Bấm Login để đăng nhập.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi SQL Server: " + ex.getMessage(),
                    "Lỗi SQL Server", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doLogin() {
        try {
            String user = txt_username.getText().trim();
            if (UserDao.login(user, txt_password.getPassword())) {
                currentUser = user;
                txt_ticket.setText(UserDao.newTicket());
                HistoryDao.tryInsert("AES", "LOGIN", user, 0, 0);
                lbl_status.setText("Login thành công: " + user + ". Có thể dùng Encrypt/Decrypt.");
            } else {
                currentUser = null;
                txt_ticket.setText("");
                JOptionPane.showMessageDialog(this, "Sai username hoặc mật khẩu.",
                        "Login thất bại", JOptionPane.ERROR_MESSAGE);
            }
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi SQL Server: " + ex.getMessage(),
                    "Lỗi SQL Server", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doEncrypt() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Hãy Login trước khi dùng AES.");
            return;
        }
        String plaintext = txt_plaintext.getText();
        if (plaintext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
            return;
        }
        try {
            String ciphertext = AESCipher.encrypt(plaintext, txt_key.getText());
            txt_ciphertext.setText(ciphertext);
            lbl_status.setText("<html>Đã mã hóa AES. " + HistoryDao.tryInsert("AES", "ENCRYPT", currentUser,
                    plaintext.length(), ciphertext.length()) + "</html>");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Mã hóa thất bại:\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doDecrypt() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Hãy Login trước khi dùng AES.");
            return;
        }
        String ciphertext = txt_ciphertext.getText();
        if (ciphertext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Ciphertext trước khi giải mã.");
            return;
        }
        try {
            String plaintext = AESCipher.decrypt(ciphertext, txt_key.getText());
            txt_plaintext.setText(plaintext);
            lbl_status.setText("<html>Đã giải mã AES. " + HistoryDao.tryInsert("AES", "DECRYPT", currentUser,
                    ciphertext.length(), plaintext.length()) + "</html>");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Giải mã thất bại: sai Key hoặc bản mã bị sửa.\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doSave() {
        String path = FileTextIO.saveText(this, txt_ciphertext, "aes_ciphertext.txt");
        if (path != null) {
            lbl_status.setText("Đã lưu tệp: " + path);
        }
    }

    private void doOpen() {
        String path = FileTextIO.openText(this, txt_ciphertext);
        if (path != null) {
            lbl_status.setText("Đã mở tệp: " + path);
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new frm_AES().setVisible(true));
    }
}