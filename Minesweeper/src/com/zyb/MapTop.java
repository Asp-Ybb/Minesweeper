package com.zyb;

import com.zyb.BottomBomb;
import com.zyb.BottomNum;
import com.zyb.GameUtil;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class MapTop {
    BottomBomb bb = new BottomBomb();
    BottomNum bn = new BottomNum();

    //临时XY用于判断点击坐标在地图中的格子位置
    int temp_X;
    int temp_Y;

    //    //重置游戏
    public void reGame() {
        for (int i = 1; i <= GameUtil.MAP_w; i++) {
            for (int j = 1; j <= GameUtil.MAP_l; j++) {
                GameUtil.DATA_TOP[i][j] = 0;
            }
        }
    }

    //new Audioplay("......");每次点击都会创建新的AudioClip实例
    private static final AudioClip dingdongClip;
    private static final AudioClip boomClip;

    static {
        try {
            dingdongClip = Applet.newAudioClip(new File("Audio/dingdong.wav").toURI().toURL());
            boomClip = Applet.newAudioClip(new File("Audio/boom.wav").toURI().toURL());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load audio file", e);
        }
    }

    //设置逻辑，用来判断是否揭开和插旗
    public void logic() {

        temp_X = (GameUtil.MOUSE_X - GameUtil.OFFSET) / GameUtil.SQUARE_LENGTH + 1;
        temp_Y = (GameUtil.MOUSE_Y - GameUtil.OFFSET2) / GameUtil.SQUARE_LENGTH + 1;


        //当MOUSE_X小于OFFSET时，temp_X仍然为1
        if (GameUtil.MOUSE_X > GameUtil.OFFSET && GameUtil.MOUSE_Y > GameUtil.OFFSET2) {
            if (temp_X >= 1 && temp_X <= GameUtil.MAP_w && temp_Y >= 1 && temp_Y <= GameUtil.MAP_l) {
                //左键
                if (GameUtil.LEFT && GameUtil.DATA_TOP[temp_X][temp_Y] != 1) {
                    if (GameUtil.DATA_BOTTOM[temp_X][temp_Y] != -1) {
                        dingdongClip.play();
                    }
                    GameUtil.DATA_TOP[temp_X][temp_Y] = -1;
                    openSpace(temp_X, temp_Y);
                    GameUtil.LEFT = false;
                } else if (GameUtil.LEFT && GameUtil.DATA_TOP[temp_X][temp_Y] == 1) {//-1无覆盖 0覆盖 1插旗 2差错旗
                    dingdongClip.play();
                    GameUtil.DATA_TOP[temp_X][temp_Y] = 0;
                    GameUtil.LEFT = false;
                }
                //右键
                if (GameUtil.RIGHT && GameUtil.DATA_TOP[temp_X][temp_Y] != 1 && GameUtil.DATA_TOP[temp_X][temp_Y] != -1 && GameUtil.FLAG_NUM < GameUtil.BOMB_MAX) {
                    dingdongClip.play();
                    GameUtil.DATA_TOP[temp_X][temp_Y] = 1;
                    GameUtil.FLAG_NUM++;
                    GameUtil.RIGHT = false;
                } else if (GameUtil.RIGHT && GameUtil.DATA_TOP[temp_X][temp_Y] == 1) {
                    dingdongClip.play();
                    GameUtil.DATA_TOP[temp_X][temp_Y] = 0;
                    GameUtil.FLAG_NUM--;
                    GameUtil.RIGHT = false;
                } else if (GameUtil.RIGHT && GameUtil.DATA_TOP[temp_X][temp_Y] == -1) {
                    //周围块闪动
//                    for (int i = temp_X - 1; i <= temp_X + 1; i++) {
//                        for (int j = temp_Y - 1; j <= temp_Y + 1; j++) {
//                            if(GameUtil.DATA_TOP[i][j] == 0){
//
//                            }
//                        }
//                    }
                    numOpen(temp_X, temp_Y);
                    GameUtil.RIGHT = false;
                }
            }
        }
        Boom(temp_X, temp_Y);
        Victory();
    }


    //失败判定
    boolean Boom(int x, int y) {
        for (int i = 1; i <= GameUtil.MAP_w; i++) {
            for (int j = 1; j <= GameUtil.MAP_l; j++) {
                if (GameUtil.DATA_BOTTOM[i][j] == -1 && GameUtil.DATA_TOP[i][j] == -1) {
                    GameUtil.BOOM_count++;
                    if (GameUtil.BOOM_count == 1) {
                        boomClip.play();
                        GameUtil.DATA_BOTTOM[i][j] = -2;
                    }
                    GameUtil.STATE = 2;
                    seeBomb();
                    return true;
                }
            }
        }
        return false;
    }

    //失败显示所有雷
    public void seeBomb() {
        for (int i = 1; i <= GameUtil.MAP_w; i++) {
            for (int j = 1; j <= GameUtil.MAP_l; j++) {
                if (GameUtil.DATA_BOTTOM[i][j] == -1 && GameUtil.DATA_TOP[i][j] == 0) {
                    GameUtil.DATA_TOP[i][j] = -1;
                }
                if (GameUtil.DATA_BOTTOM[i][j] != -1 && GameUtil.DATA_TOP[i][j] == 1) {
                    GameUtil.DATA_TOP[i][j] = 2;
                }
            }
        }
    }

    //判断胜利
    public boolean Victory() {
        int count = 0;
        for (int i = 1; i <= GameUtil.MAP_w; i++) {
            for (int j = 1; j <= GameUtil.MAP_l; j++) {
                if (GameUtil.DATA_TOP[i][j] != -1) {
                    count++;
                }
            }
        }
        //胜利之后所有雷的覆盖自动翻开
        if (count == GameUtil.BOMB_MAX) {
            GameUtil.STATE = 1;
            for (int i = 1; i <= GameUtil.MAP_w; i++) {
                for (int j = 1; j <= GameUtil.MAP_l; j++) {
                    if (GameUtil.DATA_TOP[i][j] == 0) {
                        GameUtil.DATA_TOP[i][j] = 1;
                        GameUtil.FLAG_NUM = 5;
                    }
                }
            }
            return true;
        }
        return false;
    }

    //数字处右键翻开
    public void numOpen(int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (GameUtil.DATA_TOP[i][j] == 1) {
                    count++;
                }
            }
        }

        if (count == GameUtil.DATA_BOTTOM[temp_X][temp_Y]) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (GameUtil.DATA_TOP[i][j] != 1) {    //-1无覆盖 0覆盖 1插旗 2差错旗
                        GameUtil.DATA_TOP[i][j] = -1;
                    }
                    if (i >= 1 && i <= GameUtil.MAP_w && j >= 1 && j <= GameUtil.MAP_l) {
                        openSpace(i, j);
                    }

                }
            }
        }
