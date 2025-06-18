package com.pixelma.calculator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.pixelma.calculator.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //TODO add google sign

        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_multi).setOnClickListener(this);
        findViewById(R.id.btn_all).setOnClickListener(this);

    }

    private void startGame(int operator) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.OPERATOR, operator);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_plus) {
            startGame(0);
        } else if (id == R.id.btn_minus) {
            startGame(1);
        } else if (id == R.id.btn_multi) {
            startGame(2);
        } else if (id == R.id.btn_divide) {
            startGame(3);
        } else if (id == R.id.btn_all) {
            startGame(-1);
        }
    }
}
