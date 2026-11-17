# LAB 2 - Mã hóa đối xứng hiện đại

- Họ tên: Trần Trung Hiếu
- MSSV: 1150080052
- Video làm bài: https://youtu.be/nW5l4RlmG8Q

## Nội dung
Chương trình mã hóa/giải mã bằng DES, TripleDES, AES-GCM (có đăng ký/đăng nhập), RC4, Twofish, AES-CTR (Java Swing) và AES-CBC (Python). Lịch sử thao tác và tài khoản lưu trong SQL Server.

## Cách chạy
**Java (NetBeans + Maven)**
1. Mở project `LAB2_MaHoaHienDai_TranTrungHieu_1150080052` bằng NetBeans (JDK 17 trở lên).
2. Clean and Build để Maven tải thư viện.
3. Chạy từng form bằng Run File (Shift+F6), ví dụ `frm_DES`, `frm_AES`.

**SQL Server**
1. Chạy `01_Tao_CSDL.sql`, rồi `02_Tao_Tai_Khoan_SQL.sql` trong SSMS.
2. Kết nối: `127.0.0.1`, cổng `14333`, database `LAB2_MaHoaHienDai`, user `lab2_user`. Mật khẩu nhập trên form, không lưu trong mã nguồn.

**Python**
```
py -m pip install cryptography
py aes_cbc_gui.py --self-test
py aes_cbc_gui.py
```
