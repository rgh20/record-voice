package com.example.recordervoice

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {


    var buttonStart: Button? = null
    var buttonStop: Button? = null
    var buttonPlayLastRecordAudio: Button? = null
    var buttonStopPlayingRecording: Button? = null

    //----
    var AudioSavePathInDevice: String? = null

    var mediaRecorder: MediaRecorder? = null

    var random: Random? = null

    var RandomAudioFileName = "ABCDEFGHIJKLMNOP"

    val RequestPermissionCode = 1

    var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ///----

        buttonStart = findViewById(R.id.button)
        buttonStop = findViewById(R.id.button2)
        buttonPlayLastRecordAudio = findViewById(R.id.button3)
        buttonStopPlayingRecording = findViewById(R.id.button4)
//----------------
        buttonStop?.setEnabled(false)
        buttonPlayLastRecordAudio?.setEnabled(false)
        buttonStopPlayingRecording?.setEnabled(false)
//---------------------------------------------
        random = Random()
        buttonStart?.setOnClickListener {

            if (checkPermission()) {
                AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            CreateRandomAudioFileName(5) + "AudioRecording.3gp"

                MediaRecorderReady()

                try {
                    mediaRecorder?.prepare();
                    mediaRecorder?.start();
                } catch (e: IllegalStateException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                buttonStart?.setEnabled(false)
                buttonStop?.setEnabled(true)

                Toast.makeText(
                    this, "Recording started",
                    Toast.LENGTH_LONG
                ).show();
            } else {
                requestPermission();
            }

        }
//-------
        buttonStop?.setOnClickListener {
            mediaRecorder!!.stop()
            buttonStop?.setEnabled(false)
            buttonPlayLastRecordAudio?.setEnabled(true)
            buttonStart?.setEnabled(true)
            buttonStopPlayingRecording?.setEnabled(false)
            Toast.makeText(
                this@MainActivity, "Recording Completed",
                Toast.LENGTH_LONG
            ).show()


        }
        //--------------------------------
        buttonPlayLastRecordAudio?.setOnClickListener {


            buttonStop?.setEnabled(false)
            buttonStart?.setEnabled(false)
            buttonStopPlayingRecording?.setEnabled(true)
            mediaPlayer = MediaPlayer()
            try {
                mediaPlayer!!.setDataSource(AudioSavePathInDevice)
                mediaPlayer!!.prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mediaPlayer!!.start()
            Toast.makeText(
                this@MainActivity, "Recording Playing",
                Toast.LENGTH_LONG
            ).show()
        }
        //------------------
        buttonStopPlayingRecording?.setOnClickListener {

            buttonStop?.setEnabled(false)
            buttonStart?.setEnabled(true)
            buttonStopPlayingRecording?.setEnabled(false)
            buttonPlayLastRecordAudio?.setEnabled(true)
            if (mediaPlayer != null) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                MediaRecorderReady()
            }
        }

    }
    ///----------

    fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            RECORD_AUDIO
        )
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED
    }

    //-----------------
    fun MediaRecorderReady() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder!!.setOutputFile(AudioSavePathInDevice)
    }

    //----------------
    fun CreateRandomAudioFileName(s: Int): String? {
        val stringBuilder = StringBuilder(s)
        var i = 0
        while (i < s) {
            stringBuilder.append(RandomAudioFileName[random!!.nextInt(RandomAudioFileName.length)])
            i++
        }
        return stringBuilder.toString()
    }
//---------------------------

    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(WRITE_EXTERNAL_STORAGE, RECORD_AUDIO),
            RequestPermissionCode
        )
    }
    //-------------------

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RequestPermissionCode) {

            if (grantResults.size > 0) {
                var StoragePermission = grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED;
                var RecordPermission = grantResults[1] ==
                        PackageManager.PERMISSION_GRANTED;

                if (StoragePermission && RecordPermission) {
                    Toast.makeText(
                        this, "Permission Granted",
                        Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                        this, "Permission Denied",
                        Toast.LENGTH_LONG
                    ).show();
                }
            }
        }

    }


    ///----


}

