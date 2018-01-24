package id.findcat.app.view.camera;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTextChanged.Callback;
import butterknife.Unbinder;
import id.findcat.app.FindcatDexApp;
import id.findcat.app.R;
import id.findcat.app.preference.GlobalPreferences;
import id.findcat.app.preference.PrefKey;
import id.findcat.app.utils.ActivityUtils;
import id.findcat.app.utils.Const;
import id.findcat.app.view.splash.SplashActivity;

/**
 * Created by rakasn on 23/01/18.
 */
@SuppressLint("ValidFragment")
public class SettingFragment extends DialogFragment {

  Unbinder unbinder;
  @BindView(R.id.etHost)
  TextInputEditText etHost;
  @BindView(R.id.rbServer)
  RadioButton rbServer;
  @BindView(R.id.rbLocal)
  RadioButton rbLocal;
  @BindView(R.id.rbGrupServer)
  RadioGroup rbGrupServer;
  private boolean isLocalSelected = false;
  private GlobalPreferences preferences;
  String http = "http://";

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_setting, container);
    unbinder = ButterKnife.bind(this, rootView);

    return rootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    preferences = new GlobalPreferences(FindcatDexApp.getContext());
    getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT);
    getDialog().setTitle("CHOOSE SERVER");
    etHost.setText(preferences.read(PrefKey.base_url,String.class));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }


  @OnClick({R.id.rbServer,R.id.rbLocal}) void OnServerChoose(RadioButton group){
    switch (group.getId()) {
      case R.id.rbLocal:
        isLocalSelected = true;
        etHost.setText("");
        return;
      case R.id.rbServer:
        isLocalSelected = false;
        etHost.setText(Const.BASE_URL_PROD);
        return;
    }
  }

  @OnClick(R.id.btnSave)
  public void onBtnSaveClicked() {
    String url = etHost.getText().toString();
    if (isLocalSelected){
      String base_url = http+url;
      Log.e("onBtnSaveClicked", "SettingFragment" + base_url);
      preferences.write(PrefKey.base_url, base_url, String.class);
    }else {
      preferences.write(PrefKey.base_url, url, String.class);
      Log.e("onBtnSaveClicked", "SettingFragment" + url);
    }
    getDialog().dismiss();
  }
}