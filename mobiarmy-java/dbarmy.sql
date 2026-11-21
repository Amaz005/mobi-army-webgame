-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th7 13, 2026 lúc 03:41 PM
-- Phiên bản máy phục vụ: 5.7.34
-- Phiên bản PHP: 8.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `mobiarmy`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(50) NOT NULL,
  `xu` int(10) NOT NULL DEFAULT 1000,
  `nvBuy` varchar(50) NOT NULL DEFAULT '[1, 1, 1, 0, 0, 0, 0, 0, 0]',
  `online` tinyint(4) NOT NULL DEFAULT 0,
  `itembag` varchar(500) NOT NULL DEFAULT '[[0,99],[1,99],[2,99],[3,99],[4,99],[5,99],[6,99],[7,99],[8,99],[9,99],[10,99],[11,99],[12,99],[13,99],[14,99],[15,99],[16,99],[17,99],[18,99],[19,99],[20,99],[21,99]]',
  `superadmin` tinyint(4) NOT NULL DEFAULT 0,
  `banbe` varchar(500) NOT NULL DEFAULT '[3]',
  `nhanvat` int(11) NOT NULL DEFAULT 0,
  `exp` int(11) NOT NULL DEFAULT 0,
  `killdead` varchar(500) DEFAULT '[0, 0]',
  `thongtin` varchar(500) DEFAULT '[]'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `account`
--

INSERT INTO `account` 
(`id`, `username`, `password`, `xu`, `nvBuy`, `online`, `itembag`, `superadmin`, `banbe`, `nhanvat`, `exp`, `killdead`, `thongtin`) VALUES
(1, 'a', '1', 1000000, '[1, 1, 1, 0, 0, 0, 0, 0, 0]', 0, '[[0,99],[1,99],[2,99],[3,99],[4,99],[5,99],[6,0],[7,0],[8,0],[9,0],[10,0],[11,0],[12,0],[13,0],[14,0],[15,0],[16,0],[17,0],[18,0],[19,0],[20,0],[21,0]]', 0, '[3]', 9, 0, '[0, 0]', '[]'),
(2, 'a', '1', 1000000, '[1, 1, 1, 0, 0, 0, 0, 0, 0]', 0, '[[0,99],[1,99],[2,99],[3,99],[4,99],[5,99],[6,99],[7,99],[8,99],[9,99],[10,99],[11,99],[12,99],[13,99],[14,99],[15,99],[16,99],[17,99],[18,99],[19,99],[20,99],[21,99]]', 1, '[3]', 1, 50000, '[0, 0]', '[]'),
(3, 'nguoiduatin', '3', 1000, '[1, 1, 1, 0, 0, 0, 0, 0, 0]', 0, '[[0,99],[1,99],[2,0],[3,0],[4,0],[5,0],[6,0],[7,0],[8,0],[9,0],[10,0],[11,0],[12,0],[13,0],[14,0],[15,0],[16,0],[17,0],[18,0],[19,0],[20,0],[21,0]]', 1, '[3]', 1, 500000, '[0, 0]', '[]');
-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `item`
--

