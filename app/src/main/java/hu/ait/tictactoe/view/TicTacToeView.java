package hu.ait.tictactoe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Chronometer;

import hu.ait.tictactoe.MainActivity;
import hu.ait.tictactoe.R;
import hu.ait.tictactoe.model.TicTacToeModel;

public class TicTacToeView extends View {

    private Paint paintBg;
    private Paint paintLine;
    private Paint paintText;

    private Paint paintCirle;
    private Paint paintCross;

    private Bitmap bitmapBg;

    private short winner;

    private long time1WhenStopped = 0;
    private long time2WhenStopped = 0;

    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(Color.BLACK);
        paintBg.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintText = new Paint();
        paintText.setColor(Color.RED);

        paintCirle = new Paint();
        paintCirle.setColor(Color.MAGENTA);
        paintCirle.setStyle(Paint.Style.STROKE);
        paintCirle.setStrokeWidth(5);

        paintCross = new Paint();
        paintCross.setColor(Color.BLUE);
        paintCross.setStyle(Paint.Style.STROKE);
        paintCross.setStrokeWidth(5);

        bitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.sky_background);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmapBg = Bitmap.createScaledBitmap(bitmapBg, getWidth(), getHeight(), false);
        paintText.setTextSize(getHeight() / 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        canvas.drawBitmap(bitmapBg, 0, 0, null);

        drawGameBoard(canvas);

        drawPlayers(canvas);
    }

    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TicTacToeModel.getInstance().getFieldContent(i, j) == TicTacToeModel.CIRCLE) {

                    // draw a circle at the center of the field

                    // X coordinate: left side of the square + half width of the square
                    float centerX = i * getWidth() / 3 + getWidth() / 6;
                    float centerY = j * getHeight() / 3 + getHeight() / 6;
                    int radius = getHeight() / 6 - 2;

                    canvas.drawCircle(centerX, centerY, radius, paintCirle);

                } else if (TicTacToeModel.getInstance().getFieldContent(i, j) == TicTacToeModel.CROSS) {
                    canvas.drawLine(i * getWidth() / 3, j * getHeight() / 3,
                            (i + 1) * getWidth() / 3,
                            (j + 1) * getHeight() / 3, paintCross);

                    canvas.drawLine((i + 1) * getWidth() / 3, j * getHeight() / 3,
                            i * getWidth() / 3, (j + 1) * getHeight() / 3, paintCross);
                }
            }
        }
    }

    private void drawGameBoard(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);

        canvas.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3, paintLine);

        canvas.drawLine(0, getHeight() / 3 * 2, getWidth(), getHeight() / 3 * 2, paintLine);

        canvas.drawLine(getWidth() / 3, 0, getWidth() / 3, getHeight(), paintLine);

        canvas.drawLine(getWidth() / 3 * 2, 0, getWidth() / 3 * 2, getHeight(), paintLine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (winner != TicTacToeModel.EMPTY) {
            if (winner == -1) {
                ((MainActivity) getContext()).setPlayerText("Game is draw!");
            } else {
                ((MainActivity) getContext()).setPlayerText("Player " + winner + " won!");
            }
            ((MainActivity) getContext()).getTimePlayer1().stop();
            ((MainActivity) getContext()).getTimePlayer2().stop();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tX = ((int) event.getX()) / (getWidth() / 3);
            int tY = ((int) event.getY()) / (getHeight() / 3);


            if (TicTacToeModel.getInstance().getFieldContent(tX, tY) == TicTacToeModel.EMPTY) {
                TicTacToeModel.getInstance().makeMove(tX, tY, TicTacToeModel.getInstance().getNextPlayer());

                TicTacToeModel.getInstance().changeNextPlayer();
                invalidate(); // redraw everything on View

                String next = "O";
                Chronometer timer1 = ((MainActivity) getContext()).getTimePlayer1();
                Chronometer timer2 = ((MainActivity) getContext()).getTimePlayer2();
                if (TicTacToeModel.getInstance().getNextPlayer() == TicTacToeModel.CROSS) {
                    next = "X";

                    time1WhenStopped = timer1.getBase() - SystemClock.elapsedRealtime();
                    timer1.stop();

                    timer2.setBase(SystemClock.elapsedRealtime() + time2WhenStopped);
                    timer2.start();
                }
                else{
                    time2WhenStopped = timer2.getBase() - SystemClock.elapsedRealtime();
                    timer2.stop();

                    timer1.setBase(SystemClock.elapsedRealtime() + time1WhenStopped);
                    timer1.start();
                }

                ((MainActivity) getContext()).setPlayerText(getResources().getString(R.string.next_player, next));
            }

            winner = TicTacToeModel.getInstance().checkWinner();
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    public void resetGame() {
        TicTacToeModel.getInstance().resetModel();
        ((MainActivity) getContext()).setPlayerText("Touch the game area to play");

        winner = TicTacToeModel.EMPTY;

        ((MainActivity) getContext()).getTimePlayer1().stop();
        ((MainActivity) getContext()).getTimePlayer2().stop();
        ((MainActivity) getContext()).getTimePlayer1().setBase(SystemClock.elapsedRealtime());
        time1WhenStopped = 0;
        ((MainActivity) getContext()).getTimePlayer2().setBase(SystemClock.elapsedRealtime());
        time2WhenStopped = 0;
        invalidate();
    }
}