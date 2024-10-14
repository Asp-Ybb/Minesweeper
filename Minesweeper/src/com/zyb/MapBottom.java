package com.zyb;

import java.awt.*;

public class MapBottom {

    BottomBomb bb = new BottomBomb();
    BottomNum bn = new BottomNum();

    {
        bb.reBomb();
        bn.reNum();
    }

    //重置游戏
    public void reGame() {
        for (int i = 1; i <= GameUtil.MAP_w; i++) {
            for (int j = 1; j <= GameUtil.MAP_l; j++) {
                GameUtil.DATA_BOTTOM[i][j] = 0;
            }
        }
        GameUtil.BOOM_count = 0;
        GameUtil.FLAG_NUM = 0;
        bb.reBomb();
        bn.reNum();
    }


    public void PaintSelf(Graphics g) {


        //横线
        g.setColor(Color.black);

        for (int i = 0; i < GameUtil.OFFSET + GameUtil.MAP_l * GameUtil.SQUARE_LENGTH; i += GameUtil.SQUARE_LENGTH) {
            g.drawLine(GameUtil.OFFSET,
                    GameUtil.OFFSET2 + i,
                    GameUtil.OFFSET + GameUtil.MAP_w * GameUtil.SQUARE_LENGTH,
                    GameUtil.OFFSET2 + i);
        }


        for (int i = 0; i < GameUtil.OFFSET + GameUtil.MAP_w * GameUtil.SQUARE_LENGTH; i += GameUtil.SQUARE_LENGTH) {
            g.drawLine(GameUtil.OFFSET + i,
                    GameUtil.OFFSET2,
                    GameUtil.OFFSET + i,
                    GameUtil.OFFSET2 + GameUtil.MAP_l * GameUtil.SQUARE_LENGTH);
        }

        //打印地雷
        for (int i = 1; i < GameUtil.MAP_w + 1; i++) {
            for (int j = 1; j < GameUtil.MAP_l + 1; j++) {
                if (GameUtil.DATA_BOTTOM[i][j] == -1) {
                    int x = GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1;
                    int y = GameUtil.OFFSET2 + (j - 1) * GameUtil.SQUARE_LENGTH + 1;//+1居中
                    g.drawImage(GameUtil.bomb, x, y, GameUtil.SQUARE_LENGTH - 2, GameUtil.SQUARE_LENGTH - 2, null);
                }
            }
        }

        //打印数字
        for (int i = 1; i < GameUtil.MAP_w + 1; i++) {
            for (int j = 1; j < GameUtil.MAP_l + 1; j++) {
                if (GameUtil.DATA_BOTTOM[i][j] > 0) {
                    int x = GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1;
                    int y = GameUtil.OFFSET2 + (j - 1) * GameUtil.SQUARE_LENGTH + 1;//+1居中
                    g.drawImage(GameUtil.nums[GameUtil.DATA_BOTTOM[i][j]], x, y, GameUtil.SQUARE_LENGTH - 2, GameUtil.SQUARE_LENGTH - 2, null);
                }
            }
        }

        for (int i = 0; i < GameUtil.MAP_w + 1; i++) {
            for (int j = 0; j < GameUtil.MAP_l + 1; j++) {
                if (GameUtil.DATA_BOTTOM[i][j] == -2) {
                    int x = GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1;
                    int y = GameUtil.OFFSET2 + (j - 1) * GameUtil.SQUARE_LENGTH + 1;//+1居中
                    g.drawImage(GameUtil.bombboom, x, y, GameUtil.SQUARE_LENGTH - 2, GameUtil.SQUARE_LENGTH - 2, null);
                }
            }
        }

        //绘制雷数
        GameUtil.printWord(g, " " + (GameUtil.BOMB_MAX - GameUtil.FLAG_NUM), GameUtil.OFFSET, GameUtil.OFFSET2 - GameUtil.OFFSET, 30, Color.red);
        //绘制计时
        GameUtil.printWord(g, " " + (GameUtil.END_TIME - GameUtil.START_TIME) / 1000, GameUtil.OFFSET + (GameUtil.MAP_w - 1) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET2 - GameUtil.OFFSET, 30, Color.red);
        //总共时间
        GameUtil.TOTAL_TIME = (GameUtil.END_TIME - GameUtil.START_TIME) / 1000;

        switch (GameUtil.STATE) {
            case 0:
                GameUtil.END_TIME = System.currentTimeMillis();
                g.drawImage(GameUtil.gaming, (GameUtil.OFFSET + GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2, GameUtil.OFFSET2 / 2, GameUtil.SQUARE_LENGTH, GameUtil.SQUARE_LENGTH, null);
                break;
            case 1:
                g.drawImage(GameUtil.victory, (GameUtil.OFFSET + GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2, GameUtil.OFFSET2 / 2, GameUtil.SQUARE_LENGTH, GameUtil.SQUARE_LENGTH, null);
                break;
            case 2:
                g.drawImage(GameUtil.defeat, (GameUtil.OFFSET + GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2, GameUtil.OFFSET2 / 2, GameUtil.SQUARE_LENGTH, GameUtil.SQUARE_LENGTH, null);
                break;
        }
        //难度选择按钮
        {
            g.setColor(Color.white);
            g.fillRect(GameUtil.OFFSET, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            g.setColor(Color.black);
            g.drawRect(GameUtil.OFFSET, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            Font f = new Font("宋体", Font.BOLD, 15);//字体名,风格,大小
            g.setFont(f);
            g.drawString("难度选择", GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 4, GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 12);
        }
        //闯关模式按钮
        if (!GameUtil.BreakDetec && !GameUtil.BreakDetec) {
            g.setColor(Color.white);
            g.fillRect(GameUtil.OFFSET + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            g.setColor(Color.black);
            g.drawRect(GameUtil.OFFSET + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            Font f = new Font("宋体", Font.BOLD, 15);//字体名,风格,大小
            g.setFont(f);
            g.drawString("闯关模式", GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 4 + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 12);
        } else if (GameUtil.STATE == 1 && GameUtil.BreakDetec) {
            g.setColor(Color.white);
            g.fillRect(GameUtil.OFFSET + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            g.setColor(Color.black);
            g.drawRect(GameUtil.OFFSET + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            Font f = new Font("宋体", Font.BOLD, 15);//字体名,风格,大小
            g.setFont(f);
            g.drawString("下 一 关", GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 4 + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 12);
        } else if (GameUtil.STATE == 2 && GameUtil.BreakDetec) {
            g.setColor(Color.white);
            g.fillRect(GameUtil.OFFSET + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            g.setColor(Color.black);
            g.drawRect(GameUtil.OFFSET + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            Font f = new Font("宋体", Font.BOLD, 15);//字体名,风格,大小
            g.setFont(f);
            g.drawString("重新开始", GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 4 + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH, GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 12);
        }

        //英雄榜
        {
            g.setColor(Color.white);
            g.fillRect((GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2 + GameUtil.OFFSET - GameUtil.SQUARE_LENGTH, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            g.setColor(Color.black);
            g.drawRect((GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2 + GameUtil.OFFSET - GameUtil.SQUARE_LENGTH, GameUtil.OFFSET, GameUtil.SQUARE_LENGTH * 2, GameUtil.SQUARE_LENGTH / 2);
            Font f = new Font("宋体", Font.BOLD, 15);//字体名,风格,大小
            g.setFont(f);
            g.drawString("英 雄 榜", (GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2 + GameUtil.OFFSET - GameUtil.SQUARE_LENGTH + GameUtil.OFFSET / 4, GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 12);
//            g.setFont(f);
//            g.drawString("难度选择", GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 4, GameUtil.OFFSET * 3 / 2 - GameUtil.OFFSET / 12);
        }
    }
}
