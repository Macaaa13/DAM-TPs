package com.example.tp1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText titulo, direccRet, precio, correo;
    private Spinner spinnerCategorias;
    private Switch switchDescuento;
    private SeekBar seekBarDescuento;
    private TextView porcentaje, direccionRetiro, tvTitulo, tvPrecio, tvCorreo;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private CheckBox retiro, terminos;
    private Button publicar;
    private String textoPlanoValido, precioActual, mailValido;
    private Boolean direccionRetiroActivada = false, ofrecerEnvioActivado = false;
    private Integer p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Para las validaciones
        textoPlanoValido = (String) getResources().getText(R.string.textoPlanoValido);
        mailValido = (String) getResources().getText(R.string.correoValido);

        //Spinner para Categorias
        spinnerCategorias = (Spinner) findViewById(R.id.spinnerCategoria);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(spinnerAdapter);

        //Switch para Ofrecer Descuento de Envio
        porcentaje = (TextView) findViewById(R.id.textViewPorcentaje);
        seekBarDescuento = (SeekBar) findViewById(R.id.seekBarDescuento);
        switchDescuento = (Switch) findViewById(R.id.switchDescuentoEnvio);
        switchDescuento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    seekBarDescuento.setVisibility(View.VISIBLE);
                    porcentaje.setVisibility(View.VISIBLE);
                    ofrecerEnvioActivado = true;
                } else{
                    seekBarDescuento.setProgress(0);
                    seekBarDescuento.setVisibility(View.GONE);
                    porcentaje.setText("0/100");
                    porcentaje.setVisibility(View.GONE);
                    ofrecerEnvioActivado = false;
                }
            }
        });

        //SeekBar para Porcentaje de Descuento
        seekBarDescuento.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                p = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { porcentaje.setText(p+"/100");
            }
        });

        //Checkbox para Retiro en Persona
        direccionRetiro = (TextView) findViewById(R.id.textViewDireccionRetiro);
        direccRet = (EditText) findViewById(R.id.editTextDireccionRetiro);
        retiro = (CheckBox) findViewById(R.id.checkBoxRetiro);
        retiro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    direccionRetiro.setVisibility(View.VISIBLE);
                    direccRet.setVisibility(View.VISIBLE);
                    direccionRetiroActivada = true;
                } else{
                    direccionRetiro.setVisibility(View.GONE);
                    direccRet.setVisibility(View.GONE);
                    direccionRetiroActivada = false;
                }
            }
        });

        //Checkbox para Terminos y Condiciones
        publicar = (Button) findViewById(R.id.buttonPublicar);
        terminos = (CheckBox) findViewById(R.id.checkBoxTerminos);
        terminos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    publicar.setEnabled(true);
                } else{
                    publicar.setEnabled(false);
                }
            }
        });

        //Boton para Publicar y Validaciones
        publicar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int camposIncorrectos = 0;
                //--- Titulo ---
                titulo = (EditText) findViewById(R.id.editTextTitulo);
                tvTitulo = (TextView) findViewById(R.id.textViewTitulo);
                if(!Pattern.matches(textoPlanoValido, titulo.getText())){
                    tvTitulo.setTextColor(Color.RED);
                    camposIncorrectos++;
                } else{
                    tvTitulo.setTextColor(Color.BLACK);
                }
                //--- Direccion de retiro ---
                if(direccionRetiroActivada){
                    if(!Pattern.matches(textoPlanoValido, direccRet.getText())){
                        direccionRetiro.setTextColor(Color.RED);
                        camposIncorrectos++;
                    } else{
                        direccionRetiro.setTextColor(Color.BLACK);
                    }
                }
                //--- Precio ---
                precio = (EditText) findViewById(R.id.editTextPrecio);
                tvPrecio = (TextView) findViewById(R.id.textViewPrecio);
                precioActual = precio.getText().toString();
                if(precioActual.isEmpty() || Float.valueOf(precioActual)==0){
                    tvPrecio.setTextColor(Color.RED);
                    camposIncorrectos++;
                } else{
                    tvPrecio.setTextColor(Color.BLACK);
                }
                //--- Ofrecer descuento de envio ---
                if(ofrecerEnvioActivado){
                    if(p==0){
                        direccionRetiro.setTextColor(Color.RED);
                        Toast.makeText(MainActivity.this, "Por favor seleccione un porcentaje mayor a 0 o quite la opcion de ofrecer descuento de envio", Toast.LENGTH_LONG).show();
                    } else{
                        direccionRetiro.setTextColor(Color.BLACK);
                    }
                }
                //Correo electronico
                tvCorreo = (TextView) findViewById(R.id.textViewCorreo);
                correo = (EditText) findViewById(R.id.editTextCorreo);
                if(!correo.getText().toString().isEmpty() && !Pattern.matches(mailValido, correo.getText())){
                    tvCorreo.setTextColor(Color.RED);
                    camposIncorrectos++;
                } else{
                    tvCorreo.setTextColor(Color.BLACK);
                }
                //Toast
                if(camposIncorrectos>0){
                    Toast.makeText(MainActivity.this,"Por favor, completar los campos correctamente",Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(MainActivity.this,"Publicacion Exitosa",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}