package com.mobiarmy.fight;

import com.mobiarmy.data.Until;
import com.mobiarmy.server.ServerManager;

public class SpecialBullets {

    public static class ItemBomB52 extends Bullet {
        public ItemBomB52(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (!this.collect) return;

            int horizontalOffset = Until.nextInt(130, 150);
            int verticalOffset = Until.nextInt(285, 300);
            int dropX = this.x - horizontalOffset;
            int dropY = this.y - verticalOffset;
            int dropVx = Until.nextInt(8, 13);

            this.bullMNG.addBullet(new B52Bullet(this.bullMNG, (byte)3, this.satThuong, this.pl, dropX, dropY, dropVx, 0, 0, 80, (short)this.x, (short)this.y));
        }
    }

    public static class B52Bullet extends Bullet {
        private final short toX;
        private final short toY;

        public B52Bullet(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100, short toX, short toY) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isXuyenMap = true;
            this.isXuyenPlayer = true;
            this.toX = toX;
            this.toY = toY;
        }

        @Override
        public void nextXY() {
            if (this.collect) return;

            int dyRemaining = toY - this.y;
            int estFramesLeft = this.vy > 0 ? Math.max(dyRemaining / this.vy, 3) : 8;
            int dx = toX - this.x;
            this.vx = (short) Math.max(-12, Math.min(12, dx / estFramesLeft));

            super.nextXY();
            if (this.collect) return;

            if (this.y >= toY) {
                this.x = toX;
                this.y = toY;
                this.xArray.set(this.xArray.size() - 1, (short)this.x);
                this.yArray.set(this.yArray.size() - 1, (short)this.y);
                this.collect = true;
                this.hitRealCollision = true;
                if (this.isCanCollision) {
                    this.fm.mapMNG.collision(this.x, this.y, this);
                }
            }
        }
    }

    public static class ItemBomHenGio extends Bullet {
        public ItemBomHenGio(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
            this.isXuyenPlayer = true;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.collect) {
                this.isCanCollision = true;
                this.bullMNG.buls.add(new BulletManager.BulletsObj(this));
            }
        }
    }

    public static class ItemKhoangDat extends Bullet {
        private final int nFrame;

        public ItemKhoangDat(BulletManager bullMNG, byte bullId, Player pl, int x, int y, byte force) {
            super(bullMNG, bullId, 0, pl, x, y, 0, 0, 0, 0);
            this.nFrame = force * 2;
        }

        @Override
        public void nextXY() {
            this.y += 2;
            this.frame++;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            if (this.fm.mapMNG.isCollision((short)this.x, (short)this.y)) {
                this.fm.mapMNG.collision(this.x, this.y, this);
            }
            if (this.frame == nFrame) {
                this.collect = true;
            }
        }
    }

    public static class ItemTeleport extends Bullet {
        public ItemTeleport(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
            this.isXuyenPlayer = true;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.collect) {
                this.fm.getPlayerTurn().setXY((short)this.x, (short)this.y);
            }
        }
    }

    public static class ItemXuyenDat extends Bullet {
        private final int forceLimit;

        public ItemXuyenDat(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100, int force) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.forceLimit = force / 2;
            this.isXuyenMap = true;
            this.isXuyenPlayer = false;
        }

        @Override
        public void nextXY() {
            this.frame++;
            short preX = (short)this.x, preY = (short)this.y;
            this.x += this.vx;
            this.lastX = this.x;
            this.y += this.vy;
            this.lastY = this.y;

            short[] collisionPoint = this.bullMNG.getCollisionPoint(preX, preY, (short)this.x, (short)this.y, this.isXuyenPlayer, this.isXuyenMap);
            if (collisionPoint != null) {
                this.collect = true;
                this.x = collisionPoint[0];
                this.y = collisionPoint[1];
                this.xArray.add((short)this.x);
                this.yArray.add((short)this.y);
                if (this.isCanCollision) {
                    this.fm.mapMNG.collision(this.x, this.y, this);
                }
                return;
            }

            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            if (this.x < -100 || this.x > this.fm.mapMNG.getWidth() + 100 || this.y > this.fm.mapMNG.getHeight() + 200) {
                this.xArray.add((short)this.x);
                this.yArray.add((short)this.y);
                this.collect = true;
                return;
            }
            if (this.frame == this.forceLimit - 1) {
                this.xArray.add((short)this.x);
                this.yArray.add((short)this.y);
                this.collect = true;
                return;
            }

            this.vyTemp2 -= this.g100;
            if (Math.abs(this.vyTemp2) >= 100) {
                this.vy -= (this.vyTemp2 / 100);
                this.vyTemp2 %= 100;
            }

            if (this.bullMNG.hasVoiRong) {
                for (BulletManager.VoiRong vr : this.bullMNG.voiRongs) {
                    if (this.x >= vr.x - 5 && this.x <= vr.x + 10) {
                        this.vx -= 2;
                        this.vy -= 2;
                        break;
                    }
                }
            }
        }
    }

    public static class ItemTraiPha extends Bullet {
        public ItemTraiPha(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (!this.collect) return;

            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)12, this.satThuong, this.pl, this.x - 8, this.y - 493, -1, 2, 0, 100));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)12, this.satThuong, this.pl, this.x + 12, this.y - 496, 0, 1, 0, 100));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)12, this.satThuong, this.pl, this.x - 19, this.y - 505, -2, 1, 0, 100));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)12, this.satThuong, this.pl, this.x + 18, this.y - 505, 1, 1, 0, 100));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)12, this.satThuong, this.pl, this.x + 20, this.y - 512, 2, 0, 0, 100));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)12, this.satThuong, this.pl, this.x - 20, this.y - 512, -3, 0, 0, 100));
        }
    }

    public static class ItemVoiRong extends Bullet {
        public ItemVoiRong(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.collect) {
                this.bullMNG.hasVoiRong = true;
                this.bullMNG.voiRongs.add(new BulletManager.VoiRong(this.x, this.y, 3));
            }
        }
    }

    public static class ItemMuaDan extends Bullet {
        public ItemMuaDan(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (!this.isMaxY && !this.collect) return;

            this.collect = true;
            for (int i = 0; i < 10; i++) {
                this.xArray.add((short)this.x);
                this.yArray.add((short)this.y);
            }

            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x + 18, this.y - 20, 2, -1, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x - 19, this.y - 20, -3, -1, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x + 16, this.y - 23, 3, -2, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x - 17, this.y - 23, 4, -2, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x + 14, this.y - 26, 3, -3, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x - 15, this.y - 26, -4, -3, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x + 11, this.y - 28, 3, -4, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x - 12, this.y - 28, -4, -4, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x + 8, this.y - 30, 2, -5, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x - 9, this.y - 30, -3, -5, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x + 5, this.y - 31, 1, -6, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x - 6, this.y - 31, -2, -6, 15, 60));
            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)29, this.satThuong, this.pl, this.x - 2, this.y - 31, 1, -7, 15, 60));
        }
    }

    public static class ApaBullet extends Bullet {
        private final byte force;
        private final byte force2;
        private final int satThuongGoc;

        public ApaBullet(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100, byte force, byte force2) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.force = force;
            this.force2 = force2;
            this.satThuongGoc = 150;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.force2 != this.frame) return;

            this.isCanCollision = false;
            this.satThuong = 0;
            this.fm.mapMNG.collision(this.x, this.y, this);

            int arg = this.bullMNG.arg + Until.toArg0_360(Until.getArg(this.pl.x - this.x, this.pl.y - this.y));
            if (this.bullMNG.arg < 90) arg = 180 - arg;
            arg -= 15;

            for (int i = 0; i < 3; i++, arg += 15) {
                int nx = this.x + ((20 * Until.cos(arg)) >> 10);
                int ny = this.y - 12 - ((20 * Until.sin(arg)) >> 10);
                int nvx = (this.force * Until.cos(arg)) >> 11;
                int nvy = -((this.force * Until.sin(arg)) >> 11);
                this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)18, 150, this.pl, nx, ny, nvx, nvy, 30, 100));
            }
        }
    }

    public static class ChickyBullet extends Bullet {
        private final byte force2;
        private final int satThuongGoc;

        public ChickyBullet(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100, byte force2) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.force2 = force2;
            this.satThuongGoc = satThuong;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.force2 == this.frame) {
                this.bullMNG.addBullet(new ChickyTrung(this.bullMNG, (byte)20, this.satThuongGoc, this.pl, this.lastX, this.lastY, 0, 0, 20, 50));
            }
        }
    }

    public static class ChickyTrung extends Bullet {
        public ChickyTrung(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
        }
    }

    public static class TazranBullet extends Bullet {
        private byte quayLai;
        private final boolean addTZ;

        public TazranBullet(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.quayLai = -1;
            this.addTZ = vx <= 0;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.quayLai == 0) {
                this.vx += (short)(this.addTZ ? 1 : -1);
                this.quayLai = 1;
            } else if (this.quayLai == 1) {
                this.vx += (short)(this.addTZ ? 2 : -2);
            } else if (this.isMaxY) {
                this.quayLai = 0;
            }
        }
    }

    public static class BalloonLazer extends Bullet {
        public BalloonLazer(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y) {
            super(bullMNG, bullId, satThuong, pl, x, y, 0, 0, 0, 100);
        }

        @Override
        public void nextXY() {
            this.frame++;
            Player closest = this.fm.getPlayerClosest(this.x, this.y);
            this.x = closest.x;
            this.y = closest.y;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);

            if (this.x < -200 || this.x > this.fm.mapMNG.getWidth() + 200 || this.y > this.fm.mapMNG.getHeight() + 200) {
                this.collect = true;
                return;
            }

            short preX = (short)this.x, preY = (short)this.y;
            this.lastX = this.x;
            this.y += this.vy;
            this.lastY = this.y;

            short[] collisionPoint = this.bullMNG.getCollisionPoint(preX, preY, (short)this.x, (short)this.y, this.isXuyenPlayer, this.isXuyenMap);
            if (collisionPoint != null) {
                this.collect = true;
                this.x = collisionPoint[0];
                this.y = collisionPoint[1];
                this.xArray.add((short)this.x);
                this.yArray.add((short)this.y);
                if (this.isCanCollision) {
                    this.fm.mapMNG.collision(this.x, this.y, this);
                }
            }
        }
    }

    public static class BigBoomBum extends Bullet {
        public BigBoomBum(BulletManager bullMNG, byte bullId, int satThuong, Player pl) {
            super(bullMNG, bullId, satThuong, pl, pl.x, pl.y - 12, 0, 0, 0, 0);
        }

        @Override
        public void nextXY() {
            this.collect = true;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            this.y += 2;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            // Stub for BigBoom cast
            // ((Bosses.BigBoom)this.pl).bomAction();
            if (this.isCanCollision) {
                this.fm.mapMNG.collision(this.x, this.y, this);
            }
        }
    }

    public static class BigRocKet extends Bullet {
        private final short toX;
        private boolean flyshoot;

        public BigRocKet(BulletManager bullMNG, byte bullId, int satThuong, Player pl) {
            super(bullMNG, bullId, satThuong, pl, pl.x, pl.y - (pl.height + 12), 0, -50, 0, 0);
            this.g100 = 80;

            java.util.List<Short> livingX = new java.util.ArrayList<>();
            for (int i = 0; i < ServerManager.maxPlayers; i++) {
                if (this.fm.players[i] != null && !this.fm.players[i].isDie) {
                    livingX.add(this.fm.players[i].x);
                }
            }
            this.toX = livingX.isEmpty() ? this.pl.x : livingX.get(Until.nextInt(livingX.size()));
            this.flyshoot = true;
        }

        @Override
        public void nextXY() {
            if (this.flyshoot) {
                this.vy = -50;
                if (this.y < -1200) {
                    this.vy = 0;
                    for (int i = 0; i < 21; i++) {
                        this.x -= (short)((this.pl.x - this.toX) / 20);
                        this.frame++;
                        this.xArray.add((short)this.x);
                        this.yArray.add((short)this.y);
                    }
                    this.x = this.toX;
                    this.flyshoot = false;
                }
            }
            super.nextXY();
        }
    }

    public static class ItemTenLua extends Bullet {
        private final byte splitFrame;

        public ItemTenLua(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100, byte force) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
            this.isXuyenPlayer = true;
            this.isXuyenMap = true;
            this.splitFrame = (byte) Math.max(force * 3 / 4, 6);
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.frame != this.splitFrame && !this.collect) return;

            Player targetPl = ItemTenLuaChild.pickTarget(this.bullMNG, this.pl);
            short toX, toY;
            if (targetPl != null) {
                toX = (short) Math.max(0, Math.min(this.bullMNG.fm.mapMNG.getWidth(), targetPl.x + Until.nextInt(-40, 40)));
                toY = (short) targetPl.y;
            } else {
                toX = (short) this.x;
                toY = (short) this.bullMNG.fm.mapMNG.getHeight();
            }

            this.bullMNG.addBullet(new ItemTenLuaChild(this.bullMNG, (byte)27, this.satThuong, this.pl, this.x, this.y, 2, -1, 15, 60, toX, toY, 0));
            this.bullMNG.addBullet(new ItemTenLuaChild(this.bullMNG, (byte)27, this.satThuong, this.pl, this.x, this.y, -3, -1, 15, 60, toX, toY, 1));
            this.bullMNG.addBullet(new ItemTenLuaChild(this.bullMNG, (byte)27, this.satThuong, this.pl, this.x, this.y, 3, -2, 15, 60, toX, toY, 2));

            this.collect = true;
        }
    }

    public static class ItemTenLuaChild extends Bullet {
        private final short toX;
        private final short toY;
        private final int slot;

        private final byte weaveFrames;
        private final byte approachFrames;
        private final int slotDelay;

        private boolean approachArmed;

        public ItemTenLuaChild(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100, short toX, short toY, int slot) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.toX = toX;
            this.toY = toY;
            this.slot = slot;
            this.isXuyenMap = true;
            this.isXuyenPlayer = true;
            this.weaveFrames = (byte) Until.nextInt(10, 16);
            this.approachFrames = (byte) Until.nextInt(20, 28);
            this.slotDelay = slot * 3;
        }

        @Override
        public void nextXY() {
            if (this.frame < this.weaveFrames) {
                this.vx += (short) Math.round(Math.sin(this.frame * 0.6) * 2);
                super.nextXY();
                return;
            }

            if (!this.approachArmed) {
                this.isXuyenPlayer = false;
                this.approachArmed = true;
            }

            int approachFrame = this.frame - this.weaveFrames;
            if (approachFrame >= this.approachFrames) {
                this.frame++;
                this.xArray.add((short)this.x);
                this.yArray.add((short)this.y);
                this.collect = true;
                this.fm.mapMNG.collision(this.x, this.y, this);
                return;
            }

            int effectiveFrame = Math.max(approachFrame - this.slotDelay, 0);
            int framesLeft = Math.max(this.approachFrames - effectiveFrame, 4);
            double remaining = Math.max(0, Math.min(1, framesLeft / (double)this.approachFrames));

            int lateral = this.slot == 0 ? 0 : (int)(18 * remaining) * (this.slot == 1 ? -1 : 1);
            short targetX = (short) (this.toX + lateral);

            int dx = targetX - this.x;
            this.vx = (short) Math.max(-14, Math.min(14, dx / framesLeft));

            int dy = this.toY - this.y;
            if (dy > 0) {
                short nudge = (short) Math.max(0, Math.min(3, dy / framesLeft));
                if (nudge > this.vy) this.vy = nudge;
            }

            super.nextXY();
        }

        public static Player pickTarget(BulletManager bullMNG, Player shooter) {
            FightManager fm = bullMNG.fm;
            java.util.List<Player> candidates = new java.util.ArrayList<>();

            if (fm.type == 5) {
                for (int i = ServerManager.maxPlayers; i < fm.allCount; i++) {
                    Player p = fm.players[i];
                    if (p != null && !p.isDie) candidates.add(p);
                }
            } else {
                for (int i = 0; i < ServerManager.maxPlayers; i++) {
                    Player p = fm.players[i];
                    if (p != null && !p.isDie && p.team != shooter.team) candidates.add(p);
                }
            }

            return candidates.isEmpty() ? null : candidates.get(Until.nextInt(candidates.size()));
        }
    }

    public static class ItemChuotGanBom extends Bullet {
        private final int nStep;
        private final boolean addX;
        private int nYRoi;

        public ItemChuotGanBom(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, byte force, boolean addX) {
            super(bullMNG, bullId, satThuong, pl, addX ? x + 1 : x - 1, y, 0, 0, 0, 0);
            this.nStep = force * 3;
            this.addX = addX;
            this.nYRoi = 0;
        }

        @Override
        public void nextXY() {
            this.nYRoi++;
            for (int i = 0; i < this.nYRoi; i++) {
                if (this.fm.mapMNG.isCollision((short)this.x, (short)this.y)) {
                    this.nYRoi = 0;
                    break;
                }
                this.y++;
            }

            final byte step = 4;
            this.x += (short)(this.addX ? step : -step);

            if (this.fm.mapMNG.isCollision((short)this.x, (short)(this.y - 5))) {
                this.x += (short)(this.addX ? -step : step);
            } else {
                for (int i = 4; i >= 0; i--) {
                    if (this.fm.mapMNG.isCollision((short)this.x, (short)(this.y - i))) {
                        this.y -= (short)i;
                        break;
                    }
                }
            }

            if (this.y > this.fm.mapMNG.getHeight() + 100) {
                this.xArray.add((short)this.x);
                this.yArray.add((short)this.y);
                this.collect = true;
                return;
            }

            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            if (this.frame == this.nStep) {
                this.collect = true;
                this.fm.mapMNG.collision(this.x, this.y, this);
                return;
            }
            this.frame++;
        }
    }

    public static class ItemSaoBang extends Bullet {
        public ItemSaoBang(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (!this.collect) return;

            this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)24, this.satThuong, this.pl, this.x, this.y - 187, 0, 3, 0, 50));
            for (int i = 1; i < 7; i++) {
                int xOffset = i * (i % 2 == 0 ? 30 : -30);
                this.bullMNG.addBullet(new Bullet(this.bullMNG, (byte)24, this.satThuong, this.pl, this.x + xOffset, this.y - 187, 0, Math.abs(3 - i), 0, 50));
            }
        }
    }

    public static class MGTBulletNew extends Bullet {
        private final byte force;

        public MGTBulletNew(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100, byte force) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            bullMNG.mgtAddX = 0;
            bullMNG.mgtAddY = 0;
            this.force = force;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (!this.isMaxY) return;

            this.collect = true;
            int nextX = this.x - this.xArray.get(0);
            int nextY = this.y - this.yArray.get(0);
            int arg = Until.getArg(nextX, nextY);
            nextX = ((this.force + 5) * Until.cos((short)arg)) >> 10;
            nextY = ((this.force + 5) * Until.sin((short)arg)) >> 10;

            short lx = this.xArray.get(this.xArray.size() - 1);
            short ly = this.yArray.get(this.yArray.size() - 1);
            while (true) {
                if (lx < -100 || lx > this.fm.mapMNG.getWidth() + 100 || ly > this.fm.mapMNG.getHeight() + 100) {
                    break;
                }
                short[] collisionPoint = this.bullMNG.getCollisionPoint(lx, ly, (short)(lx + nextX), (short)(ly - nextY), false, false);
                if (collisionPoint != null) {
                    lx = collisionPoint[0];
                    ly = collisionPoint[1];
                    break;
                }
                lx += (short)nextX;
                ly -= (short)nextY;
            }

            this.fm.mapMNG.collision(lx, ly, this);
            this.xArray.add(lx);
            this.yArray.add(ly);
            this.bullMNG.mgtAddX = (byte)nextX;
            this.bullMNG.mgtAddY = (byte)nextY;
        }
    }

    public static class MGTBulletOld extends Bullet {
        private final int force;

        public MGTBulletOld(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx100, int vy100, int force) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx100, vy100, 0, 0);
            this.force = force;
        }

        @Override
        public void nextXY() {
            this.frame++;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);

            if (this.x < -200 || this.x > this.fm.mapMNG.getWidth() + 200 || this.y > this.fm.mapMNG.getHeight() + 200) {
                this.collect = true;
                return;
            }

            short preX = (short)this.x, preY = (short)this.y;
            this.vxTemp += (short)Math.abs(this.vx);
            this.vyTemp += (short)Math.abs(this.vy);

            if (Math.abs(this.vxTemp) >= 100) {
                this.x += (short)(this.vx > 0 ? this.vxTemp / 100 : -(this.vxTemp / 100));
                this.vxTemp %= 100;
            }
            if (Math.abs(this.vyTemp) >= 100) {
                this.y += (short)(this.vy > 0 ? this.vyTemp / 100 : -(this.vyTemp / 100));
                this.vyTemp %= 100;
            }

            short[] collisionPoint = this.bullMNG.getCollisionPoint(preX, preY, (short)this.x, (short)this.y, this.isXuyenPlayer, this.isXuyenMap);
            if (collisionPoint != null) {
                this.collect = true;
                this.x = collisionPoint[0];
                this.y = collisionPoint[1];
                this.xArray.add((short)this.x);
                this.yArray.add((short)this.y);
                if (this.isCanCollision) {
                    this.fm.mapMNG.collision(this.x, this.y, this);
                }
                return;
            }

            if (this.frame == this.force) {
                this.vy = (short)-this.vy;
                this.vxTemp = 0;
                this.vyTemp = 0;
            }
        }
    }
    public static class ItemDongBang extends Bullet {
        public ItemDongBang(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.collect) {
                // stub for ApplyAreaEffect
            }
        }
    }

    public static class ItemKhoiDoc extends Bullet {
        public ItemKhoiDoc(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.collect) {
                // stub for ApplyAreaEffect
            }
        }
    }

    public static class ItemLaser extends Bullet {
        public ItemLaser(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
            this.isCanCollision = false;
        }

        @Override
        public void nextXY() {
            super.nextXY();
            if (this.collect) {
                this.bullMNG.addBullet(new ItemLaserDelay(this.bullMNG, (byte)15, this.satThuong, this.pl, this.x, this.y));
            }
        }
    }

    public static class ItemLaserDelay extends Bullet {
        public ItemLaserDelay(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y) {
            super(bullMNG, bullId, satThuong, pl, x, y, 0, 0, 0, 100);
        }
    }

    public static class Jump extends Bullet {
        public Jump(BulletManager bullMNG, byte bullId, int satThuong, Player pl) {
            super(bullMNG, bullId, satThuong, pl, pl.x, pl.y - 12, 0, 0, 0, 0);
        }

        @Override
        public void nextXY() {
            this.collect = true;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            this.y += 2;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            if (this.isCanCollision) {
                this.fm.mapMNG.collision(this.x, this.y, this);
            }
        }
    }

    public static class SpiderDropSilk extends Bullet {
        public SpiderDropSilk(BulletManager bullMNG, byte bullId, int satThuong, Player pl) {
            super(bullMNG, bullId, satThuong, pl, pl.x, pl.y - 12, 0, 0, 0, 0);
        }

        @Override
        public void nextXY() {
            this.collect = true;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            this.y += 38;
            this.xArray.add((short)this.x);
            this.yArray.add((short)this.y);
            
            // Stub for Spider target
            
            // this.fm.mapMNG.addEntry(new MapEntry(this.bullMNG.mangNhenId++, (short)(this.x - 21), (short)(this.y - 20), MapEntry.mangNhenData, (short)MapEntry.mangNhenW, (short)MapEntry.mangNhenH, true));
        }
    }
    
    public static class SmallBoomAdd extends Bullet {
        public SmallBoomAdd(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
        }
    }

    public static class SmallBoomBum extends Bullet {
        public SmallBoomBum(BulletManager bullMNG, byte bullId, int satThuong, Player pl) {
            super(bullMNG, bullId, satThuong, pl, pl.x, pl.y, 0, 0, 0, 0);
        }
    }

    public static class JumpOrFly extends Bullet {
        public JumpOrFly(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
        }
    }

    public static class ItemTuSat extends Bullet {
        public ItemTuSat(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
        }
    }
    
    public static class ItemToNhen extends Bullet {
        public ItemToNhen(BulletManager bullMNG, byte bullId, int satThuong, Player pl, int x, int y, int vx, int vy, int msg, int g100) {
            super(bullMNG, bullId, satThuong, pl, x, y, vx, vy, msg, g100);
        }
    }
}
