import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private int n;
    private GameSolver redSolver, blueSolver;
    private String redName, blueName;

    private JFrame frame;//窗口
    private JLabel modeError, sizeError;//标签（功能错误 大小错误）

    String[] players = {"挑选玩家", "玩家",  "人机"};
    private JRadioButton[] sizeButton;//单选按钮组件

    JComboBox<String> redList, blueList;//列表框
    ButtonGroup sizeGroup;//按钮组

    public Main() {

        frame = new JFrame();//窗口实例化
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗口关闭
        redList = new JComboBox<String>(players);//红色列表框实例化
        blueList = new JComboBox<String>(players);//蓝色列表框实例化

        sizeButton = new JRadioButton[8];//设置9个按钮
        sizeGroup = new ButtonGroup();//按钮组实例化
        for(int i=0; i<8; i++) {
            String size = String.valueOf(i+3);
            sizeButton[i] = new JRadioButton(size + " x " + size);
            sizeGroup.add(sizeButton[i]);//将按钮添加在按钮组里
        }
    }

    private JLabel getEmptyLabel(Dimension d) {//标签实例化 设定标签大小
        JLabel label = new JLabel();
        label.setPreferredSize(d);
        return label;
    }

    private boolean startGame;//开始游戏

    private GameSolver getSolver(int level) {//选择对手
        if(level == 1) return new RandomSolver();
        else return null;
    }

    private ActionListener submitListener = new ActionListener() {//监听按钮
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int rIndex = redList.getSelectedIndex();//选择红方玩家的顺序，从零开始
            int bIndex = blueList.getSelectedIndex();//选择蓝方玩家的顺序，从零开始
            if(rIndex==0 || bIndex==0) {
                modeError.setText("在继续之前，您必须选择玩家.");//玩家模式报错
                return;
            }
            else {
                modeError.setText("");//有无没区别
                redName = players[rIndex];
                blueName = players[bIndex];
                if(rIndex > 1) redSolver = getSolver(rIndex - 1);
                if(bIndex > 1) blueSolver = getSolver(bIndex - 1);
            }
            for(int i=0; i<8; i++) {//选择模式
                if(sizeButton[i].isSelected()) {
                    n = i+3;
                    startGame = true;
                    return;
                }
            }
            sizeError.setText("在继续之前，您必须选择棋盘的大小.");//选择模式报错
        }
    };

    public void initGUI() {

        redSolver = null;
        blueSolver = null;

        JPanel grid = new JPanel(new GridBagLayout());//面板容器实例化
        GridBagConstraints constraints = new GridBagConstraints();//布局实例化，网格布局管理器

        constraints.gridx = 0;//水平，第0行单元格
        constraints.gridy = 0;//垂直，第0列单元格
        JLabel titleLabel = new JLabel(new ImageIcon(getClass().getResource("title.png")));//提取图片
        grid.add(titleLabel, constraints);//加入容器组件，将图片加入容器
        
        ++constraints.gridy;//垂直位置加一
        grid.add(getEmptyLabel(new Dimension(500,25)), constraints);//容器区域大小

        modeError = new JLabel("", SwingConstants.CENTER);//玩家模式报错容器
        modeError.setForeground(Color.RED);//设置颜色为红色（字体颜色）
        modeError.setPreferredSize(new Dimension(500, 25));//容器区域大小
        ++constraints.gridy;//垂直位置加一
        grid.add(modeError, constraints);//添加玩家模式报错容器

        JPanel modePanel = new JPanel(new GridLayout(2, 2));//新的容器实例化 （两行两列）
        modePanel.setPreferredSize(new Dimension(400, 50));//容器区域大小
        modePanel.add(new JLabel("<html><font color='red'>Player-1:", SwingConstants.CENTER));//红色玩家显示区域
        modePanel.add(new JLabel("<html><font color='blue'>Player-2:", SwingConstants.CENTER));//蓝色玩家显示区域
        modePanel.add(redList);//添加红色列表
        modePanel.add(blueList);//添加蓝色列表
        redList.setSelectedIndex(0);//红色列表列表显示第一个
        blueList.setSelectedIndex(0);//蓝色色列表列表显示第一个
        ++constraints.gridy;//继续垂直加一
        grid.add(modePanel, constraints);//添加游戏模式容器按钮

        ++constraints.gridy;//继续垂直位置加一
        grid.add(getEmptyLabel(new Dimension(500,25)), constraints);//添加显示区域

        sizeError = new JLabel("", SwingConstants.CENTER);//游戏模式报错区域
        sizeError.setForeground(Color.RED);//游戏模式的颜色
        sizeError.setPreferredSize(new Dimension(500, 25));//添加显示区域
        ++constraints.gridy;//继续垂直位置加一
        grid.add(sizeError, constraints);//添加游戏模式报错区域

        ++constraints.gridy;//继续垂直位置加一
        JLabel messageLabel = new JLabel("选择棋盘的大小：");
        messageLabel.setPreferredSize(new Dimension(400, 50));
        grid.add(messageLabel, constraints);

        JPanel sizePanel = new JPanel(new GridLayout(4, 2));//四行两列的容器
        sizePanel.setPreferredSize(new Dimension(400, 100));//显示区域大小
        for(int i=0; i<8; i++)
            sizePanel.add(sizeButton[i]);
        sizeGroup.clearSelection();
        ++constraints.gridy;
        grid.add(sizePanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

        JButton submitButton = new JButton("开始游戏");
        submitButton.addActionListener(submitListener);//添加监听按钮
        ++constraints.gridy;
        grid.add(submitButton, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

        frame.setContentPane(grid);//容器加入窗口
        frame.pack();//Frame.pack()是JAVA语言的一个函数，这个函数的作用就是根据窗口里面的布局及组件的preferredSize来确定frame的最佳大小。
        frame.setLocationRelativeTo(null);//居中显示
        frame.setVisible(true);//窗体可见

        startGame = false;
        while(!startGame) {
            try {
                Thread.sleep(100);//休眠100毫秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new GamePlay(this, frame, n, redSolver, blueSolver, redName, blueName);
    }

    public static void main(String[] args) {
        new Main().initGUI();
    }

}
