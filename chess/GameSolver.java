import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public abstract class GameSolver {
 public abstract Edge getNextMove(final Board board, int color );
}
class RandomSolver extends GameSolver {

    public  ArrayList<Integer> n = new ArrayList<Integer>();
    public  ArrayList<Integer> m = new ArrayList<Integer>();
    @Override
    public Edge getNextMove(final Board board, int color) {
        ArrayList<Edge> moves = board.getAvailableMoves();


        Collections.sort(n);

        ArrayList<Edge> e = getMaxBoxMove(board,color);
        ArrayList<Edge> a = getBoxMove(board,color);
        ArrayList<Edge> b = getSafeMove(board,color);
        ArrayList<Edge> c = getMinDangerMove(board,color);
            if (!a.isEmpty()){
                if(!m.isEmpty()&&!n.isEmpty()){
                    if (m.get(0)==2&&n.get(0)==1){
                        if (n.size()>1){
                            if (n.get(1)>m.get(0))moves=c;
                            else moves=a;
                        }else moves=a;
                    }
                    else {
                        moves=a;
                    }
                }

            }

        if (a.isEmpty()){
            moves=b;
        }

        if (a.isEmpty()&&b.isEmpty()){
            moves=c;
        }
        n.clear();
        m.clear();

        return moves.get(new Random().nextInt(moves.size()));

    }

    public  ArrayList<Edge>  getBoxMove(Board board, int color){//在所有可走的边中选出可以得分的边
        Board board1 = board;
        ArrayList<Edge> moves = board.getAvailableMoves();
        ArrayList<Edge> bMoves = new ArrayList<Edge>();
        for (Edge move : moves) {
            Board newBoard = board1.getNewBoard(move, color);
            if (newBoard.getScore(color) > board.getScore(color)){
                bMoves.add(move);
            }
            //board1.lostNewBoard(move, color);
        }
        return bMoves;
    }

    public  ArrayList<Edge>  getSafeMove(Board board, int color){
        Board board1 = board;
        ArrayList<Edge> moves = board.getAvailableMoves();//获取可走的边
        ArrayList<Edge> bMoves = new ArrayList<Edge>();
        for (Edge i : moves) {
            Board newBoard = board1.getNewBoard(i, color);
            ArrayList<Edge> x = newBoard.getAvailableMoves();//模拟所有可走的边中得到的新局面中可走的边
            bMoves.add(i);
            for (Edge j : x) {
                Board newBoard1 = newBoard.getNewBoard(j, color);
                if (newBoard1.getScore(color) > newBoard.getScore(color)) {//极大极小值算法（二层搜索树）删除所有第二步可以让对手得分的走法
                    bMoves.remove(i);
                }
                //newBoard.lostNewBoard(j, color);
            }
           // board1.lostNewBoard(i, color);
        }
        return bMoves;
    }

    public  ArrayList<Edge>  getDangerMove(Board board, int color){
        Board board1 = board;
        ArrayList<Edge> moves = board.getAvailableMoves();
        ArrayList<Edge> bMoves = new ArrayList<Edge>();
        for (Edge i : moves) {
            Board newBoard = board1.getNewBoard(i, color);
            ArrayList<Edge> x = newBoard.getAvailableMoves();
            for (Edge j : x) {
                Board newBoard1 = newBoard.getNewBoard(j, color);
                if (newBoard1.getScore(color) > newBoard.getScore(color)) {
                    bMoves.add(i);
                }
            }
        }
        return bMoves;
    }


    public  ArrayList<Edge>  getMinDangerMove(Board board, int color){
        Board board1 = board;
        ArrayList<Edge> moves = getDangerMove(board1,color);
        ArrayList<Edge> bMoves = new ArrayList<Edge>();
        ArrayList<Edge> x ;
        n = new ArrayList<Integer>();
        int a = 0;
        for (Edge move : moves){
            Board newBoard = board1.getNewBoard(move,color);
            while (true){
                x=getBoxMove(newBoard,color);
                if (x.isEmpty()) break;
                a++;
                newBoard = newBoard.getALLNewBoard(newBoard,x.get(0),color);
            }
            if (n.isEmpty()){
                bMoves.add(move);
                n.add(a);
            }
            if (a<n.get(0)){
                bMoves.remove(0);
                bMoves.add(move);
                //n.remove(0);
            }
            if (a!=n.get(0)){
                n.add(a);
            }
            Collections.sort(n);
            a=0;
        }
        return bMoves;
    }

    public  ArrayList<Edge>  getMaxBoxMove(Board board, int color){//求出最大可以连续得分的走法
        Board board1 = board;//参数棋盘
        ArrayList<Edge> moves = getBoxMove(board1,color);//获得参数棋盘中可以得分的边
        if (moves.isEmpty()) return moves;
        //ArrayList<Edge> bMoves = new ArrayList<Edge>();
        m = new ArrayList<Integer>();
        ArrayList<Edge> x ;
        int b = 1;
        Board newBoard = board1.getNewBoard(moves.get(0),color);//在所有选中边中模拟下一步走法
        while (true){
            x=getBoxMove(newBoard,color);//在模拟出的棋盘中继续找出可以得分的边 二层搜索
            if (x.isEmpty()) break;
            b++;
            newBoard = newBoard.getALLNewBoard(newBoard,x.get(0),color);
        }
        m.add(b);
        return moves;
    }



}