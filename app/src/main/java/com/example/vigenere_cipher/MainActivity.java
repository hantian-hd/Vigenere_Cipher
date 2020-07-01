package com.example.vigenere_cipher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //deklarasi variable
    EditText keyEdit;
    EditText pesanEdit;
    Button enkripsiBtn;
    Button dekripsiBtn;
    Button copyBtn;
    TextView hasil;

    String key;
    String pesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //definisi
        keyEdit = (EditText)findViewById(R.id.editKey);
        pesanEdit = (EditText)findViewById(R.id.editPesan);
        hasil = (TextView)findViewById(R.id.txtHasilVigenere);

        enkripsiBtn = (Button)findViewById(R.id.btn_Enkripsi);
        dekripsiBtn = (Button)findViewById(R.id.btn_Dekripsi);
        copyBtn = (Button)findViewById(R.id.btn_Copy);

        //respon pada button
        responToButton();
    }

    public void responToButton() {
        // pencet button enkripsi
        enkripsiBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = keyEdit.getText().toString();
                pesan = pesanEdit.getText().toString();
                hasil.setText(enkripsi());
            }
        }));
        // button dekripsi
        dekripsiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key = keyEdit.getText().toString();
                pesan = pesanEdit.getText().toString();
                hasil.setText(dekripsi());
            }
        });
        // button copy untuk copy hasil enkrip/dekrip ke clipboard
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",hasil.getText());
                clipboard.setPrimaryClip(clip);

                //Toast
                Context context = getApplicationContext();
                CharSequence text = "Copied to Clipboard";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public String enkripsi(){
        // panjang string plaintext & key dan bisa digunakan sebagai index
        int plainttextPanjang = pesan.toCharArray().length;
        int keyPanjang = key.toCharArray().length;
        // membuat teksnya sebagai char array, menjadi kodeascii
        char [] plaintext = new char[plainttextPanjang]; //plainteks
        plaintext = pesan.toCharArray();
        char [] keykunci = new char[keyPanjang]; //key
        keykunci = key.toUpperCase().toCharArray();
        char [] encrypted = new char[plainttextPanjang]; //hasil enkripsi nantinya

        // loop digunakan untuk menggeser setiap huruf
        for(int i=0;i<plainttextPanjang;i++){
            //-32 (perbedaan antara 'A' dan 'a') jika diperlukan, memberikan sensitivitas huruf.
            // Ci = (Pi + Ki mod m )mod 26
            // huruf KAPITAL semua
            if((plaintext[i] >= 65 && plaintext[i]<=90) && (keykunci[i%(keyPanjang)] >= 65 && keykunci[i%(keyPanjang)]<=90)){
                encrypted[i]= (char) (65+(plaintext[i]+keykunci[i%(keyPanjang)])%26);
            }
            // pesan huruf kecil
            else if((plaintext[i] < 65 || plaintext[i]>90) && (keykunci[i%(keyPanjang)] < 65 || keykunci[i%(keyPanjang)]>90)){
                encrypted[i]= (char) (65+(plaintext[i]-32+keykunci[i%(keyPanjang)]-32)%26);
            }
            // selain alfabet
            else
                encrypted[i]= (char) (65+(plaintext[i]+keykunci[i%(keyPanjang)]-32)%26);
        }
        return new String(encrypted);
    }

    public String dekripsi(){
        // panjang string cipher & key dan bisa digunakan sebagai index
        int encryptedPanjang  = pesan.toCharArray().length;
        int keyPanjang = key.toCharArray().length;
        // membuat teksnya sebagai char array, menjadi kodeascii
        char [] encrypted = new char[encryptedPanjang];;
        encrypted = pesan.toCharArray();
        char [] keykunci = new char[keyPanjang];
        keykunci = key.toUpperCase().toCharArray();
        char [] decrypted = new char[encryptedPanjang]; // teks hasil dekripsi nantinya

        // loop digunakan untuk menggeser setiap huruf
        for(int i=0;i<encryptedPanjang;i++){
            // +26 untuk antisipasi kalau ada kasus hasil negatif
            // Pi = (Ci - Ki mod m )mod 26
            // huruf KAPITAL
            if((encrypted[i] >= 65 && encrypted[i]<=90) && (keykunci[i%(keyPanjang)] >= 65 && keykunci[i%(keyPanjang)]<=90)){
                decrypted[i]= (char) (65+(encrypted[i]-keykunci[i%(keyPanjang)]+26)%26);
            }
            // pesan huruf kecil
            else if((encrypted[i] < 65 || encrypted[i]>90) && (keykunci[i%(keyPanjang)] < 65 || keykunci[i%(keyPanjang)]>90)){
                decrypted[i]= (char) (65+(encrypted[i]-32-keykunci[i%(keyPanjang)]-32+26)%26);
            }
            // selain alfabet
            else
                decrypted[i]= (char) (65+(encrypted[i]-keykunci[i%(keyPanjang)]-32+26)%26);
        }

        //System.out.println(decrypted); //test output
        return new String(decrypted);
    }
}