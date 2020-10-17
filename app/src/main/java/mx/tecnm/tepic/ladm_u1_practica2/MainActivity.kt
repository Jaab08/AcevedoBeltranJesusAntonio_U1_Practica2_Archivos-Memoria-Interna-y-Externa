package mx.tecnm.tepic.ladm_u1_practica2

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var nomArchivo : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnguardar.setOnClickListener {
            if (rdbtninterna.isChecked()){
                if(guardarEnMemoriaInterna()==true) {
                    AlertDialog.Builder(this).setTitle("ATENCION")
                        .setMessage("ARCHIVO GUARDADO EN MEMORIA INTERNA")
                        .setPositiveButton("ok"){d,i -> d.dismiss()}
                        .show()
                }else{
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO SE PUDO GUARDAR ARCHIVO")
                        .setPositiveButton("ok"){d,i -> d.dismiss()}
                        .show()
                }
            } else {
                if (rdbtnsd.isChecked()) {
                    if (verificarPermisos() == true) {
                        if (guardarEnMemoriaExterna() == true) {
                            AlertDialog.Builder(this).setTitle("ATENCION")
                                .setMessage("ARCHIVO GUARDADO EN SD")
                                .setPositiveButton("ok") { d, i -> d.dismiss() }
                                .show()
                        } else {
                            AlertDialog.Builder(this).setTitle("ERROR")
                                .setMessage("NO SE PUDO GUARDAR ARCHIVO O NO HAY MEMORIA SD INSERTADA")
                                .setPositiveButton("ok") { d, i -> d.dismiss() }
                                .show()
                        }
                    } else {
                        pedirPermiso()
                        AlertDialog.Builder(this).setTitle("ATENCION")
                            .setMessage("FAVOR DE VOLVER A GUARDAR EL ARCHIVO")
                            .setPositiveButton("ok") { d, i -> d.dismiss() }
                            .show()
                    }
                } else {
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO SE HA SELECCIONADO NINGUN ALOJAMIENTO DE MEMORIA")
                        .setPositiveButton("ok") { d, i -> d.dismiss() }
                        .show()
                }
            }
        } // boton guardar

        btnabrir.setOnClickListener {
            if (rdbtninterna.isChecked()){
                if (abrirDesdeMemoriaInterna().isEmpty()==false){
                    AlertDialog.Builder(this).setTitle("ATENCION")
                        .setMessage("SE LEYÓ ARCHIVO CORRECTAMENTE")
                        .setPositiveButton("ok"){d,i -> d.dismiss()}
                        .show()
                } else {
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("ERROR ARCHIVO NO ENCONTRADO")
                        .setPositiveButton("ok"){d,i -> d.dismiss()}
                        .show()
                }
            }else{
                if (rdbtnsd.isChecked()) {
                    if (abrirDesdeMemoriaExterna().isEmpty() == false) {
                        AlertDialog.Builder(this).setTitle("ATENCION")
                            .setMessage("SE LEYÓ ARCHIVO CORRECTAMENTE")
                            .setPositiveButton("ok") { d, i -> d.dismiss() }
                            .show()
                    } else {
                        AlertDialog.Builder(this).setTitle("ERROR")
                            .setMessage("ERROR ARCHIVO NO ENCONTRADO O MEMORIA SD NO INSERTADA")
                            .setPositiveButton("ok") { d, i -> d.dismiss() }
                            .show()
                    }
                } else{
                    AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("NO SE HA SELECCIONADO NINGUN ALOJAMIENTO DE MEMORIA")
                        .setPositiveButton("ok") { d, i -> d.dismiss() }
                        .show()
                }
            }

        }// boton abrir

    }

    private fun pedirPermiso() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }

    private fun verificarPermisos(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            return false
        }
    }

    // -------------------- FUNCIONES ABRIR -------------------------
    private fun abrirDesdeMemoriaExterna(): String {
        var contenido = ""
        nomArchivo = etarchivo.text.toString()

        try {
            val rutaSD = Environment.getExternalStorageDirectory()
            val flujo = File(rutaSD.absolutePath, nomArchivo)
            val flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(flujo)))
            contenido = flujoEntrada.readLine()
            etfrase.setText(contenido)
            flujoEntrada.close()
        } catch (io: Exception) {
            return ""
        }
        return contenido
    }

    private fun abrirDesdeMemoriaInterna(): String {
        var contenido = ""
        nomArchivo = etarchivo.text.toString()
        try {
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(nomArchivo)))

            contenido = flujoEntrada.readLine()
            flujoEntrada.close()
            etfrase.setText(contenido)

        } catch (io : IOException){
            return ""
        }
        return contenido
    }

    //----------------- FUNCIONES GUARDAR ----------------------
    private fun guardarEnMemoriaExterna() : Boolean {
        nomArchivo = etarchivo.text.toString()

        try {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                return false;
            }
            var rutaSD = Environment.getExternalStorageDirectory()
            var archivoEnSD = File(rutaSD.absolutePath, nomArchivo)
            var flujoSalida = OutputStreamWriter(FileOutputStream(archivoEnSD) )

            flujoSalida.write(etfrase.text.toString())
            flujoSalida.flush()
            flujoSalida.close()

        } catch (io : Exception) {
            return false;
        }
        return true
    }

    private fun guardarEnMemoriaInterna(): Boolean {
        nomArchivo = etarchivo.text.toString()
        try{
            var flujosalida = OutputStreamWriter(openFileOutput(nomArchivo, MODE_PRIVATE) )

            var data = etfrase.text.toString()

            flujosalida.write(data)
            flujosalida.flush()
            flujosalida.close()

        }catch (io: IOException) {
            return false
        }
        return true;
    }
}