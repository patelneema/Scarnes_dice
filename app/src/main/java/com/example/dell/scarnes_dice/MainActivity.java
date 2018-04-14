package com.example.dell.scarnes_dice;


import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public int user_overall_score=0;
    public int user_turn_score=0;
    public int computer_overall_score=0;
    public int computer_turn_score=0;
    public int[] images={R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};

    Button reset,hold,roll;
    TextView status;
    ImageView diceImage;

    Timer computerTimer;

    String userScoreLabel = "Your Score : ";
    String compScoreLabel = " Computer Score : ";
    String userTurnScoreLabel = "\nYour Turn Score : ";
    String compTurnScoreLabel = "\nComputer Turn Score : ";
    int computerTurnScore;
    int userOverallScore;
    int  computerOverallScore;
    int userTurnScore;


    String labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        reset=(Button)findViewById(R.id.Button);
        hold=(Button)findViewById(R.id.button);
        roll=(Button)findViewById(R.id.button2);
        status=(TextView)findViewById(R.id.textView);

        diceImage=(ImageView)findViewById(R.id.imageView);

        roll.setOnClickListener(this);
        hold.setOnClickListener(this);
        reset.setOnClickListener(this);



    }

    public void Rollclicked()
    {
        Log.d("Click","Roll");
        int random_no=Randomno();
        diceImage.setImageResource(images[random_no]);

        random_no++;
        if(random_no==1){
            userTurnScore = 0;

            labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore + "\n you lost your chance";
            enableButtons(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            },3000);

        }
        else
        {
            userTurnScore+=random_no;
            labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore;
        }

        status.setText(labelText);

    }
    public void Holdclicked()
    {
        Log.d("Click","Hold");
        userOverallScore += userTurnScore;  //Calculate Overall score only when user loses or presses hold button
        userTurnScore = 0;

        labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore;
        status.setText(labelText);
        if(userOverallScore>=100) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setText("You Are the Winner!!!");
                    enableButtons(false);
                }
            });
            Resetclicked();
        }
        else
        {
            computerTurn();
        }

    }


    public  void Resetclicked()
    {
        Log.d("Click","Reset");
        userTurnScore=0;
        userOverallScore=0;
        computerTurnScore=0;
        computerOverallScore=0;

        labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                status.setText(labelText);
                enableButtons(true);
            }
        });


    }


    public void computerTurn(){

        computerTimer=new Timer();

        computerTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                // Disable buttons for computer chance
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableButtons(false);
                    }
                });

                int computerRolledNumber=Randomno();

                final int finalRolled=computerRolledNumber;
                //Changing thw dice Image
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        diceImage.setImageResource(images[finalRolled]);
                    }
                });

                computerRolledNumber++;

                if(computerRolledNumber==1)
                {
                    computerTurnScore=0;
                    labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore
                            + "\nComputer rolled a one and lost it's chance \nNow it's your chance";

                    //Enable Buttons and Set Label Again
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            enableButtons(true);
                            status.setText(labelText);
                        }
                    });

                    //cancel the timer, this is exiting out of function
                    computerTimer.cancel();
                }

                else {

                    computerTurnScore += computerRolledNumber;

                    labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + userTurnScoreLabel + userTurnScore
                            + "\nComputer rolled a " + computerRolledNumber
                            + compTurnScoreLabel + computerTurnScore;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            status.setText(labelText);
                        }
                    });

                    if(computerTurnScore>20)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enableButtons(true);
                            }
                        });


                        computerOverallScore += computerTurnScore;
                        computerTurnScore = 0;
                        labelText = userScoreLabel + userOverallScore + compScoreLabel + computerOverallScore + "\nComputer holds \nNow Your chance";

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                status.setText(labelText);
                            }
                        });
                        if(computerOverallScore>100)
                        {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    labelText="Computer : I won the Game !!!";
                                    status.setText(labelText);
                                    enableButtons(false);
                                }
                            });


                            Resetclicked();

                        }
                        computerTimer.cancel();

                    }
                }

            }//end of run//
        },0,3000);//end of timer

    }

    private void enableButtons(boolean isEnabled) {
        roll.setEnabled(isEnabled);
        hold.setEnabled(isEnabled);
    }
    public static int Randomno()
    {
        Random random = new Random();
        int randomNumber = random.nextInt(6);
        return randomNumber;
    }

    @Override
    public void onClick(View view) {
        int v_id=view.getId();
        if(v_id==R.id.Button)
        {
            Rollclicked();
        }
        if(v_id==R.id.button)
        {
            Holdclicked();
        }
        if(v_id==R.id.button2)
        {
            Resetclicked();
        }
    }

}


