/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50715
 Source Host           : localhost:3306
 Source Schema         : mobiarmy

 Target Server Type    : MySQL
 Target Server Version : 50715
 File Encoding         : 65001

 Date: 28/06/2026 10:37:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` int(11) NOT NULL,
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `xu` int(10) NOT NULL DEFAULT 1000,
  `luong` int(6) NOT NULL DEFAULT 0,
  `online` tinyint(4) NOT NULL DEFAULT 0,
  `itembag` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '[]',
  `superadmin` tinyint(4) NOT NULL DEFAULT 0,
  `banbe` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '[]',
  `nhanvat` int(11) NOT NULL DEFAULT 0,
  `exp` int(11) NOT NULL DEFAULT 1000,
  `killdead` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '[]',
  `thongtin` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '[]',
  `active` tinyint(4) NOT NULL DEFAULT 1,
  `owned_mask` int(11) NULL DEFAULT 7,
  `char_levels` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '[1,1,1,0,0,0,0,0,0]',
  `item_counts` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '[20,20,20,20,20,20,20,20,20]',
  `selected_char` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (1, 'buinhulac', 'a', 1000, 0, 0, '[]', 0, '[]', 0, 1000, '[]', '[]', 1, 7, '[1,1,1,0,0,0,0,0,0]', '[20,20,20,20,20,20,20,20,20]', 0);
INSERT INTO `account` VALUES (2, 'a', 'a', 1000, 0, 0, '[]', 0, '[]', 0, 1000, '[]', '[]', 1, 7, '[1,1,1,0,0,0,0,0,0]', '[20,20,20,20,20,20,20,20,20]', 0);

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `item_id` tinyint(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `xu` smallint(4) NOT NULL,
  `luong` smallint(3) NOT NULL,
  `nDcMang` smallint(3) NOT NULL,
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES (1, 'HP', 0, -1, 2);
INSERT INTO `item` VALUES (2, 'Teleport', 0, -1, 2);
INSERT INTO `item` VALUES (3, 'Bắn x2', 200, -1, 1);
INSERT INTO `item` VALUES (4, 'Di chuyển x2', 70, -1, 1);
INSERT INTO `item` VALUES (5, 'Tàng hình', 100, -1, 1);
INSERT INTO `item` VALUES (6, 'Ngưng gió', 100, -1, 1);
INSERT INTO `item` VALUES (7, 'Bom phá đất', 200, -1, 1);
INSERT INTO `item` VALUES (8, 'Lựu đạn', 200, -1, 1);
INSERT INTO `item` VALUES (9, 'Bom hủy diệt', 500, -1, 1);
INSERT INTO `item` VALUES (10, 'Tơ nhện', 200, -1, 1);
INSERT INTO `item` VALUES (11, 'HP đồng đội', 700, -1, 1);
INSERT INTO `item` VALUES (12, 'Đạn trái phá', 500, -1, 1);
INSERT INTO `item` VALUES (13, 'Túi đựng item 1', 100, -1, -1);
INSERT INTO `item` VALUES (14, 'Túi đựng item 2', 200, -1, -1);
INSERT INTO `item` VALUES (15, 'Túi đựng item 3', 300, -1, -1);
INSERT INTO `item` VALUES (16, 'Túi đựng item 4', 400, -1, -1);
INSERT INTO `item` VALUES (17, 'Đạn lazer', 500, -1, 1);
INSERT INTO `item` VALUES (18, 'Vòi rồng', 200, -1, 1);
INSERT INTO `item` VALUES (19, 'Chuột gắn bom', 400, -1, 1);
INSERT INTO `item` VALUES (20, 'Tên lửa', 300, -1, 1);
INSERT INTO `item` VALUES (21, 'Đạn xuyên đất', 300, -1, 1);
INSERT INTO `item` VALUES (22, 'Sao băng', 400, -1, 1);
INSERT INTO `item` VALUES (23, 'Mưa đạn', 500, -1, 1);
INSERT INTO `item` VALUES (24, 'Khoang đất', 150, -1, 1);
INSERT INTO `item` VALUES (25, 'Tự sát', 600, -1, 1);
INSERT INTO `item` VALUES (26, 'Bom mù', 600, -1, 1);
INSERT INTO `item` VALUES (27, 'Khoang đất 2', 500, -1, 1);
INSERT INTO `item` VALUES (28, 'UFO', 500, -1, 1);
INSERT INTO `item` VALUES (29, 'Đóng băng', 300, -1, 1);
INSERT INTO `item` VALUES (30, 'Khói độc', 700, -1, 1);
INSERT INTO `item` VALUES (31, 'Tơ nhện 2', 600, -1, 1);
INSERT INTO `item` VALUES (32, 'Bom hẹn giờ', 400, -1, 1);
INSERT INTO `item` VALUES (33, 'HP 50%', 500, -1, 1);
INSERT INTO `item` VALUES (34, 'HP 100%', 1000, -1, 1);
INSERT INTO `item` VALUES (35, 'Vô hinh', 500, -1, 1);
INSERT INTO `item` VALUES (36, 'Ma cà rồng', 800, -1, 1);

-- ----------------------------
-- Table structure for map
-- ----------------------------
DROP TABLE IF EXISTS `map`;
CREATE TABLE `map`  (
  `id` tinyint(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `file` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `bg` smallint(5) NOT NULL DEFAULT 0,
  `mapAddY` smallint(5) NOT NULL DEFAULT 0,
  `bullEffShower` smallint(5) NOT NULL DEFAULT 0,
  `inWaterAddY` smallint(5) NOT NULL DEFAULT 0,
  `cl2AddY` smallint(5) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of map
-- ----------------------------
INSERT INTO `map` VALUES (1, 'Cây cầu băng', 'bangtuyet_caycaubang.map', 7, 0, 0, -10, 0);
INSERT INTO `map` VALUES (2, 'Sống sót', 'bangtuyet_songsot.map', 0, -10, -50, -10, 0);
INSERT INTO `map` VALUES (3, 'Khu năng lượng', 'bencang_khunangluong.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (4, 'Nhà máy điện', 'bencang_nhamaydien.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (5, 'Bãi nước trũng', 'bien_bainuoctrung.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (6, 'Đảo rêu', 'bien_daoreu.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (7, 'Đảo vòng cung', 'bien_daovongcung.map', 1, 170, -100, 0, 1);
INSERT INTO `map` VALUES (8, 'Dãy đá ngầm', 'bien_daydangam.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (9, 'Thuyền trôi', 'bien_thuyentroi.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (10, 'Cầu tre', 'bobien_cautre.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (11, 'Đảo dừa', 'bobien_daodua.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (12, 'Đảo giun', 'bobien_daogiun.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (13, 'Căn cứ thép', 'cancuthep_cancuthep.map', 6, 90, 0, 30, 0);
INSERT INTO `map` VALUES (14, 'Sân bay', 'cancuthep_sanbay.map', 6, 90, 0, 0, 0);
INSERT INTO `map` VALUES (15, 'Trụ điện ', 'cancuthep_trudien.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (16, 'Cheo leo', 'dinhnui_cheoleo.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (17, 'Chơi vơi', 'dinhnui_choivoi.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (18, 'Hang tuyết', 'dinhnui_hangtuyet.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (19, 'Bãi khai thác', 'dongco_baikhaithac.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (20, 'Đồng cỏ', 'dongco_dongco.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (21, 'Vách núi', 'dongco_vachnui.map', 4, 100, -100, 0, 2);
INSERT INTO `map` VALUES (22, 'Vực thẳm chết', 'dongco_vucthamchet.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (23, 'Chiến trường', 'hoangmac_chientruong.map', 1, 170, -100, 0, 1);
INSERT INTO `map` VALUES (24, 'Đảo uông luông', 'hoangmac_daouongluong.map', 0, 170, -50, 0, -1);
INSERT INTO `map` VALUES (25, 'Đồi cát', 'hoangmac_doicat.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (26, 'Khu sụp lún', 'hoangmac_khusuplun.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (27, 'Mỏm đá tử thần', 'hoangmac_momdatuthan.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (28, 'Bên dưới hố sâu', 'rungchet_benduoihosau.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (29, 'Hòn đất', 'rungchet_hondat.map', 1, 170, -50, 0, -1);
INSERT INTO `map` VALUES (30, 'Khu giao tranh', 'rungchet_khugiaotranh.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (31, 'Sống hoặc chết', 'rungchet_songhoackhet.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (32, 'Đài tế thần', 'thanhphoco_daitethan.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (33, 'Đền Hoàng', 'thanhphoco_denhoang.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (34, 'Pháo thành', 'thanhphoco_phaothanh.map', 0, 0, 0, 0, 0);
INSERT INTO `map` VALUES (82, 'Ngẫu nhiên', 'randomMap.map', 13, -1, 80, -1, -1);
INSERT INTO `map` VALUES (83, 'Bom 1', 'boss_bom1.map', 7, -1, -80, -42, 0);
INSERT INTO `map` VALUES (84, 'Bom 2', 'boss_bom2.map', 7, -1, -80, 0, 0);
INSERT INTO `map` VALUES (85, 'Thành phố nhện', 'boss_nhenmay.map', 10, -1, 45, -10, 0);
INSERT INTO `map` VALUES (86, 'Thành phố máy', 'boss_robot.map', 10, -1, 45, -1, -1);
INSERT INTO `map` VALUES (87, 'Khủng long máy', 'boss_trex.map', 7, -1, -80, 0, 0);
INSERT INTO `map` VALUES (88, 'Khu vực cấm', 'boss_ufo.map', 12, -1, -50, 200, 2);
INSERT INTO `map` VALUES (89, 'Đỉnh hi mã lạp sơn', 'boss_ballon.map', 2, -1, -50, -1, -1);
INSERT INTO `map` VALUES (90, 'Hang nhện', 'boss_hangnhen.map', 13, -1, 80, -1, -1);

-- ----------------------------
-- Table structure for nhanvat
-- ----------------------------
DROP TABLE IF EXISTS `nhanvat`;
CREATE TABLE `nhanvat`  (
  `nhanvat_id` tinyint(3) NOT NULL AUTO_INCREMENT,
  `name` varchar(15) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `xu` int(10) NOT NULL,
  `luong` smallint(3) NOT NULL,
  `ma_sat_gio` smallint(4) NOT NULL,
  `goc_min` smallint(4) NOT NULL,
  `sat_thuong` smallint(4) NOT NULL,
  `sat_thuong_dan` smallint(4) NOT NULL,
  `so_dan` smallint(4) NOT NULL,
  PRIMARY KEY (`nhanvat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nhanvat
