package com.zyb;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;

public class GameUtil {
    //英雄榜排名数组
    static int[] ranking = new int[3];
    static String[] name = new String[3];
    static int[] score = new int[3];


    //总分数
    static int HERO_SCORE;
    //英雄榜姓名
    static String HERO_NAME;
    //英雄榜排名
    static int HERO_RANKING = 1;
    //闯关模式判断
    static boolean BreakDetec = false;
    static int LEVEL = 1;
    static int DETEC = 1;

    //难度 1初级 2中级 3高级
    static int BOOM_count = 0;

    static int MAP_w = 9;
    static int MAP_l = 9;
    static int OFFSET = 35;
    static int SQUARE_LENGTH = 40;
    static int OFFSET2 = 4 * OFFSET;

    //插旗数量
    static int FLAG_NUM = 0;

    //计时
    static long START_TIME;
    static long END_TIME;
    static long TOTAL_TIME;

    //游戏状态 0-游戏中 1-胜利 2-失败
    static int STATE = 0;

    //鼠标坐标
    static int MOUSE_X;
    static int MOUSE_Y;
    //鼠标状态
    static boolean LEFT = false;
    static boolean RIGHT = false;

    //    static int BOMB_MAX;
    static int BOMB_MAX = 10;

    //-1-雷 0-空 1-8-数字 -2雷爆炸
    //DATA_BOTTOM设置比雷区大一圈，防止边界越界
    static int[][] DATA_BOTTOM = new int[50][50];
    //-1无覆盖 0覆盖 1插旗 2差错旗
    static int[][] DATA_TOP = new int[50][50];

    static Image bomb = Toolkit.getDefaultToolkit().getImage("imgs/bomb.png");

    static Image[] nums = new Image[9];

    static {
        for (int i = 0; i < 9; i++) {
            nums[i] = Toolkit.getDefaultToolkit().getImage("imgs/" + i + ".png");
        }
    }

    static Image block = Toolkit.getDefaultToolkit().getImage("imgs/block.png");

    static Image flag = Toolkit.getDefaultToolkit().getImage("imgs/flag.png");

    static Image notflag = Toolkit.getDefaultToolkit().getImage("imgs/notflag.png");

    static Image bombboom = Toolkit.getDefaultToolkit().getImage("imgs/bombboom.png");

    static Image victory = Toolkit.getDefaultToolkit().getImage("imgs/victory.png");

    static Image defeat = Toolkit.getDefaultToolkit().getImage("imgs/defeat.png");

    static Image gaming = Toolkit.getDefaultToolkit().getImage("imgs/gaming.png");

    //绘制字符
    static void printWord(Graphics g, String str, int x, int y, int size, Color color) {
        g.setColor(color);
        g.setFont(new Font("仿宋", Font.BOLD, size));
//        g.drawString(" " + (GameUtil.BOMB_MAX - GameUtil.FLAG_NUM), GameUtil.OFFSET, GameUtil.OFFSET2 / 2);
        g.drawString(str, x, y);
    }

}
