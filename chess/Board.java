import java.awt.Point;
import java.util.ArrayList;

public class Board implements Cloneable {

    final static int RED = 0;
    final static int BLUE = 1;
    final static int BLACK = 2;
    final static int BLANK = 3;

    private int[][] hEdge;
    private int[][] vEdge;
    private int[][] box;
    private int n, redScore, blueScore;

    public Board(int n) {//设置棋盘
        hEdge = new int[n-1][n];//纵向边
        vEdge = new int[n][n-1];//横向边
        box = new int[n-1][n-1];//封闭区域
        fill(hEdge,BLANK);
        fill(vEdge,BLANK);
        fill(box,BLANK);
        this.n = n;
        redScore = blueScore = 0;
    }

    public Board clone() {//复制棋盘的空白
        Board cloned = new Board(n);

        for(int i=0; i<(n-1); i++)
            for(int j=0; j<n; j++)
                cloned.hEdge[i][j] = hEdge[i][j];

        for(int i=0; i<n; i++)
            for(int j=0; j<(n-1); j++)
                cloned.vEdge[i][j] = vEdge[i][j];

        for(int i=0; i<(n-1); i++)
            for(int j=0; j<(n-1); j++)
                cloned.box[i][j] = box[i][j];

        cloned.redScore = redScore;
        cloned.blueScore = blueScore;

        return cloned;
    }

    private void fill(int[][] array, int val) {//填充棋盘颜色
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j]=val;
    }

    public int getSize() { return n; }

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getScore(int color) {
        if(color == RED) return redScore;
        else return blueScore;
    }

    public static int toggleColor(int color) {//换棋的颜色（更换执棋人的函数）
        if(color == RED)
            return BLUE;
        else
            return RED;
    }

    public ArrayList<Edge> getAvailableMoves() {//获取空边
        ArrayList<Edge> ret = new ArrayList<Edge>();
        for(int i=0; i<(n-1);i++)
            for(int j=0; j<n; j++)
                if(hEdge[i][j] == BLANK)
                    ret.add(new Edge(i,j,true));
        for(int i=0; i<n; i++)
            for(int j=0; j<(n-1); j++)
                if(vEdge[i][j] == BLANK)
                    ret.add(new Edge(i,j,false));
        return ret;
    }

    public ArrayList<Point> setHEdge(int x, int y, int color) {//成盒的条件(走的是横边)
        hEdge[x][y]=BLACK;
        ArrayList<Point> ret = new ArrayList<Point>();
        if(y<(n-1) && vEdge[x][y]==BLACK && vEdge[x+1][y]==BLACK && hEdge[x][y+1]==BLACK) {
            //(x,y)这条横边的右上方的横边，右下角的横边以及右边的纵边
            box[x][y]=color;
            ret.add(new Point(x,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        if(y>0 && vEdge[x][y-1]==BLACK && vEdge[x+1][y-1]==BLACK && hEdge[x][y-1]==BLACK) {
            //(x,y)这条纵边的左上角的横边，左下角的横边以及坐边的纵边
            box[x][y-1]=color;
            ret.add(new Point(x,y-1));
            if(color == RED) redScore++;
            else blueScore++;
        }
        return ret;
    }

    public ArrayList<Point> setVEdge(int x, int y, int color) {//成盒的条件(走的是纵边)
        vEdge[x][y]=BLACK;
        ArrayList<Point> ret = new ArrayList<Point>();
        if(x<(n-1) && hEdge[x][y]==BLACK && hEdge[x][y+1]==BLACK && vEdge[x+1][y]==BLACK) {
            box[x][y]=color;
            ret.add(new Point(x,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        if(x>0 && hEdge[x-1][y]==BLACK && hEdge[x-1][y+1]==BLACK && vEdge[x-1][y]==BLACK) {
            box[x-1][y]=color;
            ret.add(new Point(x-1,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        return ret;
    }

    public ArrayList<Point> setPrecedeStatus(Edge edge, int color){
        int x = edge.getX(),y=edge.getY();
        ArrayList<Point> ret = new ArrayList<Point>();
        if(edge.isHorizontal()){
            if(y<(n-1) && vEdge[x][y]==BLACK && vEdge[x+1][y]==BLACK && hEdge[x][y+1]==BLACK) {
                box[x][y]=BLANK;
                ret.add(new Point(x,y));
                if(color == RED) redScore--;
                else blueScore--;
            }
            if(y>0 && vEdge[x][y-1]==BLACK && vEdge[x+1][y-1]==BLACK && hEdge[x][y-1]==BLACK) {
                //(x,y)这条纵边的左上角的横边，左下角的横边以及坐边的纵边
                box[x][y-1]=BLANK;
                ret.add(new Point(x,y-1));
                if(color == RED) redScore--;
                else blueScore--;
            }
            hEdge[x][y]=BLANK;
        }
        else{
            if(x<(n-1) && hEdge[x][y]==BLACK && hEdge[x][y+1]==BLACK && vEdge[x+1][y]==BLACK) {
                box[x][y]=BLANK;
                ret.add(new Point(x,y));
                if(color == RED) redScore--;
                else blueScore--;
            }
            if(x>0 && hEdge[x-1][y]==BLACK && hEdge[x-1][y+1]==BLACK && vEdge[x-1][y]==BLACK) {
                box[x-1][y]=BLANK;
                ret.add(new Point(x-1,y));
                if(color == RED) redScore--;
                else blueScore--;
            }
            vEdge[x][y]=BLANK;
        }
        return ret;
    }


    public boolean isComplete() {//游戏完成的条件
        return (redScore + blueScore) == (n - 1) * (n - 1);
    }

    public int getWinner() {//输赢的条件
        if(redScore > blueScore) return RED;
        else if(redScore < blueScore) return BLUE;
        else return BLANK;
    }

    public Board getNewBoard(Edge edge, int color) {
        Board ret = clone();
        if(edge.isHorizontal())
            ret.setHEdge(edge.getX(), edge.getY(), color);
        else
            ret.setVEdge(edge.getX(), edge.getY(), color);
        return ret;
    }

    private int getEdgeCount(int i, int j) {//盒周围的角的数量
        int count = 0;
        if(hEdge[i][j] == BLACK) count++;
        if(hEdge[i][j+1] == BLACK) count++;
        if(vEdge[i][j] == BLACK) count++;
        if(vEdge[i+1][j] == BLACK) count++;
        return count;
    }

    public int getBoxCount(int nSides) {//总共有多少个盒
        int count = 0;
        for(int i=0; i<(n-1); i++)
            for(int j=0; j<(n-1); j++) {
                if(getEdgeCount(i, j) == nSides)
                    count++;
            }
        return count;
    }

    public Board getALLNewBoard( Board board,Edge edge, int color) {
        Board ret = board;
        if(edge.isHorizontal())
            ret.setHEdge(edge.getX(), edge.getY(), color);
        else
            ret.setVEdge(edge.getX(), edge.getY(), color);
        return ret;
    }
}
