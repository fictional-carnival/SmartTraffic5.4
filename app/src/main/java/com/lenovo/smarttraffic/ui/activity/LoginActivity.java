package com.lenovo.smarttraffic.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lenovo.smarttraffic.InitApp;
import com.lenovo.smarttraffic.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author Amoly
 * @date 2019/4/11.
 * description：
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditTextName;
    private EditText mEditTextPassword;
    private TextInputLayout mTextInputLayoutName;
    private TextInputLayout mTextInputLayoutPswd;
    private CheckBox jz;
    private TextView textlogCB;


    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar(findViewById(R.id.toolbar), true, getString(R.string.login));

        mTextInputLayoutName = findViewById(R.id.textInputLayoutName);
        mTextInputLayoutPswd = findViewById(R.id.textInputLayoutPassword);
        jz = findViewById(R.id.jzpwdCB);
        textlogCB = findViewById(R.id.textlogCB);

        mEditTextName = findViewById(R.id.editTextName);
        mTextInputLayoutName.setErrorEnabled(true);
        mEditTextPassword = findViewById(R.id.editTextPassword);
        mTextInputLayoutPswd.setErrorEnabled(true);
        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(this);
        mEditTextName.addTextChangedListener(new TextWatcher() {
            @Override/*内容要改变之前调用*/
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /*从start位置开始，count个字符（空字符是0）将被after个字符替换*/

            }

            @Override/*内容要改变时调用*/
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*说明在s字符串中，从start位置开始的count个字符刚刚取代了长度为before的旧文本*/
            }

            @Override/*内容要改变之后调用*/
            public void afterTextChanged(Editable s) {
                //这个方法被调用，那么说明s字符串的某个地方已经被改变。
                checkName(s.toString(), false);
            }
        });

        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPwd(s.toString(), false);
            }
        });

        textlogCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        initUI();
    }

    private void initUI() {
        boolean jzmm = InitApp.sp.getBoolean("jzmm", false);
        if (jzmm) {
            mEditTextName.setText(InitApp.sp.getString("UserName", null));
            mEditTextPassword.setText(InitApp.sp.getString("UserPwd", null));
        }
        jz.setChecked(jzmm);
    }

    //网络设置
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.network_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettings() {
        String ip = InitApp.sp.getString("ip", "192.168.95.110");
        String[] str_ip = ip.split(".");
        String port = InitApp.sp.getString("port", "8081");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.create();
        View inflate = View.inflate(this, R.layout.ipsettings, null);
        EditText et_ip1 = inflate.findViewById(R.id.et_ip1);
        EditText et_ip2 = inflate.findViewById(R.id.et_ip2);
        EditText et_ip3 = inflate.findViewById(R.id.et_ip3);
        EditText et_ip4 = inflate.findViewById(R.id.et_ip4);

        Button bt_set = inflate.findViewById(R.id.bt_set);
        Button bt_qx = inflate.findViewById(R.id.bt_qx);
        dialog.setView(inflate);
        dialog.show();
        et_ip1.setHint(str_ip[0]);
        et_ip2.setHint(str_ip[1]);
        et_ip3.setHint(str_ip[2]);
        et_ip4.setHint(str_ip[3]);

        bt_qx.setOnClickListener(view -> {
            dialog.dismiss();
        });
        bt_set.setOnClickListener(view -> {
            String etip1 = et_ip1.getText().toString();
            String etip2 = et_ip2.getText().toString();
            String etip3 = et_ip3.getText().toString();
            String etip4 = et_ip4.getText().toString();
            String etip = etip1 + "." + etip2 + "." + etip3 + "." + etip4;
            if (TextUtils.isEmpty(etip1) || TextUtils.isEmpty(etip2) || TextUtils.isEmpty(etip3) || TextUtils.isEmpty(etip4)) {
                InitApp.toast("IP或端口号不能为空");
                return;
            }
            String regex = "((25[0-5]|2[0-4]\\d|[0,1]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[0,1]?\\d\\d?)";
            if (!Pattern.compile(regex).matcher(etip).matches()) {
                InitApp.toast("IP格式错误");
                return;
            }
            int p = Integer.parseInt(etip);
            if (p > 0 && p < 65535) {
                if ("192.168.95.110".equals(etip) && 8081 == p) {
                    InitApp.edit.putString("ip", etip);
                    InitApp.edit.commit();
                    InitApp.toast("设置成功");
                    dialog.dismiss();
                }else {
                    InitApp.toast("服务器连接失败");
                }
            }else {
                InitApp.toast("端口号错误");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginBtn) {
            hideSoftInput(v);
            if (!checkName(mEditTextName.getText(), true))
                return;
            if (!checkPwd(mEditTextPassword.getText(), true))
                return;
            login();
        }
    }

    private void login() {
        String username = mEditTextName.getText().toString();
        String userpwd = mEditTextPassword.getText().toString();
        HashMap map = new HashMap();
        map.put("UserName", username);
        map.put("UserPwd", userpwd);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, InitApp.Url + "user_login", new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if ("S".equals(jsonObject.getString("RESULT"))) {
                        InitApp.edit.putString("UserName", username);
                        InitApp.edit.putString("UserPwd", userpwd);
                        InitApp.edit.putBoolean("isLogin", true);
                        InitApp.edit.putBoolean("jzmm", jz.isChecked());
                        InitApp.edit.commit();
                        InitApp.toast("登录成功");
                        finish();
                    }else {
                        InitApp.toast("登陆失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mEditTextName.setText(null);
                mEditTextPassword.setText(null);
                InitApp.toast("用户名或密码错误");
            }
        });
        InitApp.queue.add(request);
    }

    private boolean checkPwd(CharSequence pswd, boolean isLogin) {
        if (TextUtils.isEmpty(pswd)) {
            if (isLogin) {
                mTextInputLayoutPswd.setError(getString(R.string.error_pwd_empty));
                return false;
            }
        } else {
            mTextInputLayoutPswd.setError(null);
        }
        return true;
    }

    private boolean checkName(CharSequence name, boolean isLogin) {
        if (TextUtils.isEmpty(name)) {
            if (isLogin) {
                mTextInputLayoutName.setError(getString(R.string.error_login_empty));
                return false;
            }
        } else {
            mTextInputLayoutName.setError(null);
        }
        return true;
    }


    /**
     * 隐藏键盘输入法
     *
     * @param view The view.
     */
    public static void hideSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) InitApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0, new ResultReceiver(new Handler()) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_SHOWN
                        || resultCode == InputMethodManager.RESULT_SHOWN) {
                    toggleSoftInput();
                }
            }
        });
    }

    /**
     * 软键盘切换
     */
    public static void toggleSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) InitApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

}
