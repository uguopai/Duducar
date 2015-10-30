package com.guokrspace.dududriver.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.guokrspace.dududriver.R;
import com.guokrspace.dududriver.common.Constants;
import com.guokrspace.dududriver.net.HandlerMessageTag;
import com.guokrspace.dududriver.net.ResponseHandler;
import com.guokrspace.dududriver.net.SocketClient;
import com.guokrspace.dududriver.view.EditTextHolder;
import com.guokrspace.dududriver.view.LoadingDialog;
import com.guokrspace.dududriver.view.WinToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends BaseActivity implements
        View.OnClickListener, Handler.Callback,
        EditTextHolder.OnEditTextFocusChangeListener
{
    private static final String TAG = LoginActivity.class.getSimpleName();
    /**
     * 用户账户
     */
    private EditText mUserNameEt;
    /**
     * 密码
     */
    private EditText mPassWordEt;
    /**
     * 登录button
     */
    private Button mSignInBt;

    private Button btSkip;

    /**
     * 验证码Button
     */
    private Button mRegcodeBt;

    /**
     * 设备id
     */
    private String mDeviceId;
    /**
     * 忘记密码
     */
    /**
     * 输入用户名删除按钮
     */
    private FrameLayout mFrUserNameDelete;
    /**
     * 输入密码删除按钮
     */
    private FrameLayout mFrPasswordDelete;
    /**
     * logo
     */
    private ImageView mLoginImg;
    /**
     * 软键盘的控制
     */
    private InputMethodManager mSoftManager;

    private Thread  thread;
    private boolean threadStopFlag = false;

    private int messageid;
    private int CurrentState = STATE_READY;

    private static final int STATE_READY      = 0;  // Free
    private static final int STATE_WAIT_SMS   = 1; //Register code reqeusted, wait for SMS
    private static final int STATE_WAIT_TOKEN = 2;//SMS sent, wait for token
    private static final int STATE_LOGIN      = 3;
    private static final int STATE_LOGINED      = 4;

    private static final int REQUEST_CODE_REGISTER = 200;
    public static final String INTENT_EMAIL = "intent_email";
    public static final String INTENT_PASSWORD = "intent_password";
    private static final int HANDLER_LOGIN_SUCCESS = 1;
    private static final int HANDLER_LOGIN_FAILURE = 2;
    private static final int HANDLER_REGISTER_REQUESTED = 12;
    private static final int HANDLER_REGISTER_SUCCESS = 7;
    private static final int HANDLER_REGISTER_FAILURE = 8;
    private static final int HANDLER_VERIFY_SUCCESS = 10;
    private static final int HANDLER_VERIFY_FAILURE = 11;
    private static final int HANDLER_LOGIN_HAS_FOCUS = 3;
    private static final int HANDLER_LOGIN_HAS_NO_FOCUS = 4;
    private static final int HANDLER_TIMERTICK = 5;
    private static final int HANDLER_TIMER_TIMEOUT = 6;

    private static final int HANDLER_LOGIN_SMS = 9;


    private Handler mHandler;

    private ImageView mImgBackgroud;

    String userName;
    String token;
    private boolean isFirst = false;
    private boolean isSuccess = false;
    private LoadingDialog mDialog;
    private EditTextHolder mEditUserNameEt;
    private EditTextHolder mEditPassWordEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
         * Init the SocketClient
         */
//        mTcpClient = null;
//        conctTask = new connectTask(); //Connect to server
//        conctTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    protected void onPause() {
        super.onPause();
        if (mSoftManager == null) {
            mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getCurrentFocus() != null) {
            mSoftManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);// 隐藏软键盘
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void initView() {

        mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mLoginImg = (ImageView) findViewById(R.id.de_login_logo);
        mUserNameEt = (EditText) findViewById(R.id.app_username_et);
        mPassWordEt = (EditText) findViewById(R.id.app_password_et);
        mSignInBt = (Button) findViewById(R.id.app_sign_in_bt);
        btSkip = (Button) findViewById(R.id.app_skip_bt);
        mRegcodeBt = (Button)findViewById(R.id.app_regcode_bt);
        mImgBackgroud = (ImageView) findViewById(R.id.de_img_backgroud);
        mFrUserNameDelete = (FrameLayout) findViewById(R.id.fr_username_delete);
        mFrPasswordDelete = (FrameLayout) findViewById(R.id.fr_pass_delete);

        mSignInBt.setOnClickListener(this);
        btSkip.setOnClickListener(this);
        mRegcodeBt.setOnClickListener(this);
//        mRegister.setOnClickListener(this);
//        mLeftTitle.setOnClickListener(this);
//        mRightTitle.setOnClickListener(this);
        mHandler = new Handler(this);
        mDialog = new LoadingDialog(this);

        mEditUserNameEt = new EditTextHolder(mUserNameEt, mFrUserNameDelete, null);
        mEditPassWordEt = new EditTextHolder(mPassWordEt, mFrPasswordDelete, null);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                mImgBackgroud.startAnimation(animation);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_regcode_bt://验证码
                userName = mUserNameEt.getEditableText().toString();
                if (TextUtils.isEmpty(userName)) {
                    WinToast.toast(this, R.string.login_erro_is_null);
                    return;
                }
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                Map<String, String> registerParams = new HashMap<>();
                registerParams.put("cmd", Constants.CMD_REGISTER);
                registerParams.put("role", "1");
                registerParams.put("mobile", "13900000003");
                messageid = SocketClient.getInstance().sendRequest(registerParams, new ResponseHandler() {
                    @Override
                    public void onSuccess(String messageBody) {

                    }

                    @Override
                    public void onFailure(String error) {

                    }

                    @Override
                    public void onTimeout() {

                    }
                });
                CurrentState = STATE_WAIT_SMS;

                Message mess = Message.obtain();
                mess.what = HANDLER_REGISTER_REQUESTED;
                mHandler.sendMessage(mess);
                break;
            case R.id.app_sign_in_bt://登录
                userName = mUserNameEt.getEditableText().toString();
                String passWord = mPassWordEt.getEditableText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
                    WinToast.toast(this, R.string.login_erro_is_null);
                    return;
                }
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }

                if(CurrentState == STATE_WAIT_SMS) {
                    Map<String, String> verifyParams = new HashMap<>();
                    verifyParams.put("cmd", Constants.CMD_VERIFY);
                    verifyParams.put("role", "1");
                    verifyParams.put("mobile", "13900000003");
                    verifyParams.put("verifycode", "1111");
                    messageid = SocketClient.getInstance().sendRequest(verifyParams, new ResponseHandler() {
                        @Override
                        public void onSuccess(String messageBody) {

                        }

                        @Override
                        public void onFailure(String error) {

                        }

                        @Override
                        public void onTimeout() {

                        }
                    });
                    CurrentState = STATE_WAIT_TOKEN;
                }

                break;
            case R.id.app_username_et:
            case R.id.app_password_et:
                mess = Message.obtain();
                mess.what = HANDLER_LOGIN_HAS_FOCUS;
                mHandler.sendMessage(mess);
                break;
            case R.id.app_skip_bt:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onEditTextFocusChange(View v, boolean hasFocus) {
        Message mess = Message.obtain();
        switch (v.getId()) {
            case R.id.app_username_et:
            case R.id.app_password_et:
                if (hasFocus) {
                    mess.what = HANDLER_LOGIN_HAS_FOCUS;
                }
                mHandler.sendMessage(mess);
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {

        if (msg.what == HANDLER_LOGIN_FAILURE) {
            if (mDialog != null)
                mDialog.dismiss();
            WinToast.toast(LoginActivity.this, R.string.login_failure);
            startActivity(new Intent(this, OldMainActivity.class));
            finish();
        } else if (msg.what == HANDLER_LOGIN_SUCCESS) {
            if (mDialog != null)
                mDialog.dismiss();
            WinToast.toast(LoginActivity.this, R.string.login_success);
//            startActivity(new Intent(this, MainActivity.class));
            setResult(RESULT_OK);
            finish();
        } else if (msg.what == HANDLER_LOGIN_HAS_FOCUS) {
            mLoginImg.setVisibility(View.GONE);
        } else if (msg.what == HANDLER_LOGIN_HAS_NO_FOCUS) {
            mLoginImg.setVisibility(View.VISIBLE);
        } else if (msg.what == HANDLER_TIMERTICK) {
             mRegcodeBt.setText((String) msg.obj);
             mRegcodeBt.setEnabled(false);
        } else if(msg.what == HANDLER_REGISTER_REQUESTED) {
            mRegcodeBt.setEnabled(false);
            TimerTick(60);
        } else if(msg.what == HANDLER_TIMER_TIMEOUT) {
            threadStopFlag = true;
            mRegcodeBt.setText("获取验证码");
            mRegcodeBt.setEnabled(true);
        } else if(msg.what == HANDLER_REGISTER_SUCCESS){
            if (mDialog != null)
                mDialog.dismiss();
        } else if(msg.what == 0x6001) { //Ensure the sync of the request and response
            JSONObject message = (JSONObject)msg.obj;
            if(message==null) return false;
            if(SocketClient.getInstance().messageParsor.getFieldVal(message, Constants.FIELD_STATUS, Integer.class) == 1)
            {
                    if (CurrentState == STATE_WAIT_SMS) {
                        mHandler.sendEmptyMessage(HANDLER_REGISTER_SUCCESS);
                    } else if (CurrentState == STATE_WAIT_TOKEN) {
                        CurrentState = STATE_LOGIN;
                        token = SocketClient.getInstance().messageParsor.getFieldVal(message, Constants.FIELD_TOKEN, String.class);
                        mHandler.sendEmptyMessage(HANDLER_VERIFY_SUCCESS);
                        Map<String, String> loginParams = new HashMap<>();
                        loginParams.put("cmd", Constants.CMD_LOGIN);
                        loginParams.put("role", "1");
                        loginParams.put("mobile", "13900000003");
                        loginParams.put("token", token);
                        messageid = SocketClient.getInstance().sendRequest(loginParams, new ResponseHandler() {
                            @Override
                            public void onSuccess(String messageBody) {

                            }

                            @Override
                            public void onFailure(String error) {

                            }

                            @Override
                            public void onTimeout() {

                            }
                        });
                    } else if (CurrentState  == STATE_LOGIN) {
                        CurrentState = STATE_LOGINED;
                        mHandler.sendEmptyMessage(HANDLER_LOGIN_SUCCESS);
                    }

                    if(messageid >= 0) {
                        Log.i("","");
                    }
            } else {
                if(CurrentState == STATE_WAIT_SMS)
                {
                    mHandler.sendEmptyMessage(HANDLER_REGISTER_FAILURE);
                } else if(CurrentState == STATE_WAIT_TOKEN) {
                    mHandler.sendEmptyMessage(HANDLER_VERIFY_FAILURE);
                } else if(CurrentState == STATE_LOGIN) {
                    mHandler.sendEmptyMessage(HANDLER_LOGIN_FAILURE);
                }

            }

            Log.i("","");
        } else if(msg.what == HandlerMessageTag.MESSAGE_TIMEOUT)
        {
            Log.i("","");
        }

        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {

            final AlertDialog.Builder alterDialog = new AlertDialog.Builder(this);
            alterDialog.setMessage("确定退出应用？");
            alterDialog.setCancelable(true);

            alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Process.killProcess(Process.myPid());
                }
            });
            alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alterDialog.show();
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mSoftManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                Message mess = Message.obtain();
                mess.what = HANDLER_LOGIN_HAS_NO_FOCUS;
                mHandler.sendMessage(mess);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        event.getKeyCode();
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_ESCAPE:
                Message mess = Message.obtain();
                mess.what = HANDLER_LOGIN_HAS_NO_FOCUS;
                mHandler.sendMessage(mess);
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    private void TimerTick(final int max_seconds) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int seconds_left = max_seconds;
                while (seconds_left > 0 && !threadStopFlag) {
                    seconds_left--;
                    mHandler.sendMessage(mHandler.obtainMessage(HANDLER_TIMERTICK, seconds_left + "秒"));
                    try {
                        thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(HANDLER_TIMER_TIMEOUT);
            }
        });
        if (!thread.isAlive()) {
            thread.start();
        }
    }
}
