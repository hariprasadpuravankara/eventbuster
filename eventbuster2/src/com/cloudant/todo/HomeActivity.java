package com.cloudant.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final Button btn=(Button)findViewById(R.id.createbutton);
        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent myIntent = new Intent(HomeActivity.this, TodoActivity.class);
                HomeActivity.this.startActivity(myIntent);
            }
        });

        final Button btn1=(Button)findViewById(R.id.search);
        btn1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this.getApplicationContext(), "Step1!", Toast.LENGTH_SHORT).show();

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8)
                {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    //your codes here

                }

                SpeechToText service = new SpeechToText();
                //service.setEndPoint("https://stream.watsonplatform.net/speech-to-text/api");
                service.setUsernameAndPassword( "c546776d-fa30-4239-a645-ea4dfae4c40f","OWtMJvqcGsBy");
                String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";;


              /*  MediaRecorder myAudioRecorder = new MediaRecorder();
                myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.);
                myAudioRecorder.setOutputFile(outputFile);
                Toast.makeText(getApplicationContext(), "Start recording...", Toast.LENGTH_SHORT).show();*/
                System.out.println("*****************service initiated**********");
                try{
              /*  myAudioRecorder.prepare();
                myAudioRecorder.start();

                    Toast.makeText(getApplicationContext(), "Stopped recording!", Toast.LENGTH_SHORT).show();

                    myAudioRecorder.stop();
                    myAudioRecorder.release();*/

                //File audio = new File("src/test/resources/sample1.wav");

                    //File audio = new File(outputFile);

                    Toast.makeText(HomeActivity.this.getApplicationContext(), "Start recording!", Toast.LENGTH_SHORT).show();
                    System.out.println("*****************Start recording**********");
                    bufferSize = AudioRecord.getMinBufferSize(8000,
                            AudioFormat.CHANNEL_CONFIGURATION_MONO,
                            AudioFormat.ENCODING_PCM_16BIT);


                    startRecording();
                    Toast.makeText(getApplicationContext(), "Start recording!", Toast.LENGTH_SHORT).show();
                    try{ Thread.sleep(6000); }catch(InterruptedException e){ }

                    stopRecording();
                    Toast.makeText(getApplicationContext(), "Stopped recording!", Toast.LENGTH_SHORT).show();
                    System.out.println("*****************Stopped recording**********");
                    File audio = new File(finalfilename);
                    Toast.makeText(getApplicationContext(), "FileName "+finalfilename, Toast.LENGTH_SHORT).show();
                    System.out.println("*****************Start recording*********FileName "+finalfilename);
                    Toast.makeText(getApplicationContext(), "File Prepared",
                            Toast.LENGTH_SHORT).show();

                    SpeechResults transcript = service.recognize(audio).execute();

                    System.out.println("*****************Stopped recording**********"+transcript.getResults());
                    List<Transcript> results = transcript.getResults();

                    PrintStream out = new PrintStream(System.out, true, "UTF-8");
                    String searchedWord =null;
                    for (Transcript object : results)
                    {
                        System.out.println( "*********OBJ********");
                        for(com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative sp :object.getAlternatives()){
                            searchedWord =  sp.getTranscript().toString();
                        }
                    }


                    System.out.println("*****************searchedWord**********"+searchedWord);

                    Intent myIntent = new Intent(HomeActivity.this, SearchResult.class);

                    myIntent.putExtra("searchedWord", searchedWord);

                    HomeActivity.this.startActivity(myIntent);

                }catch (Exception e){
                    System.out.println("*****************Inside Exception**********");
                    e.printStackTrace();                }

                System.out.println("*****************Finisg=hed**********");
            }
        });
    }

    private AudioRecord  recorder;
    private boolean isRecording = false;

    Thread  recordingThread ;
    private void startRecording(){
          recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING, bufferSize);

        int i = recorder.getState();
        if(i==1)
            recorder.startRecording();

        isRecording = true;

          recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {


                // TODO
                writeAudioDataToFile();
            }
        },"AudioRecorder Thread");

        recordingThread.start();
    }

    private int bufferSize = 0;
    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private String getTempFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            file.mkdirs();
        }

        File tempFile = new File(filepath,AUDIO_RECORDER_TEMP_FILE);

        if(tempFile.exists())
            tempFile.delete();

        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void writeAudioDataToFile() {
        byte data[] = new byte[bufferSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int read = 0;

        if (null != os) {
            int i=0;
            while(isRecording){
                read = recorder.read(data, 0, bufferSize);

                if(AudioRecord.ERROR_INVALID_OPERATION != read){
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private void stopRecording(){
        if(null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if(i==1)
                recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }

        copyWaveFile(getTempFilename(),getFilename());
        deleteTempFile();
    }
    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
    }
    private void deleteTempFile() {
        File file = new File(getTempFilename());

        file.delete();
    }

    private String finalfilename ;
    private void copyWaveFile(String inFilename,String outFilename) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 2;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;


            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while (in.read(data) != -1) {
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finalfilename = outFilename;
    }

        private void WriteWaveFileHeader(
                FileOutputStream out, long totalAudioLen,
        long totalDataLen, long longSampleRate, int channels,
        long byteRate) throws IOException {

            byte[] header = new byte[44];

            header[0] = 'R'; // RIFF/WAVE header
            header[1] = 'I';
            header[2] = 'F';
            header[3] = 'F';
            header[4] = (byte) (totalDataLen & 0xff);
            header[5] = (byte) ((totalDataLen >> 8) & 0xff);
            header[6] = (byte) ((totalDataLen >> 16) & 0xff);
            header[7] = (byte) ((totalDataLen >> 24) & 0xff);
            header[8] = 'W';
            header[9] = 'A';
            header[10] = 'V';
            header[11] = 'E';
            header[12] = 'f'; // 'fmt ' chunk
            header[13] = 'm';
            header[14] = 't';
            header[15] = ' ';
            header[16] = 16; // 4 bytes: size of 'fmt ' chunk
            header[17] = 0;
            header[18] = 0;
            header[19] = 0;
            header[20] = 1; // format = 1
            header[21] = 0;
            header[22] = (byte) channels;
            header[23] = 0;
            header[24] = (byte) (longSampleRate & 0xff);
            header[25] = (byte) ((longSampleRate >> 8) & 0xff);
            header[26] = (byte) ((longSampleRate >> 16) & 0xff);
            header[27] = (byte) ((longSampleRate >> 24) & 0xff);
            header[28] = (byte) (byteRate & 0xff);
            header[29] = (byte) ((byteRate >> 8) & 0xff);
            header[30] = (byte) ((byteRate >> 16) & 0xff);
            header[31] = (byte) ((byteRate >> 24) & 0xff);
            header[32] = (byte) (2 * 16 / 8); // block align
            header[33] = 0;
            header[34] = RECORDER_BPP; // bits per sample
            header[35] = 0;
            header[36] = 'd';
            header[37] = 'a';
            header[38] = 't';
            header[39] = 'a';
            header[40] = (byte) (totalAudioLen & 0xff);
            header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
            header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
            header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        try{
            out.write(header, 0, 44);
        }catch(Exception e){
            e.printStackTrace();;
        }


    }


}
