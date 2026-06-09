package com.sagarsweets.in.wizard;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sagarsweets.in.ApiControllers.LoginRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.SaveDobRequest;
import com.sagarsweets.in.ApiModel.SaveEmailRequest;
import com.sagarsweets.in.ApiModel.SaveEmailResponse;
import com.sagarsweets.in.R;
import com.sagarsweets.in.Session.LoginSession;
import com.sagarsweets.in.utils.ButtonLoaderUtil;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileWizardDialog extends DialogFragment {

    ViewFlipper flipper;
    EditText etDob;
    TextInputEditText etEmail;

    MaterialButton btnEmail,btnFinish;

    ProgressBar progressEmail,progressFinish;
    TextInputLayout emailLayout,dobLayout;
    String email = "";
    String dob = "";

    String userId;
    String device;
    ApiService apiService;
    LoginSession loginSession;

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null
                && getDialog().getWindow() != null) {
            // Outside click disabled
            getDialog().setCanceledOnTouchOutside(false);

            // Back button disabled (optional)
            setCancelable(false);
            int width =
                    (int) (
                            getResources()
                                    .getDisplayMetrics()
                                    .widthPixels
                                    * 0.92
                    );



            getDialog()
                    .getWindow()
                    .setLayout(
                            width,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
            getDialog()
                    .getWindow()
                    .setBackgroundDrawableResource(
                            android.R.color.transparent
                    );
        }
    }
    public ProfileWizardDialog(
            String email,
            String dob,
            String userId,
            String device,
            LoginSession loginSession) {

        this.email = email;
        this.dob = dob;
        this.userId = userId;
        this.device = device;
        this.loginSession = loginSession;
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.dialog_profile_wizard,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            Bundle savedInstanceState) {

        flipper = view.findViewById(R.id.wizardFlipper);
        etDob = view.findViewById(R.id.etDob);
        emailLayout = view.findViewById(R.id.emailLayout);
        dobLayout = view.findViewById(R.id.dobLayout);
        btnEmail = view.findViewById(R.id.btnEmail);
        btnFinish = view.findViewById(R.id.btnFinish);
        etEmail = view.findViewById(R.id.etEmail);
        progressEmail = view.findViewById(R.id.progressEmail);
        progressFinish = view.findViewById(R.id.progressFinish);

        apiService = LoginRetrofitClient
                .getClient()
                .create(ApiService.class);

        emailBtnClicked(view);
        dobButtonClicked(view);
        setCalendar();
        openMissingStep();

        view.findViewById(R.id.btnClose)
                .setOnClickListener(v -> {
                    dismiss();
                });



    }

    private void dobButtonClicked(View view) {
        dobLayout.setError(null);
        btnFinish.setOnClickListener(v -> {
            String dob = etDob.getText().toString().trim();
            if(TextUtils.isEmpty(dob)){
                dobLayout.setError("DOB is required");
                return;
            }
            ButtonLoaderUtil.showLoading(btnFinish,progressFinish);
            SaveDobRequest saveDobRequest = new SaveDobRequest(userId,device,dob);
            apiService.saveDobProfile(saveDobRequest).enqueue(new Callback<SaveEmailResponse>() {
                @Override
                public void onResponse(Call<SaveEmailResponse> call, Response<SaveEmailResponse> response) {
                    ButtonLoaderUtil.hideLoading(btnFinish,progressFinish,"Continue");
                    if(response.body() != null && response.body().getStatus() == true){
                        loginSession.setDob(dob);
                        dismiss();
                    }else{
                        dobLayout.setError(response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SaveEmailResponse> call, Throwable t) {
                    ButtonLoaderUtil.hideLoading(btnFinish,progressFinish,"Continue");
                    dobLayout.setError(t.getMessage());
                }
            });

        });
    }

    private void emailBtnClicked(View view) {
        btnEmail.setOnClickListener(v -> {
                String email =
                        etEmail.getText()
                                .toString()
                                .trim();
                if (TextUtils.isEmpty(email)) {

                    emailLayout.setError(
                            "Email is required"
                    );
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches()) {

                    emailLayout.setError(
                            "Enter valid email"
                    );

                    return;
                }
                emailLayout.setError(null);
                ButtonLoaderUtil.showLoading(btnEmail,progressEmail);
                SaveEmailRequest request = new SaveEmailRequest(userId,device,email);
                apiService.saveEmailProfile(request).enqueue(new Callback<SaveEmailResponse>() {
                    @Override
                    public void onResponse(Call<SaveEmailResponse> call, Response<SaveEmailResponse> response) {
                        ButtonLoaderUtil.hideLoading(btnEmail,progressEmail,"Continue");
                        if(response.body() != null && response.body().getStatus() != false){
                            loginSession.setEmail(email);
                            flipper.showNext();
                        }else{
                                emailLayout.setError(response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<SaveEmailResponse> call, Throwable t) {
                        ButtonLoaderUtil.hideLoading(btnEmail,progressEmail,"Continue");
                        emailLayout.setError(t.getMessage());
                    }
                });

            });
    }

    private void setCalendar() {


        etDob.setOnClickListener(v -> {

            Calendar calendar =
                    Calendar.getInstance();

            int year =
                    calendar.get(Calendar.YEAR);

            int month =
                    calendar.get(Calendar.MONTH);

            int day =
                    calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker =
                    new DatePickerDialog(
                            requireContext(),

                            (datePicker,
                             selectedYear,
                             selectedMonth,
                             selectedDay) -> {

                                String dob =
                                        String.format(
                                                "%02d-%02d-%d",
                                                selectedDay,
                                                selectedMonth + 1,
                                                selectedYear
                                        );

                                etDob.setText(dob);

                            },

                            year,
                            month,
                            day
                    );

            // Future DOB disable
            picker.getDatePicker()
                    .setMaxDate(
                            System.currentTimeMillis()
                    );

            picker.show();
        });
    }

    private void openMissingStep() {
        if (empty(email)) {
            flipper.setDisplayedChild(0);

        } else if (empty(dob)) {
            flipper.setDisplayedChild(1);

        } else {
            dismiss();
        }
    }

    boolean empty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
