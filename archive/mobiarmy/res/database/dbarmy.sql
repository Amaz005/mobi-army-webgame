-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost
-- Thời gian đã tạo: Th10 15, 2025 lúc 02:35 PM
-- Phiên bản máy phục vụ: 5.7.34
-- Phiên bản PHP: 8.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `dbarmy`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(50) NOT NULL,
  `xu` int(10) NOT NULL DEFAULT '1000',
  `luong` int(6) NOT NULL DEFAULT '0',
  `online` tinyint(4) NOT NULL DEFAULT '0',
  `itembag` varchar(500) NOT NULL DEFAULT '[]',
  `superadmin` tinyint(4) NOT NULL DEFAULT '0',
  `banbe` varchar(500) NOT NULL DEFAULT '[]',
  `nhanvat` int(11) NOT NULL DEFAULT '0',
  `exp` int(11) NOT NULL DEFAULT '1000',
  `killdead` varchar(500) DEFAULT '[]',
  `thongtin` varchar(500) DEFAULT '[]',
  `active` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `account`
--

INSERT INTO `account` (`id`, `username`, `password`, `xu`, `luong`, `online`, `itembag`, `superadmin`, `banbe`, `nhanvat`, `exp`, `killdead`, `thongtin`, `active`) VALUES
(1, 'buinhulac', 'a', 1000, 0, 0, '[]', 0, '[]', 0, 1000, '[]', '[]', 1),
(2, 'a', 'a', 1000, 0, 0, '[]', 0, '[]', 0, 1000, '[]', '[]', 1);

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
(15, 'Túi đựng item 3', 300, -1, -1),
(16, 'Túi đựng item 4', 400, -1, -1),
(17, 'Đạn lazer', 500, -1, 1),
(18, 'Vòi rồng', 200, -1, 1),
(19, 'Chuột gắn bom', 400, -1, 1),
(20, 'Tên lửa', 300, -1, 1),
(21, 'Đạn xuyên đất', 300, -1, 1),
(22, 'Sao băng', 400, -1, 1),
(23, 'Mưa đạn', 500, -1, 1),
(24, 'Khoang đất', 150, -1, 1),
(25, 'Tự sát', 600, -1, 1),
(26, 'Bom mù', 600, -1, 1),
(27, 'Khoang đất 2', 500, -1, 1),
(28, 'UFO', 500, -1, 1),
(29, 'Đóng băng', 300, -1, 1),
(30, 'Khói độc', 700, -1, 1),
(31, 'Tơ nhện 2', 600, -1, 1),
(32, 'Bom hẹn giờ', 400, -1, 1),
(33, 'HP 50%', 500, -1, 1),
(34, 'HP 100%', 1000, -1, 1),
(35, 'Vô hinh', 500, -1, 1),
(36, 'Ma cà rồng', 800, -1, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `map`
--

CREATE TABLE `map` (
  `id` tinyint(3) NOT NULL,
  `name` varchar(30) COLLATE utf8_bin NOT NULL,
  `file` varchar(100) COLLATE utf8_bin NOT NULL,
  `bg` smallint(5) NOT NULL DEFAULT '0',
  `mapAddY` smallint(5) NOT NULL DEFAULT '0',
  `bullEffShower` smallint(5) NOT NULL DEFAULT '0',
  `inWaterAddY` smallint(5) NOT NULL DEFAULT '0',
  `cl2AddY` smallint(5) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Đang đổ dữ liệu cho bảng `map`
--

INSERT INTO `map` (`id`, `name`, `file`, `bg`, `mapAddY`, `bullEffShower`, `inWaterAddY`, `cl2AddY`) VALUES
(1, 'Cây cầu băng', 'bangtuyet_caycaubang.map', 7, 0, 0, -10, 0),
(2, 'Sống sót', 'bangtuyet_songsot.map', 0, -10, -50, -10, 0),
(3, 'Khu năng lượng', 'bencang_khunangluong.map', 0, 0, 0, 0, 0),
(4, 'Nhà máy điện', 'bencang_nhamaydien.map', 0, 0, 0, 0, 0),
(5, 'Bãi nước trũng', 'bien_bainuoctrung.map', 0, 0, 0, 0, 0),
(6, 'Đảo rêu', 'bien_daoreu.map', 0, 0, 0, 0, 0),
(7, 'Đảo vòng cung', 'bien_daovongcung.map', 1, 170, -100, 0, 1),
(8, 'Dãy đá ngầm', 'bien_daydangam.map', 0, 0, 0, 0, 0),
(9, 'Thuyền trôi', 'bien_thuyentroi.map', 0, 0, 0, 0, 0),
(10, 'Cầu tre', 'bobien_cautre.map', 0, 0, 0, 0, 0),
(11, 'Đảo dừa', 'bobien_daodua.map', 0, 0, 0, 0, 0),
(12, 'Đảo giun', 'bobien_daogiun.map', 0, 0, 0, 0, 0),
(13, 'Căn cứ thép', 'cancuthep_cancuthep.map', 6, 90, 0, 30, 0),
(14, 'Sân bay', 'cancuthep_sanbay.map', 6, 90, 0, 0, 0),
(15, 'Trụ điện ', 'cancuthep_trudien.map', 0, 0, 0, 0, 0),
(16, 'Cheo leo', 'dinhnui_cheoleo.map', 0, 0, 0, 0, 0),
(17, 'Chơi vơi', 'dinhnui_choivoi.map', 0, 0, 0, 0, 0),
(18, 'Hang tuyết', 'dinhnui_hangtuyet.map', 0, 0, 0, 0, 0),
(19, 'Bãi khai thác', 'dongco_baikhaithac.map', 0, 0, 0, 0, 0),
(20, 'Đồng cỏ', 'dongco_dongco.map', 0, 0, 0, 0, 0),
(21, 'Vách núi', 'dongco_vachnui.map', 4, 100, -100, 0, 2),
(22, 'Vực thẳm chết', 'dongco_vucthamchet.map', 0, 0, 0, 0, 0),
(23, 'Chiến trường', 'hoangmac_chientruong.map', 1, 170, -100, 0, 1),
(24, 'Đảo uông luông', 'hoangmac_daouongluong.map', 0, 170, -50, 0, -1),
(25, 'Đồi cát', 'hoangmac_doicat.map', 0, 0, 0, 0, 0),
(26, 'Khu sụp lún', 'hoangmac_khusuplun.map', 0, 0, 0, 0, 0),
(27, 'Mỏm đá tử thần', 'hoangmac_momdatuthan.map', 0, 0, 0, 0, 0),
(28, 'Bên dưới hố sâu', 'rungchet_benduoihosau.map', 0, 0, 0, 0, 0),
(29, 'Hòn đất', 'rungchet_hondat.map', 1, 170, -50, 0, -1),
(30, 'Khu giao tranh', 'rungchet_khugiaotranh.map', 0, 0, 0, 0, 0),
(31, 'Sống hoặc chết', 'rungchet_songhoackhet.map', 0, 0, 0, 0, 0),
(32, 'Đài tế thần', 'thanhphoco_daitethan.map', 0, 0, 0, 0, 0),
(33, 'Đền Hoàng', 'thanhphoco_denhoang.map', 0, 0, 0, 0, 0),
(34, 'Pháo thành', 'thanhphoco_phaothanh.map', 0, 0, 0, 0, 0),
(82, 'Ngẫu nhiên', 'randomMap.map', 13, -1, 80, -1, -1),
(83, 'Bom 1', 'boss_bom1.map', 7, -1, -80, -42, 0),
(84, 'Bom 2', 'boss_bom2.map', 7, -1, -80, 0, 0),
(85, 'Thành phố nhện', 'boss_nhenmay.map', 10, -1, 45, -10, 0),
(86, 'Thành phố máy', 'boss_robot.map', 10, -1, 45, -1, -1),
(87, 'Khủng long máy', 'boss_trex.map', 7, -1, -80, 0, 0),
(88, 'Khu vực cấm', 'boss_ufo.map', 12, -1, -50, 200, 2),
(89, 'Đỉnh hi mã lạp sơn', 'boss_ballon.map', 2, -1, -50, -1, -1),
(90, 'Hang nhện', 'boss_hangnhen.map', 13, -1, 80, -1, -1);

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
(4, 'King Kong', 40000, 32, 40, 30, 420, 10, 4),
(5, 'Rocketer', 30000, 24, 50, 20, 321, 12, 3),
(6, 'Granos', 20000, 16, 30, 30, 310, 7, 5),
(7, 'Chicky', 100000, 80, 20, -45, 450, 23, 2),
(8, 'Tarzan', 50000, 40, 10, 1, 340, 30, 1),
(9, 'Apache', 70000, 64, 30, -45, 431, 11, 4),
(10, 'Magenta', 120000, 96, 40, 20, 410, 35, 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`);

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
  MODIFY `nhanvat_id` tinyint(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