//        //右键闪烁效果
//        else {
//            for (int i = x - 1; i <= x + 1; i++) {
//                for (int j = y - 1; j <= y + 1; j++) {
//
//                }
//            }
//        }

    }

    //播放WAV方法
    public class AudioPlay {
        private AudioClip audioClip = null;

        public AudioPlay(String path) {
            File file = new File(path);
            try {
                audioClip = Applet.newAudioClip(file.toURI().toURL());
                audioClip.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openSpace(int x, int y) {
        if (GameUtil.DATA_BOTTOM[x][y] == 0 && GameUtil.DATA_TOP[x][y] != 1) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (GameUtil.DATA_TOP[i][j] != -1 && GameUtil.DATA_TOP[i][j] != 1) {
                        GameUtil.DATA_TOP[i][j] = -1;
                        if (i >= 1 && i <= GameUtil.MAP_w && j >= 1 && j <= GameUtil.MAP_l) {
                            openSpace(i, j);
                        }
                    }
                }
            }
        }
    }

    public void PaintSelf(Graphics g) {
        logic();

        for (int i = 1; i < GameUtil.MAP_w + 1; i++) {
            for (int j = 1; j < GameUtil.MAP_l + 1; j++) {
                if (GameUtil.DATA_TOP[i][j] == 0) {
                    int x = GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1;
                    int y = GameUtil.OFFSET2 + (j - 1) * GameUtil.SQUARE_LENGTH + 1;//+1居中
                    g.drawImage(GameUtil.block, x, y, GameUtil.SQUARE_LENGTH - 2, GameUtil.SQUARE_LENGTH - 2, null);
                }
            }
        }

        for (int i = 0; i < GameUtil.MAP_w + 1; i++) {
            for (int j = 0; j < GameUtil.MAP_l + 1; j++) {
                if (GameUtil.DATA_TOP[i][j] == 1) {
                    int x = GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1;
                    int y = GameUtil.OFFSET2 + (j - 1) * GameUtil.SQUARE_LENGTH + 1;//+1居中
                    g.drawImage(GameUtil.flag, x, y, GameUtil.SQUARE_LENGTH - 2, GameUtil.SQUARE_LENGTH - 2, null);
                }
            }
        }

        for (int i = 0; i < GameUtil.MAP_w + 1; i++) {
            for (int j = 0; j < GameUtil.MAP_l + 1; j++) {
                if (GameUtil.DATA_TOP[i][j] == 2) {
                    int x = GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1;
                    int y = GameUtil.OFFSET2 + (j - 1) * GameUtil.SQUARE_LENGTH + 1;//+1居中
                    g.drawImage(GameUtil.notflag, x, y, GameUtil.SQUARE_LENGTH - 2, GameUtil.SQUARE_LENGTH - 2, null);
                }
            }
        }
    }
}
