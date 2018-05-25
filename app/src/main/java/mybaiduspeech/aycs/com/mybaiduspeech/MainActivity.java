package mybaiduspeech.aycs.com.mybaiduspeech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private TextView m_Text;
    private EventManager asr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startbtn = (Button)findViewById(R.id.start);
        Button stopbtn = (Button)findViewById(R.id.stop);
        m_Text = (TextView)findViewById(R.id.result);

        startbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                start();
            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stop();
            }
        });

        asr = EventManagerFactory.create(this, "asr");

        asr.registerListener(speechlistener);
    }

    private void start() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        String json = new JSONObject(params).toString();
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }

    private  void  stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    EventListener speechlistener = new EventListener() {
        @Override
        public void onEvent(String name, String params, byte[] data, int offset, int length) {
            if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)){
                // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
            }
            if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)){
                // 识别结束
            }
            if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
                String result = "";
                if (params.contains("\"nlu_result\"")) {
                    if (length > 0 && data.length > 0) {
                        result += ", 语义解析结果：" + new String(data, offset, length);
                    }
                }
                m_Text.setText(result);
            }
            // ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
        }
    };
}
