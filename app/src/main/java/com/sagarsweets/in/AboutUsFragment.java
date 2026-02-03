package com.sagarsweets.in;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.sagarsweets.in.Adapters.TeamAdapter;
import com.sagarsweets.in.ApiControllers.OtpRetrofitClient;
import com.sagarsweets.in.ApiInterface.ApiService;
import com.sagarsweets.in.ApiModel.AboutResponse;
import com.sagarsweets.in.ApiModel.AboutUsModel;
import com.sagarsweets.in.ApiModel.SliderResponse;
import com.sagarsweets.in.ApiModel.TeamModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AboutUsFragment extends Fragment {

    List <AboutUsModel> aboutUsModel;
    TeamModel teamModel;
    TextView about_us_description,present_title,presentDescription,testimonialTitle,testimonialDesc;
    RecyclerView rvTeam;
    ShimmerFrameLayout shimmerLayout;
    ScrollView mainLayout;
    TeamAdapter teamAdapter;
    List<TeamModel> teamList = new ArrayList<>();
    public AboutUsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        mainLayout = view.findViewById(R.id.main);
        about_us_description = view.findViewById(R.id.about_us_description);
        present_title = view.findViewById(R.id.present_title);
        presentDescription = view.findViewById(R.id.presentDescription);
        testimonialTitle = view.findViewById(R.id.testimonialTitle);
        testimonialDesc = view.findViewById(R.id.testimonialDesc);
        rvTeam = view.findViewById(R.id.rvTeam);
        rvTeam.setLayoutManager(new LinearLayoutManager(getContext()));
        teamAdapter = new TeamAdapter(getContext(), teamList);
        rvTeam.setAdapter(teamAdapter);

        loadFromServer();
        // Inflate the layout for this fragment
        return view;
    }

    private void showShimmer() {
        shimmerLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        shimmerLayout.startShimmer();
    }

    private void hideShimmer() {
        shimmerLayout.stopShimmer();
        shimmerLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }


    private void loadFromServer() {
        showShimmer();
        ApiService apiService = OtpRetrofitClient.getApiService();
        apiService.getAboutUs().enqueue(new Callback<AboutResponse>() {
            @Override
            public void onResponse(Call<AboutResponse> call, Response<AboutResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    hideShimmer();
                    List<AboutUsModel> aboutUsModel = response.body().getAboutUs();
                    List<TeamModel> teams = response.body().getOurTeam();

                    if (teams != null && !teams.isEmpty()) {
                        teamList.clear();
                        teamList.addAll(teams);
                        teamAdapter.notifyDataSetChanged();
                    }
                    if (aboutUsModel != null && !aboutUsModel.isEmpty()) {
                        String desc_html = aboutUsModel.get(0).getDescription();
                        String testi_html = aboutUsModel.get(0).getTestimonial_description();
                        String pres_html = aboutUsModel.get(0).getPresents_description();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            about_us_description.setText(
                                    Html.fromHtml(desc_html, Html.FROM_HTML_MODE_COMPACT)
                            );
                            testimonialDesc.setText(Html.fromHtml(testi_html, Html.FROM_HTML_MODE_COMPACT));
                            presentDescription.setText(Html.fromHtml(pres_html, Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            testimonialDesc.setText(Html.fromHtml(testi_html));
                            about_us_description.setText(Html.fromHtml(desc_html));
                            presentDescription.setText(Html.fromHtml(pres_html));
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<AboutResponse> call, Throwable t) {
                // TODO: show error / toast / retry
                hideShimmer();
            }
        });
    }

}