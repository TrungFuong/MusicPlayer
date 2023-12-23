package com.example.project;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.icu.text.CaseMap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //khai báo
    TextView txtTitle, txtTimeSong, txtTimeTotal;
    SeekBar skSong;
    ImageView cd;
    ImageButton btPrev, btPlay, btStop, btNext;
    Switch switchLoop;
    //Tạo mảng chứa bài hát
    ArrayList<Song> arraySong;
    int position = 0;
    MediaPlayer mediaPlayer;
    Animation animation;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuoption, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.timKiem:
//                timkiem();
//                break;
            case R.id.count:
                count();
                break;
            case R.id.exit:
                exit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        AddSong();
        animation = AnimationUtils.loadAnimation(this, R.anim.cd_rotation);
        khoiTaoMediaPlayer();
//        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
//        txtTitle.setText(arraySong.get(position).getTitle());
        //BỊ GỌI LẠI!!!! --> TÁCH FUNCTION RIÊNG???
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                btPlay.setImageResource(R.drawable.play);
                khoiTaoMediaPlayer();
            }
        });
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    //Đang phát nhạc thì pause, imageBtn Play --> imageBtnPause
                    mediaPlayer.pause();
                    btPlay.setImageResource(R.drawable.play);
                } else {
                    //Đang pause thì play, đổi
                    mediaPlayer.start();
                    btPlay.setImageResource(R.drawable.pause);
                }
                setTimeTotal();
                getTimeSong();
                cd.startAnimation(animation);
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position > arraySong.size() - 1) {
                    position = 0;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                btPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                getTimeSong();
                Loop();
            }
        });
        btPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if (position < 0) {
                    position = arraySong.size() - 1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                khoiTaoMediaPlayer();
                mediaPlayer.start();
                btPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                getTimeSong();
                Loop();
            }
        });
        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Khi kéo thì cập nhật giá trị liên tục
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Giá trị khi bắt đầu nhấn vào
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Giá trị khi buông tay
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
    }

    private void setTimeTotal() {
        SimpleDateFormat dinhDangThoiGian = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhDangThoiGian.format(mediaPlayer.getDuration())); //getDuration trả thời gian
        //seekbar chạy từ min -> max --> seekbar max = getDuration
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void getTimeSong() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangThoiGian = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhDangThoiGian.format(mediaPlayer.getCurrentPosition()));
                //liên tục cập nhật thời gian chạy để seekbar chạy theo
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                //tự động chuyển bài
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer medPlayer) {
                        position++;
                        if (position > arraySong.size() - 1) {
                            position = 0;
                        }
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        khoiTaoMediaPlayer();
                        mediaPlayer.start();
                        btPlay.setImageResource(R.drawable.pause);
                        setTimeTotal();
                        getTimeSong();
                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    private void Loop() {
        switchLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mediaPlayer.setLooping(isChecked);
            }
        });
    }

//    public void timkiem() {
//        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        View v = LayoutInflater.from(this).inflate(R.layout.search, null);
//        EditText songName = v.findViewById(R.id.in_song);
//        b.setView(v);
//        b.setPositiveButton("FIND", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                String songName = arraySong.get(position).getTitle().toString();
//                boolean timkiem = false;
//                int j;
//                for (j = 0; j < arraySong.size(); j++) {
//                    if (arraySong.get(j).getTitle().equals(songName)) {
//                        timkiem = true;
//                        break;
//                    }
//                }
//                if (timkiem == true) {
//                    position = j;
//                    khoiTaoMediaPlayer();
//                    timkiem = false;
//                } else {
//                    Toast.makeText(MainActivity.this, "Khong tim thay!", Toast.LENGTH_SHORT);
//                }
//            }
//        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//            }
//        }).show();
//    }

    private void count() {
        Toast.makeText(this, "Trong máy có " + arraySong.size() + " bài hát", Toast.LENGTH_SHORT).show();
    }

    private void exit(){
        Intent i = new Intent(MainActivity.this, Login.class);
        startActivity(i);
    }
    private void khoiTaoMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
        Loop();
    }

    private void AddSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Chưa bao giờ", R.raw.chua_bao_gio));
        arraySong.add(new Song("Đoạn kết mới", R.raw.doan_ket_moi));
        arraySong.add(new Song("Đôi lời tình ca", R.raw.doi_loi_tinh_ca));
        arraySong.add(new Song("Đứa nào làm em buồn", R.raw.dua_nao_lam_em_buon));
        arraySong.add(new Song("Ném câu yêu vào trong không trung", R.raw.nem_cau_yeu_vao_trong_khong_trung));
        arraySong.add(new Song("Nép vào anh và nghe anh hát", R.raw.nep_vao_anh_va_nghe_anh_hat));
        arraySong.add(new Song("Ngủ đi để thấy nhau còn hồn nhiên", R.raw.ngu_di_de_thay_nhau_con_hon_nhien));
    }

    //Ánh xạ
    private void AnhXa() {
        txtTimeSong = findViewById(R.id.textViewTimeSong);
        txtTimeTotal = findViewById(R.id.textViewTimeTotal);
        txtTitle = findViewById(R.id.textviewTitle);
        skSong = findViewById(R.id.seekBar);
        btNext = (ImageButton) findViewById(R.id.buttonNext);
        btPlay = (ImageButton) findViewById(R.id.buttonPlay);
        btStop = (ImageButton) findViewById(R.id.buttonStop);
        btPrev = (ImageButton) findViewById(R.id.buttonPrev);
        cd = (ImageView) findViewById(R.id.imageViewCD);
        switchLoop = (Switch) findViewById(R.id.switchLoop);
    }
}