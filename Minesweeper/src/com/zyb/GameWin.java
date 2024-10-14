package com.zyb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

//循环的时候，得输出一个值，才能进入下一次循环

public class GameWin extends JFrame {
    //查看英雄榜前三
    public static void selectHero() throws ClassNotFoundException, SQLException {
        int i = 0;

        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/zyb?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, username, password);

        Statement sta = conn.createStatement();

        ResultSet rs = sta.executeQuery("select * from herotop where ranking <= 3");
        while (rs.next()) {
            //0-第三名 1-第二名 2-第一名
            GameUtil.ranking[i] = rs.getInt("ranking");
            GameUtil.name[i] = rs.getString("name");
            GameUtil.score[i] = rs.getInt("time");
            i++;
            if (i == 3) {
                break;
            }
        }

        StringBuilder message = new StringBuilder();
        for (int j = 2; j >= 0; j--) {
            //准备显示的信息
            message.append("排名：")
                    .append(GameUtil.ranking[j])
                    .append(" ID：")
                    .append(GameUtil.name[j])
                    .append(" 分数：")
                    .append(GameUtil.score[j])
                    .append("\n");
        }

        JOptionPane.showMessageDialog(null, message.toString(), "英雄榜", JOptionPane.INFORMATION_MESSAGE);
        rs.close();
        sta.close();
        conn.close();

    }

    //插入英雄榜
    public void addHero() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/zyb?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, username, password);

        Statement sta = conn.createStatement();

        ResultSet rs = sta.executeQuery("select * from herotop");

        int a = 0;

        while (rs.next()) {
            System.out.println(rs.getString("name"));
            if (GameUtil.HERO_SCORE >= rs.getInt("time")) {//40
                GameUtil.HERO_RANKING = rs.getInt("ranking") + 1;
                System.out.println(GameUtil.HERO_RANKING);
                break;
            } else {
                GameUtil.HERO_RANKING = 1;
                break;
            }
        }

        sta.executeUpdate("UPDATE herotop SET ranking = ranking + 1 WHERE ranking >= " + GameUtil.HERO_RANKING);

        int i = sta.executeUpdate("insert into herotop (ranking, name, time) values (" + GameUtil.HERO_RANKING + "," + "'" + GameUtil.HERO_NAME + "'" + "," + GameUtil.HERO_SCORE + ")");

