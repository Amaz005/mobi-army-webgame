package com.mobiarmy.server.team;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lớp đọc dữ liệu hình ảnh từ mảng byte đã được đóng gói.
 * Cho phép truy xuất dữ liệu theo tên file.
 */
public final class TeamImageInput {

    private static final Logger LOGGER = Logger.getLogger(TeamImageInput.class.getName());

    private String[] names;
    private int[] dataStarts;
    private int[] dataLengths;
    private byte[] data;

    /**
     * Khởi tạo đối tượng từ dữ liệu byte đầu vào.
     *
     * @param inputData mảng byte chứa dữ liệu đã đóng gói
     */
    public TeamImageInput(byte[] inputData) {
        try (
            ByteArrayInputStream bas = new ByteArrayInputStream(inputData);
            DataInputStream ds = new DataInputStream(bas)
        ) {
            int totalDataLength = 0;
            int numFiles = ds.readUnsignedByte(); // số lượng file

            names = new String[numFiles];
            dataStarts = new int[numFiles];
            dataLengths = new int[numFiles];

            // Đọc metadata cho từng file
            for (int i = 0; i < numFiles; i++) {
                names[i] = readFileName(ds);

                dataStarts[i] = totalDataLength;
                dataLengths[i] = ds.readUnsignedShort();
                totalDataLength += dataLengths[i];
            }

            // Đọc toàn bộ dữ liệu file
            data = new byte[totalDataLength];
            ds.readFully(data);
            TeamImageData.pack(data);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading TeamImageInput data", e);
        }
    }

    /**
     * Đọc tên file từ stream và giải mã bằng XOR.
     */
    private String readFileName(DataInputStream ds) throws IOException {
        int nameLength = ds.readByte();
        byte[] nameData = new byte[nameLength];
        ds.readFully(nameData);
        TeamImageData.pack(nameData);
        return new String(nameData);
    }

    /**
     * Lấy dữ liệu của file theo tên.
     *
     * @param name tên file cần lấy
     * @return mảng byte dữ liệu hoặc null nếu không tìm thấy
     */
    public byte[] getData(String name) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) {
                byte[] fileData = new byte[dataLengths[i]];
                System.arraycopy(data, dataStarts[i], fileData, 0, dataLengths[i]);
                return fileData;
            }
        }
        LOGGER.warning("File \"" + name + "\" not found!");
        return null;
    }
}
