package me.sunzheng.mana.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import me.sunzheng.mana.MainActivity;
import me.sunzheng.mana.R;
import me.sunzheng.mana.utils.PreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements LoginContract.LoginView {
    SharedPreferences sharedPreferences;
    private LoginContract.LoginPresenter mPresenter;
    private OnFragmentInteractionListener mListener;
    private TextInputEditText loginUserNameEditText;
    private TextInputEditText loginPassWordEditText;
    private Button mButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(PreferenceManager.Global.STR_SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginUserNameEditText = (TextInputEditText) view.findViewById(R.id.login_username_textinputedittext);
        loginPassWordEditText = (TextInputEditText) view.findViewById(R.id.login_passowrd_textinputedittext);
        ((Button) view.findViewById(android.R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login(loginUserNameEditText.getText().toString(), loginPassWordEditText.getText().toString());
            }
        });
        ((CheckBox)view.findViewById(R.id.checkbox)).setChecked(sharedPreferences.getBoolean(PreferenceManager.Global.BOOL_IS_REMEMBERD,false));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isRememberd = sharedPreferences.getBoolean(PreferenceManager.Global.BOOL_IS_REMEMBERD, false);
        if (isRememberd)
            onLoginSuccess();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setPresenter(LoginContract.LoginPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressIntractor(boolean active) {

    }

    @Override
    public void onLoginSuccess() {
        sharedPreferences.edit()
                .putBoolean(PreferenceManager.Global.BOOL_IS_REMEMBERD, ((CheckBox) getView().findViewById(R.id.checkbox)).isChecked())
                .commit();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