//        sta.executeUpdate("SELECT * FROM herotop order by field(ranking,1,0,2)");


        rs.close();
        sta.close();
        conn.close();
    }

    private int Width;
    private int Height;

    public GameWin() {
        updateSize();
        launch();
    }

    private void updateSize() {
        Width = GameUtil.OFFSET * 2 + GameUtil.MAP_w * GameUtil.SQUARE_LENGTH;
        Height = (GameUtil.OFFSET2 + GameUtil.OFFSET) + GameUtil.MAP_l * GameUtil.SQUARE_LENGTH;
    }

    Image offScreenImage = null;

    MapBottom mapBottom = new MapBottom();
    MapTop mapTop = new MapTop();

    public void launch() {
        GameUtil.START_TIME = System.currentTimeMillis();
        setSize(Width, Height);
        setLocationRelativeTo(null);
        setTitle("扫雷");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        //设置鼠标事件
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (GameUtil.STATE) {
                    case 0:
                        GameUtil.END_TIME = System.currentTimeMillis();
                        switch (e.getButton()) {
                            case 1:
                                GameUtil.MOUSE_X = e.getX();
                                GameUtil.MOUSE_Y = e.getY();
                                GameUtil.LEFT = true;
                                break;
                            case 3:
                                GameUtil.MOUSE_X = e.getX();
                                GameUtil.MOUSE_Y = e.getY();
                                System.out.println("2");
                                GameUtil.RIGHT = true;
                                break;
                        }
                    case 1:
                    case 2:
                        if (e.getButton() == 1) {
                            //难度选择
                            if (e.getX() >= GameUtil.OFFSET
                                    && e.getX() <= GameUtil.OFFSET + GameUtil.SQUARE_LENGTH * 2
                                    && e.getY() >= GameUtil.OFFSET
                                    && e.getY() <= GameUtil.OFFSET + GameUtil.SQUARE_LENGTH / 2) {
                                DifficultySelection();
                                setTitle("扫雷");
                                GameUtil.BreakDetec = false;
                                mapTop.reGame();
                                mapBottom.reGame();
                                GameUtil.START_TIME = System.currentTimeMillis();
                                GameUtil.STATE = 0;
                                updateSize(); // 更新窗口尺寸
                                setSize(Width, Height); // 重新设置窗口大小
                                setLocationRelativeTo(null);
                                repaint(); // 触发重绘
                            }
                            //游戏重置
                            if (e.getX() >= (GameUtil.OFFSET + GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2
                                    && e.getX() <= GameUtil.OFFSET + (GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2
                                    && e.getY() >= GameUtil.OFFSET2 / 2
                                    && e.getY() <= GameUtil.OFFSET2 / 2 + GameUtil.SQUARE_LENGTH) {
                                mapTop.reGame();
                                mapBottom.reGame();
                                GameUtil.START_TIME = System.currentTimeMillis();
                                GameUtil.STATE = 0;
                            }
                            //闯关模式
                            if (e.getX() >= GameUtil.OFFSET + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH
                                    && e.getX() <= GameUtil.OFFSET + (GameUtil.MAP_w - 2) * GameUtil.SQUARE_LENGTH + GameUtil.SQUARE_LENGTH * 2
                                    && e.getY() >= GameUtil.OFFSET
                                    && e.getY() <= GameUtil.OFFSET + GameUtil.SQUARE_LENGTH / 2) {
                                GameUtil.BreakDetec = true;
                                try {
                                    BreakThrough(0);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                } catch (ClassNotFoundException classNotFoundException) {
                                    classNotFoundException.printStackTrace();
                                }
                            }
                            //英雄榜
                            if (e.getX() >= (GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2 + GameUtil.OFFSET - GameUtil.SQUARE_LENGTH
                                    && e.getX() <= (GameUtil.MAP_w * GameUtil.SQUARE_LENGTH) / 2 + GameUtil.OFFSET - GameUtil.SQUARE_LENGTH + GameUtil.SQUARE_LENGTH * 2
                                    && e.getY() >= GameUtil.OFFSET
                                    && e.getY() <= GameUtil.OFFSET + GameUtil.SQUARE_LENGTH / 2) {
                                try {
                                    selectHero();
                                } catch (ClassNotFoundException classNotFoundException) {
                                    classNotFoundException.printStackTrace();
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                        }
                        break;
                }
            }
        });

        //设置循环重绘(必须重绘才能反复调用MapTop中的logic()),一直刷新
        while (true) {
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    //难度选择弹窗
    public static void DifficultySelection() {
        int choice = 0;
        Object[] options = {"初级", "中级", "高级", "自定义"};
        String s = (String) JOptionPane.showInputDialog(null,
                "请选择你想选择的难度:\n",
                "难度选择",
                JOptionPane.PLAIN_MESSAGE,
                new ImageIcon("xx.png"),
                options,
                "xx");
        switch (s) {
            case "初级":
                choice = 1;
                break;
            case "中级":
                choice = 2;
                break;
            case "高级":
                choice = 3;
                break;
            case "自定义":
                choice = 4;
                break;
        }
        Difficulty(choice, 0);
    }

    static int sum = 0;

    //闯关模式
    public void BreakThrough(int level1) throws SQLException, ClassNotFoundException {
        int a = 0;
        for (int i = 0, detect = 3; i < GameUtil.LEVEL; i++, detect++) {
            a = i;
            if (detect % 3 == 0) {
                level1++;
            }
            if (GameUtil.STATE == 1 && GameUtil.LEVEL <= 4) {
                sum += GameUtil.TOTAL_TIME;
                GameUtil.STATE = 0;
                GameUtil.LEVEL++;
            } else if (GameUtil.STATE == 0) {
                sum = sum + (int) GameUtil.TOTAL_TIME * 3;
            }
            if (GameUtil.LEVEL > 4) {
                GameUtil.BreakDetec = false;
                break;
            }
            Difficulty(level1, i);
            mapTop.reGame();
            mapBottom.reGame();
            GameUtil.START_TIME = System.currentTimeMillis();
            GameUtil.STATE = 0;
            updateSize(); // 更新窗口尺寸
            setSize(Width, Height); // 重新设置窗口大小
            setLocationRelativeTo(null);
        }
        setTitle("扫雷第" + (a + 1) + "关");
        if (!GameUtil.BreakDetec) {
            setTitle("扫雷闯关模式胜利！！");
            GameUtil.HERO_SCORE = sum;
            GameUtil.HERO_NAME = JOptionPane.showInputDialog(null, "请输入你的ID：", "输入", JOptionPane.WARNING_MESSAGE);
            System.out.println(GameUtil.HERO_NAME);
            addHero();
        }
    }

    //难度选择&闯关模式
    public static void Difficulty(int choice, int level) {
        switch (choice) {
            case 1:
                GameUtil.MAP_w = 9 + 1 * level;
                GameUtil.MAP_l = 9 + 1 * level;
                GameUtil.BOMB_MAX = 10 + 2 * level;
//                GameUtil.BOMB_MAX = 2 * level;//测试
                break;
            case 2:
                GameUtil.MAP_w = 16 + 1 * level;
                GameUtil.MAP_l = 16;
                GameUtil.BOMB_MAX =40 + 2 * level;
//                GameUtil.BOMB_MAX = 2 * level;//测试
                break;
            case 3:
                GameUtil.MAP_w = 30 + 1 * level;
                GameUtil.MAP_l = 16;
                GameUtil.BOMB_MAX =99 +  2 * level;
//                GameUtil.BOMB_MAX = 2 * level;//测试
                break;
            case 4:
                // 创建面板和文本框
                JPanel panel = new JPanel(new GridLayout(3, 2));
                JTextField lengthField = new JTextField();
                JTextField widthField = new JTextField();
                JTextField heightField = new JTextField();
                // 将文本框添加到面板
                panel.add(new javax.swing.JLabel("长度:"));
                panel.add(lengthField);
                panel.add(new javax.swing.JLabel("宽度:"));
                panel.add(widthField);
                panel.add(new javax.swing.JLabel("雷数:"));
                panel.add(heightField);

                // 显示对话框
                int result = JOptionPane.showConfirmDialog(null, panel, "输入长、宽、雷数", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        // 获取用户输入的值
                        int l = (int) Double.parseDouble(lengthField.getText());
                        int w = (int) Double.parseDouble(widthField.getText());
                        int b = (int) Double.parseDouble(heightField.getText());
                        if (b < w * l) {
                            // 打印结果
                            System.out.println("长度: " + l);
                            System.out.println("宽度: " + w);
                            System.out.println("雷数: " + b);
                            GameUtil.MAP_w = w;
                            GameUtil.MAP_l = l;
                            GameUtil.BOMB_MAX = b;
                        } else {
                            JOptionPane.showMessageDialog(null, "雷数过多", "警告", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "数值不正确", "警告", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

        }
    }


    @Override//自动调用
    public void paint(Graphics g) {//定义在窗口上的画笔

        offScreenImage = createImage(Width, Height);//双缓存技术额外设置一个画板
        Graphics gImage = offScreenImage.getGraphics();
        Color b = new Color(234, 208, 181);
        gImage.setColor(b);
        gImage.fillRect(0, 0, Width, Height);
        mapBottom.PaintSelf(gImage);
        mapTop.PaintSelf(gImage);
        g.drawImage(offScreenImage, 0, 0, null);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public static void main(String[] args) {
        GameUtil.STATE = 0;
        GameWin gameWin = new GameWin();
        gameWin.launch();
    }
}
