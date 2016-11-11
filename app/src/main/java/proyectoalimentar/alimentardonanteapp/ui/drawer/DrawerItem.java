package proyectoalimentar.alimentardonanteapp.ui.drawer;

import android.support.v4.app.Fragment;


import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.ui.donations.DonationFragment;
import proyectoalimentar.alimentardonanteapp.ui.donations.NewDonationFragment;
import proyectoalimentar.alimentardonanteapp.ui.signOut.SignOutFragment;
import proyectoalimentar.alimentardonanteapp.ui.terms_and_condition.TermsAndConditionFragment;

public enum DrawerItem {

    DONATIONS(R.id.nav_item_donations, DonationFragment::new),
    NEW_DONATION(R.id.nav_item_new_donation, NewDonationFragment::new),
    TERMS(R.id.nav_item_terms, TermsAndConditionFragment::new),
    SIGN_OUT(R.id.sign_out, SignOutFragment::new);

    private final int layoutRes;
    private final FragmentFactory fragmentFactory;

    DrawerItem(int layoutRes, FragmentFactory fragmentFactory) {
        this.layoutRes = layoutRes;
        this.fragmentFactory = fragmentFactory;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public Fragment createFragment() {
        return fragmentFactory.createFragment();
    }
}
