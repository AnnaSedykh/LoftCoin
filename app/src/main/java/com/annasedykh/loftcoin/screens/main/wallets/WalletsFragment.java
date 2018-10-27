package com.annasedykh.loftcoin.screens.main.wallets;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.annasedykh.loftcoin.App;
import com.annasedykh.loftcoin.R;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.prefs.Prefs;
import com.annasedykh.loftcoin.screens.currencies.CurrenciesBottomSheet;
import com.annasedykh.loftcoin.screens.currencies.CurrenciesBottomSheetListener;
import com.annasedykh.loftcoin.screens.main.wallets.adapters.WalletsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WalletsFragment extends Fragment implements CurrenciesBottomSheetListener {

    @BindView(R.id.wallets_toolbar)
    Toolbar toolbar;

    @BindView(R.id.wallets_pager)
    ViewPager walletsPager;

    @BindView(R.id.new_wallet)
    ViewGroup newWallet;

    @BindView(R.id.transactions_recycler)
    RecyclerView transactionsRecycler;

    private Unbinder unbinder;

    private WalletsPagerAdapter walletsPagerAdapter;
    private WalletsViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prefs prefs = ((App) getActivity().getApplication()).getPrefs();

        viewModel = ViewModelProviders.of(this).get(WalletsViewModelImpl.class);
        walletsPagerAdapter = new WalletsPagerAdapter(prefs);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.wallets_screen_title);
        toolbar.inflateMenu(R.menu.menu_wallets);

        walletsPager.setPageMargin(countWalletsPagerMargin());
        walletsPager.setOffscreenPageLimit(5);
        walletsPager.setAdapter(walletsPagerAdapter);

        Fragment bottomSheet = getFragmentManager().findFragmentByTag(CurrenciesBottomSheet.TAG);
        if (bottomSheet != null) {
            ((CurrenciesBottomSheet) bottomSheet).setListener(this);
        }

        viewModel.getWallets();

        initOutputs();
        initInputs();
    }

    private int countWalletsPagerMargin() {
        int screenWidth = getScreenWidth();
        int walletItemWidth = getResources().getDimensionPixelOffset(R.dimen.item_wallet_width);
        int walletItemMargin = getResources().getDimensionPixelOffset(R.dimen.item_wallet_margin);
        int pageMargin = (screenWidth - walletItemWidth) - walletItemMargin;
        return -pageMargin;
    }

    private void initOutputs() {
        newWallet.setOnClickListener(view -> viewModel.onNewWalletClick());

        toolbar.getMenu().findItem(R.id.menu_item_add_wallet)
                .setOnMenuItemClickListener(item -> {
                    viewModel.onNewWalletClick();
                    return true;
                });
    }

    private void initInputs() {

        viewModel.wallets().observe(this,
                walletModels -> walletsPagerAdapter.setWallets(walletModels));

        viewModel.walletsVisible().observe(this,
                isVisible -> walletsPager.setVisibility(isVisible ? View.VISIBLE : View.GONE));

        viewModel.newWalletVisible().observe(this,
                isVisible -> newWallet.setVisibility(isVisible ? View.VISIBLE : View.GONE));

        viewModel.selectCurrency().observe(this, o -> showCurrencyBottomSheet());
    }

    private void showCurrencyBottomSheet() {
        CurrenciesBottomSheet bottomSheet = new CurrenciesBottomSheet();
        bottomSheet.show(getFragmentManager(), CurrenciesBottomSheet.TAG);
        bottomSheet.setListener(this);

    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        viewModel.onCurrencySelected(coin);
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return width;
    }
}
