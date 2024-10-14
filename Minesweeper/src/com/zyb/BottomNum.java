package com.zyb;

public class BottomNum {
    public void reNum() {
        for (int i = 1; i <= GameUtil.MAP_w; i++) {
            for (int j = 1; j <= GameUtil.MAP_l; j++) {
                if (GameUtil.DATA_BOTTOM[i][j] == -1) {
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (GameUtil.DATA_BOTTOM[k][l] >= 0) {
                                GameUtil.DATA_BOTTOM[k][l]++;
                            }
                        }
                    }
                }
            }
        }

    }
}
