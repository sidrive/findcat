package id.findcat.app.view.camera;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.findcat.app.R;
import java.net.URL;

/**
 * Created by rakasn on 23/01/18.
 */
@SuppressLint("ValidFragment")
public class SettingFragment extends DialogFragment implements OnClickListener {

  Unbinder unbinder;
  @BindView(R.id.rbServer)
  RadioButton rbServer;
  @BindView(R.id.rbLocal)
  RadioButton rbLocal;
  @BindView(R.id.text_local)
  EditText textLocal;
  @BindView(R.id.btnOk)
  Button btn;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_setting, container);
    unbinder = ButterKnife.bind(this, rootView);

    if (rbServer.isSelected()){
      Log.e("onCreateView", "SettingFragment" + "tidak");
      textLocal.setVisibility(View.GONE);
    }
    if (rbLocal.isChecked()) {
      Log.e("onCreateView", "SettingFragment" + "Tampil");
      textLocal.setVisibility(View.VISIBLE);
    }
    btn.setOnClickListener(this);
    return rootView;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onClick(View view) {
    /*if (rbServer.isChecked()){
      URL.base_url.replace(textLocal.getText(),"http://app.findcat.id");
      Log.e("onClick", "SettingFragment" + URL.base_url);
    } else if (rbLocal.isChecked()) {
      URL.base_url.toString().replace(URL.base_url,textLocal.getText().toString());
      Log.e("onClick", "SettingFragment" + URL.base_url);
    }*/
  }
}