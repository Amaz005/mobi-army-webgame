package com.mobiarmy.server.team;

/**
 * Lớp thực hiện mã hóa/giải mã dữ liệu bằng thuật toán XOR với một khóa cố định.
 */
public class TeamImageData {

    /**
     * Khóa XOR dùng để mã hóa/giải mã dữ liệu.
     */
    private static final byte[] XOR_KEY = {
        78, 103, 117, 121, 101, 110, 86, 97, 110, 77, 105, 110, 104
    };

    /**
     * Thực hiện mã hóa/giải mã XOR trên mảng byte đầu vào.
     * Phương thức này có tính chất đối xứng: gọi lại lần nữa sẽ trả về dữ liệu gốc.
     *
     * @param data Mảng byte cần mã hóa/giải mã (sẽ bị thay đổi trực tiếp).
     */
    protected static void pack(byte[] data) {
        int keyLength = XOR_KEY.length;
        for (int i = 0; i < data.length; i++) {
            data[i] ^= XOR_KEY[i % keyLength];
        }
    }
}
