package hu.ait.tictactoe;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import hu.ait.tictactoe.view.TicTacToeView;

public class MainActivity extends AppCompatActivity {

    private TextView tvPlayer;

    private Chronometer timePlayer1;

    private Chronometer timePlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPlayer = (TextView) findViewById(R.id.tvPlayer);

        timePlayer1 = (Chronometer) findViewById(R.id.player1_time);
        timePlayer1.setBase(SystemClock.elapsedRealtime());

        timePlayer2 = (Chronometer) findViewById(R.id.player2_time);
        timePlayer2.setBase(SystemClock.elapsedRealtime());

        final TicTacToeView gameView = (TicTacToeView) findViewById(R.id.gameView);

        Button btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.resetGame();
            }
        });

        ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) findViewById(
                R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();
    }

    public void setPlayerText(String text) {
        tvPlayer.setText(text);
    }

    public Chronometer getTimePlayer1(){
        return timePlayer1;
    }

    public Chronometer getTimePlayer2(){
        return timePlayer2;
    }
}

