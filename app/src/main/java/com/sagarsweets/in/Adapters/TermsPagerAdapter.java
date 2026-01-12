package com.sagarsweets.in.Adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sagarsweets.in.Adapters.PolicyRelated.PolicyFragment;

public class TermsPagerAdapter extends FragmentStateAdapter {

    public TermsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return PolicyFragment.newInstance(
                        "We respect your privacy. Your personal information is kept secure and used only for order processing."
                );

            case 1:
                return PolicyFragment.newInstance(
                        "Orders are processed within 24–48 hours and delivered within 3–7 working days."
                );

            case 2:
                return PolicyFragment.newInstance(
                        "Returns are accepted within 7 days of delivery. Refunds are processed within 5–7 business days."
                );

            case 3:
                return PolicyFragment.newInstance(
                        "Returns are accepted within 7 days of delivery. Refunds are processed within 5–7 business days."
                );

            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

