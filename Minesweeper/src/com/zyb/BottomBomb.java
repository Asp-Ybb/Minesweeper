package com.zyb;

import java.util.Random;

public class BottomBomb {

    public void reBomb() {
        int bomb[] = new int[GameUtil.BOMB_MAX * 2];
        Random r = new Random();
        int x, y;
        for (int i = 0; i < GameUtil.BOMB_MAX * 2; i += 2) {
            boolean Detect = false;
            while (!Detect) {
                Detect = true;
                x = r.nextInt(GameUtil.MAP_w) + 1; //1-11
                y = r.nextInt(GameUtil.MAP_l) + 1; //1-11
                for (int j = 0; j < GameUtil.BOMB_MAX * 2; j += 2) {
                    if (x == bomb[j] && y == bomb[j + 1]) {
                        Detect = false;
                        break;
                    }
                }
                bomb[i] = x;
                bomb[i + 1] = y;
            }
        }

        for (int i = 0; i < GameUtil.BOMB_MAX * 2; i += 2) {
            GameUtil.DATA_BOTTOM[bomb[i]][bomb[i + 1]] = -1;
        }
    }
}
