package com.example.hjc.jiyuanabc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hjc.jiyuanabc.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.sunflower.FlowerCollector;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static String tag="MainActivity";
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private HashMap<String,String> result=new LinkedHashMap<String, String>();
    private int startReturnCode=0;
    private boolean mTranslateEnable=false;
    private SpeechUtility speechUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechUtility=SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5b5a861c");
        if(speechUtility==null){
            Log.d(tag,"SpeechUtility=null");
        }
        speechRecognizer=SpeechRecognizer.createRecognizer(this,initListener);
        initLayout();
    }

    private InitListener initListener=new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(tag,"SpeechRecognizer init() code = " + code);
            if(code!= ErrorCode.SUCCESS){
                showTip("初始化失败,错误码："+code);
            }
        }
    };



    @Override
    public void onClick(View v) {
        if(speechRecognizer==null){
            showTip("初始化失败");
            return;
        }
        switch (v.getId()){
            case R.id.main_bt_start:
                FlowerCollector.onEvent(this,"iat_recognize");
                editText.setText(null);
                result.clear();
                startReturnCode=speechRecognizer.startListening(mRecognizerListener);
                if(startReturnCode!=ErrorCode.SUCCESS){
                    showTip("error code:"+startReturnCode);
                }else{
                    showTip("请开始说话..");
                }
                break;
            case R.id.main_bt_end:
                speechRecognizer.stopListening();
                showTip("停止听写");
                break;
            default:break;
        }
    }

    private void showTip(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }

    private void initLayout(){
        editText=findViewById(R.id.main_edit1);
        findViewById(R.id.main_bt_start).setOnClickListener(this);
        findViewById(R.id.main_bt_end).setOnClickListener(this);
    }

    private void printResult(RecognizerResult results){
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        result.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : result.keySet()) {
            resultBuffer.append(result.get(key));
        }

        editText.setText(resultBuffer.toString());
        //editText.setSelection(editText.length());
    }


    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(tag, results.getResultString());
            if( mTranslateEnable ){
                //printTransResult( results );
            }else{
                printResult(results);
            }

            if (isLast) {
                // TODO 最后的结果
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(tag, "返回音频数据："+data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( null != speechRecognizer ){
            // 退出时释放连接
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(this);
        FlowerCollector.onPageStart(tag);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(tag);
        FlowerCollector.onPause(this);
        super.onPause();
    }
}
