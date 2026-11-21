package com.mobiarmy.server.team;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mobiarmy.server.Util;

/**
 * Lớp TeamImageOutput dùng để đóng gói dữ liệu hình ảnh (tên file + nội dung file)
 * thành một mảng byte với cơ chế mã hóa XOR.
 */
public class TeamImageOutput {

    private static final Logger LOGGER = Logger.getLogger(TeamImageOutput.class.getName());

    private final ArrayList<String> names;
    private final ArrayList<String> files;

    public TeamImageOutput() {
        this.names = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    /**
     * Thêm một file vào danh sách cần đóng gói.
     *
     * @param name tên file
     * @param file đường dẫn file
     */
    public void addFile(String name, String file) {
        this.names.add(name);
        this.files.add(file);
    }

    /**
     * Đóng gói toàn bộ dữ liệu thành mảng byte.
     *
     * @return mảng byte đã đóng gói hoặc null nếu có lỗi
     */
    public byte[] output() {
        try (
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(bas);
            ByteArrayOutputStream dataBuffer = new ByteArrayOutputStream()
        ) {
            ds.writeByte(names.size());

            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);

                // Đóng gói tên file
                ds.writeByte(name.length());
                byte[] nameBytes = name.getBytes();
                TeamImageData.pack(nameBytes);
                ds.write(nameBytes);

                // Đọc dữ liệu file
                byte[] fileBytes = Util.getFile(files.get(i));
                if (fileBytes == null) {
                    throw new IOException("File " + name + " not found!");
                }

                ds.writeShort(fileBytes.length);
                dataBuffer.write(fileBytes);
            }

            // Đóng gói dữ liệu file
            byte[] dataPack = dataBuffer.toByteArray();
            TeamImageData.pack(dataPack);
            ds.write(dataPack);

            return bas.toByteArray();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error while generating TeamImageOutput", e);
            return null;
        }
    }
}