-- ----------------------------
INSERT INTO `nhanvat` VALUES (1, 'Gunner', 0, 0, 80, -45, 280, 28, 1);
INSERT INTO `nhanvat` VALUES (2, 'Miss 6', 0, 0, 50, -45, 290, 14, 2);
INSERT INTO `nhanvat` VALUES (3, 'Electician', 0, 0, 80, -45, 300, 10, 3);
INSERT INTO `nhanvat` VALUES (4, 'King Kong', 40000, 32, 40, 30, 420, 10, 4);
INSERT INTO `nhanvat` VALUES (5, 'Rocketer', 30000, 24, 50, 20, 321, 12, 3);
INSERT INTO `nhanvat` VALUES (6, 'Granos', 20000, 16, 30, 30, 310, 7, 5);
INSERT INTO `nhanvat` VALUES (7, 'Chicky', 100000, 80, 20, -45, 450, 23, 2);
INSERT INTO `nhanvat` VALUES (8, 'Tarzan', 50000, 40, 10, 1, 340, 30, 1);
INSERT INTO `nhanvat` VALUES (9, 'Apache', 70000, 64, 30, -45, 431, 11, 4);
INSERT INTO `nhanvat` VALUES (10, 'Magenta', 120000, 96, 40, 20, 410, 35, 1);

SET FOREIGN_KEY_CHECKS = 1;