CREATE TABLE `item` (
  `item_id` tinyint(3) NOT NULL,
  `name` varchar(15) COLLATE utf8_bin NOT NULL,
  `xu` smallint(4) NOT NULL,
  `luong` smallint(3) NOT NULL,
  `nDcMang` smallint(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Đang đổ dữ liệu cho bảng `item`
--

INSERT INTO `item` (`item_id`, `name`, `xu`, `luong`, `nDcMang`) VALUES
(1, 'HP', 0, -1, 2),
(2, 'Teleport', 0, -1, 2),
(3, 'Bắn x2', 200, -1, 1),
(4, 'Di chuyển x2', 70, -1, 1),
(5, 'Tàng hình', 100, -1, 1),
(6, 'Ngưng gió', 100, -1, 1),
(7, 'Bom phá đất', 200, -1, 1),
(8, 'Lựu đạn', 200, -1, 1),
(9, 'Bom hủy diệt', 500, -1, 1),
(10, 'Tơ nhện', 200, -1, 1),
(11, 'HP đồng đội', 700, -1, 1),
(12, 'Đạn trái phá', 500, -1, 1),
(13, 'Túi đựng item 1', 100, -1, -1),
(14, 'Túi đựng item 2', 200, -1, -1),
(15, 'Đạn lazer', 500, -1, 1),
(16, 'Vòi rồng', 200, -1, 1),
(17, 'Chuột gắn bom', 400, -1, 1),
(18, 'Tên lửa', 300, -1, 1),
(19, 'Đạn xuyên đất', 300, -1, 1),
(20, 'Sao băng', 400, -1, 1),
(21, 'Mưa đạn', 500, -1, 1),
(22, 'Khoang đất', 150, -1, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `map`
--

-- Thay thế TOÀN BỘ đoạn `map` table cũ trong dbarmy.sql/armydb.sql bằng đoạn này.
-- Đã thêm cột `is_boss` (0 = map thường, 1 = map boss) — không phụ thuộc khoảng id nữa,
-- bạn thêm map thường mới id nào cũng được sau này mà không lo đụng độ với map boss.
-- Cú pháp CREATE TABLE/INSERT dưới đây chạy được cả trên SQLite lẫn MySQL thật.

DROP TABLE IF EXISTS `map`;

CREATE TABLE `map` (
  `id` INTEGER PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `file` VARCHAR(100) NOT NULL,
  `bg` SMALLINT NOT NULL DEFAULT 0,
  `mapAddY` SMALLINT NOT NULL DEFAULT 0,
  `bullEffShower` SMALLINT NOT NULL DEFAULT 0,
  `inWaterAddY` SMALLINT NOT NULL DEFAULT 0,
  `cl2AddY` SMALLINT NOT NULL DEFAULT 0,
  `is_boss` TINYINT(1) NOT NULL DEFAULT 0
);

INSERT INTO `map` (`id`, `name`, `file`, `bg`, `mapAddY`, `bullEffShower`, `inWaterAddY`, `cl2AddY`, `is_boss`) VALUES
(1, 'Cây cầu băng', 'bangtuyet_caycaubang.map', 7, -10, -50, -1, 0, 0),
(2, 'Sống sót', 'bangtuyet_songsot.map', 7, -10, -50, -1, 0, 0),
(3, 'Khu năng lượng', 'bencang_khunangluong.map', 7, 0, 0, -1, 0, 0),
(4, 'Nhà máy điện', 'bencang_nhamaydien.map', 6, 0, 0, 0, 0, 0),
(5, 'Bãi nước trũng', 'bien_bainuoctrung.map', 8, 80, 180, 0, 0, 0),
(6, 'Đảo rêu', 'bien_daoreu.map', 8, 170, 150, 0, 1, 0),
(7, 'Đảo vòng cung', 'bien_daovongcung.map', 1, 0, 0, 1, 0, 0),
(8, 'Dãy đá ngầm', 'bien_daydangam.map', 0, 0, 0, 0, 0, 0),
(9, 'Thuyền trôi', 'bien_thuyentroi.map', 0, 0, 0, 0, 0, 0),
(10, 'Cầu tre', 'bobien_cautre.map', 0, 0, 0, 0, 0, 0),
(11, 'Đảo dừa', 'bobien_daodua.map', 0, 0, 0, 0, 0, 0),
(12, 'Đảo giun', 'bobien_daogiun.map', 0, 0, 0, 0, 0, 0),
(13, 'Căn cứ thép', 'cancuthep_cancuthep.map', 10, 90, 0, -1, 0, 0),
(14, 'Sân bay', 'cancuthep_sanbay.map', 10, 90, 0, -1, 0, 0),
(15, 'Trụ điện ', 'cancuthep_trudien.map', 10, 0, 0, -1, 0, 0),
(16, 'Cheo leo', 'dinhnui_cheoleo.map', 2, 0, 0, -1, 0, 0),
(17, 'Chơi vơi', 'dinhnui_choivoi.map', 2, 0, 0, -1, 0, 0),
(18, 'Hang tuyết', 'dinhnui_hangtuyet.map', 2, 0, 0, -1, 0, 0),
(19, 'Bãi khai thác', 'dongco_baikhaithac.map', 4, 0, 100, -1, 0, 0),
(20, 'Đồng cỏ', 'dongco_dongco.map', 4, 0, 0, 1, 0, 0),
(21, 'Vách núi', 'dongco_vachnui.map', 4, 0, 100, 1, 2, 0),
(22, 'Vực thẳm chết', 'dongco_vucthamchet.map', 4, 0, 100, 1, 0, 0),
(23, 'Chiến trường', 'hoangmac_chientruong.map', 1, 0, 0, 1, 1, 0),
(24, 'Đảo uông luông', 'hoangmac_daouongluong.map', 1, 0, 0, 1, -1, 0),
(25, 'Đồi cát', 'hoangmac_doicat.map', 1, 0, 0, 1, 0, 0),
(26, 'Khu sụp lún', 'hoangmac_khusuplun.map', 1, 0, 0, 1, 0, 0),
(27, 'Mỏm đá tử thần', 'hoangmac_momdatuthan.map', 1, 0, 0, 1, 0, 0),
(28, 'Bên dưới hố sâu', 'rungchet_benduoihosau.map', 0, 0, 0, 0, 0, 0),
(29, 'Hòn đất', 'rungchet_hondat.map', 12, 170, -50, 0, -1, 0),
(30, 'Khu giao tranh', 'rungchet_khugiaotranh.map', 0, 0, 0, 0, 0, 0),
(31, 'Sống hoặc chết', 'rungchet_songhoackhet.map', 0, 0, 0, 0, 0, 0),
(32, 'Đài tế thần', 'thanhphoco_daitethan.map', 3, 0, 150, -1, 0, 0),
(33, 'Đền Hoang', 'thanhphoco_denhoang.map', 3, 0, 150, -1, 0, 0),
(34, 'Pháo thành', 'thanhphoco_phaothanh.map', 3, 0, 150, -1, 0, 0),
(35, 'Kim tự tháp', 'kimtuthap.map', 1, 140, 0, 1, 1, 0),
(36, 'Cầu vồng băng', 'cauvongbang.map', 7, 0, 0, -1, 0, 0),
(37, 'Đảo mây', 'daomay.map', 14, 0, 0, -1, -1, 0),
(38, 'Thang mây', 'thangmay.map', 14, 0, 0, -1, -1, 0),
(39, 'Không lối thoát', 'khongloithoat.map', 4, 50, -50, -1, 1, 0),
(40, 'Cầu sắt', 'causat.map', 10, -80, -80, 0, 0, 0),
(41, 'Bám trụ', 'bamtru.map', 10, 80, 80, -1, 0, 0),
(42, 'Lưới sắt', 'luoisat.map', 10, 50, 0, -1, 0, 0),
(43, 'Thoát thân', 'thoatthan.map', 6, 90, 0, -1, 0, 0),
(44, 'Thiên đường', 'thienduong.map', 14, 0, 0, -1, -1, 0),
(45, 'Miền đất hứa', 'miendathua.map', 14, 0, 0, 0, -1, 0),
(46, 'Mê cung', 'mecung.map', 13, 0, 0, -1, -1, 0),
(47, 'Hang đá', 'hangda.map', 13, 0, 0, -1, -1, 0),
(48, 'Cây cầu dừa', 'caycaudua.map', 5, 100, 100, 0, 0, 0),
(49, 'Đền tế', 'dente.map', 3, 150, 0, -1, -1, 0),
(50, 'Bong bóng 1', 'bongbong1.map', 14, 0, 0, -1, -1, 0),
(51, 'Bong bóng 2', 'bongbong2.map', 14, 0, 0, -1, -1, 0),
(52, 'Cầu vồng', 'cauvong.map', 14, 0, 0, -1, -1, 0),
(53, 'Ngẫu nhiên', 'randomMap.map', 30, -1, 80, -1, -1, 0),
(83, 'Bom 1', 'boss_bom1.map', 7, -1, -80, -42, 0, 1),
(84, 'Bom 2', 'boss_bom2.map', 7, -1, -80, 1, 0, 1),
(85, 'Thành phố nhện', 'boss_nhenmay.map', 10, -1, 45, -10, 0, 1),
(86, 'Thành phố máy', 'boss_robot.map', 10, -1, 45, -1, -1, 1),
(87, 'Khủng long máy', 'boss_trex.map', 11, -1, -80, 0, 0, 1),
(88, 'Khu vực cấm', 'boss_ufo.map', 12, -1, -50, 200, 2, 1),
(89, 'Đỉnh hi mã lạp sơn', 'boss_ballon.map', 2, -1, -50, -1, -1, 1),
(90, 'Hang nhện', 'boss_hangnhen.map', 13, -1, 80, -1, -1, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvat`
--

CREATE TABLE `nhanvat` (
  `nhanvat_id` tinyint(3) NOT NULL,
  `name` varchar(15) COLLATE utf8_bin NOT NULL,
  `xu` int(10) NOT NULL,
  `luong` smallint(3) NOT NULL,
  `ma_sat_gio` smallint(4) NOT NULL,
  `goc_min` smallint(4) NOT NULL,
  `sat_thuong` smallint(4) NOT NULL,
  `sat_thuong_dan` smallint(4) NOT NULL,
  `so_dan` smallint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Đang đổ dữ liệu cho bảng `nhanvat`
--

INSERT INTO `nhanvat` (`nhanvat_id`, `name`, `xu`, `luong`, `ma_sat_gio`, `goc_min`, `sat_thuong`, `sat_thuong_dan`, `so_dan`) VALUES
(1, 'Gunner', 0, 0, 80, -45, 280, 28, 1),
(2, 'Miss 6', 0, 0, 50, -45, 290, 14, 2),
(3, 'Electician', 0, 0, 80, -45, 300, 10, 3),
(4, 'King Kong', 40000, 32, 40, 30, 400, 10, 4),
(5, 'Rocketer', 30000, 24, 50, 20, 320, 12, 3),
(6, 'Granos', 20000, 16, 30, 30, 310, 7, 5),
(7, 'Chicky', 100000, 80, 20, -45, 450, 23, 2),
(8, 'Tarzan', 50000, 40, 10, 1, 350, 30, 1),
(9, 'Apache', 70000, 64, 30, -45, 450, 11, 4);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `item`
--
ALTER TABLE `item`
  ADD PRIMARY KEY (`item_id`);

--
-- Chỉ mục cho bảng `map`
--
ALTER TABLE `map`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `nhanvat`
--
ALTER TABLE `nhanvat`
  ADD PRIMARY KEY (`nhanvat_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `item`
--
ALTER TABLE `item`
  MODIFY `item_id` tinyint(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT cho bảng `map`
--
ALTER TABLE `map`
  MODIFY `id` tinyint(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT cho bảng `nhanvat`
--
ALTER TABLE `nhanvat`
  MODIFY `nhanvat_id` tinyint(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
